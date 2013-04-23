package gui;

import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

class TableNameComboBoxModel extends AbstractListModel implements ComboBoxModel {
	private Object selectedObject = null;
	private Vector<String> strings;
	
	public TableNameComboBoxModel(Vector<String> vector) {
	    strings = vector;
	}
	
	public void setVector(Vector<String> vector) {
	    strings = vector;
	    fireContentsChanged(this, -1, -1);
	}
	public void setSelectedItem(Object item) {
		selectedObject = item;
		//dbg("" + selectedObject);
		fireContentsChanged(this, -1, -1);
	}
	public Object getSelectedItem() {
		return selectedObject;
	}
	public Object getElementAt(int i) {
		if (strings != null) {
			return strings.get(i);
		}
		return null;
	}
	public int getSize() {
		if (strings != null) {
		    return strings.size();
		}
		return 0;
	}
}