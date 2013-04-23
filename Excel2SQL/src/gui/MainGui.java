package gui;

import io.ExcelIO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.Column;
import data.Sheet;

public class MainGui extends JFrame implements SyntaxPaneListener {
    private static int ICONSIZE = 16;

    private JPanel contentPane;
    private JTable table;
    private XSSFWorkbook workbook;
    private JComboBox<String> jcbTableNames;
    private TableNameComboBoxModel boxModel;
    private SheetTableModel tableModel;
    // private TableCellRenderer headerRenderer;
    private JPanel pComboWheresTypes;
    private XSSFSheet xssfSheet;
    private Sheet sheet;
    private JPanel pTable;
    private boolean[] boolArray;
    private ColumnRenderer columnRenderer;
    private JButton btnPreview;
    private final Action prevAction = new PrevAction();
    private final Action openAction = new OpenAction();
    private Preview preview;
    private JPanel panelTFandCB;
    private JLabel lblNewLabel;
    private JTextField textField;
    private JPanel pMain;
    private JTextArea taSheetInfo;
    private File fileExcel;
    private JToolBar toolBar;
    private SyntaxHighlightEditorPane paneSyntax;

    private Vector<String> vectorTables;

    private JPanel pCheckWheres;
    private JTextArea taPreview;

    private static final String[] KEYWORDS = new String[] { "select", "insert",
	    "update", "from", "where", "set", "group", "by", "inner", "join",
	    "order" };
    private StringBuilder sbColumnNames;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    MainGui frame = new MainGui();
		    frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the frame.
     */
    @SuppressWarnings("unchecked")
    public MainGui() {
	// fileExcel = new File("poitest.xlsx");

	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    UIManager
		    .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (fileExcel == null) {

	    JFileChooser fc = new JFileChooser(".");
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setFileFilter(new ExcelFilter());
	    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		String filePath = fc.getSelectedFile().getPath();
		fileExcel = new File(filePath);

	    } else {
		JOptionPane
			.showMessageDialog(this,
				"Es wurde keine Datei ausgewählt.\nDas Programm wird geschlossen");
		System.exit(0);
	    }

	}
	this.setTitle(fileExcel.getName());
	workbook = ExcelIO.loadWorkbookFromFile(fileExcel.getAbsolutePath());
	xssfSheet = workbook.getSheetAt(0);
	sheet = new Sheet(xssfSheet);
	checkValid(sheet);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	vectorTables = getTablesFromWb();
	setBounds(100, 100, 600, 450);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(contentPane);

