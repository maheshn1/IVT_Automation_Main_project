package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import ivt.automation.ProjSpecs.MappingDoc;
import ivt.automation.core.BusinessRules;
import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.GenUtils;
import ivt.automation.utils.XlsxFile;

public class TUKLPF extends IVTBase {

    static List<String> ibmAndNCFiles = new ArrayList<>();
    static List<String> eventsList = new ArrayList<>();
    static List<String> brMapDocTUKAL = new ArrayList<>();
    static Map<String, String> brMapDocMAP = new HashMap<String, String>();
    static List<List<String>> tempALdList = new ArrayList<>();
    static File IBMFile;
    static File NCFile;

    static String custAccountNumber = null;
    static String taxValCalc = null;
    static String startTUKLateFeeBlock = "BSTARTOTCLIST", endTUKLATEFEEBlock = "BENDOTCLIST", startOTCBlock = "BSTARTOTC", endOTCBlock = "BENDOTC";
    static Pattern regex = Pattern.compile("[{}=]\" \"");
    GenUtils gu = new GenUtils();
    BusinessRules br6 = new BusinessRules();

    public boolean fetchLPFDetailsFromFiles(String IBMFile, String NCFile, String custAccountNumber) throws Exception {
        List<String> ibmTUKLATEFEEAL = new ArrayList<>();
        List<String> ncTUKLATEFEEAL = new ArrayList<>();
        List<String> ibmLATEFEEPaymentAL = new ArrayList<>();
        List<String> ncLATEFEEPaymentAL = new ArrayList<>();
        Map<String, String> ibmTUKLATEFEEMAP = new HashMap<String, String>();
        Map<String, String> ncTUKLATEFEEMAP = new HashMap<String, String>();
        int ibmOTCBlocks = 0, ncOTCBlocks = 0;        
        XlsxFile xlsxfile = new XlsxFile();
        eventsList = xlsxfile.fetchTagNames(propertyFileRead("IBMBrTag"), propertyFileRead("NC_IBM_Maps"));
        
      //  System.out.println("************************Checking for TUKLATEFEE TAGS*********************************");
                System.out.println("\n\n"+IBMFile+"<-------------------------:"+ACCOUNTNUMBER+":--------------------------->"+NCFile);
        
        for (String brEventTag : eventsList) {
            if (brEventTag.toUpperCase().trim().contains("TUKLATEFEE")) {
                brMapDocTUKAL = br6.fetchBRMapping(brEventTag);
                ibmTUKLATEFEEAL = fetchTUKLATEFEEAL(IBMFile);
                ncTUKLATEFEEAL = fetchTUKLATEFEEAL(NCFile);
                ibmOTCBlocks = getCountOfBlocks(ibmTUKLATEFEEAL);
                ncOTCBlocks = getCountOfBlocks(ncTUKLATEFEEAL);
                System.out.println(ibmOTCBlocks + "::<-IBM---OTC Blocks---NC->:" + ncOTCBlocks);
                ibmTUKLATEFEEMAP = convertTUKLATEFEEAL2MAP(ibmTUKLATEFEEAL, ibmOTCBlocks);
                ncTUKLATEFEEMAP = convertTUKLATEFEEAL2MAP(ncTUKLATEFEEAL, ncOTCBlocks);
                ibmLATEFEEPaymentAL = addNull2MissingTags(ibmTUKLATEFEEMAP);
                ncLATEFEEPaymentAL = addNull2MissingTags(ncTUKLATEFEEMAP);
                //gu.displayAL(ncLATEFEEPaymentAL);
                compareTUKLATEFEEAL(ibmLATEFEEPaymentAL, ncLATEFEEPaymentAL);
            }
        }
        return TUKLATEFEE_FLAG;
    }


