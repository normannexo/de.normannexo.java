package gui;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class SyntaxHighlightEditorPane extends JTextPane {

    List<String> keywords;
    Pattern pattern;
    SyntaxDocument syntaxDoc;
    List<SyntaxPaneListener> listeners;

    public SyntaxHighlightEditorPane(List<String> keywords) {
	super();
	this.keywords = keywords;
	this.setContentType("text/html");
	if (this.keywords != null) {
	    createPattern();
	}
    }
    
    public void addSyntaxPaneListener(SyntaxPaneListener spl) {
	if (listeners == null) {
	    listeners = new ArrayList<SyntaxPaneListener>();
	}
	listeners.add(spl);
	
    }
    
    public void updateSyntaxPaneListeners(String string) {
	if (listeners != null) {
	    for (SyntaxPaneListener spl : listeners) {
		spl.bracketEvent(string);
	    }
	}
    }

    public SyntaxHighlightEditorPane(String[] arrStr) {
	super();
	this.keywords = new ArrayList<String>();
	syntaxDoc = new SyntaxDocument();
	this.setStyledDocument(syntaxDoc);

	for (String s : arrStr) {
	    keywords.add(s);
	}
	createPattern();
    }

    private void createPattern() {
	StringBuilder sb = new StringBuilder();
	for (String s : keywords) {

	    sb.append("^" + s + "|\\b" + s + "\\b|\\b" + s + "|");
	    s = s.toUpperCase();
	    sb.append("^" + s + "|\\b" + s + "\\b|\\b" + s + "|");
	    s = s.toLowerCase();
	    sb.append("^" + s + "|\\b" + s + "\\b|\\b" + s + "|");
	}
	sb.deleteCharAt(sb.length() - 1);
	System.out.println(sb);
	pattern = Pattern.compile(sb.toString());

    }

    String highlight(String t) {

	StringBuffer sb = new StringBuffer();
	Matcher matcher = pattern.matcher(t);
	matcher.useTransparentBounds(true);
	StringBuilder sbRepl = new StringBuilder();
	while (matcher.find()) {
	    sbRepl.setLength(0);
	    // System.out.println(matcher.group() + " " + matcher.start() + " "
	    // + matcher.end());
	    sbRepl.append("<font face=\"courier new\" color=red><b>"
		    + matcher.group().toUpperCase() + "</b></font>");
	    // sb.replace(matcher.start(), matcher.end(), sbRepl.toString());
	    matcher.appendReplacement(sb, sbRepl.toString());

	}
	matcher.appendTail(sb);
	// System.out.println(sb);

	return (sb.toString());

    }
    
    public void findBracketsContent() {
	String t = new String();
	Pattern bracketPattern = Pattern.compile("P\\{.*?\\}|I\\{.*?\\}");
	try {
	    t = syntaxDoc.getText(0, syntaxDoc.getLength());
	    
	} catch (BadLocationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	Matcher matcher = bracketPattern.matcher(t);
	
	while (matcher.find()) {
	    String s = matcher.group(0);
	    s = s.substring(2, s.length()-1);
	    updateSyntaxPaneListeners(s);

	}
	int pos = this.getCaretPosition();
	syntaxDoc.setBold(pos, pos, false);
    }

    public void highlightStyle() {
	String t = new String();
	int pos = this.getCaretPosition();
	try {
	    t = syntaxDoc.getText(0, syntaxDoc.getLength());
	    
	} catch (BadLocationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	syntaxDoc.setBold(0, t.length(), false);
	Matcher matcher = pattern.matcher(t);
	matcher.useTransparentBounds(true);
	while (matcher.find()) {
	    int length = matcher.end() - matcher.start();
	    try {
		syntaxDoc.remove(matcher.start(), length);
		syntaxDoc.insertString(matcher.start(),matcher.group(0).toUpperCase(), null);
	    } catch (BadLocationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    syntaxDoc.setBold(matcher.start(), matcher.end(), true);
	    this.setCaretPosition(pos);

	}
	pos = this.getCaretPosition();
	syntaxDoc.setBold(pos, pos, false);
	

	

    }
    
    public void highlightPattern(String pattern) {
	highlightStyle();
	Pattern p = Pattern.compile(pattern);
	String s = getRawText();
	
	Matcher matcher = p.matcher(s);
	while (matcher.find()) {
	    String match = matcher.group();
	    int length = matcher.end() - matcher.start();
	    
	    syntaxDoc.setGreen(matcher.start(), matcher.end(), true);

	}
	
    }

    String removeTags(String string) {
	String strPattern = "<(\\w).*?>|</(\\w).*?>";
	StringBuffer sb = new StringBuffer();
	Pattern tagPattern = Pattern.compile(strPattern);
	Matcher tagMatcher = tagPattern.matcher(string);
	while (tagMatcher.find()) {
	    tagMatcher.appendReplacement(sb, "");
	}
	tagMatcher.appendTail(sb);
	return sb.toString();

    }

    String removeWhitespace(String string) {
	String strPattern = "^\\s*|xXxXx\\s*";
	StringBuffer sb = new StringBuffer();
	Pattern tagPattern = Pattern.compile(strPattern);
	Matcher tagMatcher = tagPattern.matcher(string);
	while (tagMatcher.find()) {
	    tagMatcher.appendReplacement(sb, "");
	}
	tagMatcher.appendTail(sb);
	return sb.toString();

    }

    private class SyntaxDocument extends DefaultStyledDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setBold(int start, int end, boolean active) {
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setBold(sas, active);
	   
	    StyleConstants.setForeground(sas, active?Color.RED:Color.BLACK);
	    setCharacterAttributes(start, end - start, sas, false);
	}
	
	public void setGreen(int start, int end, boolean active) {
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    
	   
	    StyleConstants.setForeground(sas, active?new Color(0x129D3D):Color.BLACK);
	    setCharacterAttributes(start, end - start, sas, false);
	    
	}

    }
    
    public String getRawText() {
	try {
	    return syntaxDoc.getText(0, syntaxDoc.getLength());
	} catch (BadLocationException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public void setText(String s) {
	super.setText(s);
	syntaxDoc.setBold(2, 4, true);
    }

}
