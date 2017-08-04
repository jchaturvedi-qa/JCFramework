package framework.util;

import framework.JCAnnotation.JCDataProviderParams;
import framework.config.JCExecutionConfig;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jchaturvedi on 14-07-2017.
 */
public class JCDataProvider {


    private static Map<String, String> resolveDataProviderParams(Method testMethod) {
        if (testMethod == null)
            throw new IllegalArgumentException("Test Method context cannot be null.");

        JCDataProviderParams  args = testMethod.getAnnotation(JCDataProviderParams.class);
        if (args == null)
            throw new IllegalArgumentException("Test Method context has no CPDataProviderParams annotation.");

        if (args.value() == null || args.value().length == 0)
            throw new IllegalArgumentException("Test Method context has a malformed CPDataProviderParams annotation.");

        Map<String, String> arguments = new HashMap<String, String>();
        for (int i = 0; i < args.value().length; i++) {
            String[] parts = args.value()[i].split("=");
            arguments.put(parts[0].trim(), parts[1].trim());
        }
        return arguments;
    }

    /**
     * ExcelDataProvider accepts Method on run time with test method.
     * @param testMethod : this is test method using the data provider .
     * @return Object Arry for data provider
     */
    @DataProvider(name = "ExcelDataProvider")

    public static synchronized Object[][] ExcelDataProvider(Method testMethod) {
        Map<String, String> arguments = resolveDataProviderParams(testMethod);
        Object[][] retObjArr = JCExcelUtils.getInstance().getTableData(arguments.get("fileName"), arguments.get("sheetName"), arguments.get("tableName"));
        Reporter.log("==========Reading Excel Dataprovider paramer passed in annotation for test method " + testMethod.getName()+"\n");
        Reporter.log("==File Name is "+arguments.get("fileName")+"\n");
        Reporter.log("==sheetName is "+arguments.get("sheetName")+"\n");
        Reporter.log("==table Name is "+arguments.get("tableName")+"\n");
        Reporter.log("=============================================================");
        if (retObjArr == null) {
            throw new RuntimeException("Data Table is either malformed or empty [Workbook | Sheet | Table] => [" +
                    arguments.get("fileName") + " | " + arguments.get("sheetName") + " | " +
                    arguments.get("tableName") + "]");
        }
        return (retObjArr);
    }


    /**
     * @param testMethod
     * @return DataProvider for JSON, takes a fileName and testcaseName
     * Expect the file content to be in this format
     * {"TestcaseName1":[
     * {"test1param1":"test1param1val1", "test1param2":"test1param2val"},
     * {"test2param1":"test2param1val1", "test2param2":"test2param2val"},
     * ...
     * {"testNparam1":"testNparam1val1", "testNparam2":"testNparam2val"}
     * ],
     * <p>
     * "TestcaseName2":[
     * {"test1param1":"test1param1val1", "test1param2":"test1param2val"},
     * {"test2param1":"test2param1val1", "test2param2":"test2param2val"},
     * ...
     * {"testNparam1":"testNparam1val1", "testNparam2":"testNparam2val"}
     * ],
     * <p>
     * For given testCaseName, the dataprovider returns one row at a time
     */
    @DataProvider(name = "JSONDataProvider")
    public static synchronized Object[][] jsonDataProvider(Method testMethod) {
        Map<String, String> arguments = resolveDataProviderParams(testMethod);
        try {

            //Read the file
            String filePath = new File("").getAbsolutePath() + File.separator + JCExecutionConfig.TEST_DATA_PATH +
                    File.separator + arguments.get("fileName");
            Reporter.log("==========Reading JSON Dataprovider paramer passed in annotation for test method " + testMethod.getName()+"\n");
            Reporter.log("==File Name is "+arguments.get("fileName")+"\n");
            Reporter.log("==testcase Name is "+arguments.get("testcaseName")+"\n");
            Reporter.log("=============================================================");
            FileInputStream targetStream = new FileInputStream(new File(filePath));
            //to check if file exist at metnioned path or not
            File f = new File(filePath);
            if(!f.exists())
            Reporter.log("No such file exist : Plesae verify your filelocation \n ");
            //=======================
            String testString = IOUtils.toString(targetStream, "UTF8");
            //Take each test json, and populate to a two dimensional array
            JSONObject jsonObject = new JSONObject(testString);
            /**
             * JC- The below code has been added to support Header & body style test date from JSON File.
             * The header data will be specified only once in the test cases. If you need to specify header data,
             * add another tag "headerData" in JCDataProviderParams annotation (See SingleRowReportsTest file for an example)
             * The body will have data that can have multiple test cases with different scenario.
             */
            if (null != arguments.get("headerData")) {
                Reporter.log("<br> This Testcase has Common Data Header");
                JSONObject headerArray = (JSONObject) jsonObject.get(arguments.get("headerData"));
                JSONArray testArray = (JSONArray) jsonObject.get(arguments.get("testcaseName"));
                Object[][] testJsonArray = new Object[testArray.length()][1];
                for (int i = 0; i < testArray.length(); i++) {
                    ((JSONObject) testArray.get(i)).append("headerData", headerArray);
                    testJsonArray[i][0] = testArray.get(i);
                }
                return (testJsonArray);
            } else {
                JSONArray testArray = (JSONArray) jsonObject.get(arguments.get("testcaseName"));

                Object[][] testJsonArray = new Object[testArray.length()][1];
                for (int i = 0; i < testArray.length(); i++) {
                    testJsonArray[i][0] = testArray.get(i);
                }

                return (testJsonArray);
            }

        } catch (Exception ex) {
            throw new RuntimeException("JSON data file is either blank or wrong Key(testCaseName) is defined. => [" +
                    arguments.get("fileName") + " | " +
                    arguments.get("testcaseName") + "] " + ex.toString());

        }
    }


}