	paneSyntax = new SyntaxHighlightEditorPane(KEYWORDS);
	paneSyntax.addSyntaxPaneListener(this);
	paneSyntax.addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//		    paneSyntax.highlightStyle();
//		    paneSyntax.findBracketsContent();
		}
	    }

	    @Override
	    public void keyTyped(KeyEvent e) {
		 
		    paneSyntax.highlightStyle();
		    paneSyntax.findBracketsContent();
		
	    }

	});

	tableModel = new SheetTableModel(sheet);
	// headerRenderer = new SheetTableHeaderRenderer();

	columnRenderer = new ColumnRenderer(sheet);

	pMain = new JPanel();
	contentPane.add(pMain, BorderLayout.CENTER);
	pMain.setLayout(new BorderLayout(0, 0));

	// gbl_panelTFandCB.rowWeights = new double[]{1.0, 0.0};
	// gbl_panelTFandCB.columnWeights = new double[]{1.0, 0.0, 0.0};
	// gbl_panel.columnWidths = new int[] { 172, 28, 46, 0 };
	// gbl_panel.rowHeights = new int[] { 20, 0, 0 };
	// gbl_panel.columnWeights = new double[] { 1.0, 0.0, 0.0,
	// Double.MIN_VALUE };
	// gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };

	jcbTableNames = new JComboBox(boxModel = new TableNameComboBoxModel(
		vectorTables));
	jcbTableNames.setSelectedIndex(0);
	GridBagConstraints gbc_jcbTableNames = new GridBagConstraints();
	gbc_jcbTableNames.anchor = GridBagConstraints.WEST;
	gbc_jcbTableNames.fill = GridBagConstraints.HORIZONTAL;
	gbc_jcbTableNames.insets = new Insets(0, 5, 0, 5);
	gbc_jcbTableNames.gridx = 0;
	gbc_jcbTableNames.gridy = 1;

	jcbTableNames.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String sheetName = (String) jcbTableNames.getSelectedItem();
		xssfSheet = workbook.getSheet(sheetName);
		sheet = new Sheet(xssfSheet);
		
		checkValid(sheet);
		resetAll();
	    }
	});

	pTable = new JPanel();

	table = new JTable(tableModel);
	buildColumnRenderer();
	pTable.setLayout(new BorderLayout(0, 0));

	JScrollPane scrollPane = new JScrollPane(table);
	pTable.add(jcbTableNames, BorderLayout.NORTH);
	pTable.add(scrollPane, BorderLayout.CENTER);

	pMain.add(pTable, BorderLayout.CENTER);
	pMain.add(paneSyntax, BorderLayout.NORTH);

	taSheetInfo = new JTextArea();
	taSheetInfo.setRows(4);
	taSheetInfo.setBackground(Color.BLACK);
	taSheetInfo.setForeground(Color.WHITE);
	taSheetInfo.setText(getSheetInfo());
	contentPane.add(taSheetInfo, BorderLayout.SOUTH);

	toolBar = new JToolBar();
	contentPane.add(toolBar, BorderLayout.NORTH);
	// table.setAutoCreateColumnsFromModel(false);
	toolBar.add(openAction);
	toolBar.add(prevAction);

    }

    private void buildColumnRenderer() {
	int iColumns = table.getColumnCount();
	for (int i = 0; i < iColumns; i++) {
	    TableColumn column = table.getColumnModel().getColumn(i);
	    column.setCellRenderer(columnRenderer);
	}
    }

    private Vector<String> getTablesFromWb() {
	int iSheets = workbook.getNumberOfSheets();
	Vector<String> sheetNames = new Vector<String>();
	for (int i = 0; i < iSheets; i++) {
	    sheetNames.add(workbook.getSheetName(i));
	}
	return sheetNames;
    }

    private int getColumnCountFromSheet() {
	int lastRow = xssfSheet.getLastRowNum();
	short lastCol = 0;
	short lastCellInRow;
	XSSFRow row = null;

	for (int i = 0; i < lastRow; i++) {
	    row = xssfSheet.getRow(i);
	    lastCellInRow = row.getLastCellNum();
	    if (lastCellInRow > lastCol) {
		lastCol = lastCellInRow;
	    }
	}
	return lastCol;
    }

    private class PrevAction extends AbstractAction {
	public PrevAction() {
	    super("Preview", getIcon("Find", "general"));
	    putValue(SHORT_DESCRIPTION, "Generiere Statements");
	}

	public void actionPerformed(ActionEvent e) {
	    createStatements(false);
	    preview = new Preview();
	    preview.setText(createStatements(false));
	    preview.setVisible(true);

	}

    }

    private class OpenAction extends AbstractAction {
	public OpenAction() {
	    super("Datei öffnen", getIcon("Open", "general"));
	    putValue(SHORT_DESCRIPTION, "Datei öffnen");
	}

	public void actionPerformed(ActionEvent e) {
	    JFileChooser fc = new JFileChooser(".");
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setFileFilter(new ExcelFilter());
	    if (fc.showOpenDialog(MainGui.this) == JFileChooser.APPROVE_OPTION) {
		String filePath = fc.getSelectedFile().getPath();
		fileExcel = new File(filePath);
		workbook = ExcelIO.loadWorkbookFromFile(fileExcel
			.getAbsolutePath());
		xssfSheet = workbook.getSheetAt(0);
		sheet = new Sheet(xssfSheet);
		checkValid(sheet);
		resetAll();

	    } else {

	    }

	}

    }

    private String createUpdate() {
	String table = textField.getText().trim();
	if (table.equals("") || table == null) {
	    return "";
	}
	int iRows = xssfSheet.getLastRowNum();
	int iCol = getColumnCountFromSheet();
	StringBuilder sbTotal = new StringBuilder();
	StringBuilder sbStmt = new StringBuilder();
	StringBuilder sbWhere = new StringBuilder();
	StringBuilder sbSet = new StringBuilder();
	List<String> listWheres = new ArrayList<String>();
	List<String> listSets = new ArrayList<String>();

	Row currRow;
	for (int i = 1; i <= iRows; i++) {
	    listWheres.clear();
	    listSets.clear();
	    sbStmt.setLength(0);
	    sbWhere.setLength(0);
	    sbSet.setLength(0);
	    sbStmt.append("UPDATE " + table + " ");

	    currRow = xssfSheet.getRow(i);
	    for (int j = 0; j < iCol; j++) {
		if (boolArray[j]) {
		    sbWhere.setLength(0);
		    sbWhere.append(xssfSheet.getRow(0).getCell(j)
			    .getStringCellValue()
			    + " = ");
		    Cell cell = xssfSheet.getRow(i).getCell(j);
		    if (isCellNumeric(cell)) {
			sbWhere.append("" + (int) cell.getNumericCellValue());
		    } else if (isCellString(cell)) {
			String strCleaned = cleanString(cell
				.getStringCellValue());
			sbWhere.append("'" + strCleaned + "'");
		    }
		    listWheres.add(new String(sbWhere.toString()));
		} else {
		    sbSet.setLength(0);
		    sbSet.append(xssfSheet.getRow(0).getCell(j)
			    .getStringCellValue()
			    + " = ");
		    Cell cell = xssfSheet.getRow(i).getCell(j);
		    if (isCellNumeric(cell)) {
			sbSet.append("" + (int) cell.getNumericCellValue());
		    } else if (isCellString(cell)) {
			String strCleaned = cleanString(cell
				.getStringCellValue());
			sbSet.append("'" + strCleaned + "'");
		    }
		    listSets.add(new String(sbSet.toString()));

		}
	    }
	    Iterator<String> iterator = listSets.iterator();
	    if (iterator.hasNext()) {
		sbStmt.append("SET " + iterator.next());

		while (iterator.hasNext()) {
		    sbStmt.append(", " + iterator.next());
		}
		sbStmt.append(" ");
	    }
	    iterator = listWheres.iterator();
	    if (iterator.hasNext()) {
		sbStmt.append(" WHERE " + iterator.next());

		while (iterator.hasNext()) {
		    sbStmt.append(" AND " + iterator.next());
		}
	    }
	    sbTotal.append(sbStmt.toString() + ";\n");
	    System.out.println(sbStmt.toString());
	}
	return new String(sbTotal.toString());
    }

    private String createUpdateFromColumns(boolean preview) {
	String table = textField.getText().trim();
	while (table == null || table.equals("")) {
	    table = JOptionPane
		    .showInputDialog("Bitte erst einen Tabellennamen eingeben");
	    if (table != null) {
		table.trim();
	    }

	}
	textField.setText(table);
	int iRows = sheet.getRowCount() - 1;
	int iCol = sheet.getColCount();
	StringBuilder sbTotal = new StringBuilder();
	StringBuilder sbStmt = new StringBuilder();
	StringBuilder sbWhere = new StringBuilder();
	StringBuilder sbSet = new StringBuilder();
	List<String> listWheres = new ArrayList<String>();
	List<String> listSets = new ArrayList<String>();

	Row currRow;
	for (int i = 0; i < (preview ? 1 : iRows); i++) {
	    listWheres.clear();
	    listSets.clear();
	    sbStmt.setLength(0);
	    sbWhere.setLength(0);
	    sbSet.setLength(0);
	    sbStmt.append("UPDATE " + table + " ");

	    currRow = xssfSheet.getRow(i);
	    for (int j = 0; j < iCol; j++) {
		if (!sheet.getColumn(j).isIgnore()) {
		    if (sheet.getColumn(j).isWhere()) {
			sbWhere.setLength(0);
			sbWhere.append(sheet.getColumn(j).getName() + " = ");
			Cell cell = xssfSheet.getRow(i).getCell(j);
			if (sheet.getColumn(j).getType() == Column.COL_TYPE_NUMBER) {
			    sbWhere.append("" + sheet.getColumn(j).getNumber(i));
			} else if (sheet.getColumn(j).getType() == Column.COL_TYPE_STRING) {
			    String strCleaned = cleanString(sheet.getColumn(j)
				    .getString(i));
			    sbWhere.append("'" + strCleaned + "'");
			}
			listWheres.add(new String(sbWhere.toString()));
		    } else {
			sbSet.setLength(0);
			sbSet.append(sheet.getColumn(j).getName() + " = ");
			Cell cell = xssfSheet.getRow(i).getCell(j);
			if (sheet.getColumn(j).getType() == Column.COL_TYPE_NUMBER) {
			    sbSet.append("" + sheet.getColumn(j).getNumber(i));
			} else if (sheet.getColumn(j).getType() == Column.COL_TYPE_STRING) {
			    String strCleaned = cleanString(sheet.getColumn(j)
				    .getString(i));
			    sbSet.append("'" + strCleaned + "'");
			}
			listSets.add(new String(sbSet.toString()));

		    }
		}
	    }
	    Iterator<String> iterator = listSets.iterator();
	    if (iterator.hasNext()) {
		sbStmt.append("SET " + iterator.next());

		while (iterator.hasNext()) {
		    sbStmt.append(", " + iterator.next());
		}
		sbStmt.append(" ");
	    }
	    iterator = listWheres.iterator();
	    if (iterator.hasNext()) {
		sbStmt.append(" WHERE " + iterator.next());

		while (iterator.hasNext()) {
		    sbStmt.append(" AND " + iterator.next());
		}
	    }
	    sbTotal.append(sbStmt.toString() + ";\n");
	    System.out.println(sbStmt.toString());
	}
	return new String(sbTotal.toString());
    }

    private String createStatements(boolean preview) {
	boolean insOnly = true;
	StringBuilder sbTotal = new StringBuilder();
	StringBuffer sbStatement = new StringBuffer();

	String rawText = new String(paneSyntax.getRawText());
	Pattern bracketPattern = Pattern.compile("P\\{.*?\\}|I\\{.*?\\}");

	Matcher matcher = bracketPattern.matcher(rawText);

	while (matcher.find()) {
	    String s = matcher.group(0);
	    if (matcher.group(0).charAt(0) == 'P') {
		insOnly = false;
	    }
	    s = s.substring(1, s.length() - 1);

	}

	int iRows = sheet.getRowCount() - 1;
	int iCol = sheet.getColCount();

	StringBuilder sbStmt = new StringBuilder();
	StringBuilder sbWhere = new StringBuilder();
	StringBuilder sbSet = new StringBuilder();
	List<String> listWheres = new ArrayList<String>();
	List<String> listSets = new ArrayList<String>();

	Row currRow;
	if (!insOnly) {
	    for (int i = 0; i < (preview ? 1 : iRows); i++) {
		matcher.reset();
		sbStatement.setLength(0);
		while (matcher.find()) {
		    String s = matcher.group(0);
		    s = s.substring(2, s.length() - 1);
		    Column c = sheet.getColumnByName(s);
		    if (c != null) {
			matcher.appendReplacement(sbStatement, "");
			sbStatement.append(c.getValueAsString(i));
		    }

		}
		matcher.appendTail(sbStatement);
		System.out.println(sbStatement.toString());
		sbTotal.append(sbStatement.toString() + "\n");
	    }
	    return sbTotal.toString();
	} else {
	    sbTotal.setLength(0);

	    matcher.reset();
	    sbStatement.setLength(0);
	    while (matcher.find()) {
		String s = matcher.group(0);
		matcher.appendReplacement(sbStatement, "");
		if (s.charAt(0) == 'I') {
		    s = s.substring(2, s.length() - 1);
		    Column c = sheet.getColumnByName(s);
		    if (c != null) {
			sbStatement.append("(");
			for (int i = 0; i < sheet.getRowCount() - 1; i++) {
			    
			    sbStatement.append(c.getValueAsString(i) + ", ");
			}
			
			sbStatement.delete(sbStatement.length()-2, sbStatement.length());
			sbStatement.append(")");
		    }
		    

		}
		
	    }
	    matcher.appendTail(sbStatement);
		
	    return sbStatement.toString();
	}
    }

    private String cleanString(String string) {
	string.trim();
	String patt = "'";
	Pattern r = Pattern.compile(patt);

	StringBuilder sb = new StringBuilder(string);
	Matcher m = r.matcher(sb);
	int start = 0;
	String replString = "\\'";
	while (m.find(start)) {

	    sb.replace(m.start(), m.end(), "\\'");
	    start = m.start() + replString.length();

	}

	patt = "\n";
	r = Pattern.compile(patt);
	m = r.matcher(sb);
	start = 0;
	replString = "\' + char(13) + char(10) + \'";
	while (m.find(start)) {
	    sb.replace(m.start(), m.end(), replString);
	    start = m.start() + replString.length();
	}
	return sb.toString();
    }

    public boolean isCellNumeric(Cell cell) {
	if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
	    return true;
	} else {
	    return false;
	}

    }

    public boolean isCellString(Cell cell) {
	if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	    return true;
	} else {
	    return false;
	}

    }

    public String getSheetInfo() {
	StringBuilder info = new StringBuilder();
	info.append("File: " + fileExcel.getAbsolutePath() + "\n");
	info.append("Aktuelle Tabelle: " + xssfSheet.getSheetName() + "\n");
	info.append(getColumnCountFromSheet() + " Spalten\n");
	info.append(xssfSheet.getLastRowNum() + " Datenzeilen");
	return info.toString();
    }

    public ImageIcon getIcon(String name, String category) {

	String imgLocation = "/toolbarButtonGraphics/" + category + "/" + name
		+ ICONSIZE + ".gif";
	URL url = this.getClass().getResource(imgLocation);
	return new ImageIcon(url);
    }

    public class ExcelFilter extends FileFilter {

	public boolean accept(File f) {
	    if (f.isDirectory()) {
		return true;
	    }

	    String strPath = f.getPath().toUpperCase();

	    if (strPath.endsWith("XLSX")) {
		return true;
	    } else {
		return false;
	    }

	}

	public String getDescription() {
	    return "Excel 2007 - Dateien";
	}
    }

    public void resetAll() {
	sbColumnNames = null;
	tableModel.setSheet(sheet);

	columnRenderer.setSheet(sheet);
	buildColumnRenderer();

	taSheetInfo.setText(getSheetInfo());
	vectorTables = getTablesFromWb();
	boxModel.setVector(vectorTables);

	setVisible(true);

    }

    public void showPreview() {
	taPreview.setText(createUpdateFromColumns(true));
    }

    public void checkValid(Sheet sheet) {
	if (!sheet.isValid()) {
	    JOptionPane.showConfirmDialog(this,
		    "Tabelle kann in diesem Programm nicht benutzt werden");
	}
    }

    @Override
    public void bracketEvent(String content) {
	System.out.println(content);
	Column c = sheet.getColumnByName(content);
	if (c != null) {
	    c.setIgnore(false);
	    System.out.println("yes");
	    if (sbColumnNames == null) {
		setColumnNamesPattern(sheet);
	    }
	    paneSyntax.highlightPattern(sbColumnNames.toString());
	    table.repaint();

	}

    }

    public void setColumnNamesPattern(Sheet sheet) {
	sbColumnNames = new StringBuilder();
	for (Column c : sheet.getColumns()) {
	    sbColumnNames.append("P\\{" + c.getName() + "\\}" + "|I\\{"
		    + c.getName() + "\\}" + "|");
	}
	sbColumnNames
		.delete(sbColumnNames.length() - 1, sbColumnNames.length());
	System.out.println(sbColumnNames.toString());
    }
}
