package ivt.automation.ProjSpecs;

import ivt.automation.core.IVTBase;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.*;

public class MappingDoc extends IVTBase {

      static HashMap<String, ArrayList<String>> brTagMp = new LinkedHashMap<String, ArrayList<String>>();
      static Hashtable<Object, Object> retMap = new Hashtable<Object, Object>();


    public Hashtable<Object, Object> getBRMappingDoc(String mapDocFil,String brMapDocShet) throws Exception {
        String mapDocFile=null,brMapDocSheet=null;
        ArrayList<String> brCorspndngTags = new ArrayList<String>();
        String cellVal="", mapKey="";
        mapDocFile=mapDocFil;
        brMapDocSheet=brMapDocShet;
        int rowNum=0, colNum =0,rowCnt=0,cellCnt=0,colCnt=0;
        short rowLastCellNo=0;
        try{
        FileInputStream fis = new FileInputStream(mapDocFile);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet(brMapDocSheet);
        if (sheet == null) {
            throw new IllegalArgumentException("No sheet exists with name " + brMapDocSheet + " at Dir \\ " + mapDocFil);
        }
        Cell cell = null;  Row row;

        Iterator<Row> rowItr;
        Iterator<Cell> cellItr;

       // Header header = sheet.getHeader();
        rowItr = sheet.rowIterator();

        while(rowItr.hasNext()) {
            Row Trow = (Row) rowItr.next();
            colCnt=Trow.getPhysicalNumberOfCells();
            cellItr=Trow.cellIterator();
            mapKey=Trow.getCell(0).getStringCellValue();
            while(cellItr.hasNext() ) {
                cell = cellItr.next();
                for (int c = 0; c < colCnt; c++) {
                    cell = Trow.getCell(c);
                    rowLastCellNo = Trow.getLastCellNum();
                    if(cell.getCellType()==CellType.STRING)
                        cellVal = cell.getStringCellValue().toUpperCase().trim();
                    else if(cell.getCellType()==CellType.NUMERIC)
                        cellVal = String.valueOf(cell.getNumericCellValue());
//                    cellVal = (cell.getStringCellValue());
//                    cellVal = cellVal.trim().toUpperCase();
                    brCorspndngTags.add(cellVal);
                }
                brTagMp=createHashTab(brCorspndngTags);
                break;
            }
            //System.out.println(brTagMp.keySet()+":::::::::::::"+brTagMp.get("EVTOTAL_16"));
            retMap=getHashTab(brTagMp);
            //System.out.println("k<----"+retMap.keySet()+"V----->"+retMap.entrySet());
            brCorspndngTags.clear();
        }
        }catch(Exception e){
        System.out.println(e);}
        return retMap;
    }

    public Hashtable<Object, Object> getHashTab(HashMap<String, ArrayList<String>> brTagMp) {
        HashMap<Object, Object> brTagMpTemp = new LinkedHashMap<>();
        Hashtable<Object, Object> retMapTemp = new Hashtable<Object, Object>();
        for(Map.Entry e : brTagMp.entrySet()){
            Object a = e.getKey().toString().trim();
            Object b =  e.getValue().toString().trim();
            brTagMpTemp.put(a,b);
           // retMapTemp.put(a,brTagMpTemp);
            retMap.put(a,brTagMpTemp);
        }
        return retMap;
    }

    public HashMap<String, ArrayList<String>> createHashTab(ArrayList<String> brCorspndngTags) {
        HashMap<String, ArrayList<String>> brTagMpTemp = new LinkedHashMap<String, ArrayList<String>>();
        String brCorspndngKey=brCorspndngTags.get(0);
        brCorspndngTags.remove(0);
        brTagMpTemp.put(brCorspndngKey,brCorspndngTags);
       // brTagMpTemp.put(brCorspndngTags.get(0),brCorspndngTags);
        //System.out.println("KS--->"+brTagMpTemp.keySet()+":::::::::::::"+brTagMpTemp.entrySet());
        return  brTagMpTemp;
    }

    public void parseMap(Hashtable<Object, Object> anyMap){
        String s = null,t = null;
        for(Map.Entry mapEntries : anyMap.entrySet()){
            s= mapEntries.getKey().toString();
            t=mapEntries.getValue().toString();
            //System.out.println(s+"-->"+t);
        }
    }

    public void parseMap (HashMap < String, ArrayList < String >> anyMap){
        String s = null, t = null;
        for (Map.Entry mapEntries : anyMap.entrySet()) {
            s = mapEntries.getKey().toString();
            t = mapEntries.getValue().toString();
            //System.out.println(s + "-->" + t);
        }
    }


}