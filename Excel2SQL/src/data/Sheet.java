package data;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Sheet {

    XSSFSheet sheet;
    private List<Column> columns;
    private int colCount;
    private int rowCount;
    private boolean valid;

    public Sheet(XSSFSheet xssfSheet) {
	sheet = xssfSheet;
	checkValid(sheet);
	if (!this.isValid()) {

	} else {
	    this.setValid(true);
	    colCount = sheet.getRow(0).getLastCellNum();
	    rowCount = sheet.getLastRowNum() + 1;
	    Cell cell = null;
	    String colName;

	    columns = new ArrayList<Column>();

	    if (rowCount > 1) {
		for (int i = 0; i < colCount; i++) {
		    colName = sheet.getRow(0).getCell(i).getStringCellValue();

		    if (checkCellType(i) == Column.COL_TYPE_STRING) {
			List<String> strings = new ArrayList<String>();
			for (int j = 1; j < rowCount; j++) {
			    if (sheet.getRow(j).getCell(i) != null) {
				if (sheet.getRow(j).getCell(i).getCellType() == cell.CELL_TYPE_STRING) {
				    strings.add(sheet.getRow(j).getCell(i)
					    .getStringCellValue());
				} else if (sheet.getRow(j).getCell(i)
					.getCellType() == cell.CELL_TYPE_NUMERIC) {
				    strings.add(""
					    + (int) sheet.getRow(j).getCell(i)
						    .getNumericCellValue());

				} else {
				    strings.add("");
				}
			    } else {
				strings.add("");
			    }
			}
			Column col = new Column(i, colName,
				Column.COL_TYPE_STRING);
			col.setStringList(strings);
			columns.add(col);

		    } else if (checkCellType(i) == Column.COL_TYPE_NUMBER) {
			List<Integer> numbers = new ArrayList<Integer>();
			for (int j = 1; j < rowCount; j++) {
			    numbers.add((int) sheet.getRow(j).getCell(i)
				    .getNumericCellValue());
			}
			Column col = new Column(i, colName,
				Column.COL_TYPE_NUMBER);
			col.setNumberList(numbers);
			columns.add(col);

		    }
		}
	    }
	}
    }

    private void checkValid(XSSFSheet sheet) {
	if ((sheet.getLastRowNum() < 1)) {
	    this.valid = false;
	} else {
	    this.valid = true;
	}

    }

    private void setValid(boolean b) {
	this.valid = b;

    }

    public int getColCount() {
	return colCount;
    }

    public int getRowCount() {
	return rowCount;
    }

    private Column createColumn() {
	return null;
    }

    public void dump() {
	for (Column c : columns) {
	    System.out.println(c.getName() + " type:" + c.getType());
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < rowCount - 1; i++) {
		if (c.getType() == Column.COL_TYPE_STRING) {
		    sb.append(c.getString(i) + " ");
		} else {
		    sb.append(c.getNumber(i) + " ");
		}
	    }
	    System.out.println(sb.toString());
	}
    }

    public Column getColumn(int colIndex) {
	if (columns != null) {
	    return columns.get(colIndex);
	} else {
	    return null;
	}
    }

    public boolean isValid() {
	// TODO Auto-generated method stub
	return valid;
    }

    public int checkCellType(int colIndex) {
	int cellType = 0;
	Cell cell;
	for (int i = 1; i < rowCount; i++) {
	    cell = sheet.getRow(i).getCell(colIndex);
	    if (cell != null) {
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
		    return Column.COL_TYPE_STRING;
		}
	    } else {
		return Column.COL_TYPE_STRING;
	    }
	}
	return Column.COL_TYPE_NUMBER;
    }

    public Column getColumnByName(String s) {
	for (Column c : columns) {
	    if (c.getName().equals(s)) {
		return c;
	    }
	}
	return null;
    }

    public List<Column> getColumns() {
	return columns;
    }
}
