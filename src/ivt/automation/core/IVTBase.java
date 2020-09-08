package ivt.automation.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class IVTBase {

	//Reading Properties file code
	//Start Tag List
	//End Tag List
	//checking change
	//Extract Tag Name
	
	public static List<String> ibmAndNCFiles = new ArrayList<>();
	static String propFile = "C:\\Users\\204747\\IVT_WorkSpace\\IVT_Automation_Main_project\\ivtAuto.properties";
	static Properties prop = new Properties();
	static BufferedReader br;
	
	public static void fetchIBMAndNCFiles() throws Exception {
		IVTExcelReport.createExcelSheet();
		ibmAndNCFiles = Files.searchIBMFileForNCFile();		
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
