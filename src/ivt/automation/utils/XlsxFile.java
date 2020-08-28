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

	static String mapDocPath = null;
	static XSSFWorkbook workbook;
	static XSSFSheet sheet;
	static XSSFRow row;
	static XSSFCell cell;
	static List<String> allSingleTags = new ArrayList<>();

	public static List<String> fetchTagNames(String sheetname,String mapDocPath) throws Exception {

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