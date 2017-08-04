import framework.JCAnnotation.JCDataProviderParams;
import framework.config.JCFactory;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import framework.config.JCWebDriver;
import framework.util.JCDataProvider;
/**
 * Created by jchaturvedi on 11-07-2017.
 */
public class Login extends JCWebDriver{
   //WebDriver driver ;


    @BeforeTest
    public void beforeTesst()
    {
        //driver=  getDriver();


      //  Reporter.log("Browser invoked  :: \n" + driver.getTitle());
        Reporter.log("BeforeTest ends here ===== \n");

    }
   /* @JCDataProviderParams({"fileName=testdata.xls", "sheetName=Sheet1", "tableName=t1"})
    @Test (dataProvider = "ExcelDataProvider" , dataProviderClass = JCDataProvider.class )
    public void verfyTest(String username)
    {
          Reporter.log("I am inside test ");
          System.out.println("I am  in test");
          driver.findElement(By.xpath("./*//*[@id='identifierId'][@type=\"email\"]")).sendKeys(username);
          driver.findElement(By.xpath("./*//*[@id='identifierNext']/content/span")).click();
          Assert.assertTrue(false);

    }*/


    @JCDataProviderParams({"fileName=testdata.json", "testcaseName=TestcaseName1"})
    @Test (dataProvider = "JSONDataProvider" , dataProviderClass = JCDataProvider.class )
    public void verfyTestJson(JSONObject testData)
    {    WebDriver driver = JCFactory.getDriver();

       // driver = getDriver();

        driver.get("https://gmail.com");

        String username= testData.getString("userName");
        Reporter.log("I am inside test ");
        driver.findElement(By.xpath(".//*[@id='identifierId'][@type=\"email\"]")).sendKeys(username);
        driver.findElement(By.xpath(".//*[@id='identifierNext']/content/span")).click();
        Assert.assertTrue(false);

    }
   /*  covered in my test suite in listner where i am closing the browser after test suite .
   @AfterTest
    public void closeBrowser()
    {
        Reporter.log("I am closing browser");
        quitDriver();
    }*/
}
