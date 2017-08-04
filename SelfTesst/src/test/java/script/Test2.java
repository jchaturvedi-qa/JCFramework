package script;

import framework.config.JCFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Created by jchaturvedi on 29-07-2017.
 */
public class Test2 {

    WebDriver driver ;

    @Test
    public void laucnhWebdriver()
    {
        driver =JCFactory.getDriver();
        driver.get("www.google.co.in");
    }
}
