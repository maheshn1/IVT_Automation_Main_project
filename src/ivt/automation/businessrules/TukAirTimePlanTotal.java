package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukAirTimePlanTotal extends IVTBase {

	
	public void compareAirTimePlanTotal(String ibmFile, String ncFile) throws Exception {

		IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();
		IVTSingleTagCompareFiles ivtSingleTagFunction = new IVTSingleTagCompareFiles();
		OTCCommonTagsFunctionality otcCommonFunction = new OTCCommonTagsFunctionality();

		List<String> ibmlist = new ArrayList<>();
		List<String> ncSubslist = new ArrayList<>();
		LinkedHashMap<String,String> ibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> subsValue = new LinkedHashMap<String,String>();
		List<String> ibmtags = new ArrayList<>();
		String ibmMissingTagValue = "", ncMissingTagValue="";
		String o2ProdLRTot = "O2PRODLRTOT";
		String tukAirTimePlanTotal = "SUBS";	
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
			o2ProdLRTot = "NULL";			
		}
		try {
			if(ibmMapvalue.get(o2ProdLRTot) != null && !(ibmMapvalue.get(o2ProdLRTot).isEmpty())) { 
				ibmAirTimeTotalValue = Double.parseDouble(ibmMapvalue.get(o2ProdLRTot));
			}			
		}
		catch(Exception e) {
			System.out.println(o2ProdLRTot+" Tag Value is missing");
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
		else {
			tukAirTimePlanTotal="NULL";
		}
		
		if(o2ProdLRTot.equalsIgnoreCase("NULL") && tukAirTimePlanTotal.equalsIgnoreCase("NULL")) {
			System.out.println("Respective tags not present for this Customer Account No");
		}		
		else if(o2ProdLRTot.equalsIgnoreCase("NULL") || tukAirTimePlanTotal.equalsIgnoreCase("NULL")) {
			if(o2ProdLRTot.equalsIgnoreCase("NULL")) {
				ibmMissingTagValue = "IBM Tag Missing";
				o2ProdLRTot = "O2PRODLRTOT";				
				printUnMatchedReportInExcelSheet(o2ProdLRTot, ibmMissingTagValue, tukAirTimePlanTotal, Double.toString(ncAirTimeValue), "NA");
			}
			if(tukAirTimePlanTotal.equalsIgnoreCase("NULL")) {
				ncMissingTagValue = "NC Tag Missing";
				tukAirTimePlanTotal = "SUBS";
				printUnMatchedReportInExcelSheet(o2ProdLRTot, Double.toString(ibmAirTimeTotalValue), tukAirTimePlanTotal, ncMissingTagValue, "NA");
			}			
		}		
		else {
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
}
