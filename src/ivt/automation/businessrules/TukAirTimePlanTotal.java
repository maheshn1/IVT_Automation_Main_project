package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukAirTimePlanTotal extends IVTBase {

	public static String o2ProdLRTot = "O2PRODLRTOT";
	public static String tukAirTimePlanTotal = "SUBS";	

	public static void compareAirTimePlanTotal(String ibmFile, String ncFile) throws Exception {

		List<String> ibmlist = new ArrayList<>();
		List<String> ncSubslist = new ArrayList<>();
		LinkedHashMap<String,String> ibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> subsValue = new LinkedHashMap<String,String>();
		List<String> ibmtags = new ArrayList<>();
		double ibmAirTimeTotalValue = 0.0;
		double ncAirTimeValue = 0.0;
		double diff = 0.0;
		double ncSubsCharge = 0.0;
		int ncSubsTaxCode = 0;

		ibmtags.add(o2ProdLRTot);

		ibmlist = IVTMultiTagCommonFunctionalities.getTagName(ibmFile,ibmtags);
		ibmMapvalue = IVTSingleTagCompareFiles.convertList2Map(ibmlist);
		ibmAirTimeTotalValue = Double.parseDouble(ibmMapvalue.get(o2ProdLRTot));
		
		ncSubslist = IVTMultiTagCommonFunctionalities.fetchMultiOccurenceTag(ncFile,tukAirTimePlanTotal);
		for (String s1 : ncSubslist) {
			subsValue = IVTMultiTagCommonFunctionalities.convertString2Map(s1);
			ncSubsCharge = IVTMultiTagCommonFunctionalities.getOnlyValues(subsValue, 3, "\\|", tukAirTimePlanTotal);
			ncSubsTaxCode = (int)IVTMultiTagCommonFunctionalities.getOnlyValues(subsValue, 6, "\\|", tukAirTimePlanTotal);
			ncAirTimeValue= ncAirTimeValue + OTCCommonTagsFunctionality.taxCodeCalculation(ncSubsCharge, ncSubsTaxCode);			
			subsValue.clear();
		}

		if(ibmAirTimeTotalValue!=ncAirTimeValue) {
			if(ibmAirTimeTotalValue > ncAirTimeValue) {
				diff = ibmAirTimeTotalValue - ncAirTimeValue;				
			}
			else {
				diff = ncAirTimeValue - ibmAirTimeTotalValue;
			}
			System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping:" + o2ProdLRTot + " vs "+tukAirTimePlanTotal+"(*)"+" IBM Value:: " + ibmAirTimeTotalValue
					+ " NC Value:: " + ncAirTimeValue);
			printUnMatchedReportInExcelSheet(o2ProdLRTot, Double.toString(ibmAirTimeTotalValue), tukAirTimePlanTotal+"(*)", Double.toString(ncAirTimeValue), Double.toString(diff));
		}
		else
		{
			printMatchedReportInExcelSheet(o2ProdLRTot, Double.toString(ibmAirTimeTotalValue), tukAirTimePlanTotal+"(*)", Double.toString(ncAirTimeValue), Double.toString(diff));
		}
	}
}
