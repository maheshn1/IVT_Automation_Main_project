package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class OTCCommonTagsFunctionality extends IVTBase{ 

	public double fetchOTCPriceTags(String fileName, String OTCName, String wildCardSearch) throws Exception {
		
		IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();

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
							priceVal=Double.parseDouble(ivtMultiTagCommonFunction.convertStr2MapWithDelim(priceDem).get("OTCPRICE"));
							break;
						}

						if(map1.get(i).contains("OTCTAXCODE")) {
							String taxDem=map1.get(i);
							taxVal=Integer.parseInt(ivtMultiTagCommonFunction.convertStr2MapWithDelim(taxDem).get("OTCTAXCODE"));
							finalOtcPrice= finalOtcPrice + taxCodeCalculation(priceVal, taxVal);
						}
						break;
					}						
				}

			}
		}	
		br1.close();
		//System.out.println(finalOtcPrice);
		return finalOtcPrice;
	}

	public double taxCodeCalculation(double otcPrice, int taxCode) {
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



