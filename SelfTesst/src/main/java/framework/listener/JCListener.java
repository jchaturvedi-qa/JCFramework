package framework.listener;

import framework.config.JCExecutionConfig;
import framework.config.JCFactory;
import framework.config.JCWebDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jchaturvedi on 12-07-2017.
 */
public class JCListener extends JCWebDriver implements ITestListener, ISuiteListener, IInvokedMethodListener, IRetryAnalyzer, IAnnotationTransformer, IConfigurationListener {

    /**
     * Invoked each time before a test will be invoked.
     * The <code>ITestResult</code> is only partially filled with the references to
     * class, method, start millis and status.
     *
     * @param result the partially filled <code>ITestResult</code>
     * @see ITestResult#STARTED
     */
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Executing TestCase - " + result.getMethod().getMethodName() + " on thread - " + Thread.currentThread().getId());
        Reporter.log("Executing TestCase - " + result.getMethod().getMethodName() + " on thread - " + Thread.currentThread().getId());
    }

    /**
     * Invoked each time a test succeeds.
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#SUCCESS
     */
    @Override
    public void onTestSuccess(ITestResult result) {
    }

    /**
     * Invoked each time a test fails.
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#FAILURE
     */
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Case Failed - " + result.getMethod().getMethodName());
        Reporter.log("Test Case Failed - " + result.getMethod().getMethodName());
        createScreenshot(result);

    }

    /**
     * Invoked each time a test is skipped.
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#SKIP
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        /**
         *
         *  Raghu - Commenting this because it is hard to debug the test cases that failed in first attempt.
         */

        /*
        if(result.getTestContext().getFailedConfigurations().size() == 0 && result.getTestContext().getSkippedConfigurations().size() == 0){
            if (!(result.getThrowable() instanceof SkipException)) {
                result.getTestContext().getSkippedTests().removeResult(result.getMethod());
            }
        }
        */
    }
    /**
     * Invoked each time a method fails but has been annotated with
     * successPercentage and this failure still keeps it within the
     * success percentage requested.
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#SUCCESS_PERCENTAGE_FAILURE
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }
    /**
     * Invoked after the test class is instantiated and before
     * any configuration method is called.
     *
     * @param context
     */
    @Override
    public void onStart(ITestContext context) {

    }
    /**
     * Invoked after all the tests have run and all their
     * Configuration methods have been called.
     *
     * @param context
     */
    @Override
    public void onFinish(ITestContext context) {

    }
    Date startDate;

    /**
     * This method is invoked before the SuiteRunner starts.
     *
     * @param suite
     */
    @Override
    public void onStart(ISuite suite) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        startDate = new Date();
        System.out.println("START TIME = " + dateFormat.format(startDate));
        Reporter.log("START TIME = " + dateFormat.format(startDate));
    }

    @Override
    public void onFinish(ISuite suite) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = new Date();
        System.out.println("END TIME = " + dateFormat.format(endDate));
        Reporter.log("END TIME = " + dateFormat.format(endDate));
        JCFactory.closeDriver();
    }
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    private void createScreenshot(final ITestResult result) {
        final DateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy HH-mm-ss");
        final String fileName = result.getMethod().getMethodName() + "_" + timeFormat.format(new Date()) + ".png";
       final WebDriver driver = JCFactory.getDriver();
       // final WebDriver driver = getDriver();

        try {
            File scrFile;
            if (driver.getClass().equals(RemoteWebDriver.class)) {
                scrFile = ((TakesScreenshot) new Augmenter().augment(driver))
                        .getScreenshotAs(OutputType.FILE);
            } else {
                scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            }

            String outputDir = result.getTestContext().getOutputDirectory();
            outputDir = outputDir.substring(0, outputDir.lastIndexOf(File.separator)) + "/html";
            final File saved = new File(outputDir, fileName);
            FileUtils.copyFile(scrFile, saved);
            Reporter.log("<a href=\"" + fileName + "\" target=\"_blank\"><b>Screenshot</b></a><br>");
        } catch (IOException e) {
            //TODO
        }
    }

    private static int MAX_RETRY_COUNT = Integer.parseInt(JCExecutionConfig.MAX_RETRY_COUNT);
    AtomicInteger count = new AtomicInteger(MAX_RETRY_COUNT);
    @Override
    public boolean retry(ITestResult result) {
        boolean retry = false;
        if (count.intValue() > 0) {
            if (!(result.getMethod().getMethodName().contains("API"))) {
                createScreenshot(result);
            } else {
               // responseXMLSnapshot(result);
            }
            Reporter.log("Test Case Failed : " + result.getMethod().getMethodName() + ", Retrying " + (MAX_RETRY_COUNT - count.intValue() + 1) + " out of " + MAX_RETRY_COUNT);
            System.out.println("Test Case Failed : " + result.getMethod().getMethodName() + ", Retrying " + (MAX_RETRY_COUNT - count.intValue() + 1) + " out of " + MAX_RETRY_COUNT);
            retry = true;
            count.decrementAndGet();
        }
        return retry;
    }

    /**
     * This method sets the RetryAnalyzer parameter for every test case in order to re-run when a test case fails.
     *
     * @param annotation
     * @param testClass
     * @param testConstructor
     * @param testMethod
     * @author - raghunathtayanna on 2/5/16.
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,Constructor testConstructor, Method testMethod) {
        IRetryAnalyzer retry = annotation.getRetryAnalyzer();
        if (retry == null) {
            annotation.setRetryAnalyzer(JCListener.class);
        }

    }

    /**
     * This method is called whenever a Configuration method fails (@BeforeClass, @AfterClass, @BeforeMethod, @AfterMethod etc..)
     *
     * @param result
     * @author - raghunathtayanna on 03/24/16.
     */

    @Override
    public void onConfigurationFailure(ITestResult result) {
        System.out.println("Configuration Failure - " + result.getMethod().getMethodName());
        Reporter.log("Configuration Failure - " + result.getMethod().getMethodName());
            createScreenshot(result);

    }
    @Override
    public void onConfigurationSuccess(ITestResult result) {
    }

    @Override
    public void onConfigurationSkip(ITestResult result) {
        System.out.println("Configuration Skip - " + result.getMethod().getMethodName());
        Reporter.log("Configuration Skip - " + result.getMethod().getMethodName());
        createScreenshot(result);

    }
}
