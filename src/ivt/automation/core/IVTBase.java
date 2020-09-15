package ivt.automation.core;

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
	public static int ACCOUNT_NUMBER = 0;	
	public static int IBMTAG_NUMBER = 1;
	public static int IBMVALUE_NUMBER = 2;
	public static int NCTAG_NUMBER = 3;
	public static int NCVALUE_NUMBER = 4;
	public static int DIFFERENCE_NUMBER = 5;
	public static int FLAG_NUMBER = 6;

	public static int IBMValue_row = 1;
	public static int NCValue_row = 1;
	public static int flag_row = 1;

	public static String ACCOUNTNUMBER = null;
	public static String CCAFILENAME = null;
	public static String sheetName = "IBM_NC_AllTags";


	public static List<String> ibmAndNCFiles = new ArrayList<>();
	public static List<String> ccaAndNCFiles = new ArrayList<>();
	public static String propFile = "C:\\Users\\094539\\Desktop\\IVT DOCS\\Code\\IVT_Automation_WorkSapce\\IVT_Automation_Main_project\\ivtAuto.properties";
	public static Properties prop = new Properties();

	public static List<String> fetchIBMAndNCFiles() throws Exception {
		IVTExcelReport.createExcelSheet();
		ibmAndNCFiles = Files.searchIBMFileForNCFile();	
		return ibmAndNCFiles;
	}

	public static List<String> fetchCCAAndNCFiles() throws Exception {
		IVTExcelReport.createCCAExcelSheet();
		ccaAndNCFiles = Files.searchCCAFileForNCFile();
		return ccaAndNCFiles;
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

	public static void printUnMatchedReportInExcelSheet(String ibmTag, String ibmValue, String ncTag, String ncValue, String difference) throws Exception 
	{
		IVTExcelReport.setCellValues(sheetName, IBMValue_row, ACCOUNT_NUMBER, ACCOUNTNUMBER);
		IVTExcelReport.setCellValues(sheetName, IBMValue_row, IBMTAG_NUMBER, ibmTag);
		IVTExcelReport.setCellValues(sheetName, IBMValue_row++, IBMVALUE_NUMBER, ibmValue);
		IVTExcelReport.setCellValues(sheetName, NCValue_row, NCTAG_NUMBER, ncTag);
		IVTExcelReport.setCellValues(sheetName, NCValue_row, NCVALUE_NUMBER, ncValue);
		IVTExcelReport.setCellValues(sheetName, NCValue_row++, DIFFERENCE_NUMBER, difference);
		IVTExcelReport.setCellValues(sheetName, flag_row++, FLAG_NUMBER, "NO");
	}
	
	public static void printMatchedReportInExcelSheet(String ibmTag, String ibmValue, String ncTag, String ncValue, String difference) throws Exception 
	{
		IVTExcelReport.setCellValues(sheetName, IBMValue_row, ACCOUNT_NUMBER, ACCOUNTNUMBER);
		IVTExcelReport.setCellValues(sheetName, IBMValue_row, IBMTAG_NUMBER, ibmTag);
		IVTExcelReport.setCellValues(sheetName, IBMValue_row++, IBMVALUE_NUMBER, ibmValue);
		IVTExcelReport.setCellValues(sheetName, NCValue_row, NCTAG_NUMBER, ncTag);
		IVTExcelReport.setCellValues(sheetName, NCValue_row, NCVALUE_NUMBER, ncValue);
		IVTExcelReport.setCellValues(sheetName, NCValue_row++, DIFFERENCE_NUMBER, difference);
		IVTExcelReport.setCellValues(sheetName, flag_row++, FLAG_NUMBER, "YES");
	}


}
