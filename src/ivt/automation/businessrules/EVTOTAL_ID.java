package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.ProjSpecs.MappingDoc;
import ivt.automation.core.BusinessRules;
import ivt.automation.core.IVTBase;
import ivt.automation.utils.GenUtils;
import ivt.automation.utils.XlsxFile;

public class EVTOTAL_ID extends IVTBase {

    static LinkedHashMap<String, String> ibmEventMap = new LinkedHashMap<String, String>();
    static Hashtable<Object, Object> brMapDoc = new Hashtable<Object, Object>();

    static List<String> ibmAndNCFiles = new ArrayList<>();
    static List<String> eventsList = new ArrayList<>();
    static File IBMFile;
    static File NCFile;

    static String custAccountNumber=null;
    BusinessRules br1 = new BusinessRules();
    MappingDoc md = new MappingDoc();
    static boolean EVTOTAL_FLAG=false;

    // ncMapDocAL:- List from brMapDoc... i.e. mapping Tag for IBM Key
    // ncEventMap:- Map for all the NC Tags (from ncMaDocAL)
    public boolean compareBREvents(String IBMFile,String NCFile,String custAccountNumber) throws Exception {
        LinkedHashMap<String, String> ncEventMap = new LinkedHashMap<String, String>();
        List<String> ibmMapAL = new ArrayList<>();
        List<String> ncMapDocAL = new ArrayList<>();
        List<String> ncMapDocALTemp = new ArrayList<>();
        List<String> ncMapDocALValues = new ArrayList<>();
        Object ibmKey,ncKey,mapDocKey;
        String ibmTagName=null,ncTagName=null;
        double ncEventTagTotal=0, ibmEventTagTotal=0,IBMVALUE_After_Tax=0.00,Tax=0.00;
        int ncMapDocALSize=0; String ibmMapTagVal=null;
        Pattern ncregex = Pattern.compile("[{}=]");
        XlsxFile xlsxfile = new XlsxFile();
        eventsList = xlsxfile.fetchTagNames(propertyFileRead("IBMBrTag"),propertyFileRead("NC_IBM_Maps"));
        
        System.out.println("*********************Checking for EVTOTAL******************");

        for(String brEventTag : eventsList) {
            if (brEventTag.toUpperCase().contains("EVTOTAL_")) {
                try {
                    ibmMapAL = GenUtils.fetchArrayListContainingTag(IBMFile, brEventTag);
                    ibmEventMap = GenUtils.convertList2Map(ibmMapAL);
                }catch(Exception e){
                    System.out.println("fetchArrayListContainingTag EVTOTAL_ID is Null..."+e);
                }
                for (Map.Entry ibmMapEntries : ibmEventMap.entrySet()) {
                    ibmKey = StringUtils.substringBefore(ibmMapEntries.toString(), "=").toUpperCase().trim();
                    ibmTagName=fetchEventNames("IBM_".concat(ibmKey.toString())).toUpperCase().trim();
                    ibmEventTagTotal = 0;
                    ncEventTagTotal = 0;
                    ncMapDocAL.clear();
                    ncEventMap.clear();
                    ncMapDocALValues.clear();
                    try {
                    ncMapDocAL = br1.fetchBRMapping(ibmKey);
                    ncMapDocALSize = ncMapDocAL.size();
                    for (int i = 0; i < ncMapDocALSize; i++) {
                        ncMapDocALTemp.clear();
                            ncMapDocALTemp = GenUtils.fetchArrayListContainingTag(NCFile.toUpperCase().trim(), ncMapDocAL.get(i).toUpperCase().trim());
                            for (String z : ncMapDocALTemp) {
                                ncMapDocALValues.add(z);
                            }
                            ncEventMap = GenUtils.convertList2Map(ncMapDocALValues);
                        }
                    }catch(Exception e){
                        System.out.println("ncMapDoc in EVTOTAL_ID is null.."+e);

                    }
                    ncTagName=StringUtils.substringBefore((ncEventMap.toString()),"=").toUpperCase();
                    ncTagName=("NC_").concat(ncTagName.replaceAll(ncregex.toString(),"").trim().toUpperCase());
                    ncTagName=fetchEventNames(ncTagName);
                    ncEventTagTotal = fetchEventTagTotal(ncEventMap);
                    ibmMapTagVal = ibmMapEntries.getValue().toString().toUpperCase();
                    ibmMapTagVal = ibmMapTagVal.replaceAll("\\|", "").trim();
                    ibmEventTagTotal = ibmEventTagTotal + Integer.parseInt(ibmMapTagVal);
                    try{
                        Tax = (int) (ibmEventTagTotal * .2);
                        IBMVALUE_After_Tax = ibmEventTagTotal + Tax;
                    }catch(Exception e){
                        System.out.println("Tax in EVTOTAL_ID cannot be Null... "+e);

                    }
                   // System.out.println("\n" + ibmEventTagTotal + "+ Tax@20% :- " + Tax + " ibm after adding Tax:- " + (ibmEventTagTotal + Tax));
                    if ((IBMVALUE_After_Tax) != (ncEventTagTotal)) {
                     //   System.out.println("CustAccountNumber:-" + custAccountNumber + " Tags NOT EQUAL--------->>>>" + ibmMapEntries.getKey() + ":-" + ibmEventTagTotal + "  :: " + ncEventMap + ":- " + ncEventTagTotal);
                        printEventMatchedReportInExcelSheet(ibmMapEntries.toString(), ibmTagName, String.valueOf(ibmEventTagTotal),String.valueOf(IBMVALUE_After_Tax),ncEventMap.toString(), ncTagName,String.valueOf((ncEventTagTotal + Tax)),"NA");
                        EVTOTAL_FLAG=true;
                    } else {
                       // System.out.println("CustAccountNumber:-" + custAccountNumber + " Tags EQUAL--------->>>>" + ibmMapEntries.getKey() + ":-" + ibmEventTagTotal + "  :: " + ncEventMap + ":- " + ncEventTagTotal);
                        printEventUnMatchedReportInExcelSheet(ibmMapEntries.toString(), ibmTagName, String.valueOf(ibmEventTagTotal), String.valueOf(IBMVALUE_After_Tax),ncEventMap.toString(), ncTagName,String.valueOf((ncEventTagTotal + Tax)),"NA");
                        EVTOTAL_FLAG=false;
                    }
                }
            }
        }
        return EVTOTAL_FLAG;
    }

