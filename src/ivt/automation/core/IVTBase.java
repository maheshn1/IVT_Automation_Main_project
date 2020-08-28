package ivt.automation.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Properties;

public class IVTBase {

	//Reading Properties file code
	//Start Tag List
	//End Tag List
	//checking change
	//Extract Tag Name
	
	static String propFile = "C:\\workspace\\IVT_Automation_Main_Project\\ivtAuto.properties";
	static Properties prop = new Properties();
	static BufferedReader br;
	static LinkedHashMap<String,String> tagNameAndValueIBM = new LinkedHashMap<String,String>();
	static LinkedHashMap<String,String> tagNameAndValueNC = new LinkedHashMap<String,String>();
	
	public  LinkedHashMap<String,String> extractTagNameAndValuesIBM(String str){
    	String tag[] = str.split(" ");
    	String tagname = tag[0];
    	String val = tag[1];
    	String value = val.replaceAll("\\|", "");
    	tagNameAndValueIBM.put(tagname, value);
    	return tagNameAndValueIBM;
    }
	
	public  LinkedHashMap<String,String> extractTagNameAndValuesNC(String str){
    	String tag[] = str.split(" ");
    	String tagname = tag[0];
    	String val = tag[1];
    	String value = val.replaceAll("\\|", "");
    	tagNameAndValueNC.put(tagname, value);
    	return tagNameAndValueNC;
    }
	
	public static String propertyFileRead(String propFileName) throws Exception {
		FileReader fr = new FileReader(propFile);
		prop.load(fr);
		return prop.getProperty(propFileName);
	}
	
}
