package ivt.automation.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class GenUtils {

	//Line to ArrayList
	//Extract TagName
	//Conversion of MAP to String and vice-versa
	//Extract Tag Values, if we pass TagName
	//Compare two ArrayList

	public static LinkedHashMap<String,String> convertList2Map(List<String> eventsList) throws IOException {
		LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
		tempLHM.clear();
		for(String s : eventsList){
			String key = StringUtils.substringBefore(s," ");
			String val = StringUtils.substringAfter(s," ");
			tempLHM.put(key,val);
		}
		//   System.out.println(tempLHM.keySet() + "<--------->" + tempLHM.values());
		return tempLHM;
	}

	public static LinkedHashMap<String, String> convertArrayList2Map(List<String> eventsList) throws IOException {
		// this method accepts ArrayList<String> but returns only last list as Map as its object
		// AL1, AL2,AL3 Map for only A3  :-  {OTCTYPENAME=Late Payment Fees, OTCPRICE=280, OTCTAXCODE=4, OTCDATE=null}
		LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
		List<LinkedHashMap<String, String>> tempListLHM = new ArrayList<>();
		String tempStr=null,tagKey=null,tagVal=null;
		String[] strArr=null;
		tempLHM.clear();
		for(String s : eventsList){
			s=s.replaceAll("[={}+]"," ").trim();
			strArr=s.split(",");
			for(int i=0;i<strArr.length;i++) {
				tempStr = strArr[i].trim();
				tagKey = StringUtils.substringBefore(tempStr, " ");
				tagVal = StringUtils.substringAfter(tempStr, " ");
				tempLHM.put(tagKey, tagVal);
			}

		}
		return tempLHM;
	}

	public static LinkedHashMap<String, String> convertMapObjectString2Map(String eventsList) throws IOException {
		// converts String of Type {OTCTYPENAME=Late Payment Fees, OTCPRICE=280, OTCTAXCODE=4, OTCDATE=null}

		LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
		List<LinkedHashMap<String, String>> tempListLHM = new ArrayList<>();
		String tempStr=null,tagKey=null,tagVal=null;
		String[] strArr=null;
		tempLHM.clear();
		eventsList=eventsList.replaceAll("[={}+]"," ").trim();
		strArr=eventsList.split(",");
		for(int i=0;i<strArr.length;i++) {
			tempStr = strArr[i].trim();
			tagKey = StringUtils.substringBefore(tempStr, " ");
			tagVal = StringUtils.substringAfter(tempStr, " ");
			tempLHM.put(tagKey, tagVal);
		}
		return tempLHM;
	}

	public static LinkedHashMap<String,String> convertList2Map(String eventsList) throws IOException {
		LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
		tempLHM.clear();
		String key = StringUtils.substringBefore(eventsList," ");
		String val = StringUtils.substringAfter(eventsList," ");
		tempLHM.put(key,val);
		return tempLHM;
	}

	public static List<LinkedHashMap<String,String>> convertList2MapList(List<String> eventsList) throws IOException {
		// this method accepts List<String> but returns only last list as Map as its object
		// AL1, AL2,AL3 Map for only A3  :-  {OTCTYPENAME=Late Payment Fees, OTCPRICE=280, OTCTAXCODE=4, OTCDATE=null}
		LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
		List<LinkedHashMap<String,String>> hashMapList = new ArrayList<LinkedHashMap<String,String>>();
		hashMapList.clear();
		tempLHM.clear();
		for(String s : eventsList){
			String key = StringUtils.substringBefore(s," ");
			String val = StringUtils.substringAfter(s," ");
			tempLHM.put(key,val);
			hashMapList.add(tempLHM);
		}
		return hashMapList;
	}

	public void displayMap(LinkedHashMap<String,String> map){
		System.out.println("\n\n");
		System.out.println("\n------------Map----------------");
		for(Map.Entry me : map.entrySet()){
			// System.out.println(map.keySet()+"::::"+me.getKey()+"<-----map---->"+me.getValue());
			System.out.println(me.getKey()+" <-k--:---v-> "+me.getValue());
		}
	}

	public void displayListOfLinkedMap(List<LinkedHashMap<String, String>> AL){
		System.out.println("\n------------AL----------------");
		for(LinkedHashMap<String, String> s : AL){
			// System.out.println(map.keySet()+"::::"+me.getKey()+"<-----map---->"+me.getValue());
			System.out.println(s);
		}
		System.out.println("\n");
	}

	public void displayAL(List<String> AL){
		System.out.println("\n------------AL----------------");
		for(String s : AL){
			// System.out.println(map.keySet()+"::::"+me.getKey()+"<-----map---->"+me.getValue());
			System.out.println(s);
		}
		System.out.println("\n");
	}
	public static List<String> fetchArrayListContainingTag(String fileName, String eventTag) throws IOException {
		List<String> tempAL = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line=null; String[] evTOTALTag;
		while(((line = br.readLine()) != null)) {
			if((line.toUpperCase().contains(eventTag.toUpperCase()))){
				String s = StringUtils.substringBefore(line," ");
				if(eventTag.equalsIgnoreCase(s)){
					tempAL.add(line);
				}
			}
		}
		br.close();
		return tempAL;
	}

	public static List<String> fetchArrayListContainingTagTUKSUM(String fileName, String eventTag) throws IOException {
		List<String> tempAL = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line=null; String[] evTOTALTag;
		while(((line = br.readLine()) != null)) {
			if(line.toUpperCase().contains(eventTag.toUpperCase())){
				tempAL.add(line);
			}
		}
		br.close();
		return tempAL;
	}

}
