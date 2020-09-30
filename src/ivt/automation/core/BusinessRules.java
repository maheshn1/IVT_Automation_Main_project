package ivt.automation.core;

import ivt.automation.ProjSpecs.MappingDoc;

import java.util.*;
import java.util.regex.Pattern;

public class BusinessRules extends IVTBase{
    static Hashtable<Object, Object> brMapDoc = new Hashtable<Object, Object>();
    MappingDoc md = new MappingDoc();

    public List<String> fetchBRMapping(Object ibmKey) throws Exception {
        List<String> mapDocAL = new ArrayList<>();
        String mapDocFile=propertyFileRead("NC_IBM_Maps");
        String mapDocSheet=propertyFileRead("BusinessRulesTag");
        String brMapString=null,  regexPattern = "/[^a-zA-Z0-9 ]/g";
        Pattern regex = Pattern.compile("[{}=]");
        int brMapStringLength=0;
        String[] sarray;
        brMapDoc= md.getBRMappingDoc(mapDocFile,mapDocSheet);
        brMapString = brMapDoc.get(ibmKey).toString();
        //  System.out.println(brMapString);
        brMapString=brMapString.replaceAll(regex.toString(),"");
        brMapString=brMapString.replaceAll("\\[",",");
        brMapString=brMapString.replaceAll("\\]","");
        brMapStringLength=brMapString.length();
        //System.out.println(brMapString);
        sarray=brMapString.split(",");
        for(int i=1;i<sarray.length;i++){
            String s = sarray[i].trim();
            mapDocAL.add(s);
        }
        return mapDocAL;
    }

    public LinkedHashMap<String,List<String>> fetchBRMappingHMap(Object ibmKey) throws Exception {
        List<String> mapDocAL = new ArrayList<>();
        LinkedHashMap<String,List<String>> tempLinkedHashMap = new LinkedHashMap<>();

        String mapDocFile=propertyFileRead("NC_IBM_Maps");
        String mapDocSheet=propertyFileRead("BusinessRulesTag");
        String brMapString=null,  regexPattern = "/[^a-zA-Z0-9 ]/g";
        Pattern regex = Pattern.compile("[{}=]");
        int brMapStringLength=0;
        String[] sarray;

        brMapDoc= md.getBRMappingDoc(mapDocFile,mapDocSheet);
        brMapString = brMapDoc.get(ibmKey).toString();
        //  System.out.println(brMapString);
        brMapString=brMapString.replaceAll(regex.toString(),"");
        brMapString=brMapString.replaceAll("\\[",",");
        brMapString=brMapString.replaceAll("\\]","");
        brMapStringLength=brMapString.length();
        //System.out.println(brMapString);
        sarray=brMapString.split(",");
        for(int i=1;i<sarray.length;i++){
            String s = sarray[i].trim();
            mapDocAL.add(s);
        }
        String s = ibmKey.toString();
        tempLinkedHashMap.put(s,mapDocAL);
        return tempLinkedHashMap;
    }


}
