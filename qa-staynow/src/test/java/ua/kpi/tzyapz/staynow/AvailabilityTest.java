package ua.kpi.tzyapz.staynow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юніт-тести вимоги BL-70: перевірка доступності номера та перетину дат бронювання.
 */
@Tag("unit")
@DisplayName("Availability — доступність номерів і перетин дат (BL-70)")
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

    // BUG-02 (Issue #16)
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
}