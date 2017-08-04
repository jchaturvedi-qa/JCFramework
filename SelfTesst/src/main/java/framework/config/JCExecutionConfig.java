package framework.config;

import java.io.File;
import framework.util.JCPropertyUtil;


/**
 * Created by jchaturvedi on 11-07-2017.
 */
public class JCExecutionConfig {

    public static final String BROWSER;
    public static final int WAIT_TIME;
    public static final String BASE_URL;
    public static final String DRIVER_BASE_URL;
    public static final String ADMIN_BASE_URL;

   /* public static final String DB_URL;
    public static final String DB_USERNAME;
    public static final String DB_NAME;
    public static final String DB_PASSWORD;
    public static final String DB_DECRYPTION_KEY;*/
    public static final String TEST_DATA_PATH;
   /* public static final String LOGIN_PASSWORD;
    public static final String TEMP_DATA_PATH;
    public static final String API_RESPONSE_DIR_PATH;*/
    public static final String ORG_NAME;
   public static final String MAX_RETRY_COUNT;
  //  public static final String SIMULATOR_BASE_URL;
    public static final String OS_NAME;
    public static final String BROWSER_NAME;
  /*  public static final String MAC_SYSTEM_URL;
    public static final String LINUX_SYSTEM_URL;
    public static final String WINDOWS_SYSTEM_URL;
    public static final String API_50_ENDPOINT;
    public static final String SOAP_API_TEST_DATA_PATH;
*/

    static {
        try {
            JCPropertyUtil.getInstance().load(new File("config.properties"));
            BROWSER = JCPropertyUtil.getInstance().getValue("BROWSER");
            WAIT_TIME = Integer.parseInt(JCPropertyUtil.getInstance().getValue("WAIT_TIME"));
            BASE_URL = JCPropertyUtil.getInstance().getValue("HOSTNAME");

            DRIVER_BASE_URL = JCPropertyUtil.getInstance().getValue("DRIVER_HOSTNAME");
            ADMIN_BASE_URL = JCPropertyUtil.getInstance().getValue("ADMIN_HOSTNAME");

          /*  DB_NAME = System.getProperty("cpDBName");
            DB_USERNAME = System.getProperty("cpDBUserName");
            DB_PASSWORD = System.getProperty("cpDBPassword");
            DB_URL = "jdbc:mysql://" + System.getProperty("cpDBHost") + "/" + DB_NAME;
            DB_DECRYPTION_KEY = System.getProperty("cpDBDecryptionKey");
            LOGIN_PASSWORD = System.getProperty("cpVoyagerPassword");*/
            TEST_DATA_PATH = JCPropertyUtil.getInstance().getValue("TEST_DATA_PATH");
        //    TEMP_DATA_PATH = JCPropertyUtil.changeSeparatorIfRequired(System.getProperty("tempDataDirectory"));
            ORG_NAME = System.getProperty("OrgName");
            MAX_RETRY_COUNT = JCPropertyUtil.getInstance().getValue("RETRY_COUNT");
          //  SIMULATOR_BASE_URL = CPPropertyUtil.getInstance().getValue("SIMULATOR_BASE_URL");
            OS_NAME = System.getProperty("OperatingSystem");
            BROWSER_NAME = System.getProperty("BrowserName");
          /*  MAC_SYSTEM_URL = CPPropertyUtil.getInstance().getValue("MAC_SYSTEM_URL");
            LINUX_SYSTEM_URL = CPPropertyUtil.getInstance().getValue("LINUX_SYSTEM_URL");
            WINDOWS_SYSTEM_URL = CPPropertyUtil.getInstance().getValue("WINDOWS_SYSTEM_URL");
            SOAP_API_TEST_DATA_PATH = CPFileUtil.changeSeparatorIfRequired(CPPropertyUtil.getInstance().getValue("SOAP_API_TEST_DATA_PATH"));
            API_50_ENDPOINT = CPPropertyUtil.getInstance().getValue("API_5.0_ENDPOINT");
            API_RESPONSE_DIR_PATH = CPFileUtil.changeSeparatorIfRequired(System.getProperty("apiResponseDirectory"));*/
           // CPFileUtil.createDirectory(API_RESPONSE_DIR_PATH);
          //  CPFileUtil.createDirectory(TEMP_DATA_PATH);

            System.setProperty("org.uncommons.reportng.escape-output", "false");
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Something wrong !!! Check configurations.", e);
        }
    }
}
