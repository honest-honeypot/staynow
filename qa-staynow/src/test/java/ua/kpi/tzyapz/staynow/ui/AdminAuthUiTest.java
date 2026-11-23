package ua.kpi.tzyapz.staynow.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI-тести автентифікації адміністратора «СтейНау» (/admin).
 * Автор: Оніщук Максим (Інженер з тестування / SDET).
 */
@Tag("ui")
@DisplayName("StayNow UI: адмінпанель — форма входу, логін і вихід")
class AdminAuthUiTest {

    private static final String ADMIN_URL = "https://automationintesting.online/admin";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = WebDriverFactory.create();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(ADMIN_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void submitCredentials(String username, String password) {
        WebElement userInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passInput = driver.findElement(By.id("password"));
        userInput.clear();
        userInput.sendKeys(username);
        passInput.clear();
        passInput.sendKeys(password);
        driver.findElement(By.id("doLogin")).click();
    }

    @Test
    @DisplayName("TC-038 (GUI-70): форма входу має заголовок, placeholder-и, маскування пароля та «Front Page»")
    void adminLoginPageDisplaysExpectedFieldsAndAttributes() {
        WebElement heading = driver.findElement(By.tagName("h2"));
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("doLogin"));
        WebElement frontPage = driver.findElement(By.id("frontPageLink"));

        assertAll("Елементи сторінки входу адміністратора",
                () -> assertEquals("Login", heading.getText().trim()),
                () -> assertEquals("Enter username", username.getAttribute("placeholder")),
                () -> assertEquals("password", password.getAttribute("type")),
                () -> assertEquals("Password", password.getAttribute("placeholder")),
                () -> assertTrue(loginBtn.isDisplayed()),
                () -> assertTrue(frontPage.getText().contains("Front Page"))
        );
    }

    @Test
    @DisplayName("TC-024 (F-80): хибний пароль показує «Invalid credentials» і не надає доступу")
    void invalidPasswordShowsErrorAndBlocksAccess() {
        submitCredentials("admin", "wrong-password-1");

        WebElement alert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert.alert-danger")));

        assertAll("Відмова у вході з хибним паролем",
                () -> assertTrue(alert.getText().contains("Invalid credentials"),
                        "Фактичний текст: " + alert.getText()),
                () -> assertTrue(driver.findElement(By.id("doLogin")).isDisplayed()),
                () -> assertFalse(driver.getCurrentUrl().contains("/admin/rooms"))
        );
    }

    @Test
    @DisplayName("TC-023 (F-80): успішний вхід з admin/password відкриває адмінпанель")
    void validAdminLoginOpensAdminPanel() {
        submitCredentials("admin", "password");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("doLogin")));
        WebElement logoutBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[normalize-space()='Logout']")));

        assertAll("Успішна автентифікація адміністратора",
                () -> assertTrue(driver.findElements(By.id("doLogin")).isEmpty()),
                () -> assertTrue(logoutBtn.isDisplayed())
        );
    }

    @Test
    @DisplayName("TC-025 (F-85): кнопка «Logout» завершує сеанс і вимагає повторного входу")
    void logoutEndsAdminSession() {
        submitCredentials("admin", "password");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("doLogin")));

        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Logout']")));
        logoutBtn.click();

        wait.until(d -> !d.getCurrentUrl().contains("/admin/rooms")
                || !d.findElements(By.id("doLogin")).isEmpty());

        driver.get(ADMIN_URL);
        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("doLogin")));
        assertTrue(loginBtn.isDisplayed(), "Після Logout адмінпанель не має бути доступною без повторного входу");
    }
}
