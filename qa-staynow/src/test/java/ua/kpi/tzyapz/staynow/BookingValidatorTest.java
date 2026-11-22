package ua.kpi.tzyapz.staynow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("BookingValidator — валідація бронювань і контактів (BL-10, BL-20)")
class BookingValidatorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 6, 10);
    private BookingValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BookingValidator();
    }

    @Test
    @DisplayName("BL-10: коректне бронювання повертає порожній список помилок")
    void validBookingReturnsNoErrors() {
        // Arrange
        LocalDate checkIn = TODAY;
        LocalDate checkOut = TODAY.plusDays(3);

        // Act
        List<BookingError> errors = validator.validate(checkIn, checkOut, 2, 2, TODAY);

        // Assert
        assertTrue(errors.isEmpty(), "Для валідного бронювання не повинно бути помилок: " + errors);
    }

    @ParameterizedTest(name = "BL-10: checkOut={0} не пізніше checkIn={1} -> CHECK_OUT_NOT_AFTER_CHECK_IN")
    @CsvSource({
            "2026-06-15, 2026-06-15",
            "2026-06-14, 2026-06-15"
    })
    void checkOutNotAfterCheckInReturnsError(LocalDate checkOut, LocalDate checkIn) {
        // Act
        List<BookingError> errors = validator.validate(checkIn, checkOut, 1, 2, TODAY);

        // Assert
        assertTrue(errors.contains(BookingError.CHECK_OUT_NOT_AFTER_CHECK_IN),
                "Має повертатися CHECK_OUT_NOT_AFTER_CHECK_IN, отримано: " + errors);
    }

    @ParameterizedTest(name = "BL-10: кількість ночей={0}, допустимо={1}")
    @CsvSource({
            "1, true",
            "2, true",
            "29, true",
            "30, true",
            "31, false"
    })
    void nightsBoundariesValidation(int nights, boolean expectedValid) {
        // Arrange
        LocalDate checkIn = TODAY.plusDays(1);
        LocalDate checkOut = checkIn.plusDays(nights);

        // Act
        List<BookingError> errors = validator.validate(checkIn, checkOut, 1, 2, TODAY);

        // Assert
        boolean hasRangeError = errors.contains(BookingError.NIGHTS_OUT_OF_RANGE);
        assertEquals(!expectedValid, hasRangeError,
                "Для " + nights + " ночей очікувалося valid=" + expectedValid + ", помилки: " + errors);
    }

    @Test
    @DisplayName("BL-10: дата заїзду раніше today повертає CHECK_IN_IN_PAST навіть якщо виїзд у майбутньому")
    void checkInInPastWithFutureCheckOutReturnsError() {
        // Arrange
        LocalDate checkIn = TODAY.minusDays(1);
        LocalDate checkOut = TODAY.plusDays(2);

        // Act
        List<BookingError> errors = validator.validate(checkIn, checkOut, 1, 2, TODAY);

        // Assert
        assertTrue(errors.contains(BookingError.CHECK_IN_IN_PAST),
                "За BL-10 дата заїзду раніше today має повертати CHECK_IN_IN_PAST, отримано: " + errors);
    }

    @ParameterizedTest(name = "BL-10: guests={0} при capacity={1}, допустимо={2}")
    @CsvSource({
            "0, 2, false",
            "1, 2, true",
            "2, 2, true",
            "3, 2, false"
    })
    void guestsBoundariesValidation(int guests, int capacity, boolean expectedValid) {
        // Arrange
        LocalDate checkIn = TODAY.plusDays(1);
        LocalDate checkOut = TODAY.plusDays(3);

        // Act
        List<BookingError> errors = validator.validate(checkIn, checkOut, guests, capacity, TODAY);

        // Assert
        assertEquals(!expectedValid, errors.contains(BookingError.GUESTS_OUT_OF_RANGE),
                "Для guests=" + guests + " і capacity=" + capacity + " отримано: " + errors);
    }

    @Test
    @DisplayName("BL-20: перевірка валідних і невалідних форматів email та телефону")
    void emailAndPhoneFormatsValidation() {
        assertAll("Перевірка isValidEmail та isValidPhone",
                () -> assertTrue(validator.isValidEmail("guest.test+1@staynow.ua")),
                () -> assertFalse(validator.isValidEmail("invalid-email")),
                () -> assertFalse(validator.isValidEmail("user@domain")),
                () -> assertFalse(validator.isValidEmail(null)),
                () -> assertTrue(validator.isValidPhone("+380501234567")),
                () -> assertFalse(validator.isValidPhone("+38050123456")),
                () -> assertFalse(validator.isValidPhone("+3805012345678")),
                () -> assertFalse(validator.isValidPhone("0501234567")),
                () -> assertFalse(validator.isValidPhone(null))
        );
    }

    @Test
    @DisplayName("BL-20 / BL-10: повне перевантаження додає INVALID_EMAIL і INVALID_PHONE, а null-дати кидають NPE")
    void fullValidateChecksContactsAndRejectsNullDates() {
        // Arrange
        LocalDate checkIn = TODAY.plusDays(1);
        LocalDate checkOut = TODAY.plusDays(2);

        // Act
        List<BookingError> errors = validator.validate(
                checkIn, checkOut, 1, 2, TODAY, "bad@", "0501234567");

        // Assert
        assertAll("Контактні помилки та null-аргументи",
                () -> assertTrue(errors.contains(BookingError.INVALID_EMAIL)),
                () -> assertTrue(errors.contains(BookingError.INVALID_PHONE)),
                () -> assertThrows(NullPointerException.class,
                        () -> validator.validate(null, checkOut, 1, 2, TODAY))
        );
    }
}
