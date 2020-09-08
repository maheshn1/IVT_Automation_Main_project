package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.FileReader;
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

}
