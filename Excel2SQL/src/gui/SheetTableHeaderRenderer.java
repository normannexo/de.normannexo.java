package gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class SheetTableHeaderRenderer implements
	TableCellRenderer, MouseListener {
    // This method is called each time a column header
    // using this renderer needs to be rendered.
    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
	
	JTableHeader header = table.getTableHeader();
	header.addMouseListener(new MouseAdapter() {

	    @Override
	    public void mouseClicked(MouseEvent e) {
		
		System.out.println("clicked!");
	    }
	    
	});
	JCheckBox checkWhere = new JCheckBox();
	checkWhere.setToolTipText("select as WHERE-Statement");
	checkWhere.addMouseListener(new MouseAdapter() {

	    @Override
	    public void mouseClicked(MouseEvent e) {
		JCheckBox cb = (JCheckBox) e.getSource();
		cb.setSelected(true);
		System.out.println("clicked!");
	    }
	    
	});
	

	JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	panel.setBorder(BorderFactory.createEtchedBorder());
	panel.add(checkWhere);
	panel.add(new JLabel("" + value ));

	
	return panel;
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

    @Override
    public void mouseClicked(MouseEvent e) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void mousePressed(MouseEvent e) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub
	
    }
}
