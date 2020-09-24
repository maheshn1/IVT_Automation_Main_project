package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukDiscountLineTotal extends IVTBase{

	public String tukDiscountLineTotal="TUKDISCOUNTLINE_TOTAL";
	public String accDiscPeriodDiscount="ACCDISCPERIODDISCOUNT", accDiscTaxCode ="ACCDISCTAXCODE";	
	public String accDiscTaxCodeFormula = null;
	
	public void compareDiscountLineTotal(String fileIBM, String fileNC) throws Exception {
		
		IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();
		IVTSingleTagCompareFiles ivtSingleTagFunction = new IVTSingleTagCompareFiles();
		
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

		accDiscTaxCodeFormula = propertyFileRead(tukDiscountLineTotal);
		
		discTagsNC.add(tukDiscountLineTotal);
		ncDisclist = ivtMultiTagCommonFunction.getTagName(fileNC,discTagsNC);
		if(!ncDisclist.isEmpty()) {
		discTagsAndValueNC = ivtSingleTagFunction.convertList2Map(ncDisclist);
		}
		else
		{
			tukDiscountLineTotal = null;
		}
		try {
			if(discTagsAndValueNC.get(tukDiscountLineTotal) != null && !(discTagsAndValueNC.get(tukDiscountLineTotal).isEmpty())) { 
				ncFinalDiscValue = Double.parseDouble(discTagsAndValueNC.get(tukDiscountLineTotal));
			}			
		}
		catch(Exception e) {
			System.out.println("NC Tag Value is not Present for the Tag:"+tukDiscountLineTotal);
		} 
				
		discTagsIBM.add(accDiscPeriodDiscount);
		ibmDisclist = ivtMultiTagCommonFunction.getTagName(fileIBM,discTagsIBM);
		if(!ibmDisclist.isEmpty()) {
		discTagsAndValueIBM = ivtSingleTagFunction.convertList2Map(ibmDisclist);
		}
		else
		{
			accDiscPeriodDiscount = null;
		}
		
		ibmDiscValue = Double.parseDouble(discTagsAndValueIBM.get(accDiscPeriodDiscount));
		accDiscTaxCodeList = ivtMultiTagCommonFunction.fetchMultiOccurenceTag(fileIBM,accDiscTaxCode);
		for (String s1 : accDiscTaxCodeList) {
			taxTotalValue = ivtMultiTagCommonFunction.convertString2Map(s1);
			taxCode = (int)ivtMultiTagCommonFunction.getOnlyValues(taxTotalValue, 2, ",", accDiscTaxCode);
			actualCost = ivtMultiTagCommonFunction.getOnlyValues(taxTotalValue, 6, ",", accDiscTaxCode);
			discountAmt = ivtMultiTagCommonFunction.getOnlyValues(taxTotalValue, 7, ",", accDiscTaxCode);

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
