package data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Column {
    public static final int COL_TYPE_STRING = 1;
    public static final int COL_TYPE_NUMBER = 2;
    
    private boolean where = false;
    private boolean ignore = true;
    
    private String name;
    private int index;
    private int type;
    
    private List<String> strings;
    private List<Integer> numbers;
    
    
    
    
    public Column(int index, String name, int type) {
	// TODO Auto-generated constructor stub
	this.index = index;
	this.name = name;
	
	this.type = type;
    }
    
    public void setNumberList(List<Integer> numbers) {
	this.numbers = numbers;
    }
    
    public void setStringList(List<String> strings) {
	this.strings = strings;
    }

    public void setIgnore(boolean b) {
	ignore = b;
    }
    
    public boolean isIgnore() {
	return ignore;
    }
    
    public void setWhere(boolean b) {
	where = b;
    }
    
    public boolean isWhere() {
	return where;
	
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public boolean setType(int type) {
        
        if ((this.type == COL_TYPE_NUMBER) && (type == COL_TYPE_STRING)) {
	    strings = new ArrayList<String>();
	    this.type = type;
	    for (Integer i : numbers) {
		strings.add("" + i);
	    }
	    return true;
	} else {
	    if (numbers != null) {
		this.type = type;
		return true;
	    } else {
		return false;
	    }
	}
    }
    
    public String getString(int row) {
	if (this.type == COL_TYPE_STRING) {
	    return strings.get(row);
	} else {
	    return null;
	}
    }
    
    public int getNumber(int row) {
	if (this.type == COL_TYPE_NUMBER) {
	    return numbers.get(row);
	} else {
	    return 0;
	}
    }
    
    public void changeType(int type) {
	
    }
    
    public String getTypeString() {
	if (this.type == COL_TYPE_NUMBER) {
	    return new String("ZAHL");
	} else {
	    return new String("TEXT");
	}
    }
    
    public String getValueAsString(int row) {
	if (this.type == COL_TYPE_STRING) {
	    String s = strings.get(row);
	    s = cleanString(s);
	    return "\'" + s + "\'";
	} else {
	   return "" + numbers.get(row);
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
  	StringBuffer sbString = new StringBuffer();
  	while (m.find()) {

  	    
  	    String s = m.group(0);
  	    m.appendReplacement(sbString, "");
  	    sbString.append(replString);

  	}
  	m.appendTail(sbString);
  	sb = new StringBuilder(sbString.toString());
  	patt = "\n";
  	r = Pattern.compile(patt);
  	m = r.matcher(sb);
  	start = 0;
  	replString = "\' + char(13) + char(10) + \'";
  	sbString.setLength(0);
  	while (m.find()) {
  	    m.appendReplacement(sbString, replString);
  	    
  	}
  	m.appendTail(sbString);
  	return sbString.toString();
      }
}
