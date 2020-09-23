package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukPaperFeeNew extends IVTBase{


	public static String o2ProdBotTot="O2PRODBOTOT";
	public static String tukPaperFee="Paper Bill Charge Type",tukMonthlyExtra_Total="SAPROD";	
	public static String tukPaperFeeFormula = null;
	
	public static void compareTukPaperFee(String fileIBM, String fileNC) throws Exception {

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

		tukPaperFeeFormula = IVTBase.propertyFileRead(o2ProdBotTot);
		
		tagsIBM.add(o2ProdBotTot);
		ibmlist = IVTMultiTagCommonFunctionalities.getTagName(fileIBM,tagsIBM);
		if(!(ibmlist.isEmpty())) {
		tagNameAndValueIBM = IVTSingleTagCompareFiles.convertList2Map(ibmlist);
		}
		else {
			o2ProdBotTot = null;
		}
		try {
			if(tagNameAndValueIBM.get(o2ProdBotTot) != null && !(tagNameAndValueIBM.get(o2ProdBotTot).isEmpty())) { 
				ibmValue =  IVTMultiTagCommonFunctionalities.sumOfTagValues(tagNameAndValueIBM);
			}			
		}
		catch(Exception e) {
			System.out.println("IBM Tag Value is not Present");
		} 
	
		ncTukPaperFee = OTCCommonTagsFunctionality.fetchOTCPriceTags(fileNC,"OTCTYPENAME",tukPaperFee);
				
		ncSaProdlist = IVTMultiTagCommonFunctionalities.fetchMultiOccurenceTag(fileNC,tukMonthlyExtra_Total);
		for (String s1 : ncSaProdlist) {
			SaProdValue = IVTMultiTagCommonFunctionalities.convertString2Map(s1);
			nctukMonthlyExtraCharge = IVTMultiTagCommonFunctionalities.getOnlyValues(SaProdValue, 3, "\\|", tukMonthlyExtra_Total);
			nctukMonthlyExtraTaxCode = (int)IVTMultiTagCommonFunctionalities.getOnlyValues(SaProdValue, 6, "\\|", tukMonthlyExtra_Total);
			saProdFinalValue= saProdFinalValue + OTCCommonTagsFunctionality.taxCodeCalculation(nctukMonthlyExtraCharge, nctukMonthlyExtraTaxCode);			
			SaProdValue.clear();
		}

		otcPriceSum = OTCCommonTagsFunctionality.fetchOTCPriceTags(fileNC,"OTCNAME","Bolt On");
		
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

