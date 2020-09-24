package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukCurrentSpendCap extends IVTBase{

	public String o2SpendCapEventsTot = "O2SPENDCAPEVENTSTOT", o2SpendCapEventsTot_Values=null;
	public String tukCurrentSpendCap = "TUKCURRENTSPENDCAP";	


	public void compareSpendCap(String fileIBM, String fileNC) throws Exception {

		IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();

		LinkedHashMap<String,String> ibmMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> ncMap = new LinkedHashMap<String,String>();
		List<String> ibmTags = new ArrayList<>();
		List<String> ncTags = new ArrayList<>();	
		double ibmSpendCapValue = 0.0;
		double ncSpendCapValue = 0.0;
		double ibmSpendCapValue1 = 0.0;
		double ncSpendCapValue1 = 0.0;
		double diff = 0.0;

		ibmTags = ivtMultiTagCommonFunction.fetchMultiOccurenceTag(fileIBM, o2SpendCapEventsTot);
		if(!ibmTags.isEmpty()) {
			for (String s : ibmTags) {
				ibmMap = ivtMultiTagCommonFunction.convertString2Map(s);
				ibmSpendCapValue = ivtMultiTagCommonFunction.getOnlyValues(ibmMap, 4, ",", o2SpendCapEventsTot);
				ibmSpendCapValue1 = ibmSpendCapValue1 + ibmSpendCapValue;
				ibmMap.clear();
			}
		}
		else
		{
			o2SpendCapEventsTot = null;
		}
		ncTags = ivtMultiTagCommonFunction.fetchMultiOccurenceTag(fileNC, tukCurrentSpendCap);
		if(!ncTags.isEmpty()) {
			for (String s1 : ncTags) {
				ncMap = ivtMultiTagCommonFunction.convertString2Map(s1);
				ncSpendCapValue = ivtMultiTagCommonFunction.getOnlyValues(ncMap, 1, "\\|", tukCurrentSpendCap);
				ncSpendCapValue1 = ncSpendCapValue1 + ncSpendCapValue;
				ncMap.clear();
			}
		}
		else
		{
			tukCurrentSpendCap = null;
		}
		if(ibmSpendCapValue1 != ncSpendCapValue1) {
			if(ibmSpendCapValue1 > ncSpendCapValue1) {
				diff = ibmSpendCapValue1 - ncSpendCapValue1;				
			}
			else {
				diff = ncSpendCapValue1 - ibmSpendCapValue1;
			}
			System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping : " + o2SpendCapEventsTot +" vs "+ tukCurrentSpendCap +" IBM Value:: " + ibmSpendCapValue1
					+ " NC Value:: " + ncSpendCapValue1);
			printUnMatchedReportInExcelSheet(o2SpendCapEventsTot, Double.toString(ibmSpendCapValue1), tukCurrentSpendCap, Double.toString(ncSpendCapValue1), Double.toString(diff));
		}
		else
		{
			printMatchedReportInExcelSheet(o2SpendCapEventsTot, Double.toString(ibmSpendCapValue1), tukCurrentSpendCap, Double.toString(ncSpendCapValue1), Double.toString(diff));
		}
	}
}
