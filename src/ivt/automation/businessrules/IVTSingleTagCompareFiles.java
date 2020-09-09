package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.businessrules.Adj;
import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;
import ivt.automation.utils.XlsxFile;

//Actual Comparison of two files
//Comparing method
//in general, we have to compare around 10k GMF files. hence values for file names and total number of GMF files should be input/fetched from Files.Java.
public class IVTSingleTagCompareFiles extends IVTBase {

	public static String line = null;
	public static List<String> singleTagsList = new ArrayList<String>();
	public static LinkedHashMap<String,String> tagNameAndValueIBM = new LinkedHashMap<String,String>();
	public static LinkedHashMap<String,String> tagNameAndValueNC = new LinkedHashMap<String,String>();
	
	public static LinkedHashMap<String, String> getIBMTagNames(String fileName) throws Exception, Exception {
		BufferedReader brIBM = new BufferedReader(new FileReader(fileName));

		while (((line = brIBM.readLine()) != null)) {
			int count = singleTagsList.size();
			for (int i = 0; i < count; i++) {
				if (line.startsWith(singleTagsList.get(i))) {
					tagNameAndValueIBM = extractTagNameAndValuesIBM(line);
					break;
				}
			}
		}
		return tagNameAndValueIBM;
	}
	
	public static LinkedHashMap<String, String> getNCTagNames(String fileName) throws Exception, Exception {
		BufferedReader brNC = new BufferedReader(new FileReader(fileName));

		while (((line = brNC.readLine()) != null)) {
			int count = singleTagsList.size();
			for (int i = 0; i < count; i++) {
				if (line.startsWith(singleTagsList.get(i))) {
					tagNameAndValueNC = extractTagNameAndValuesNC(line);
					break;
				}
			}
		}
		return tagNameAndValueNC;
	}	
		
	public static  LinkedHashMap<String,String> extractTagNameAndValuesIBM(String str){
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
	
	public static  LinkedHashMap<String,String> extractTagNameAndValuesNC(String str){
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
	
	public static void compareIBMAndNCTags(String fileIBM, String fileNC) throws Exception {
		//try {
			singleTagsList = XlsxFile.fetchTagNames(IVTBase.propertyFileRead("IBMNCTAG"),IVTBase.propertyFileRead("NC_IBM_Maps"));
			tagNameAndValueIBM.clear();
			tagNameAndValueIBM = getIBMTagNames(fileIBM);
			tagNameAndValueNC.clear();
			tagNameAndValueNC = getNCTagNames(fileNC);
			
			for (String tag : singleTagsList) {
				String IBMValue = tagNameAndValueIBM.get(tag);
				String NCValue = tagNameAndValueNC.get(tag);
				if (tag.equalsIgnoreCase("ADJ")) {
					ArrayList<String> d = Adj.ADJBusinessRule(IBMValue, NCValue);
					IBMValue = d.get(0);
					NCValue = d.get(1);
				}
				if (!IBMValue.equalsIgnoreCase(NCValue)) {
					System.out.println("Account Number " + Files.ACCOUNTNUMBER + "::Tag:" + tag + " IBM Value:: " + IBMValue
							+ " NC Value:: " + NCValue);
					IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
					IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, tag);
					IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, IBMValue);
					IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tag);
					IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, NCValue);
					IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "NO");
				} else {
					// System.out.println("Account Number "+Account_No+"::Tag:"+tag+" IBM Value::"
						//	 +IBMValue+ " NC Value:: "+NCValue);
					IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
					IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, tag);
					IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, IBMValue);
					IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tag);
					IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, NCValue);
					IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "YES");
				}

				/*
				 * else { if(IBMValue.isEmpty())
				 * System.out.println("Account Number "+Account_No+"::"+
				 * tag+" Tag value not found in IBM file");
				 * 
				 * else if(NCValue.isEmpty()){
				 * System.out.println("Account Number "+Account_No+"::"+
				 * tag+" Tag not value found in NC file"); } else {
				 * System.out.println("Account Number "+Account_No+"::"+
				 * tag+" Tag not value found in both files"); } }
				 */

			}
			singleTagsList.clear();
		//} 
			/*catch (NullPointerException e) {
			System.out.println("Respective tag not found");
		}*/
	}
}
