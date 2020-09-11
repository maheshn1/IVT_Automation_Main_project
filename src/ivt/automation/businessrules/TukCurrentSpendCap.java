package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class TukCurrentSpendCap extends IVTBase{

	public static String o2SpendCapEventsTot = "O2SPENDCAPEVENTSTOT", o2SpendCapEventsTot_Values=null;
	public static String tukCurrentSpendCap = "TUKCURRENTSPENDCAP";	


	public static void compareSpendCap(String fileIBM, String fileNC) throws Exception {
		List<String> ibmTags = new ArrayList<>();
		List<String> ncTags = new ArrayList<>();	
		double ibmSpendCapValue = 0.0;
		double ncSpendCapValue = 0.0;
		double ibmSpendCapValue1 = 0.0;
		double ncSpendCapValue1 = 0.0;

		LinkedHashMap<String,String> ibmMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> ncMap = new LinkedHashMap<String,String>();
		double ibmValue = 0.0;
		double ncValue = 0.0;

		ibmTags = IVTMultiTagCommonFunctionalities.fetchSpendCap(fileIBM, o2SpendCapEventsTot);
		for (String s : ibmTags) {
			ibmMap = IVTMultiTagCommonFunctionalities.convertString2Map(s);
			ibmSpendCapValue = IVTMultiTagCommonFunctionalities.getOnlyValues(ibmMap, 4, ",", o2SpendCapEventsTot);
			ibmSpendCapValue1 = ibmSpendCapValue1 + ibmSpendCapValue;
			ibmMap.clear();
		}

		ncTags = IVTMultiTagCommonFunctionalities.fetchSpendCap(fileNC, tukCurrentSpendCap);
		for (String s1 : ncTags) {
			ncMap = IVTMultiTagCommonFunctionalities.convertString2Map(s1);
			ncSpendCapValue = IVTMultiTagCommonFunctionalities.getOnlyValues(ncMap, 1, "\\|", tukCurrentSpendCap);
			ncSpendCapValue1 = ncSpendCapValue1 + ncSpendCapValue;
			ncMap.clear();
		}

		if(ibmSpendCapValue1!=(ncSpendCapValue1)) {
			System.out.println("Account Number " + Files.ACCOUNTNUMBER + "::Tag Mapping : " + o2SpendCapEventsTot +" vs "+ tukCurrentSpendCap +" IBM Value:: " + ibmSpendCapValue1
					+ " NC Value:: " + ncSpendCapValue1);
			printUnMatchedReportInExcelSheet(o2SpendCapEventsTot, Double.toString(ibmSpendCapValue1), tukCurrentSpendCap, Double.toString(ncSpendCapValue1));
		}
		else
		{
			printMatchedReportInExcelSheet(o2SpendCapEventsTot, Double.toString(ibmSpendCapValue1), tukCurrentSpendCap, Double.toString(ncSpendCapValue1));
		}
	}
}
