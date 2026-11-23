package ua.kpi.tzyapz.staynow.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-тести гостьової частини «СтейНау»: навігація, картки номерів, контактна форма.
 * Автор: Оніщук Максим (Інженер з тестування / SDET).
 */
@Tag("ui")
@DisplayName("StayNow UI: головна сторінка, номери та контактна форма")
class HomeAndContactUiTest {

    private static final String BASE_URL = "https://automationintesting.online/";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = WebDriverFactory.create();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.navbar-brand")));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    @Test
    @DisplayName("TC-001 (F-10): навігація «Rooms» веде до секції «Our Rooms»")
    void roomsNavigationShowsOurRoomsSection() {
        WebElement roomsLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("a.nav-link[href*='#rooms']")));
        roomsLink.click();

        WebElement roomsHeading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Our Rooms']")));
        assertAll("Секція Our Rooms",
                () -> assertTrue(roomsHeading.isDisplayed()),
                () -> assertTrue(driver.getCurrentUrl().contains("#rooms"))
        );
    }

    @Test
    @DisplayName("TC-002 (F-10): посилання «Admin» у шапці відкриває сторінку входу /admin")
    void adminLinkOpensLoginPage() {
        WebElement adminLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("a.nav-link[href='/admin']")));
        adminLink.click();

        wait.until(ExpectedConditions.urlContains("/admin"));
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username")));

        assertAll("Перехід до адмінпанелі",
                () -> assertTrue(driver.getCurrentUrl().contains("/admin")),
                () -> assertTrue(usernameField.isDisplayed())
        );
    }

    @Test
    @DisplayName("TC-003 (F-20): картки номерів у «Our Rooms» містять типи та кнопки «Book now»")
    void roomCardsDisplayTypesAndBookNowLinks() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("a[href*='/reservation/']"), 0));

        List<String> titles = driver.findElements(By.cssSelector("h5.card-title"))
                .stream().map(e -> e.getText().trim()).toList();
        List<WebElement> bookLinks = driver.findElements(By.cssSelector("a[href*='/reservation/']"));

        assertAll("Перевірка карток номерів",
                () -> assertTrue(titles.contains("Single"), "Очікувався тип Single у " + titles),
                () -> assertTrue(titles.contains("Double"), "Очікувався тип Double у " + titles),
                () -> assertTrue(titles.contains("Suite"), "Очікувався тип Suite у " + titles),
                () -> assertTrue(bookLinks.size() >= 3, "Очікувалось щонайменше 3 кнопки Book now")
        );
    }

    @Test
    @DisplayName("TC-004 (F-20): кнопка «Book now» відкриває сторінку номера з параметрами checkin і checkout")
    void bookNowTransfersDatesToReservationUrl() {
        WebElement firstBookNow = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='/reservation/']")));
        scrollAndClick(firstBookNow);

        wait.until(ExpectedConditions.urlContains("/reservation/"));
        String currentUrl = driver.getCurrentUrl();

        assertAll("URL сторінки бронювання номера",
                () -> assertTrue(currentUrl.contains("/reservation/"), "URL: " + currentUrl),
                () -> assertTrue(currentUrl.contains("checkin="), "Немає checkin у URL: " + currentUrl),
                () -> assertTrue(currentUrl.contains("checkout="), "Немає checkout у URL: " + currentUrl)
        );
    }

    @Test
    @DisplayName("TC-042 (GUI-85): контактна форма містить 5 обов'язкових полів і кнопку «Submit»")
    void contactFormContainsAllRequiredFieldsAndSubmit() {
        WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement email = driver.findElement(By.id("email"));
        WebElement phone = driver.findElement(By.id("phone"));
        WebElement subject = driver.findElement(By.id("subject"));
        WebElement description = driver.findElement(By.id("description"));
        WebElement submit = driver.findElement(By.xpath("//button[normalize-space()='Submit']"));

        assertAll("Поля контактної форми",
                () -> assertTrue(name.isDisplayed()),
                () -> assertTrue(email.isDisplayed()),
                () -> assertTrue(phone.isDisplayed()),
                () -> assertTrue(subject.isDisplayed()),
                () -> assertEquals("textarea", description.getTagName().toLowerCase()),
                () -> assertTrue(submit.isDisplayed())
        );
    }

    @Test
    @DisplayName("TC-017 (F-70): контактна форма відхиляє тему із 4 символів і показує помилку")
    void contactFormRejectsFourCharSubject() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Test Guest");
        driver.findElement(By.id("email")).sendKeys("qa.staynow.s3@example.com");
        driver.findElement(By.id("phone")).sendKeys("01234567890");
        driver.findElement(By.id("subject")).sendKeys("ABCD");
        driver.findElement(By.id("description")).sendKeys("M".repeat(20));

        WebElement submit = driver.findElement(By.xpath("//button[normalize-space()='Submit']"));
        scrollAndClick(submit);

        WebElement alert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-danger")));
        assertAll("Валідація короткої теми",
                () -> assertTrue(alert.isDisplayed()),
                () -> assertTrue(alert.getText().contains("Subject must be between 5 and 100 characters"),
                        "Фактичний текст помилки: " + alert.getText())
        );
    }

    @Test
    @DisplayName("TC-015 / TC-043 (F-70, GUI-85): мінімальні валідні дані (5/20 символів) показують подяку")
    void contactFormAcceptsMinimalValidInputAndShowsThanks() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys("Test Guest");
        driver.findElement(By.id("email")).sendKeys("qa.staynow.s3@example.com");
        driver.findElement(By.id("phone")).sendKeys("01234567890");
        driver.findElement(By.id("subject")).sendKeys("ABCDE");
        driver.findElement(By.id("description")).sendKeys("M".repeat(20));

        WebElement submit = driver.findElement(By.xpath("//button[normalize-space()='Submit']"));
        scrollAndClick(submit);

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("subject")));
        WebElement contactSection = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("contact")));
        String text = contactSection.getText();

        assertAll("Повідомлення подяки після надсилання",
                () -> assertTrue(text.contains("Test Guest"), "Немає імені гостя у: " + text),
                () -> assertTrue(text.contains("ABCDE"), "Немає теми у: " + text)
        );
    }
}
