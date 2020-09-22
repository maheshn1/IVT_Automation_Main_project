package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import ivt.automation.core.IVTBase;

public class TukLateFee extends IVTBase {

	public static String otcDate = "OTCDATE";
	public static String otcTypeName = "OTCTYPENAME Late Payment Fees";	

	public static void compareTukLateFee(String fileIBM, String fileNC) throws Exception {
		LinkedHashMap<String, String> ibm = new LinkedHashMap<>();
		LinkedHashMap<String, String> nc = new LinkedHashMap<>();
		List<String> lateFeestagIBM = new ArrayList<>();
		List<String> LateFeestagNC = new ArrayList<>();

		double ibmDoubleValue =0.0;
		double ncDoubleValue =0.0;
		double diff =0.0;
		String regex="\\d+";

		lateFeestagIBM = OTCCommonTagsFunctionality.fetchOTCTypeNameDateAndPriceTag(fileIBM);
		LateFeestagNC =  OTCCommonTagsFunctionality.fetchOTCTypeNameDateAndPriceTag(fileNC);
		List<String> lateFeestagIBMList = new ArrayList<>();
		List<String> LateFeestagNCList = new ArrayList<>();
		List<String> lateFeestagIBMDate = new ArrayList<>();
		List<String> LateFeestagNCDate = new ArrayList<>();
		List<String> lateFeestagIBMPrice = new ArrayList<>();
		List<String> LateFeestagNCPrice = new ArrayList<>();

		/*for(int k =0;k< lateFeestagIBM.size();k++){
			//for (int j=0;j<LateFeestagNC.size();j++)
			if(lateFeestagIBM.get(k).contains("Late Payment Fees")) {
				lateFeestagIBMList.add("Late Payment Fees"+k);

			}
			if(lateFeestagIBM.get(k).matches(regex)) {
				lateFeestagIBMList.add(lateFeestagIBM.get(k));
				String date = lateFeestagIBM.get(k);
				

				}
				else if(lateFeestagIBM.get(k).contains(".")) {
					lateFeestagIBM11.add(k, lateFeestagIBM.get(k));

				}
				else {
					lateFeestagIBM11.add(k, "Value Missing");
					break;
				}

			}*/


		//Collections.sort(lateFeestagIBM);
		//Collections.sort(LateFeestagNC);
		System.out.println("====================================================================================");
		System.out.println(lateFeestagIBM);
		System.out.println(LateFeestagNC);
		System.out.println("====================================================================================");
		for(int k =0;k<lateFeestagIBM.size();k++){
			if(lateFeestagIBM.get(k).matches(regex)) {
				lateFeestagIBMDate.add(lateFeestagIBM.get(k));					
			}
			else if(lateFeestagIBM.get(k).contains(".")) {
				lateFeestagIBMPrice.add(lateFeestagIBM.get(k));

			}
		}
		Collections.sort(lateFeestagIBMDate);
		System.out.println(lateFeestagIBMDate);
		System.out.println(lateFeestagIBMPrice);
		System.out.println("====================================================================================");	
		/*lateFeestagIBM.removeAll(lateFeestagIBMDate);
		System.out.println(lateFeestagIBM);*/
		
		//System.out.println("====================================================================================");	
		for(int k =0;k<LateFeestagNC.size();k++){
			if(LateFeestagNC.get(k).matches(regex)) {
				LateFeestagNCDate.add(LateFeestagNC.get(k));					
			}
			else if(LateFeestagNC.get(k).contains(".")) {
				LateFeestagNCPrice.add(LateFeestagNC.get(k));

			}
		}
		Collections.sort(LateFeestagNCDate);
		System.out.println(LateFeestagNCDate);
		System.out.println(LateFeestagNCPrice);
		System.out.println("====================================================================================");	
		/*LateFeestagNC.removeAll(LateFeestagNCDate);
		System.out.println(LateFeestagNC);
		System.out.println("====================================================================================");	*/
		
		if(lateFeestagIBM.size()>LateFeestagNC.size()) {
			lateFeestagIBMDate.removeAll(LateFeestagNCDate);
			System.out.println("Dates missing in NC: "+lateFeestagIBMDate);
		}
		else {
			LateFeestagNCDate.removeAll(lateFeestagIBMDate);
			System.out.println("Dates missing in IBM: "+LateFeestagNCDate);
		}
		System.out.println("====================================================================================");	
		System.out.println("====================================================================================");	
		//		}
		//			if (LateFeestagNC22.equals(lateFeestagIBM1)) 
		//            System.out.println("Equal"); 
		//        else
		//            System.out.println("Not equal");
		//			
		//}

	


		//}*/
		/*for(int i = 0;i<lateFeestagIBM.size();i++) {						
			ibm = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(lateFeestagIBM.get(i));

			for(int j = 0;j<LateFeestagNC.size();j++) {				
				nc = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(LateFeestagNC.get(j));


				//if(i==j) {
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
				//}				
				nc.clear();
			}
			ibm.clear();			
		}*/
		//System.out.println(ibm);
		//System.out.println(nc);
	}
}
