package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukAirTimePlanTotal extends IVTBase {

	public String o2ProdLRTot = "O2PRODLRTOT";
	public String tukAirTimePlanTotal = "SUBS";	

	public void compareAirTimePlanTotal(String ibmFile, String ncFile) throws Exception {
		
		IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();
		IVTSingleTagCompareFiles ivtSingleTagFunction = new IVTSingleTagCompareFiles();
		OTCCommonTagsFunctionality otcCommonFunction = new OTCCommonTagsFunctionality();

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

		ibmlist = ivtMultiTagCommonFunction.getTagName(ibmFile,ibmtags);
		if(!(ibmlist.isEmpty())){
			ibmMapvalue = ivtSingleTagFunction.convertList2Map(ibmlist);
		}	
		else {
			o2ProdLRTot = null;
		}
		try {
			if(ibmMapvalue.get(o2ProdLRTot) != null && !(ibmMapvalue.get(o2ProdLRTot).isEmpty())) { 
				ibmAirTimeTotalValue = Double.parseDouble(ibmMapvalue.get(o2ProdLRTot));
			}			
		}
		catch(Exception e) {
			System.out.println("IBM Tag Value is not Present");
		} 
		
		ncSubslist = ivtMultiTagCommonFunction.fetchMultiOccurenceTag(ncFile,tukAirTimePlanTotal);
		if(!(ncSubslist.isEmpty())){
			for (String s1 : ncSubslist) {
				subsValue = ivtMultiTagCommonFunction.convertString2Map(s1);
				ncSubsCharge = ivtMultiTagCommonFunction.getOnlyValues(subsValue, 3, "\\|", tukAirTimePlanTotal);
				ncSubsTaxCode = (int)ivtMultiTagCommonFunction.getOnlyValues(subsValue, 6, "\\|", tukAirTimePlanTotal);
				ncAirTimeValue= ncAirTimeValue + otcCommonFunction.taxCodeCalculation(ncSubsCharge, ncSubsTaxCode);			
				subsValue.clear();
			}
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
