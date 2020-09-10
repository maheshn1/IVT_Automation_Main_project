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

	public static LinkedHashMap<String, String> getTagName(String fileName, List<String> tags,String delimiter) throws Exception{
		BufferedReader brNC = new BufferedReader(new FileReader(fileName));

		tagNameAndValue.clear();
		while (((line = brNC.readLine()) != null)) {
			int count = tags.size();
			for (int i = 0; i < count; i++) {
				if (line.startsWith(tags.get(i))) {
					tagNameAndValue = extractTagNameAndValues(line, delimiter);
					break;
				}
			}
		}
		return tagNameAndValue;
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
	
	//this is used to convert array list to map with values having single value tags
	public static LinkedHashMap<String, String> convertArrayListToMap(String str) {
		String key = StringUtils.substringBefore(str, " ");
		String value = StringUtils.substringAfter(str, " ");
		LinkedHashMap<String, String> hm = new LinkedHashMap<>();
		String val = value.replaceAll("\\|", "");
		hm.put(key, val);
		return hm;
	}
	
	//this is used to convert array list to map with values having multiple value tags particularly for NC
	public static LinkedHashMap<String, String> convertArrListToMapNC(String str) {
		String key = StringUtils.substringBefore(str, " ");
		String value = StringUtils.substringAfter(str, " ");
		LinkedHashMap<String, String> hm = new LinkedHashMap<>();
		hm.put(key, value);
		return hm;
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
