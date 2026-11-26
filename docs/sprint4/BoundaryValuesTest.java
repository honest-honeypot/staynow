package ua.kpi.tzyapz.staynow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульні тести для перевірки граничних значень бізнес-логіки StayNow (v1).
 * Охоплює правила розрахунку цін, знижок, правил скасування та валідації дати.
 */
@Tag("unit")
@DisplayName("StayNow: Перевірка граничних значень (PriceCalculator & BookingValidator)")
class BoundaryValuesTest {

    private PriceCalculator priceCalculator;
    private BookingValidator bookingValidator;

    @BeforeEach
    void setUp() {
        priceCalculator = new PriceCalculator();
        bookingValidator = new BookingValidator();
    }

    // =========================================================================
    // BL-10: Валідація дат бронювання
    // =========================================================================

    @ParameterizedTest(name = "BL-10: Валідація дати заїзду відносно сьогодні (offset={0} днів)")
    @CsvSource({
            "0, true",   // Сьогодні — дозволено
            "1, true",   // Майбутня дата — дозволено
            "-1, false"  // Минула дата — заборонено (DEFECT-01)
    })
    @DisplayName("BL-10: Перевірка граничних значень дати заїзду")
    void bookingDateBoundariesAreValidated(int dayOffset, boolean expectedValid) {
        // Arrange
        LocalDate checkIn = LocalDate.now().plusDays(dayOffset);
        LocalDate checkOut = checkIn.plusDays(2);

        // Act
        boolean isValid = bookingValidator.isValidBookingDates(checkIn, checkOut);

        // Assert
        assertEquals(expectedValid, isValid, "Результат валідації дати не відповідає вимозі BL-10");
    }

    // =========================================================================
    // BL-20: Знижка за тривалість проживання (Tier boundary: 7 ночей = 10%)
    // =========================================================================

    @ParameterizedTest(name = "BL-20: Знижка за {0} ночей (базова ціна 100/ніч)")
    @CsvSource({
            "6, 600.00", // 6 ночей * 100 = 600.00 (без знижки) -- DEFECT-02
            "7, 630.00"  // 7 ночей * 100 - 10% (70) = 630.00 -- DEFECT-03
    })
    @DisplayName("BL-20: Перевірка порогу знижки при 6 та 7 ночах проживання")
    void discountBoundaryAtSixAndSevenNights(int nights, String expectedTotalPrice) {
        // Arrange
        BigDecimal pricePerNight = new BigDecimal("100.00");
        BigDecimal expectedPrice = new BigDecimal(expectedTotalPrice);

        // Act
        BigDecimal actualPrice = priceCalculator.calculateTotalPrice(pricePerNight, nights);

        // Assert
        assertEquals(expectedPrice, actualPrice, "Розрахунок вартості зі знижкою не відповідає BL-20");
    }

    // =========================================================================
    // BL-30: Штраф за скасування за 24 години
    // =========================================================================

    @ParameterizedTest(name = "BL-30: Скасування за {0} годин до заїзду (сума 100.00)")
    @CsvSource({
            "25, 0.00",   // >24h: без штрафу
            "24, 0.00",   // =24h: без штрафу -- DEFECT-04
            "23, 50.00"   // <24h: штраф 50% = 50.0
