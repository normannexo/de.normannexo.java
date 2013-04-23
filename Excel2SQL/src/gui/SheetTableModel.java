package gui;

import javax.swing.table.AbstractTableModel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import data.Column;
import data.Sheet;

public class SheetTableModel extends AbstractTableModel {

    XSSFSheet xssfSheet;
    Sheet sheet;

    public SheetTableModel(XSSFSheet sheet) {
	this.xssfSheet = sheet;
    }
    
    public SheetTableModel(Sheet sheet) {
	this.sheet = sheet;
    }

    @Override
    public int getRowCount() {
	return sheet.getRowCount() - 1;
    }

    @Override
    public int getColumnCount() {
	return sheet.getColCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
	// rowIndex + 1, denn rowIndex = 0 ist bereits ColumnName
	if (sheet.getColumn(columnIndex).getType() == Column.COL_TYPE_STRING) {
	    return sheet.getColumn(columnIndex).getString(rowIndex);
	} else {
	    return "" + sheet.getColumn(columnIndex).getNumber(rowIndex);
	}

    }

    @Override
    public String getColumnName(int column) {
	return sheet.getColumn(column).getName();
    }

    public void setXSSFSheet(XSSFSheet sheet) {
	this.xssfSheet = sheet;
	fireTableStructureChanged();
	fireTableDataChanged();

    }
    
    public void setSheet(Sheet sheet) {
	this.sheet = sheet;
	fireTableStructureChanged();
	fireTableDataChanged();
    }

}
