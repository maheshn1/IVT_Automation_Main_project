package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class OTCCommonTagsFunctionality { 

	public static double fetchOTCPriceTags(String fileName, String OTCName, String wildCardSearch) throws Exception {

		BufferedReader br1 = new BufferedReader(new FileReader(fileName));
		LinkedHashMap<Integer,String> map = new LinkedHashMap<>();
		LinkedHashMap<Integer,String> map1 = new LinkedHashMap<>();
		List<Integer> bStartOtcLineNoList = new ArrayList<>();
		List<Integer> bEndOtcLineNoList = new ArrayList<>();
		String line = null;
		double priceVal=0.0;
		Double finalOtcPrice=0.0;
		int itr = 1;
		int x=0,y = 0, taxVal=0; 

		while((line = br1.readLine()) != null ) {
			map.put(itr++,line );
		}
		for(int i : map.keySet()) {
			if(map.get(i).contains("OTC")) {
				map1.put(i, map.get(i));
			}	
		}

		for(int s : map1.keySet()) {
			if (map1.get(s).equalsIgnoreCase("BSTARTOTC |")) {
				bStartOtcLineNoList.add(s);
			}
		}

		for(int e : map1.keySet()) {
			if (map1.get(e).equalsIgnoreCase("BENDOTC |")) {
				bEndOtcLineNoList.add(e);
			}
		}
		for(int i : map1.keySet()) {
			for(int a = 0; a<bEndOtcLineNoList.size();a++) {

				if(i>bStartOtcLineNoList.get(a) && i< bEndOtcLineNoList.get(a)) {
					if((map1.get(i).contains(OTCName)) && (map1.get(i).contains(wildCardSearch)) ) {
						x= bStartOtcLineNoList.get(a);
						y = bEndOtcLineNoList.get(a);								
					}

					if(i>x && i<y) {
						if(map1.get(i).contains("OTCPRICE")) {
							String priceDem=map1.get(i);
							priceVal=Double.parseDouble(IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(priceDem).get("OTCPRICE"));
							break;
						}

						if(map1.get(i).contains("OTCTAXCODE")) {
							String taxDem=map1.get(i);
							taxVal=Integer.parseInt(IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(taxDem).get("OTCTAXCODE"));
							finalOtcPrice= finalOtcPrice + taxCodeCalculation(priceVal, taxVal);
						}
						break;
					}						
				}

			}
		}	
		br1.close();
		System.out.println(finalOtcPrice);
		return finalOtcPrice;
	}

	public static List<String> fetchOTCTypeNameDateAndPriceTag(String fileName) throws Exception {

		BufferedReader br1 = new BufferedReader(new FileReader(fileName));
		LinkedHashMap<Integer,String> map = new LinkedHashMap<>();
		LinkedHashMap<Integer,String> map1 = new LinkedHashMap<>();
		LinkedHashMap<Integer,String> map2 = new LinkedHashMap<>();
		List<String> lateFeesOtcTagList = new ArrayList<>();
		List<Integer> bStartOtcLineNoList = new ArrayList<>();
		List<Integer> bEndOtcLineNoList = new ArrayList<>();
		String line = null, OtcDate=null, OtcDateDem=null,lateDem=null,lateFee=null;
		double priceVal=0.0;
		Double finalOtcPrice=0.0;
		int itr = 1;
		int x=0,y = 0, taxVal=0; 

		while((line = br1.readLine()) != null ) {
			map.put(itr++,line );
		}
		for(int i : map.keySet()) {
			if(map.get(i).contains("OTC")) {
				map1.put(i, map.get(i));
			}	
		}
		//System.out.println("BSTARTOTC");
		for(int s : map1.keySet()) {
			if (map1.get(s).equalsIgnoreCase("BSTARTOTC |")) {
				bStartOtcLineNoList.add(s);	
				//System.out.println(s);
			}
		}
		//System.out.println("BENDOTC");
		for(int e : map1.keySet()) {
			if (map1.get(e).equalsIgnoreCase("BENDOTC |")) {
				bEndOtcLineNoList.add(e);	
				//System.out.println(e);
			}
		}
		for(int i : map1.keySet()) {
			for(int a = 0; a<bEndOtcLineNoList.size();a++) {

				if(i>bStartOtcLineNoList.get(a) && i< bEndOtcLineNoList.get(a)) {
					if((map1.get(i).contains("OTCTYPENAME")) && (map1.get(i).contains("Late Payment Fees")) ) {
						//map2.put(i, map.get(i));
						lateDem = map1.get(i);
						lateFee = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(lateDem).get("OTCTYPENAME");
						map2.put(i, lateFee);
						x= bStartOtcLineNoList.get(a);
						y = bEndOtcLineNoList.get(a);								
					}

					if(i>x && i<y) {
						if(map1.get(i).contains("OTCDATE")) {
							//map2.put(i, map1.get(i));
							OtcDateDem = map1.get(i);
							OtcDate = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(OtcDateDem).get("OTCDATE");
							map2.put(i, OtcDate);
							break;
						}

						if(map1.get(i).contains("OTCPRICE")) {
							//map2.put(i, map1.get(i));
							//System.out.println(map1.get(i));
							String priceDem=map1.get(i);
							priceVal=Double.parseDouble(IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(priceDem).get("OTCPRICE"));
							break;
						}

						if(map1.get(i).contains("OTCTAXCODE")) {
							//map2.put(i, map1.get(i));
							//System.out.println(map1.get(i));
							String taxDem=map1.get(i);
							taxVal=Integer.parseInt(IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(taxDem).get("OTCTAXCODE"));
							taxCodeCalculation(priceVal, taxVal);
							map2.put(i, finalOtcPrice.toString());
						}
						break;
					}						
				}

			}
		}

		System.out.println();
		System.out.println(map2);
		System.out.println();
		for(int m: map2.keySet()) {
			lateFeesOtcTagList.add(map2.get(m));
		}
		for(String k: lateFeesOtcTagList) {
			System.out.println(k);
		}
		br1.close();
		return lateFeesOtcTagList;
	}

	public static double taxCodeCalculation(double otcPrice, int taxCode) {
		double finalOtcPrice = 0.0;

		if(otcPrice!=0.0 && taxCode!=0) {
			switch(taxCode) {			
			case 2 :
				finalOtcPrice = otcPrice * 0.2;
				break;
			case 3008 :
				finalOtcPrice = otcPrice * 0.2;
				break;
			case 3006 :
				finalOtcPrice = otcPrice * 0.05;
				break;
			default:
				finalOtcPrice = otcPrice;
			}
		}
		return finalOtcPrice;


	}
}



