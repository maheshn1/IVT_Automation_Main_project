package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukPaperFee {
	
	public static String o2ProdBotTot="O2PRODBOTOT";
	public static String tukPaperFee="TUKPAPERFEE",tukMonthlyExtra_Total="TUKMONTHLYEXTRA_TOTAL",tukMonthlyExtra_Total_Values =null;
	public static List<String> tagsIBM = new ArrayList<>();
	public static List<String> tagsNC = new ArrayList<>();
	public static String tukPaperFeeFormula = null;
	public static LinkedHashMap<String,String> tagNameAndValueIBM = new LinkedHashMap<String,String>();
	public static LinkedHashMap<String,String> tagNameAndValueNC = new LinkedHashMap<String,String>();
	public static LinkedHashMap<String,String> otcPriceValue = new LinkedHashMap<String,String>();
	public static List<String> otcPriceList = new ArrayList<>();
	public static double otcPriceSum = 0.0;
	public static double ncValue = 0.0;
	public static double ibmValue = 0.0;	
	
	public static void tukPaperFeeCompare(String fileIBM, String fileNC) throws Exception {
		
		tukPaperFeeFormula = IVTBase.propertyFileRead(o2ProdBotTot);
		tagsIBM.add(o2ProdBotTot);		
		tagNameAndValueIBM = IVTMultiTagCommonFunctionalities.getTagName(fileIBM,tagsIBM,",");
		ibmValue = IVTMultiTagCommonFunctionalities.sumOfTagValues(tagNameAndValueIBM);
				
		tagsNC.add(tukPaperFee);
		tagsNC.add(tukMonthlyExtra_Total);
		// get Bolt on tags here and do tagsNC.add(bolt-on);
		tagNameAndValueNC = IVTMultiTagCommonFunctionalities.getTagName(fileNC,tagsNC,"\\|");
		
		tukMonthlyExtra_Total_Values = tagNameAndValueNC.get(tukMonthlyExtra_Total);
		String newTukMonthlyExtra_Total_Value[] = IVTBase.splitStringValue(tukMonthlyExtra_Total_Values,"\\|");
		for(int i =0;i<newTukMonthlyExtra_Total_Value.length;i++) {
			if(i==0) {
				tagNameAndValueNC.replace(tukMonthlyExtra_Total, newTukMonthlyExtra_Total_Value[i]);
				break;
			}
		}
		ncValue = IVTMultiTagCommonFunctionalities.sumOfTagValues(tagNameAndValueNC);
		
		otcPriceList = OTCCommonTagsFunctionality.fetchOTCTags(fileNC);
		for (String s : otcPriceList) {
			otcPriceValue = IVTMultiTagCommonFunctionalities.extractTagNameAndValues(s,"\\|");
			otcPriceSum = otcPriceSum + Double.parseDouble(otcPriceValue.get("OTCPRICE"));	
			otcPriceValue.clear();  
		}
		ncValue = ncValue + otcPriceSum;
		if(ibmValue != ncValue){
			System.out.println("Account Number " + IVTSingleTagCompareFiles.Account_No + "::Tag Mapping:" + tukPaperFeeFormula + " IBM Value:: " + ibmValue
					+ " NC Value:: " + ncValue);
		}
	}
	
}


 