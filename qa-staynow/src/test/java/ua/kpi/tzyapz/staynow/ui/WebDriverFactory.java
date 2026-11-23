package ua.kpi.tzyapz.staynow.ui;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Фабрика WebDriver із підтримкою Chrome і Firefox та перемикачем -Dheadless=false для демо.
 */
final class WebDriverFactory {

    private WebDriverFactory() {}

    static WebDriver create() {
        String browser = System.getProperty("browser", "auto").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        boolean useFirefox = "firefox".equals(browser)
                || ("auto".equals(browser) && isLinuxWithoutChrome());

        if (useFirefox) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            WebDriver driver = new FirefoxDriver(options);
            driver.manage().window().setSize(new Dimension(1280, 900));
            return driver;
        }

        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1280,900");
        return new ChromeDriver(options);
    }

    private static boolean isLinuxWithoutChrome() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (!os.contains("linux")) {
            return false;
        }
        boolean hasChrome = Files.exists(Path.of("/usr/bin/google-chrome"))
                || Files.exists(Path.of("/usr/bin/google-chrome-stable"))
                || Files.exists(Path.of("/usr/bin/chromium"));
        return !hasChrome && Files.exists(Path.of("/usr/bin/firefox"));
    }
}
