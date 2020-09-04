package ivt.automation.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class IVTBase {

	//Reading Properties file code
	//Start Tag List
	//End Tag List
	//checking change
	//Extract Tag Name
	
	static String propFile = "C:\\Users\\094539\\Desktop\\IVT DOCS\\Code\\IVT_Automation_WorkSapce\\IVT_Automation_Main_project\\ivtAuto.properties";
	static Properties prop = new Properties();
	static BufferedReader br;
	static LinkedHashMap<String,String> tagNameAndValueIBM = new LinkedHashMap<String,String>();
	static LinkedHashMap<String,String> tagNameAndValueNC = new LinkedHashMap<String,String>();
	
	public  LinkedHashMap<String,String> extractTagNameAndValuesIBM(String str){
		String tag[] = str.split(" ");
    	String tagname = tag[0];
    	String val, value = "";
    	if(tag.length==2) { 
	    	val = tag[1];
	    	value = val.replaceAll("\\|", "");
	    	tagNameAndValueIBM.put(tagname, value);
	    	return tagNameAndValueIBM;
    	}
    	else {
    		String subValueIBM = StringUtils.substringAfter(str, " "); 
    		tagNameAndValueIBM.put(tagname, subValueIBM);
    		return tagNameAndValueIBM;
    	}		
    }
	
	public  LinkedHashMap<String,String> extractTagNameAndValuesNC(String str){
    	String tag[] = str.split(" ");
    	String tagname = tag[0];
    	String val, value = "";
    	if(tag.length==2) { 
	    	val = tag[1];
	    	value = val.replaceAll("\\|", "");
	    	tagNameAndValueNC.put(tagname, value);
	    	return tagNameAndValueNC;
    	}
    	else {
    		String subValueNC = StringUtils.substringAfter(str, " ");
			tagNameAndValueNC.put(tagname, subValueNC);    		
    		return tagNameAndValueNC;
    	}
    }
	
	public static String propertyFileRead(String propFileName) throws Exception {
		FileReader fr = new FileReader(propFile);
		prop.load(fr);
		return prop.getProperty(propFileName);
	}
	
	public static String[] splitStringValue(String Value, String delimiter) {
		 String newVal[] = Value.split(delimiter);	
		 return newVal;
	}
	
}
