package ivt.automation.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class IVTExcelReport {

	public static FileOutputStream fos = null;
	public static FileInputStream fis = null;
	public static XSSFWorkbook workbook = null;
	public static XSSFSheet sheet = null;
	public static XSSFRow row = null;
	public static XSSFCell cell = null;

	public static DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss");
	public static Date date = new Date();
	public static String filePathdate = dateFormat.format(date).toString();
	public static File file = new File(".//ExcelReport//"+filePathdate+"_IVT.xlsx");

	public static void createExcelSheet() throws Exception {

		workbook = new XSSFWorkbook();
		fos = new FileOutputStream(file);
		XSSFSheet IBM_NC_AllTags = workbook.createSheet("IBM_NC_AllTags");
		Row row = IBM_NC_AllTags.createRow(0);

		//Create header CellStyle
		XSSFFont headerFont = workbook.createFont();
		headerFont.setColor(IndexedColors.WHITE.index);
		CellStyle headerCellStyle = IBM_NC_AllTags.getWorkbook().createCellStyle();
		// fill foreground color ...
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
		// and solid fill pattern produces solid grey cell fill
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		//to set Border to the cell
		headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		headerCellStyle.setBorderLeft(BorderStyle.DOUBLE);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.DASHED);

		headerCellStyle.setFont(headerFont);

		Cell CustomerAccount_No = row.createCell(0);
		CustomerAccount_No.setCellStyle(headerCellStyle);
		CustomerAccount_No.setCellValue("CustomerAccount_No");
		
		Cell IBM_Tag = row.createCell(1);
		IBM_Tag.setCellStyle(headerCellStyle);
		IBM_Tag.setCellValue("IBM_Tag");

		Cell IBM_TagValue = row.createCell(2);
		IBM_TagValue.setCellStyle(headerCellStyle);
		IBM_TagValue.setCellValue("IBM_TagValue");

		Cell NC_Tag = row.createCell(3);
		NC_Tag.setCellStyle(headerCellStyle);
		NC_Tag.setCellValue("NC_Tag");

		Cell NC_TagValue = row.createCell(4);
		NC_TagValue.setCellStyle(headerCellStyle);
		NC_TagValue.setCellValue("NC_TagValue");

		Cell Difference = row.createCell(5);
		Difference.setCellStyle(headerCellStyle);
		Difference.setCellValue("(NC_Tag_Value-IBM_TagValue)");

		Cell Match_Flag = row.createCell(6);
		Match_Flag.setCellStyle(headerCellStyle);
		Match_Flag.setCellValue("Match_Flag");

		workbook.write(fos);
		fos.close();
	}

	public static void createCCAExcelSheet() throws Exception {

		//workbook = new XSSFWorkbook();
		fos = new FileOutputStream(file);
		XSSFSheet IBM_NC_CCATags = workbook.createSheet("IBM_NC_CCATags");
		Row row = IBM_NC_CCATags.createRow(0);

		//Create header CellStyle
		XSSFFont headerFont = workbook.createFont();
		headerFont.setColor(IndexedColors.WHITE.index);
		CellStyle headerCellStyle = IBM_NC_CCATags.getWorkbook().createCellStyle();
		// fill foreground color ...
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
		// and solid fill pattern produces solid grey cell fill
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		//to set Border to the cell
		headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		headerCellStyle.setBorderLeft(BorderStyle.DOUBLE);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.DASHED);

		headerCellStyle.setFont(headerFont);

		Cell CustomerAccount_No = row.createCell(0);
		CustomerAccount_No.setCellStyle(headerCellStyle);
		CustomerAccount_No.setCellValue("CustomerAccount_No");

		Cell IBM_CCA_File = row.createCell(1);
		IBM_CCA_File.setCellStyle(headerCellStyle);
		IBM_CCA_File.setCellValue("IBM_CCA_File");

		Cell IBM_Tag = row.createCell(2);
		IBM_Tag.setCellStyle(headerCellStyle);
		IBM_Tag.setCellValue("IBM_Tag");

		Cell IBM_TagValue = row.createCell(3);
		IBM_TagValue.setCellStyle(headerCellStyle);
		IBM_TagValue.setCellValue("IBM_TagValue");

		Cell NC_Tag = row.createCell(4);
		NC_Tag.setCellStyle(headerCellStyle);
		NC_Tag.setCellValue("NC_Tag");

		Cell NC_TagValue = row.createCell(5);
		NC_TagValue.setCellStyle(headerCellStyle);
		NC_TagValue.setCellValue("NC_TagValue");

		Cell Difference = row.createCell(6);
		Difference.setCellStyle(headerCellStyle);
		Difference.setCellValue("(NC_Tag_Value-IBM_TagValue)");

		Cell Match_Flag = row.createCell(7);
		Match_Flag.setCellStyle(headerCellStyle);
		Match_Flag.setCellValue("Match_Flag");

		workbook.write(fos);
		fos.close();
	}

	public static void setCellValues(String sheetName, int rowNum, int cellNum,String data) throws Exception
	{	
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNum);
		if(row == null)
			row = sheet.createRow(rowNum);

		cell = row.getCell(cellNum);
		if(cell == null)
			cell = row.createCell(cellNum);	

		// XSSFFont cell_font = workbook.createFont();
		XSSFCellStyle cell_style = workbook.createCellStyle();
		cell_style.setBorderBottom(BorderStyle.MEDIUM);
		cell_style.setBorderLeft(BorderStyle.MEDIUM);
		cell_style.setBorderRight(BorderStyle.MEDIUM);
		cell_style.setBorderTop(BorderStyle.MEDIUM);
		cell.setCellStyle(cell_style);

		cell.setCellValue(data);

		fos = new FileOutputStream(file);
		workbook.write(fos);
		fos.close();
	}
}
