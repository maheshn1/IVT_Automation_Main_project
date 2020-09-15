package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class TukLateFee extends IVTBase {

	public static String otcDate = "OTCDATE";
	public static String otcTypeName = "OTCTYPENAME Late Payment Fees";	

	public static void compareTukLateFee(String fileIBM, String fileNC) throws Exception {
		List<String> lateFeestagIBM = new ArrayList<>();
		List<String> LateFeestagNC = new ArrayList<>();
		double ibmDoubleValue =0.0;
		double ncDoubleValue =0.0;
		double diff =0.0;

		lateFeestagIBM = OTCCommonTagsFunctionality.fetchOTCTypeNameDateAndPriceTag(fileIBM);
		LateFeestagNC =  OTCCommonTagsFunctionality.fetchOTCTypeNameDateAndPriceTag(fileNC);

		for(int i = 0;i<lateFeestagIBM.size();i++) {

			LinkedHashMap<String, String> ibm = new LinkedHashMap<>();			
			ibm = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(lateFeestagIBM.get(i));


			for(int j = 0;j<LateFeestagNC.size();j++) {
				LinkedHashMap<String, String> nc = new LinkedHashMap<>();
				nc = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(LateFeestagNC.get(j));


				if(i==j) {
					for(String tag : ibm.keySet()) {
						String ibmVal = ibm.get(tag);
						String ncVal = nc.get(tag);

						if(!ibmVal.equals(ncVal)) {
							if(tag.equalsIgnoreCase("OTCPRICE")) {
								ibmDoubleValue = Double.parseDouble(ibmVal);
								ncDoubleValue = Double.parseDouble(ncVal);

								if(ibmDoubleValue > ncDoubleValue) {
									diff = ibmDoubleValue - ncDoubleValue;				
								}
								else {
									diff = ncDoubleValue - ibmDoubleValue;
								}
							}
							System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping for Late Payment Fees:" + tag + " IBM Value:: " + ibmVal
									+ " NC Value:: " + ncVal);
							printUnMatchedReportInExcelSheet(tag, ibmVal, tag, ncVal, Double.toString(diff));
							diff = 0.0;
						}
						else
						{
							diff = 0.0;
							printMatchedReportInExcelSheet(tag, ibmVal, tag, ncVal, Double.toString(diff));
						}
					}
				}				
				nc.clear();
			}
			ibm.clear();
		}
	}
}
