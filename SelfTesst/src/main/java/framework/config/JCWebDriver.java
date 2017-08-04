package framework.config;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;


import static framework.config.JCDriverType.*;

/**
 * Created by jchaturvedi on 12-07-2017.
 */
public class JCWebDriver {
    private final JCDriverType defaultDriverType = CHROME;
    private final String OSName = JCExecutionConfig.OS_NAME;
    private final String operatingSystem = System.getProperty("os.name").toUpperCase();
    private final String systemArchitecture = System.getProperty("os.arch").toUpperCase();
    private WebDriver webDriver;
    private JCDriverType selectedDriverType;
    private final String browser = JCExecutionConfig.BROWSER_NAME;

    public WebDriver getDriver() {
        if (null == webDriver || ((RemoteWebDriver) webDriver).getSessionId() == null) {
            selectedDriverType = determineEffectiveDriverType();
            DesiredCapabilities desiredCapabilities = selectedDriverType.getDesiredCapabilities();

                instantiateWebDriver(desiredCapabilities);

            webDriver.manage().window().maximize();
        }
        return webDriver;
    }

    public void quitDriver() {
        if (null != webDriver) {
            webDriver.quit();
            webDriver = null;
        }
    }
    private JCDriverType determineEffectiveDriverType() {
        JCDriverType driverType = defaultDriverType;
        try {
            if (browser == null) {
                driverType = valueOf(JCExecutionConfig.BROWSER.toUpperCase());
            } else {
                driverType = valueOf(browser.toUpperCase());
            }
        } catch (IllegalArgumentException ignored) {
            System.err.println("Unknown driver specified, defaulting to '" + driverType + "'...");
        } catch (NullPointerException ignored) {
            System.err.println("No driver specified, defaulting to '" + driverType + "'...");
        }
        return driverType;
    }


    private void instantiateWebDriver(DesiredCapabilities desiredCapabilities) {
        System.out.println(" ");
        System.out.println("Current Operating System: " + operatingSystem);
        System.out.println("Current Architecture: " + systemArchitecture);
        System.out.println("Current Browser Selection: " + selectedDriverType);
        System.out.println(" ");
        webDriver = selectedDriverType.getWebDriverObject(desiredCapabilities);
    }

}
