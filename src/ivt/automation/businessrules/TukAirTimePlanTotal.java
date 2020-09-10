package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class TukAirTimePlanTotal extends IVTBase {
	
	public static String o2ProdLRTot = "O2PRODLRTOT";
	public static String tukAirTimePlanTotal = "TUKAIRTIMEPLAN_TOTAL";	
	
	public static void compareAirTimePlanTotal(String ibmFile, String ncFile) throws Exception {
		
		List<String> ibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();
		LinkedHashMap<String,String> ibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> ncMapValue = new LinkedHashMap<>();
		List<String> ibmtags = new ArrayList<>();
		List<String> nctags = new ArrayList<>();
		double ibmAirTimeTotalValue = 0.0;
		double ncAirTimeValue = 0.0;
		
		ibmtags.add(o2ProdLRTot);
		nctags.add(tukAirTimePlanTotal);
		
		ibmlist = IVTMultiTagCommonFunctionalities.getTagName(ibmFile,ibmtags);
		ibmMapvalue = IVTSingleTagCompareFiles.convertList2Map(ibmlist);
		
		nclist = IVTMultiTagCommonFunctionalities.getTagName(ncFile,nctags);
		ncMapValue = IVTSingleTagCompareFiles.convertList2Map(nclist);
		
		ibmAirTimeTotalValue = Double.parseDouble(ibmMapvalue.get(o2ProdLRTot));
		ncAirTimeValue = Double.parseDouble(ncMapValue.get(tukAirTimePlanTotal));
		
		if(ibmAirTimeTotalValue!=ncAirTimeValue) {
			System.out.println("Account Number " + Files.ACCOUNTNUMBER + "::Tag Mapping:" + o2ProdLRTot + " vs "+tukAirTimePlanTotal+" IBM Value:: " + ibmAirTimeTotalValue
					+ " NC Value:: " + ncAirTimeValue);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, o2ProdLRTot);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, Double.toString(ibmAirTimeTotalValue));
			IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tukAirTimePlanTotal);
			IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, Double.toString(ncAirTimeValue));
			IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "NO");
		}
		else
		{
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, o2ProdLRTot);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, Double.toString(ibmAirTimeTotalValue));
			IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tukAirTimePlanTotal);
			IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, Double.toString(ncAirTimeValue));
			IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "YES");
		}
		
		
 		
		
	}

}
