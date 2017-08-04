package framework.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by jchaturvedi on 12-07-2017.
 */
public class JCFactory {

    private static final JCWebDriver driver = new JCWebDriver();
    private static  WebDriverWait wait ;

    public static WebDriver  getDriver(){
        System.out.println("i am in JC Factory");
        return driver.getDriver();

    }

    public static WebDriverWait getWait(){
        wait = new WebDriverWait(getDriver(),JCExecutionConfig.WAIT_TIME);
        return wait;
    }

    public static void setWaitTime(int seconds)
    {
         wait = new WebDriverWait(getDriver(),seconds);
    }

    public static void resetWaitTime(){
        wait = new WebDriverWait(getDriver(),JCExecutionConfig.WAIT_TIME);
    }

    public static void closeDriver(){
        driver.quitDriver();
    }
}
