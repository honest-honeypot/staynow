
package ua.kpi.tzyapz.staynow;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PriceCalculatorTest {

    private final PriceCalculator calculator =
            new PriceCalculator();

    // BL-40: Високий сезон — коефіцієнт 1.30
    @Test
    void highSeasonShouldHaveFactor130() {
        LocalDate date = LocalDate.of(2026, 7, 15);

        BigDecimal actual = calculator.seasonFactor(date);

        assertEquals(
                0,
                new BigDecimal("1.30").compareTo(actual)
        );
    }

    // BL-30: П'ятниця є вихідною ніччю
    @Test
    void fridayShouldBeWeekendNight() {
        LocalDate friday = LocalDate.of(2026, 11, 20);

        boolean result = calculator.isWeekendNight(friday);

        assertTrue(
                result,
                "Ніч із п'ятниці на суботу має бути вихідною"
        );
    }

    // BL-50: Знижка 10% за 7 ночей
    @Test
    void sevenNightsShouldGetDiscount() {
        BigDecimal baseRate = new BigDecimal("100.00");

        LocalDate checkIn = LocalDate.of(2026, 4, 6);
        LocalDate checkOut = LocalDate.of(2026, 4, 13);

        BigDecimal actual = calculator.total(
                baseRate, checkIn, checkOut
        );

        // 5 звичайних ночей: 500 грн
        // 2 вихідні ночі: 230 грн
        // 730 * 0.90 = 657 грн
        BigDecimal expected = new BigDecimal("657.00");

        assertEquals(
                0,
                expected.compareTo(actual),
                "За 7 ночей повинна застосовуватися знижка 10%"
        );
    }
}
