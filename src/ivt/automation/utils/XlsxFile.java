package ivt.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxFile {

	String mapDocPath = null;
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;
	List<String> allSingleTags = new ArrayList<>();

	public List<String> fetchTagNames(String sheetname,String mapDocPath) throws Exception {
		
		FileInputStream fis = new FileInputStream(new File(mapDocPath));
		workbook = new XSSFWorkbook(fis);
		sheet = workbook.getSheet(sheetname);
				
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			String	tag = sheet.getRow(i).getCell(0).getStringCellValue().trim();
			allSingleTags.add(tag);
		}
		fis.close();
		return allSingleTags;
	}
		
}