package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OTCCommonTagsFunctionality { 

	public static List<String> fetchOTCTags(String fileName) throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		int itr = 1;
		LinkedHashMap<Integer,String> map = new LinkedHashMap<>();
		LinkedHashMap<Integer,String> map1 = new LinkedHashMap<>();
		LinkedHashMap<Integer,String> map2 = new LinkedHashMap<>();
		List<String> otcPriceTagList = new ArrayList<>();
		while((line = br.readLine()) != null ) {
			map.put(itr++,line );
		}
		for(int i : map.keySet()) {
			if(map.get(i).contains("OTC")) {
				map1.put(i, map.get(i));
			}
		}
		int x = 0;
		for(int i : map1.keySet()) {
			if( (map1.get(i).contains("OTCNAME")) && (map1.get(i).contains("Bolt On")) ){
				map2.put(i, map.get(i));
				x=i;
				for(int j : map1.keySet()) {
					if(map1.get(j).contains("OTCPRICE") && (j>x)) {
						map2.put(j, map1.get(j));
						break;
					}
				}
			}
		}
		//System.out.println(map2);
		for(int m: map2.keySet()) {
			if(map2.get(m).contains("OTCPRICE")) {
				otcPriceTagList.add(map2.get(m));
			}
		}
		return otcPriceTagList;
	}
}


