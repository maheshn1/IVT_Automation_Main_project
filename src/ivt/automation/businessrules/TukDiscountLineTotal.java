package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukDiscountLineTotal extends IVTBase{

	public static String tukDiscountLineTotal="TUKDISCOUNTLINE_TOTAL";
	public static String accDiscPeriodDiscount="ACCDISCPERIODDISCOUNT", accDiscTaxCode ="ACCDISCTAXCODE";	
	public static String accDiscTaxCodeFormula = null;
	
	public static void compareDiscountLineTotal(String fileIBM, String fileNC) throws Exception {
		
		LinkedHashMap<String,String> discTagsAndValueIBM = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> discTagsAndValueNC = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> taxTotalValue = new LinkedHashMap<String,String>();
		List<String> discTagsIBM = new ArrayList<>();
		List<String> discTagsNC = new ArrayList<>();
		List<String> ibmDisclist = new ArrayList<>();
		List<String> ncDisclist = new ArrayList<>();
		List<String> accDiscTaxCodeList = new ArrayList<>();
		int taxCode = 0;
		double actualCost = 0.0;
		double discountAmt = 0.0;
		double taxTotalSum = 0.0;	
		double ibmDiscValue = 0.0;
		double ncFinalDiscValue = 0.0;
		double ibmFinalDiscValue = 0.0;
		double diff = 0.0;

		accDiscTaxCodeFormula = IVTBase.propertyFileRead(tukDiscountLineTotal);
		
		discTagsNC.add(tukDiscountLineTotal);
		ncDisclist = IVTMultiTagCommonFunctionalities.getTagName(fileNC,discTagsNC);
		discTagsAndValueNC = IVTSingleTagCompareFiles.convertList2Map(ncDisclist);
		ncFinalDiscValue = Double.parseDouble(discTagsAndValueNC.get(tukDiscountLineTotal));

		
		discTagsIBM.add(accDiscPeriodDiscount);
		ibmDisclist = IVTMultiTagCommonFunctionalities.getTagName(fileIBM,discTagsIBM);
		discTagsAndValueIBM = IVTSingleTagCompareFiles.convertList2Map(ibmDisclist);
		ibmDiscValue = Double.parseDouble(discTagsAndValueIBM.get(accDiscPeriodDiscount));

		accDiscTaxCodeList = IVTMultiTagCommonFunctionalities.fetchMultiOccurenceTag(fileIBM,accDiscTaxCode);
		for (String s1 : accDiscTaxCodeList) {
			taxTotalValue = IVTMultiTagCommonFunctionalities.convertString2Map(s1);
			taxCode = (int)IVTMultiTagCommonFunctionalities.getOnlyValues(taxTotalValue, 2, ",", accDiscTaxCode);
			actualCost = IVTMultiTagCommonFunctionalities.getOnlyValues(taxTotalValue, 6, ",", accDiscTaxCode);
			discountAmt = IVTMultiTagCommonFunctionalities.getOnlyValues(taxTotalValue, 7, ",", accDiscTaxCode);

			switch(taxCode) {			
			case 3008 :
				taxTotalSum = taxTotalSum +((actualCost - discountAmt)*0.2);
				break;
			case 3006:
				taxTotalSum = taxTotalSum +((actualCost - discountAmt)*0.05);
				break;
			default:
				taxTotalSum = taxTotalSum +(actualCost - discountAmt);				
			}			
			taxTotalValue.clear();
		}
		
		ibmFinalDiscValue = taxTotalSum + ibmDiscValue;
		
		if(ibmFinalDiscValue != ncFinalDiscValue)
		{
			if(ibmFinalDiscValue > ncFinalDiscValue) {
				diff = ibmFinalDiscValue - ncFinalDiscValue;				
			}
			else {
				diff = ncFinalDiscValue - ibmFinalDiscValue;
			}
			System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping:" +accDiscTaxCodeFormula+" vs " + tukDiscountLineTotal + " IBM Value:: " + ibmFinalDiscValue
					+ " NC Value:: " + ncFinalDiscValue);
			printUnMatchedReportInExcelSheet(accDiscTaxCodeFormula, Double.toString(ibmFinalDiscValue), tukDiscountLineTotal, Double.toString(ncFinalDiscValue), Double.toString(diff));
		}
		else
		{
			printMatchedReportInExcelSheet(accDiscTaxCodeFormula, Double.toString(ibmFinalDiscValue), tukDiscountLineTotal, Double.toString(ncFinalDiscValue), Double.toString(diff));
		}

	}


}
