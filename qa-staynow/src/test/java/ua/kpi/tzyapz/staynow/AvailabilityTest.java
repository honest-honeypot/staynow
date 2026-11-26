package ua.kpi.tzyapz.staynow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести вимог BL-60 (політика скасування) та BL-70 (доступність номерів).
 */
@Tag("unit")
@DisplayName("Availability — політика скасування (BL-60) і доступність номерів (BL-70)")
class AvailabilityTest {

    private Availability availability;

    @BeforeEach
    void setUp() {
        availability = new Availability();
    }

    @Test
    @DisplayName("BL-70: номер доступний без зареєстрованих бронювань")
    void roomIsAvailableWithoutBookings() {
        // Arrange
        String room = "101";
        LocalDate checkIn = LocalDate.of(2026, 7, 10);
        LocalDate checkOut = LocalDate.of(2026, 7, 15);

        // Act
        boolean available = availability.isAvailable(room, checkIn, checkOut);

        // Assert
        assertTrue(available, "Порожній номер має бути доступним для бронювання");
    }

    @Test
    @DisplayName("BL-70: номер недоступний за наявності перетину дат")
    void roomIsUnavailableWhenDatesOverlap() {
        // Arrange
        String room = "101";
        availability.book(room, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 15));

        // Act
        boolean available = availability.isAvailable(room, LocalDate.of(2026, 7, 12), LocalDate.of(2026, 7, 17));

        // Assert
        assertFalse(available, "Номер має бути недоступним при перетині інтервалів дат");
    }


    @Test
    @DisplayName("BL-70: номер доступний, якщо попередній гість виїжджає в день нового заїзду")
    void roomIsAvailableWhenPreviousCheckOutEqualsNewCheckIn() {
        // Arrange
        String room = "101";
        availability.book(room, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 15));

        // Act
        boolean available = availability.isAvailable(room, LocalDate.of(2026, 7, 15), LocalDate.of(2026, 7, 20));

        // Assert
        assertTrue(available, "За BL-70 виїзд у день заїзду іншого гостя дозволено (інтервал напіввідкритий)");
    }



    @ParameterizedTest(name = "BL-60: скасування за {0} днів -> плата 0.00")
    @ValueSource(ints = {7, 8, 30})
    void cancellationFeeFreeAtSevenOrMoreDays(int daysBeforeCheckIn) {
        // Arrange
        BigDecimal total = new BigDecimal("1000.00");

        // Act
        BigDecimal fee = availability.cancellationFee(total, daysBeforeCheckIn);

        // Assert
        assertEquals(new BigDecimal("0.00"), fee,
                "За BL-60 скасування за 7 і більше днів має бути безкоштовним");
    }

    @ParameterizedTest(name = "BL-60: скасування за {0} днів -> 50% ({1})")
    @CsvSource({
            "3, 500.00",
            "4, 500.00",
            "6, 500.00"
    })
    void cancellationFeeHalfBetweenThreeAndSixDays(int daysBeforeCheckIn, String expectedFee) {
        // Arrange
        BigDecimal total = new BigDecimal("1000.00");

        // Act
        BigDecimal fee = availability.cancellationFee(total, daysBeforeCheckIn);

        // Assert
        assertEquals(new BigDecimal(expectedFee), fee);
    }

    @ParameterizedTest(name = "BL-60: скасування за {0} днів (<3) -> 100% (1000.00)")
    @ValueSource(ints = {0, 1, 2})
    void cancellationFeeFullLessThanThreeDays(int daysBeforeCheckIn) {
        // Arrange
        BigDecimal total = new BigDecimal("1000.00");

        // Act
        BigDecimal fee = availability.cancellationFee(total, daysBeforeCheckIn);

        // Assert
        assertEquals(new BigDecimal("1000.00"), fee);
    }

    @Test
    @DisplayName("BL-60: плата за скасування 50% і повернення округлюються за HALF_UP до 2 знаків")
    void cancellationFeeAndRefundRoundedHalfUp() {
        // Arrange
        BigDecimal total1 = new BigDecimal("100.01"); // 50% = 50.005 -> 50.01; refund = 50.00
        BigDecimal total2 = new BigDecimal("250.05"); // 50% = 125.025 -> 125.03; refund = 125.02

        // Assert
        assertAll("Округлення HALF_UP для cancellationFee та refund",
                () -> assertEquals(new BigDecimal("50.01"), availability.cancellationFee(total1, 5)),
                () -> assertEquals(new BigDecimal("50.00"), availability.refund(total1, 5)),
                () -> assertEquals(new BigDecimal("125.03"), availability.cancellationFee(total2, 3)),
                () -> assertEquals(new BigDecimal("125.02"), availability.refund(total2, 3))
        );
    }

    @ParameterizedTest(name = "BL-60: refund за {0} днів від 1000.00 -> {1}")
    @CsvSource({
            "8, 1000.00",
            "5, 500.00",
            "2, 0.00"
    })
    void refundMatchesTotalMinusFeeAcrossTiers(int daysBeforeCheckIn, String expectedRefund) {
        // Arrange
        BigDecimal total = new BigDecimal("1000.00");

        // Act
        BigDecimal refund = availability.refund(total, daysBeforeCheckIn);

        // Assert
        assertEquals(new BigDecimal(expectedRefund), refund);
    }

    @Test
    @DisplayName("BL-60: нульова сума total повертає 0.00 штрафу та 0.00 повернення")
    void zeroTotalReturnsZeroFeeAndRefund() {
        // Arrange
        BigDecimal zero = new BigDecimal("0.00");

        // Assert
        assertAll("Граничне значення total = 0.00",
                () -> assertEquals(new BigDecimal("0.00"), availability.cancellationFee(zero, 2)),
                () -> assertEquals(new BigDecimal("0.00"), availability.refund(zero, 2))
        );
    }

    @Test
    @DisplayName("BL-60: від'ємна або null сума total кидає IllegalArgumentException")
    void negativeOrNullTotalThrowsIllegalArgumentException() {
        assertAll("Некоректна сума total",
                () -> assertThrows(IllegalArgumentException.class,
                        () -> availability.cancellationFee(new BigDecimal("-0.01"), 5)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> availability.cancellationFee(null, 5)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> availability.refund(new BigDecimal("-10.00"), 5))
        );
    }
}
