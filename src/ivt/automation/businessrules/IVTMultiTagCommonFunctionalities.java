package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.core.IVTBase;

public class IVTMultiTagCommonFunctionalities extends IVTBase {

	public static String line = null;
	public static LinkedHashMap<String,String> tagNameAndValue = new LinkedHashMap<String,String>();
	public static LinkedHashMap<String,String> extractedTagNameAndValue = new LinkedHashMap<String,String>();
	public static double values = 0.0;

	public static List<String> getTagName(String fileName,List<String> taglist) throws Exception, Exception {
		List<String> tempAl = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while (((line = br.readLine()) != null)) {
			int count = taglist.size();
			for (int i = 0; i < count; i++) {
				if (line.startsWith(taglist.get(i))) {
					tempAl.add(line);
					break;
				}
			}
		}
		br.close();
		return tempAl;
	}	

	public static LinkedHashMap<String,String> extractTagNameAndValues(String str, String delimiter){

		String tag = StringUtils.substringBefore(str, " ");
		String values = StringUtils.substringAfter(str, " ");		
		String tagname[] = splitStringValue(values,delimiter);

		String val, value = "";
		if(tagname.length==1) {
			val = tagname[0];
			value = val.replaceAll("\\|", "");
			extractedTagNameAndValue.put(tag, value);
			return extractedTagNameAndValue;
		}
		else {
			extractedTagNameAndValue.put(tag, values);    		
			return extractedTagNameAndValue;
		}
	}

	public static double sumOfTagValues(Map<String, String> tagNameAndValueNC) {
		values = 0.0;
		for(String v : tagNameAndValueNC.keySet()) {
			values = values + Double.parseDouble(tagNameAndValueNC.get(v));
		}		
		return values;
	}
	
	//this is used to convert String to map with values having multiple value tags, we have to pass only String 
	public static LinkedHashMap<String, String> convertString2Map(String str) {
		LinkedHashMap<String, String> hm = new LinkedHashMap<>();
		String key = StringUtils.substringBefore(str, " ");
		String value = StringUtils.substringAfter(str, " ");
		hm.put(key, value);
		return hm;
	}
	
	//Convert String to Map, with delimiter replacement only for Single Tags.
	public static LinkedHashMap<String,String> convertStr2MapWithDelim(String str) throws Exception {
        LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
        String key = StringUtils.substringBefore(str, " ");
		String value = StringUtils.substringAfter(str, " ");
		String val = value.replaceAll("\\|", "");
		tempLHM.put(key, val);
		return tempLHM;
    }
	
	//Convert List to Maps, when we have multiple Values with comma or pipe as delimiter, When we Pass ArrayList
	public static LinkedHashMap<String,String> convertList2MapMultiValues(List<String> list) throws Exception {
        LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
       for(String s : list){
            String key = StringUtils.substringBefore(s," ");
            String val = StringUtils.substringAfter(s," ");
            tempLHM.put(key,val);
        }
      return tempLHM;
    }

	public static List<String> fetchSpendCap(String fileName, String tagName) throws Exception{
		BufferedReader br2 = new BufferedReader(new FileReader(fileName));
		String line = null;
		int itr = 1;
		LinkedHashMap<Integer,String> map = new LinkedHashMap<>();
		LinkedHashMap<Integer,String> map1 = new LinkedHashMap<>();
		LinkedHashMap<Integer,String> map2 = new LinkedHashMap<>();
		List<String> o2SpendCapEventsTotTagList = new ArrayList<>();

		o2SpendCapEventsTotTagList.clear();
		while((line = br2.readLine()) != null ) {
			map.put(itr++,line );
		}
		for(int i : map.keySet()) {
			if(map.get(i).contains("SPENDCAP")) {
				map1.put(i, map.get(i));
			}
		}
		for(int i : map1.keySet()) {
			for(int j : map1.keySet()) {
				if(map1.get(j).startsWith(tagName)) {
					map2.put(j, map1.get(j));
				}
			}				
		}
		//System.out.println(map2);
		
		for(int m: map2.keySet()) {
			o2SpendCapEventsTotTagList.add(map2.get(m));
		}
		/*for(String k: o2SpendCapEventsTotTagList) {
			System.out.println(k);
		}*/
		return o2SpendCapEventsTotTagList;
	}
	
	public static double getOnlyValues(LinkedHashMap<String, String> map, int position, String delimiter, String key) {
		double d = 0.0;
		String st = map.get(key);
		String arr[] = IVTBase.splitStringValue(st, delimiter);
		for(int i = 0; i<arr.length;i++) {
			if(i==position) {
				d = Double.parseDouble(arr[i]);
			}
		}
		return d;
	}

}
