package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukPaperFeeNew extends IVTBase{


	public String o2ProdBotTot="O2PRODBOTOT";
	public String tukPaperFee="Paper Bill Charge Type",tukMonthlyExtra_Total="SAPROD";	
	public String tukPaperFeeFormula = null;
	
	public void compareTukPaperFee(String fileIBM, String fileNC) throws Exception {
		
		IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();
		IVTSingleTagCompareFiles ivtSingleTagFunction = new IVTSingleTagCompareFiles();
		OTCCommonTagsFunctionality otcCommonFunction = new OTCCommonTagsFunctionality();

		LinkedHashMap<String,String> tagNameAndValueIBM = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> SaProdValue = new LinkedHashMap<String,String>();
		List<String> tagsIBM = new ArrayList<>();
		List<String> ibmlist = new ArrayList<>();
		List<String> ncSaProdlist = new ArrayList<>();
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
			o2ProdBotTot = null;
		}
		try {
			if(tagNameAndValueIBM.get(o2ProdBotTot) != null && !(tagNameAndValueIBM.get(o2ProdBotTot).isEmpty())) { 
				ibmValue =  ivtMultiTagCommonFunction.sumOfTagValues(tagNameAndValueIBM);
			}			
		}
		catch(Exception e) {
			System.out.println("IBM Tag Value is not Present");
		} 
	
		ncTukPaperFee = otcCommonFunction.fetchOTCPriceTags(fileNC,"OTCTYPENAME",tukPaperFee);
				
		ncSaProdlist = ivtMultiTagCommonFunction.fetchMultiOccurenceTag(fileNC,tukMonthlyExtra_Total);
		for (String s1 : ncSaProdlist) {
			SaProdValue = ivtMultiTagCommonFunction.convertString2Map(s1);
			nctukMonthlyExtraCharge = ivtMultiTagCommonFunction.getOnlyValues(SaProdValue, 3, "\\|", tukMonthlyExtra_Total);
			nctukMonthlyExtraTaxCode = (int)ivtMultiTagCommonFunction.getOnlyValues(SaProdValue, 6, "\\|", tukMonthlyExtra_Total);
			saProdFinalValue= saProdFinalValue + otcCommonFunction.taxCodeCalculation(nctukMonthlyExtraCharge, nctukMonthlyExtraTaxCode);			
			SaProdValue.clear();
		}

		otcPriceSum = otcCommonFunction.fetchOTCPriceTags(fileNC,"OTCNAME","Bolt On");
		
		ncValue = ncTukPaperFee + saProdFinalValue + otcPriceSum;

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

