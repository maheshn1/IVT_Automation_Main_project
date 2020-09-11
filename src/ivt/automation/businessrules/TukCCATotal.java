package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class TukCCATotal extends IVTBase{

	public static String invTotalRounded = "INVTOTALROUNDED";
	public static String tuktCCATotal = "TUKTCCA_TOTAL";
	
	public static int ACCOUNT_NUMBER = 0;
	public static int CCA_NUMBER = 1;
	public static int IBMTAG_NUMBER = 2;
	public static int IBMVALUE_NUMBER = 3;
	public static int NCTAG_NUMBER = 4;
	public static int NCVALUE_NUMBER = 5;
	public static int DIFFERENCE_NUMBER = 6;
	public static int FLAG_NUMBER = 7;
		
	public static int IBMValue_rowCCA = 1;
	public static int NCValue_rowCCA = 1;
	public static int flag_rowCCA = 1;


	public static void compareTukCCATotal(String ccaIbmFile, String ncFile) throws Exception {

		List<String> ccaibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();
		LinkedHashMap<String,String> ccaibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> ncMapValue = new LinkedHashMap<>();
		List<String> ccaIbmtags = new ArrayList<>();
		List<String> nctags = new ArrayList<>();
		double IbmCCATotalValue = 0.0;
		double ncCCATotalValue = 0.0;
		
		String b[] = ccaIbmFile.split("\\\\");
		int len = b.length;
		IVTBase.CCAFILENAME = b[len-1];
		System.out.println(IVTBase.CCAFILENAME);		
		
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
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, ACCOUNT_NUMBER, ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, CCA_NUMBER, CCAFILENAME);			
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA++, IBMVALUE_NUMBER, Double.toString(IbmCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA++, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", flag_rowCCA++, FLAG_NUMBER, "NO");
		}
		else
		{
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, ACCOUNT_NUMBER, ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, CCA_NUMBER, CCAFILENAME);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA++, IBMVALUE_NUMBER, Double.toString(IbmCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA++, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", flag_rowCCA++, FLAG_NUMBER, "YES");
		}

	}

	public static void compareTukCCAListTotal(List<String> ccaList, String ncFile) throws Exception {

		List<String> ccaibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();
		LinkedHashMap<String,String> ccaibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> ncMapValue = new LinkedHashMap<>();
		List<String> ccaIbmtags = new ArrayList<>();
		List<String> nctags = new ArrayList<>();		
		double IbmCCATotalValue = 0.0;
		double ncCCATotalValue = 0.0;
		double ccaIbmFinalValue =0.0;
		String ccaFilesNames =""; 
		
		ccaIbmtags.add(invTotalRounded);
		nctags.add(tuktCCATotal);

		for(String ccafile : ccaList) {
			String c[] = ccafile.split("\\\\");
			int len = c.length;
			IVTBase.CCAFILENAME = c[len-1];			
			ccaFilesNames= ccaFilesNames + IVTBase.CCAFILENAME+ " | ";
			
			ccaibmlist = IVTMultiTagCommonFunctionalities.getTagName(ccafile,ccaIbmtags);
			ccaibmMapvalue = IVTSingleTagCompareFiles.convertList2Map(ccaibmlist);
			IbmCCATotalValue = Double.parseDouble(ccaibmMapvalue.get(invTotalRounded));
			ccaIbmFinalValue = ccaIbmFinalValue + IbmCCATotalValue;
		}
		
		nclist = IVTMultiTagCommonFunctionalities.getTagName(ncFile,nctags);
		ncMapValue = IVTSingleTagCompareFiles.convertList2Map(nclist);			
		ncCCATotalValue = Double.parseDouble(ncMapValue.get(tuktCCATotal));

		if(ccaIbmFinalValue!=ncCCATotalValue) {
			System.out.println("Account Number " + Files.ACCOUNTNUMBER + "::Tag Mapping:" + invTotalRounded + " vs "+tuktCCATotal+" IBM Value:: " + ccaIbmFinalValue
					+ " NC Value:: " + ncCCATotalValue);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, ACCOUNT_NUMBER, ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, CCA_NUMBER, ccaFilesNames);			
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA++, IBMVALUE_NUMBER, Double.toString(ccaIbmFinalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA++, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", flag_rowCCA++, FLAG_NUMBER, "NO");
		}
		else
		{
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, ACCOUNT_NUMBER, ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, CCA_NUMBER, ccaFilesNames);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA++, IBMVALUE_NUMBER, Double.toString(ccaIbmFinalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA++, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", flag_rowCCA++, FLAG_NUMBER, "YES");
		}		
		ccaFilesNames="";
	}



}
