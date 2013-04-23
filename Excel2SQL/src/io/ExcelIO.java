package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelIO {
    private File excelFile;
    private XSSFWorkbook workbook;
    
    public static XSSFWorkbook loadWorkbookFromFile(String fileName) {
	File excelFile = new File(fileName);
	XSSFWorkbook workbook = null;
	FileInputStream fs = null;
	try {
	    fs = new FileInputStream(excelFile);
	    workbook = new XSSFWorkbook(fs);
	    
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    if (fs != null) {
		try {
		    fs.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	return workbook;
    }
}
