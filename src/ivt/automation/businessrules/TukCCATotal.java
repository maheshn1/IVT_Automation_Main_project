package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class TukCCATotal extends IVTBase{
	
	public static String invTotalRounded = "INVTOTALROUNDED";
	public static String tuktCCATotal = "TUKTCCA_TOTAL";	
	
	
	public static void compareTukCCATotal(String ccaIbmFile, String ncFile) throws Exception {
		
		List<String> ccaibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();
		LinkedHashMap<String,String> ccaibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> ncMapValue = new LinkedHashMap<>();
		List<String> ccaIbmtags = new ArrayList<>();
		List<String> nctags = new ArrayList<>();
		double IbmCCATotalValue = 0.0;
		double ncCCATotalValue = 0.0;
		
		ccaIbmtags.add(invTotalRounded);
		nctags.add(tuktCCATotal);
		
		ccaibmlist = IVTMultiTagCommonFunctionalities.getTagName(ccaIbmFile,ccaIbmtags);
		ccaibmMapvalue = IVTSingleTagCompareFiles.convertList2Map(ccaibmlist);
		
		nclist = IVTMultiTagCommonFunctionalities.getTagName(ncFile,nctags);
		ncMapValue = IVTSingleTagCompareFiles.convertList2Map(nclist);
		
		IbmCCATotalValue = Double.parseDouble(ccaibmMapvalue.get(invTotalRounded));
		ncCCATotalValue = Double.parseDouble(ncMapValue.get(tuktCCATotal));
		
		if(IbmCCATotalValue!=ncCCATotalValue) {
			System.out.println("Account Number " + Files.ACCOUNTNUMBER + "::Tag Mapping:" + invTotalRounded + " vs "+tuktCCATotal+" IBM Value:: " + IbmCCATotalValue
					+ " NC Value:: " + ncCCATotalValue);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, CCA_NUMBER, Files.CCAFILENAME);			
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, Double.toString(IbmCCATotalValue));
			IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "NO");
		}
		else
		{
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, CCA_NUMBER, Files.CCAFILENAME);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, Double.toString(IbmCCATotalValue));
			IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "YES");
		}
		
	}

}
