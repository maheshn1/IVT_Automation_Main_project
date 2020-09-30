package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.core.IVTBase;

public class IVTMultiTagCommonFunctionalities extends IVTBase {

	public BufferedReader br;
	public LinkedHashMap<String,String> tagNameAndValue = new LinkedHashMap<String,String>();
	public LinkedHashMap<String,String> extractedTagNameAndValue = new LinkedHashMap<String,String>();
	public String line = null;
	
	public List<String> getTagName(String fileName,List<String> taglist) throws Exception, Exception {
		List<String> tempAl = new ArrayList<>();
		br = new BufferedReader(new FileReader(fileName));
		while (((line = br.readLine()) != null)) {
			int count = taglist.size();
			for (int i = 0; i < count; i++) {
				if (line.startsWith(taglist.get(i).toLowerCase()) || line.startsWith(taglist.get(i).toUpperCase())) {
					tempAl.add(line);
					break;
				}
			}
		}
		br.close();
		return tempAl;
	}

	//To calculate single tag single value sum
	public double sumOfTagValues(Map<String, String> tagNameAndValueNC) {
		double values = 0.0;
		for(String v : tagNameAndValueNC.keySet()) {
			values = values + Double.parseDouble(tagNameAndValueNC.get(v));
		}		
		return values;
	}
	
	//this is used to convert String to map with values having multiple value tags, we have to pass only String 
	public LinkedHashMap<String, String> convertString2Map(String str) {
		LinkedHashMap<String, String> hm = new LinkedHashMap<>();
		String key = StringUtils.substringBefore(str, " ");
		String value = StringUtils.substringAfter(str, " ");		
		hm.put(key.toUpperCase(), value);
		return hm;
	}
	
	//Convert String to Map, with delimiter replacement only for Single Tags.
	public LinkedHashMap<String,String> convertStr2MapWithDelim(String str) throws Exception {
        LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
        String key = StringUtils.substringBefore(str, " ");
		String value = StringUtils.substringAfter(str, " ");
		String val = value.replaceAll("\\|", "");
		tempLHM.put(key.toUpperCase(), val);
		return tempLHM;
    }
	
	//Convert List to Maps, when we have multiple Values with comma or pipe as delimiter, When we Pass ArrayList
	public LinkedHashMap<String,String> convertList2MapMultiValues(List<String> list) throws Exception {
        LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
       for(String s : list){
            String key = StringUtils.substringBefore(s," ");
            String val = StringUtils.substringAfter(s," ");
            tempLHM.put(key.toUpperCase(),val);
        }
      return tempLHM;
    }

	public List<String> fetchMultiOccurenceTag(String fileName, String tagName) throws Exception{
		br = new BufferedReader(new FileReader(fileName));
		String line = null;
		int itr = 1;
		LinkedHashMap<Integer,String> map = new LinkedHashMap<>();
		LinkedHashMap<Integer,String> map1 = new LinkedHashMap<>();
		List<String> multiOccurenceTagList = new ArrayList<>();

		multiOccurenceTagList.clear();
		while((line = br.readLine()) != null ) {
			map.put(itr++,line );
		}
		for(int i : map.keySet()) {
			if(map.get(i).startsWith(tagName.toLowerCase()) || map.get(i).startsWith(tagName.toUpperCase())) {
				map1.put(i, map.get(i).toUpperCase());
			}
		}
		
		for(int m: map1.keySet()) {
			multiOccurenceTagList.add(map1.get(m));
		}
		/*for(String k: multiOccurenceTagList) {
			System.out.println(k);
		}*/
		return multiOccurenceTagList;
	}
	
	public double getOnlyValues(LinkedHashMap<String, String> map, int position, String delimiter, String key) {
		double d = 0.0;
		String st = map.get(key);
		String arr[] = splitStringValue(st, delimiter);
		for(int i = 0; i<arr.length;i++) {
			if(i==position) {
				if(arr[i].isEmpty() || arr[i].equalsIgnoreCase(" ")) {
					d=0.0;
				}
				else {
				d = Double.parseDouble(arr[i]);
				}
			}
		}
		return d;
	}
	
}