    public void compareTUKLATEFEEAL(List<String> ibmLATEFEEPaymentAL, List<String> ncLATEFEEPaymentAL) throws Exception {
        LinkedHashMap<String, String> ibmLPFmap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> ncLPFmap = new LinkedHashMap<String, String>();
        List<Map<String, String>> ibmLPFListOfmapBlock = new ArrayList<>();
        List<Map<String, String>> ncLPFListOfmapBlock = new ArrayList<>();
//        List<Map<String, String>> ibmLPFListOfmapBlockMissing = new ArrayList<>();
//        List<Map<String, String>> ncLPFListOfmapBlockMissing = new ArrayList<>();
        List<Set<Map.Entry<String, String>>> ibmNCDateEqual = new ArrayList<>();
//        List<Set<Map.Entry<String, String>>> ibmNCDateNEqual = new ArrayList<>();

//        String mapDocFile = propertyFileRead("NC_IBM_Maps");
//        String taxMapDocSheet = propertyFileRead("TaxMap");

        String ncMapTaxCode=null,ibmMapTaxCode=null;
        double ncTaxCodeVal=0.00;
        double ibmTaxCodeVal=0.00;
        double ncMapValReducedTax=0.00;
        double ncMapNEWPriceVal=0.00;
        int ncMapNEWPriceValINT=0;


        int ctr = 0, ctr1 = 0, ctr2 = 0;

        Object ibmMapKey = null, ibmMapVal = null, ncMapVal = null, ncMapKey = null, ibmDateVal = null, ncDateVal = null;
        double taxVal = 0.00;

        // fetching IBM and NC OTC Map Blocks in List
        for (String a : ibmLATEFEEPaymentAL) {
            ibmLPFmap = gu.convertMapObjectString2Map(a);
            ibmLPFListOfmapBlock.add(ibmLPFmap);
        }
        for (String b : ncLATEFEEPaymentAL) {
            ncLPFmap = gu.convertMapObjectString2Map(b);
            ncLPFListOfmapBlock.add(ncLPFmap);
        }

        System.out.println(ibmLPFListOfmapBlock.size() + ":::IBM<----Files TUK-Blocks MapSize---->NC:::" + ncLPFListOfmapBlock.size());

        for (Map<String, String> ibmLHM : ibmLPFListOfmapBlock) {
            for (Map<String, String> ncLHM : ncLPFListOfmapBlock) {

                ibmDateVal = ibmLHM.get("OTCDATE");
                ncDateVal = ncLHM.get("OTCDATE");

//               -------------------------MAPS notEQUAL Date Equal-------------------------
                for (Map.Entry me : ibmLHM.entrySet()) {
                              try {
                    ibmMapKey = me.getKey().toString().toUpperCase().trim();
                    ibmMapVal = me.getValue().toString().toUpperCase().trim();
                    ncMapVal = ncLHM.get(ibmMapKey).toString().toUpperCase().trim();
                    ibmDateVal = ibmLHM.get("OTCDATE").toString().trim();
                    ncDateVal = ncLHM.get("OTCDATE").toString().trim();
//                    System.out.println("KEYS..." + ibmMapKey + "::IBMKEY<---->VAL::" + ibmMapVal + "::::NcVal:--" + ncMapVal);
                              }catch(Exception e){
                                              System.out.println("check IBM & NC Key Val is Null"+e);
                              }
                    if (((ibmDateVal.equals(ncDateVal)))) {
//                        System.out.println(" \n\n----------MAPS notEQUAL Date EQual-------");
//                        System.out.println(ctr1++ + "====>" + ibmLHM + "::::IBM<----comparing---->NC::::" + ncLHM);
                        ibmNCDateEqual.add(ibmLHM.entrySet());

                        // IF->Date is Null in both the maps :: Else -> Date is NULL in any of the map
                        if ((ibmMapKey.equals("OTCDATE"))) {
                            if (((ibmMapVal.equals("NULL"))) && ((ncMapVal.equals("NULL")))) {
                                System.out.println("\n\n" + ctr++ + "====>" + ibmLHM + "::::IBM<----mapNOT-Equal-Date=Null (both)---->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }
                            if ((!(ibmMapVal.equals("NULL"))) && ((ncMapVal.equals("NULL")))) {
                                System.out.println("\n\n" + ctr++ + "====>" + ibmLHM + "::::IBM<----mapNOT-Equal-Date=Null (any 1)---->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }
                            if (((ibmMapVal.equals("NULL"))) && (!(ncMapVal.equals("NULL")))) {
                                System.out.println("\n\n" + ctr++ + "====>" + ibmLHM + "::::IBM<----mapNOT-Equal-Date=Null (any 1)---->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }  // IF->Date is Null in both the maps :: Else -> Date is NULL in any of the map
                            if ((!(ibmMapVal.equals("NULL"))) && (!(ncMapVal.equals("NULL")))) {
                                System.out.println("\n\n" + ctr++ + "*****>" + ibmLHM + "::::IBM<----mapNOT-Equal-Date=Not Null---->NC::::" + ncLHM);
                                boolean dateFlag = ibmMapVal.toString().equals(ncMapVal.toString()) ? true : false;
                                if (dateFlag) {
                                    printMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                    TUKLATEFEE_FLAG=true;
                                } else {
                                    printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                }
                            }
                        }
                        if ((ibmMapKey.equals("OTCPRICE"))) {
                            if ((!(ibmMapVal.equals("NULL"))) && (!(ncMapVal.equals("NULL")))) {
                                //            System.out.println("\n\n" + ctr1++ + "====>" + ibmLHM + "::::IBM<----ibm-Price.Val::NCPrice.Val---->NC::::" + ncLHM);
                                try {
                                    ncMapTaxCode = ncLHM.get("OTCTAXCODE").trim().toUpperCase();
                                    ncTaxCodeVal = fetchTaxValue(ncMapTaxCode);
                                    taxVal = ncTaxCodeVal / 100;
                                    System.out.println("TV:--" + taxVal);
                                } catch (Exception e) {
                                    System.out.println("Tax Values are NULL:-" + e);
                                }
                                taxVal = taxVal == 0 ? 0 : taxVal;
                                try {
                                    if(taxVal>0) {
                                        ncMapValReducedTax = (double) Math.round(Double.parseDouble(ncMapVal.toString()) * (taxVal));
                                        ncMapNEWPriceVal = (Double.parseDouble((ncMapVal.toString()))) - ncMapValReducedTax;
                                        ncMapNEWPriceValINT = (int) ncMapNEWPriceVal;
                                    }
                                    else if(taxVal==0){
                                        ncMapValReducedTax = (double) Math.round(Double.parseDouble(ncMapVal.toString()) * (taxVal));
                                        ncMapNEWPriceVal = (Double.parseDouble((ncMapVal.toString())));
                                        ncMapNEWPriceValINT = (int) ncMapNEWPriceVal;
                                    }
                                    //              System.out.println("\n" + ncMapVal + "::reduced @" + (taxVal) + "==" + ncMapValReducedTax + "-->" + ncMapNEWPriceVal);
                                    taxValCalc = " ::<-ncVal-----ncTaxCode>"+ncMapTaxCode+":::"+ncTaxCodeVal+" reduced @ " + taxVal + "e" +
                                            "'==" + ncMapValReducedTax + "-->" + ncMapNEWPriceValINT;
                                    boolean priceFlag = (ibmMapVal.toString().equals(String.valueOf(ncMapNEWPriceValINT))) ? true : false;
                                    if (priceFlag == true) {
                                        printMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString() + taxValCalc, "NA");
                                    TUKLATEFEE_FLAG=true;}
                                    else {
                                        printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString() + taxValCalc, "NA");
                                        TUKLATEFEE_FLAG=false;
                                    }
                                } catch (Exception e) {
                                    //            System.out.println("\nvalue cannot caluclated on Null Values:- " + e);
                                }
                            }
                            if (((ibmMapVal.equals("NULL"))) && ((ncMapVal.equals("NULL")))) {
                                //      System.out.println("\n\n" + ctr1++ + "====>" + ibmLHM + "::::IBM<----ibm-Price.Null::NCPrice.Null---->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }
                            if ((!(ibmMapVal.equals("NULL"))) && ((ncMapVal.equals("NULL")))) {
                                //    System.out.println("\n\n" + ctr1++ + "====>" + ibmLHM + "::::IBM<----ibm-Price.Null::NCPrice.Val----->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }
                            if (((ibmMapVal.equals("NULL"))) && (!(ncMapVal.equals("NULL")))) {
                                //  System.out.println("\n\n" + ctr1++ + "====>" + ibmLHM + "::::IBM<----ibm-Price.Val::NCPrice.Null----->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }
                        } // Price ends
                        if ((ibmMapKey.equals("OTCTAXCODE"))) {
                            if (((ibmMapVal.equals("NULL"))) && ((ncMapVal.equals("NULL")))) {
                                System.out.println("\n\n" + ctr++ + "====>" + ibmLHM + "::::IBM<----mapNOT-Equal-Date=Null (both)---->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }
                            if ((!(ibmMapVal.equals("NULL"))) && ((ncMapVal.equals("NULL")))) {
                                System.out.println("\n\n" + ctr++ + "====>" + ibmLHM + "::::IBM<----mapNOT-Equal-Date=Null (any 1)---->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }
                            if (((ibmMapVal.equals("NULL"))) && (!(ncMapVal.equals("NULL")))) {
                                System.out.println("\n\n" + ctr++ + "====>" + ibmLHM + "::::IBM<----mapNOT-Equal-Date=Null (any 1)---->NC::::" + ncLHM);
                                printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString(), ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString(), "NA");
                                TUKLATEFEE_FLAG=false;
                            }  // IF->Date is Null in both the maps :: Else -> Date is NULL in any of the map
                            if ((!(ibmMapVal.equals("NULL"))) && (!(ncMapVal.equals("NULL")))) {
                                System.out.println("\n\n" + ctr++ + "*****>" + ibmLHM + "::::IBM<----mapNOT-Equal-Date=Not Null---->NC::::" + ncLHM);
                                ncMapTaxCode = ncLHM.get("OTCTAXCODE");
                                ibmMapTaxCode=ibmLHM.get("OTCTAXCODE");
                                ibmMapTaxCode="CUK-"+ibmMapTaxCode;
                                ncTaxCodeVal = fetchTaxValue(ncMapTaxCode);
                                ibmTaxCodeVal=fetchTaxValue(ibmMapTaxCode);
                                double ncTaxVal = ncTaxCodeVal / 100;
                                double ibmTaxVal = ncTaxCodeVal / 100;
                                boolean taxFlag = ibmTaxVal == ncTaxVal ? true : false;
                                if (taxFlag) {
                                    printMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString()+":::" + ibmTaxCodeVal, ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString()+":::"+ncTaxCodeVal, "NA");
                                    TUKLATEFEE_FLAG=true;
                                } else {
                                    printUnMatchedReportInExcelSheet(ibmLHM.entrySet().toString(), me.getKey()+"="+ibmMapVal.toString()+":::" + ibmTaxCodeVal, ncLHM.entrySet().toString(), me.getKey()+"="+ncMapVal.toString()+":::"+ncTaxCodeVal, "NA");
                                    TUKLATEFEE_FLAG=false;
                                }
                            }
                        }

                    } // Iterating of ibmLHM Map Ends
                } // Date equal ends
                //----------MAPS notEQUAL Date NOT Equal-------"

            } // IBM MAP Ends
        }
} // compare method ends


    public List<String> addNull2MissingTags(Map<String, String> TUKLATEFEEMAP) throws IOException {
        List<String> updateTUKLATEFEAL = new ArrayList<>();
        Map<String, String> updateDummyTUKLATEFEEMap = new HashMap<>();
        Map<String, String> brMapDocTUKALMap = new HashMap<>();
        List<String> TUKFilemapKey = new ArrayList<>();
        String tagFname = null, strDummy = null, maptagVal = null, tagVal = null;
        String strDummyArr[] = null;

        // brMapDocTUKAL is Mapdoc from NC_IBM_XLS

        for (Map.Entry me : TUKLATEFEEMAP.entrySet()) {
            //  System.out.println(me.getKey()+"<------>"+me.getValue());
            updateDummyTUKLATEFEEMap.clear();
            brMapDocTUKALMap.clear();
            brMapDocTUKALMap = GenUtils.convertList2Map(brMapDocTUKAL);
            maptagVal = me.getValue().toString().trim().toUpperCase();
            strDummyArr = maptagVal.split("\\|");
            for (int i = 0; i < strDummyArr.length; i++) {
                //   System.out.println("array...."+strDummyArr[i]);
                String temp = strDummyArr[i].trim();
                tagFname = StringUtils.substringBefore(temp, " ").toString().trim().toUpperCase();
                tagVal = StringUtils.substringAfter(temp, " ").toString().trim().toUpperCase();
                TUKFilemapKey.add(tagFname);
                //System.out.println(tagFname+"<---LPF--->"+tagVal);
                updateDummyTUKLATEFEEMap.put(tagFname, tagVal);
            }
            try {
                Set<String> keysInTUKFILEMap = new HashSet<String>(updateDummyTUKLATEFEEMap.keySet());
                Set<String> keysInMasterTUKMap = new HashSet<String>(brMapDocTUKALMap.keySet());
                Set<String> inBNotA = new HashSet<String>(keysInMasterTUKMap);
//                    System.out.println(brMapDocTUKALMap);
                if (inBNotA.removeAll(keysInTUKFILEMap)) {
                    //     System.out.println("Missing tags....."+inBNotA);
                    String inBNotAStr = inBNotA.toString().replaceAll("\\[", "").replaceAll("\\]", "").toString().trim().toUpperCase();
                    if ((inBNotAStr.contains(",")) && (!(inBNotAStr.equalsIgnoreCase("")))) {
                        String[] array = inBNotAStr.split(",");
                        for (int i = 0; i < array.length; i++) {
                            updateDummyTUKLATEFEEMap.put(array[i].trim(), "NULL");
                        }
                    } else if ((!(inBNotAStr.contains(","))) && (!(inBNotAStr.equalsIgnoreCase("")))) {
                        String array = inBNotAStr;
                        updateDummyTUKLATEFEEMap.put(array.trim(), "NULL");
                    }
                    inBNotA.clear();
                }
                //  gu.displayMap(updateDummyTUKLATEFEEMap);
            } catch (NullPointerException e) {
                System.out.println("Exception thrown while adding null to missing tags method> : " + e);
            }
            updateTUKLATEFEAL.add(updateDummyTUKLATEFEEMap.toString());
        }

        return updateTUKLATEFEAL;
    }


    public static List<String> fetchTUKLATEFEEAL(String dataFile) throws IOException {
        List<String> tempAL = new ArrayList<>();
        LinkedHashMap<String, String> tempLinkedMap = new LinkedHashMap<String, String>();
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        String line = null, tukLateFeeTag = null, tukLateFeeTagVal = null, tukTagName;
        String[] evTOTALTag;
        boolean sTUKBLK = false, sOTCBLK = false;
        int ctr = 0;
        System.out.println(dataFile);
        while (((line = br.readLine()) != null)) {
            if (line.toUpperCase().trim().contains(startTUKLateFeeBlock)) {
                sTUKBLK = true;
            } else if ((line.toUpperCase().trim().contains(endTUKLATEFEEBlock))) {
                sTUKBLK = false;  
                }            
//            else {
//                                           System.out.println("File do not have"+startTUKLateFeeBlock+"::"+endTUKLATEFEEBlock);
//            }
                           
            if (sTUKBLK == true) {
                String otcChek = StringUtils.substringBefore(line, " ");
                if (otcChek.equalsIgnoreCase(startOTCBlock)) {
                    sOTCBLK = true;
                    tempAL.add(line);
                } else if (otcChek.equalsIgnoreCase(endOTCBlock)) {
                    sOTCBLK = false;
                    tempAL.add(line);
                }
//                else {
//                          System.out.println("File do not have"+startOTCBlock+"::"+endOTCBlock);
//                }
                if (sOTCBLK == true) {
                    tukTagName = StringUtils.substringBefore(line, " ").toUpperCase().trim();
                    if (brMapDocTUKAL.contains(tukTagName)) tempAL.add(line);
                }
            } // end of sTUKBLK==true
        }
//            for(String a : tempAL){
//                System.out.println("\n\n count:-" + ctr++ +"........" +a);
//            }
        return tempAL;
    }

    public static int getCountOfBlocks(List<String> tuklatefeeAL) {
        int countOfBlock = 0;
        for (String a : tuklatefeeAL) {
            if (a.toUpperCase().contains(startOTCBlock)) countOfBlock++;
        }
        return countOfBlock;
    }

    public Map<String, String> convertTUKLATEFEEAL2MAP(List<String> tuklatefeeAL, int countOfBlock) throws
            Exception {
        Map<String, List<String>> brTUKLATEFEEMap = new HashMap<>();
        Map<String, String> tempBlockLinkedHashMap = new HashMap<>();
        List<String> tempBlockAl = new ArrayList<>();
        List<List<String>> tempBlockAlDummy = new ArrayList<>();
        List<String> AlTemp = new ArrayList<>();
        List<String> AlTempDummy = new ArrayList<>();
        String tagName = null, tagVal = null, AlTempStr, tagOTCTYPNAMLatPymnt = "Late Payment Fees", key = "TUKLATEFEE", mapKey = "mk";
        String[] strArr = null;
        brTUKLATEFEEMap = br6.fetchBRMappingHMap(key);
        int ctr = 0;
        boolean otcFlag = false, sOTCBLK = false;

        List<String> al = new ArrayList<>();

        for (String a : tuklatefeeAL) {
            String tn = StringUtils.substringBefore(a, " ").trim();
            if ((a.contains(startOTCBlock))) {
                otcFlag = true;
                AlTemp.clear();
            } else if ((a.contains(endOTCBlock))) {
                otcFlag = false;
            }

            if (otcFlag == true) {
                AlTemp.add(a);
            } else if (otcFlag == false) {
                AlTempStr = AlTemp.toString().trim();
                tempBlockAl.add(AlTempStr);
            }
        }
        for (String z : tempBlockAl) {
            z = z.replaceAll(",", "").trim();
            z = z.replace("[BSTARTOTC |", "").trim();
            z = z.replaceAll("]", "").trim();
            if (z.contains("Late Payment Fees")) {
                mapKey = mapKey + ctr++;
                tempBlockLinkedHashMap.put(mapKey, z);
                mapKey = "mk";
            }
        }
        return tempBlockLinkedHashMap;
    }

    public double fetchTaxValue(String taxCode) throws Exception {
        Hashtable<Object, Object> TaxMap = new Hashtable<Object, Object>();
        String mapDocFile = propertyFileRead("NC_IBM_Maps");
        String taxMapDocSheet = propertyFileRead("TaxMap");
        String a=null;
        double taxVal = 0;
        MappingDoc md = new MappingDoc();
        TaxMap = md.getBRMappingDoc(mapDocFile, taxMapDocSheet);
        try {
            a = TaxMap.get(taxCode).toString();
        }catch(Exception e){
            System.out.println("TAX value cannot be fetched for TAX Code =NULL "+ e);
        }
        try {
            a = StringUtils.substringAfter(a, "=");
            a = a.replaceAll("[={}\\[\\]+]", "");
            //System.out.println("taxVal111:---"+a);
            taxVal = Double.parseDouble((StringUtils.substringAfter(a, ",")));
            taxVal = taxVal / 100;
            // System.out.println("taxVal2:---"+taxVal);
        }catch(Exception e){
            System.out.println("TAX value cannot be calculated for ZERO value = NULL "+ e);
        }
        return taxVal;
    }
} 
