import io.ExcelIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.Sheet;


public class PoiMain {
    
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args)  {
	XSSFWorkbook workbook = ExcelIO.loadWorkbookFromFile("poitest.xlsx");
	Sheet sheet = new Sheet(workbook.getSheetAt(0));
	sheet.dump();
	
	
    }

}