    public int fetchEventTagTotal(LinkedHashMap<String, String> ncEventMap){
        int eventTagTotal=0;String mapVal=null;
        Pattern regex = Pattern.compile("\\d+");
        for(Map.Entry me : ncEventMap.entrySet()){
            mapVal=me.getValue().toString().toUpperCase().trim();
            mapVal=mapVal.replaceAll("\\|","").trim();
            eventTagTotal= eventTagTotal+Integer.parseInt(mapVal);
        }
        //   System.out.println("tagTot===="+eventTagTotal);
        return eventTagTotal;
    }


    public List<String> fetchAllBREventsList(File fileName) throws IOException {
        List<String> tempAL = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line=null; String[] evTOTALTag;
        for (String brEventMaptag : eventsList) {
            while(((line = br.readLine()) != null)) {
                if(line.contains(brEventMaptag)){
                    String s = StringUtils.substringBefore(line," ").trim().toUpperCase();
                    if(brEventMaptag.equalsIgnoreCase(s)){
                        tempAL.add(line.toUpperCase().trim());
                    }
                }
            }
        }
        br.close();
        return tempAL;
    }

    public String fetchEventNames(String tagTypeID) throws Exception {
        Hashtable<Object,Object> eventNameMap = new Hashtable<Object,Object>();
        String mapDocFile=propertyFileRead("NC_IBM_Maps");
        String mapDocSheet=propertyFileRead("BusinessTagsEventNameSheet");
        String tagName=null; Object tagType = null;
       // System.out.println("event tagTypeID Mapping Fetched for = "+tagTypeID);
        try {
        eventNameMap=md.getBRMappingDoc(mapDocFile, mapDocSheet);
        tagType=eventNameMap.get(tagTypeID.toUpperCase().trim());
        }catch(Exception e) {
               System.out.println("Mapping for TAg:= "+tagTypeID+"is not present in BusinessTagsEventNameSheet "+e);
        }
        return tagType.toString();
    }

}
