package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import data.Sheet;

public class ColumnRenderer implements TableCellRenderer {
    private boolean[] boolArray;
    private Sheet sheet;

    // This method is called each time a column header
    // using this renderer needs to be rendered.
    public ColumnRenderer(boolean[] boolArray) {
	this.boolArray = boolArray;
    }

    public ColumnRenderer(Sheet sheet) {
	this.sheet = sheet;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
	JLabel label = new JLabel((String) value);
	label.setOpaque(true);
	label.setBackground(Color.WHITE);
	if (sheet.getColumn(vColIndex).isIgnore()) {
	    label.setBackground(new Color(0xE1E5E2));
	} else {
	    if (sheet.getColumn(vColIndex).isWhere()) {
		label.setForeground(Color.RED);
	    } else {
		label.setForeground(Color.BLACK);
	    }
	}

	return label;
    }

    // The following methods override the defaults for performance reasons
    public void validate() {
    }

    public void revalidate() {
    }

    protected void firePropertyChange(String propertyName, Object oldValue,
	    Object newValue) {
    }

    public void firePropertyChange(String propertyName, boolean oldValue,
	    boolean newValue) {
    }

    public void setBoolArray(boolean[] boolArray) {
	this.boolArray = boolArray;
    }

    public void setSheet(Sheet sheet) {
	this.sheet = sheet;
    }
}
