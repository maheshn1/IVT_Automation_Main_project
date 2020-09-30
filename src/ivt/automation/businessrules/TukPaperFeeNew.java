package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukPaperFeeNew extends IVTBase{

	public void compareTukPaperFee(String fileIBM, String fileNC) throws Exception {

		IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();
		IVTSingleTagCompareFiles ivtSingleTagFunction = new IVTSingleTagCompareFiles();
		OTCCommonTagsFunctionality otcCommonFunction = new OTCCommonTagsFunctionality();

		LinkedHashMap<String,String> tagNameAndValueIBM = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> SaProdValue = new LinkedHashMap<String,String>();
		List<String> tagsIBM = new ArrayList<>();
		List<String> ibmlist = new ArrayList<>();
		List<String> ncSaProdlist = new ArrayList<>();
		String o2ProdBotTot="O2PRODBOTOT";
		String tukPaperFee="Paper Bill Charge Type",tukMonthlyExtra_Total="SAPROD";	
		String tukPaperFeeFormula = null;
		String o2ProdBototMissingTagValue="",tukPaperFeeMissingValue ="", boltOnsMissingValue="", ncMissingValue="";
		double ncValue = 0.0;
		double ibmValue = 0.0;
		double nctukMonthlyExtraCharge = 0.0;
		double ncTukPaperFee = 0.0;
		double saProdFinalValue = 0.0;
		double diff = 0.0;
		double otcPriceSum = 0.0;
		int nctukMonthlyExtraTaxCode = 0;
		

		tukPaperFeeFormula = propertyFileRead(o2ProdBotTot);

		tagsIBM.add(o2ProdBotTot);
		ibmlist = ivtMultiTagCommonFunction.getTagName(fileIBM,tagsIBM);
		if(!(ibmlist.isEmpty())) {
			tagNameAndValueIBM = ivtSingleTagFunction.convertList2Map(ibmlist);
		}
		else {
			o2ProdBotTot = "NULL";
		}
		try {
			if(tagNameAndValueIBM.get(o2ProdBotTot) != null && !(tagNameAndValueIBM.get(o2ProdBotTot).isEmpty())) { 
				ibmValue =  ivtMultiTagCommonFunction.sumOfTagValues(tagNameAndValueIBM);
			}			
		}
		catch(Exception e) {
			System.out.println(o2ProdBotTot+" Tag Value is not Present");
		}

		ncTukPaperFee = otcCommonFunction.fetchOTCPriceTags(fileNC,"OTCTYPENAME",tukPaperFee);
		if(ncTukPaperFee==0.0) {			
			tukPaperFeeMissingValue ="Null";
			System.out.println("PaperFee OTCPrice Value is not Present");
		}

		otcPriceSum = otcCommonFunction.fetchOTCPriceTags(fileNC,"OTCNAME","Bolt On");						

		if(otcPriceSum==0.0) {
			boltOnsMissingValue = "Null";
			System.out.println("Bolt On OTCPrice Value is not Present");
		}

		ncSaProdlist = ivtMultiTagCommonFunction.fetchMultiOccurenceTag(fileNC,tukMonthlyExtra_Total);		
		if(!(ncSaProdlist.isEmpty())) {
			for (String s1 : ncSaProdlist) {
				SaProdValue = ivtMultiTagCommonFunction.convertString2Map(s1);
				nctukMonthlyExtraCharge = ivtMultiTagCommonFunction.getOnlyValues(SaProdValue, 3, "\\|", tukMonthlyExtra_Total);
				nctukMonthlyExtraTaxCode = (int)ivtMultiTagCommonFunction.getOnlyValues(SaProdValue, 6, "\\|", tukMonthlyExtra_Total);
				saProdFinalValue= saProdFinalValue + otcCommonFunction.taxCodeCalculation(nctukMonthlyExtraCharge, nctukMonthlyExtraTaxCode);			
				SaProdValue.clear();
			}
		}
		else {
			tukMonthlyExtra_Total = "NULL";	
			System.out.println("SAPROD Value is not Present");
		}

		try {
			if(Double.toString(ncTukPaperFee) != null && Double.toString(saProdFinalValue) != null && Double.toString(otcPriceSum) != null) { 
				ncValue = ncTukPaperFee + saProdFinalValue + otcPriceSum;
			}			
		}
		catch(Exception e) {
			ncValue = 0.0;
			System.out.println(tukPaperFeeFormula+" Tag Value is not Present");
		}

		if(o2ProdBotTot.equalsIgnoreCase("NULL") && tukPaperFeeMissingValue.equalsIgnoreCase("NULL") && boltOnsMissingValue.equalsIgnoreCase("NULL") && tukMonthlyExtra_Total.equalsIgnoreCase("NULL") ) {
			System.out.println("Respective tags not present for this Customer Account No");
		}		
		else if(o2ProdBotTot.equalsIgnoreCase("NULL") || tukPaperFeeMissingValue.equalsIgnoreCase("NULL") || boltOnsMissingValue.equalsIgnoreCase("NULL") || tukMonthlyExtra_Total.equalsIgnoreCase("NULL")) {
			if(o2ProdBotTot.equalsIgnoreCase("NULL")) {
				o2ProdBototMissingTagValue = "O2ProdBoltOn Tag Missing";
				o2ProdBotTot = "O2PRODBOTOT";				
				printUnMatchedReportInExcelSheet(o2ProdBotTot, o2ProdBototMissingTagValue, tukPaperFeeFormula, Double.toString(ncValue), "NA");
			}
			if(tukPaperFeeMissingValue.equalsIgnoreCase("NULL") || boltOnsMissingValue.equalsIgnoreCase("NULL") || tukMonthlyExtra_Total.equalsIgnoreCase("NULL")) {
				ncMissingValue = "PaperFee/BoltOns/SAProd Missing";
				printUnMatchedReportInExcelSheet(o2ProdBotTot, Double.toString(ibmValue), tukPaperFeeFormula, ncMissingValue, "NA");
			}			
		}		
		else {
			if(ibmValue != ncValue){
				if(ibmValue > ncValue) {
					diff = ibmValue - ncValue;				
				}
				else {
					diff = ncValue - ibmValue;
				}
				System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping:" +o2ProdBotTot+" vs " + tukPaperFeeFormula + "--> IBM Value:: " + ibmValue
						+ " NC Value:: " + ncValue);
				printUnMatchedReportInExcelSheet(o2ProdBotTot, Double.toString(ibmValue), tukPaperFeeFormula, Double.toString(ncValue), Double.toString(diff));
			}
			else
			{
				printMatchedReportInExcelSheet(o2ProdBotTot, Double.toString(ibmValue), tukPaperFeeFormula, Double.toString(ncValue), Double.toString(diff));
			}
		}
	}	
}

