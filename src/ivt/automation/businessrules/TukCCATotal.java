package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;

public class TukCCATotal extends IVTBase{

	public String invTotalRounded = "INVTOTALROUNDED";
	public String tuktCCATotal = "TUKTCCA_TOTAL";
	
	public int ACCOUNT_NUMBER = 0;
	public int CCA_NUMBER = 1;
	public int IBMTAG_NUMBER = 2;
	public int IBMVALUE_NUMBER = 3;
	public int NCTAG_NUMBER = 4;
	public int NCVALUE_NUMBER = 5;
	public int DIFFERENCE_NUMBER = 6;
	public int FLAG_NUMBER = 7;
		
	public int IBMValue_rowCCA = 1;
	public int NCValue_rowCCA = 1;
	public int flag_rowCCA = 1;

	IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();
	IVTSingleTagCompareFiles ivtSingleTagFunction = new IVTSingleTagCompareFiles();
	
	public void compareTukCCATotalSingleFile(String ccaIbmFile, String ncFile) throws Exception {
				
		LinkedHashMap<String,String> ccaibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> ncMapValue = new LinkedHashMap<>();
		List<String> ccaibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();		
		List<String> ccaIbmtags = new ArrayList<>();
		List<String> nctags = new ArrayList<>();
		double IbmCCATotalValue = 0.0;
		double ncCCATotalValue = 0.0;
		double diff = 0.0;
		
		String b[] = ccaIbmFile.split("\\\\");
		int len = b.length;
		CCAFILENAME = b[len-1];
		System.out.println(CCAFILENAME);		
		
		ccaIbmtags.add(invTotalRounded);
		nctags.add(tuktCCATotal);

		ccaibmlist = ivtMultiTagCommonFunction.getTagName(ccaIbmFile,ccaIbmtags);
		if(!(ccaibmlist.isEmpty())){
		ccaibmMapvalue = ivtSingleTagFunction.convertList2Map(ccaibmlist);
		}else
		{
			invTotalRounded = null;
		}
		try {
			if(ccaibmMapvalue.get(invTotalRounded) != null  && !(ccaibmMapvalue.get(invTotalRounded)).isEmpty()) {
		IbmCCATotalValue = Double.parseDouble(ccaibmMapvalue.get(invTotalRounded));
		}
		}
		catch(Exception e) {
			System.out.println("IBM Tag Value is Missing for the Tag:"+invTotalRounded);
		}
		nclist = ivtMultiTagCommonFunction.getTagName(ncFile,nctags);
		if(!nclist.isEmpty()) {
			ncMapValue = ivtSingleTagFunction.convertList2Map(nclist);
		}		
		try {
			if(ncMapValue.get(tuktCCATotal) != null && ncMapValue.get(tuktCCATotal).isEmpty()) {
		ncCCATotalValue = Double.parseDouble(ncMapValue.get(tuktCCATotal));
			}
		}
		catch(Exception e) {
			System.out.println("NC Tag VAlue is Missing for the Tag:"+tuktCCATotal);
		}
		
		if(IbmCCATotalValue!=ncCCATotalValue) {
			if(IbmCCATotalValue > ncCCATotalValue) {
				diff = IbmCCATotalValue - ncCCATotalValue;				
			}
			else {
				diff = ncCCATotalValue - IbmCCATotalValue;
			}
			System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping:" + invTotalRounded + " vs "+tuktCCATotal+" IBM Value:: " + IbmCCATotalValue
					+ " NC Value:: " + ncCCATotalValue);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, ACCOUNT_NUMBER, ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, CCA_NUMBER, CCAFILENAME);			
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA++, IBMVALUE_NUMBER, Double.toString(IbmCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA++, DIFFERENCE_NUMBER, Double.toString(diff));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", flag_rowCCA++, FLAG_NUMBER, "NO");
		}
		else
		{
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, ACCOUNT_NUMBER, ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, CCA_NUMBER, CCAFILENAME);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA++, IBMVALUE_NUMBER, Double.toString(IbmCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA++, DIFFERENCE_NUMBER, Double.toString(diff));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", flag_rowCCA++, FLAG_NUMBER, "YES");
		}

	}

	public void compareTukCCATotalMultiFile(List<String> ccaList, String ncFile) throws Exception {

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
		double diff = 0.0;
		
		ccaIbmtags.add(invTotalRounded);
		nctags.add(tuktCCATotal);

		for(String ccafile : ccaList) {
			String c[] = ccafile.split("\\\\");
			int len = c.length;
			CCAFILENAME = c[len-1];			
			ccaFilesNames= ccaFilesNames + CCAFILENAME+ " | ";
			
			ccaibmlist = ivtMultiTagCommonFunction.getTagName(ccafile,ccaIbmtags);
			if(!ccaibmlist.isEmpty()) {
			ccaibmMapvalue = ivtSingleTagFunction.convertList2Map(ccaibmlist);
			}
			try {
				if(ccaibmMapvalue.get(invTotalRounded) != null && !(ccaibmMapvalue.get(invTotalRounded).isEmpty())) {
			IbmCCATotalValue = Double.parseDouble(ccaibmMapvalue.get(invTotalRounded));
			}
				ccaIbmFinalValue = ccaIbmFinalValue + IbmCCATotalValue;
			}
			catch(Exception e) {
				System.out.println("IBM TAG Value is Missing for the Tag: "+invTotalRounded);
			}
		}
		
		nclist = ivtMultiTagCommonFunction.getTagName(ncFile,nctags);
		if(!nclist.isEmpty()) {
			ncMapValue = ivtSingleTagFunction.convertList2Map(nclist);
		}		
		try {
			if(ncMapValue.get(tuktCCATotal) != null && !(ncMapValue.get(tuktCCATotal).isEmpty())) {
		ncCCATotalValue = Double.parseDouble(ncMapValue.get(tuktCCATotal));
			}
		}
		catch(Exception e) {
			System.out.println("NC Tag VAlue is Missing for the Tag:"+tuktCCATotal);
		}
		
		if(ccaIbmFinalValue!=ncCCATotalValue) {
			
			if(IbmCCATotalValue > ncCCATotalValue) {
				diff = IbmCCATotalValue - ncCCATotalValue;				
			}
			else {
				diff = ncCCATotalValue - IbmCCATotalValue;
			}
			
			System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping:" + invTotalRounded + " vs "+tuktCCATotal+" IBM Value:: " + ccaIbmFinalValue
					+ " NC Value:: " + ncCCATotalValue);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, ACCOUNT_NUMBER, ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, CCA_NUMBER, ccaFilesNames);			
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA++, IBMVALUE_NUMBER, Double.toString(ccaIbmFinalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA++, DIFFERENCE_NUMBER, Double.toString(diff));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", flag_rowCCA++, FLAG_NUMBER, "NO");
		}
		else
		{
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, ACCOUNT_NUMBER, ACCOUNTNUMBER);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, CCA_NUMBER, ccaFilesNames);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA, IBMTAG_NUMBER, invTotalRounded);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", IBMValue_rowCCA++, IBMVALUE_NUMBER, Double.toString(ccaIbmFinalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCTAG_NUMBER, tuktCCATotal);
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA, NCVALUE_NUMBER, Double.toString(ncCCATotalValue));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", NCValue_rowCCA++, DIFFERENCE_NUMBER, Double.toString(diff));
			IVTExcelReport.setCellValues("IBM_NC_CCATags", flag_rowCCA++, FLAG_NUMBER, "YES");
		}		
		ccaFilesNames="";
	}



}
