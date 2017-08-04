package framework.util;


import framework.config.JCExecutionConfig;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Reporter;

/*
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
*/


import jxl.read.biff.BiffException;

import java.io.*;

import java.util.Iterator;


/**
 * Created by jchaturvedi on 14-07-2017.
 */
public class JCExcelUtils {
    /* // private static final String FILE_NAME = "/tmp/MyFirstExcel.xlsx";
    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static XSSFCell Cell;
    private static XSSFRow Row;
    public static Object[][] getTableArray(String FilePath, String SheetName) throws Exception {
        String[][] tabArray = null;
        try {
           FileInputStream excelFile = new FileInputStream(FilePath);
            ExcelWBook = new XSSFWorkbook(excelFile);
            ExcelWSheet = ExcelWBook.getSheetAt(0);
            Iterator<Row> iterator = ExcelWSheet.iterator();
            while (iterator.hasNext()) {
               int row = Row.getLastCellNum();
                System.out.println("===========row nubmer"+ row);

                Row currentRow = iterator.next();
                currentRow.getLastCellNum();
                Iterator<Cell> cellIterator = currentRow.iterator();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();

                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        System.out.print(currentCell.getStringCellValue() + "--");
                    }
                }
                System.out.println();

            }
        }
        catch (FileNotFoundException e){
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        catch (IOException e){
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
       // System.out.println("Tabe array is " + tabArray);
        return(tabArray);

    }
*/

    // create a Singleton class to read a excel file .
    private static JCExcelUtils excelUtil;
    private JCExcelUtils() {
    }
    public static synchronized JCExcelUtils getInstance() {
        if (excelUtil == null) {
            excelUtil = new JCExcelUtils();
        }
        return excelUtil;
    }

    public synchronized String[][] getTableData(String fileName, String sheetName, String tableName) {
        String filePath = new File("").getAbsolutePath() + File.separator + JCExecutionConfig.TEST_DATA_PATH +File.separator + fileName;
        Reporter.log("==============file path find by getAbsolutePath read by JCExcelUtils class is "+new File("").getAbsolutePath() );
        Reporter.log("==============file name read by JCExcelUtils class is "+filePath);

        Workbook workbook = null;
        Sheet sheet;
        String[][] tabArray = null;

        try {
            workbook = Workbook.getWorkbook(new File(filePath));

        } catch (IOException | BiffException e) {
            throw new RuntimeException(e);
        }

        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new RuntimeException("Worksheet not found: [Workbook | Sheet] => [" + fileName + " | " +
                    sheetName + "]");
        }

        int startRow, startCol, endRow, endCol, ci, cj;

        Cell tableStart = sheet.findCell(tableName);
        if (tableStart == null) {
            throw new RuntimeException("Data Table not found: [Workbook | Sheet | Table] => [" + fileName + " | " +
                    sheetName + " | " + tableName + "]");
        }

        startRow = tableStart.getRow();
        startCol = tableStart.getColumn();

        Cell tableEnd = sheet.findCell(tableName, startCol + 1, startRow + 1, 100, 64000, false);
        if (tableEnd == null) {
            throw new RuntimeException("Data Table end marker not found: [Workbook | Sheet | Table] => [" + fileName +
                    " | " + sheetName + " | " + tableName + "]");
        }

        endRow = tableEnd.getRow();
        endCol = tableEnd.getColumn();

        if ((endRow - startRow) < 2 || (endCol - startCol) < 2) {
            return null;
        }

        tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
        ci = 0;

        for (int i = startRow + 1; i < endRow; i++, ci++) {
            cj = 0;
            for (int j = startCol + 1; j < endCol; j++, cj++) {
                // Below code checks if the Excel has keyword 'RANDOM' & generates a random alphanumeric string
                // & prefixes it with 'AUTO' else it will just take the input as specified.
                if (sheet.getCell(j, i).getContents().equalsIgnoreCase("RANDOM")) {
                    String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(6);
                    tabArray[ci][cj] = "AUTO" + randomAlphanumeric;
                } else {
                    tabArray[ci][cj] = sheet.getCell(j, i).getContents();
                }
            }
        }
        return (tabArray);
    }



}
