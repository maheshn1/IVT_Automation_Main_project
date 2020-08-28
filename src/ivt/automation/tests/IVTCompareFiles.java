package ivt.automation.tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;
import ivt.automation.utils.XlsxFile;

//Actual Comparison of two files
//Comparing method
//in general, we have to compare around 10k GMF files. hence values for file names and total number of GMF files should be input/fetched from Files.Java.
public class IVTCompareFiles extends IVTBase {

	public static int ACCOUNT_NUMBER = 0;
	public static int CCA_NUMBER = 1;
	public static int IBMTAG_NUMBER = 2;
	public static int IBMVALUE_NUMBER = 3;
	public static int NCTAG_NUMBER = 4;
	public static int NCVALUE_NUMBER = 5;
	public static int DIFFERENCE_NUMBER = 6;
	public static int FLAG_NUMBER = 7;
	int Tag_row = 1;
	int IBMValue_row = 1;
	int NCValue_row = 1;
	int flag_row = 1;

	String line = null;
	static List<String> singleTagsList = new ArrayList<String>();
	LinkedHashMap<String,String> tagnameAndValueIBM = new LinkedHashMap<String,String>();
	LinkedHashMap<String,String> tagnameAndValueNC = new LinkedHashMap<String,String>();
	List<String> ibmAndNCFiles = new ArrayList<>();
	String IBM,NC;

	public LinkedHashMap<String,String> getIBMTagNames(String fileName) throws Exception, Exception {
		BufferedReader brIBM = new BufferedReader(new FileReader(fileName));

		while(((line = brIBM.readLine()) != null)) {
			int count = singleTagsList.size();
			for(int i=0;i<count;i++) {
				if(line.contains(singleTagsList.get(i))) {
					tagnameAndValueIBM = extractTagNameAndValuesIBM(line);
					break;
				}
			}
		}
		return tagnameAndValueIBM;
	}

	public LinkedHashMap<String,String> getNCTagNames(String fileName) throws Exception, Exception {
		BufferedReader brNC = new BufferedReader(new FileReader(fileName));

		while(((line = brNC.readLine()) != null)) {
			int count = singleTagsList.size();
			for(int i=0;i<count;i++) {
				if(line.contains(singleTagsList.get(i))) {
					tagnameAndValueNC = extractTagNameAndValuesNC(line);
					break;
				}
			}
		}
		return tagnameAndValueNC;
	}

	public  void fetchIBMAndNCFiles() throws Exception {
		IVTExcelReport.createExcelSheet();
		ibmAndNCFiles = Files.searchIBMFileForNCFile();

		for(String str : ibmAndNCFiles) {
			String a[] = str.split("\\|");
			IBM = a[0];
			NC = a[1];
			//System.out.println(IBM+"\n"+NC);
			compareIBMAndNCTags(IBM,NC);
		}
	}

	public void compareIBMAndNCTags(String fileIBM, String fileNC) throws Exception {

		singleTagsList = XlsxFile.fetchTagNames(IVTBase.propertyFileRead("IBMNCTAG"),IVTBase.propertyFileRead("NC_IBM_Maps"));
		tagnameAndValueIBM.clear();
		tagnameAndValueIBM = getIBMTagNames(fileIBM);
		tagnameAndValueNC.clear();		
		tagnameAndValueNC = getNCTagNames(fileNC);
		String Account_No = null;
		for(String tag : singleTagsList) {
			String IBMValue = tagnameAndValueIBM.get(tag);
			String NCValue = tagnameAndValueNC.get(tag);
			if(tag.equals("ACCOUNTNO")) {
				Account_No =  tagnameAndValueIBM.get(tag);
			}
			if(!IBMValue.equalsIgnoreCase(NCValue)) {
				System.out.println("Account Number "+Account_No+"::Tag:"+tag+" IBM Value:: "+IBMValue+ " NC Value:: "+NCValue);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER,Account_No);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER,tag);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER,IBMValue);
				IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER,tag);
				IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER,NCValue);
				IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER,"NO");
			}
			else
			{
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER,Account_No);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER,tag);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER,IBMValue);
				IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER,tag);
				IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER,NCValue);
				IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER,"YES");
			}
		}
		singleTagsList.clear();
	}

	public static void main(String[] args) throws Exception {
		IVTCompareFiles ivtcfiles = new IVTCompareFiles();

		ivtcfiles.fetchIBMAndNCFiles();
	}


}
