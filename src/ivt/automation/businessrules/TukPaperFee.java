package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukPaperFee extends IVTBase{

	public static String o2ProdBotTot="O2PRODBOTOT";
	public static String tukPaperFee="TUKPAPERFEE",tukMonthlyExtra_Total="TUKMONTHLYEXTRA_TOTAL",tukMonthlyExtra_Total_Values =null;	
	public static String tukPaperFeeFormula = null;		

	public static void compareTukPaperFee(String fileIBM, String fileNC) throws Exception {
		
		LinkedHashMap<String,String> tagNameAndValueIBM = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> tagNameAndValueNC = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> otcPriceValue = new LinkedHashMap<String,String>();
		List<String> tagsIBM = new ArrayList<>();
		List<String> tagsNC = new ArrayList<>();
		List<String> ibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();
		List<String> otcPriceList = new ArrayList<>();
		double ncValue = 0.0;
		double ibmValue = 0.0;
		double nctukMonthlyExtra = 0.0;
		double ncTukPaperFee = 0.0;
		double otcPriceSum = 0.0;
		double diff = 0.0;

		tukPaperFeeFormula = IVTBase.propertyFileRead(o2ProdBotTot);
		tagsIBM.add(o2ProdBotTot);		
		ibmlist = IVTMultiTagCommonFunctionalities.getTagName(fileIBM,tagsIBM);
		tagNameAndValueIBM = IVTSingleTagCompareFiles.convertList2Map(ibmlist);
		ibmValue = IVTMultiTagCommonFunctionalities.sumOfTagValues(tagNameAndValueIBM);

		tagsNC.add(tukPaperFee);
		tagsNC.add(tukMonthlyExtra_Total);

		nclist = IVTMultiTagCommonFunctionalities.getTagName(fileNC,tagsNC);
		tagNameAndValueNC = IVTMultiTagCommonFunctionalities.convertList2MapMultiValues(nclist);

		nctukMonthlyExtra = IVTMultiTagCommonFunctionalities.getOnlyValues(tagNameAndValueNC, 0, "\\|", tukMonthlyExtra_Total);
		ncTukPaperFee = IVTMultiTagCommonFunctionalities.getOnlyValues(tagNameAndValueNC, 0, "\\|", tukPaperFee);

		tagNameAndValueNC.replace(tukMonthlyExtra_Total, Double.toString(nctukMonthlyExtra));
		tagNameAndValueNC.replace(tukPaperFee, Double.toString(ncTukPaperFee));

		ncValue = IVTMultiTagCommonFunctionalities.sumOfTagValues(tagNameAndValueNC);

		otcPriceList = OTCCommonTagsFunctionality.fetchOTCPriceTags(fileNC);
		for (String s : otcPriceList) {
			otcPriceValue = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(s);
			otcPriceSum = otcPriceSum + Double.parseDouble(otcPriceValue.get("OTCPRICE"));	
			otcPriceValue.clear();  
		}
		ncValue = ncValue + otcPriceSum;
		
		if(ibmValue != ncValue){
			if(ibmValue > ncValue) {
				diff = ibmValue - ncValue;				
			}
			else {
				diff = ncValue - ibmValue;
			}
			System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping:" +o2ProdBotTot+" vs " + tukPaperFeeFormula + " IBM Value:: " + ibmValue
					+ " NC Value:: " + ncValue);
			printUnMatchedReportInExcelSheet(o2ProdBotTot, Double.toString(ibmValue), tukPaperFeeFormula, Double.toString(ncValue), Double.toString(diff));
		}
		else
		{
			printMatchedReportInExcelSheet(o2ProdBotTot, Double.toString(ibmValue), tukPaperFeeFormula, Double.toString(ncValue), Double.toString(diff));
		}
	}	
}
