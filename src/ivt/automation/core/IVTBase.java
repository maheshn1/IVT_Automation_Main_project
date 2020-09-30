package ivt.automation.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class IVTBase {

	//Reading Properties file code
	//Start Tag List
	//End Tag List
	//checking change
	//Extract Tag Name
	public static int ACCOUNT_NUMBER = 0;
	public static int IBMTAG_NUMBER = 1;
	public static int IBMVALUE_NUMBER = 2;
	public static int NCTAG_NUMBER = 3;
	public static int NCVALUE_NUMBER = 4;
	public static int DIFFERENCE_NUMBER = 5;
	public static int FLAG_NUMBER = 6;

	public static int IBMValue_row = 1;
	public static int NCValue_row = 1;
	public static int flag_row = 1;

	public static String ACCOUNTNUMBER = null;
	public static String CCAFILENAME = null;
	public static String sheetName = "IBM_NC_AllTags";
	public static String TAX_Val_At_NC = "20%";

	public static List<String> ibmAndNCFiles = new ArrayList<>();
	public static List<String> ccaAndNCFiles = new ArrayList<>();

	public static boolean TUKLATEFEE_FLAG=false;
	public static boolean TUKPAPEREFEE_FLAG=false;
	public static boolean EVTOTAL_FLAG=false;
	public static boolean TUKAIRTIMEPLAN_FLAG=false;
	public static boolean CCA_FLAG=false;
	public static boolean TUKCURRENTSPEND_FLAG=false;
	public static boolean TUKDISCOUNTLINE_FLAG=false;


	public static String propFile = "C:\\Users\\094539\\Desktop\\IVT DOCS\\Code\\IVT_Automation_WorkSapce\\IVT_Automation_Main_project\\ivtAuto.properties";
	public static Properties prop = new Properties();
	public static Files files = new Files();

	public static List<String> fetchIBMAndNCFiles() throws Exception {
		IVTExcelReport.createExcelSheet();
		IVTExcelReport.createEventExcelSheet();
		ibmAndNCFiles = files.searchIBMFileForNCFile("IVT_Folder");	
		return ibmAndNCFiles;
	}

	public static List<String> fetchCCAAndNCFiles() throws Exception {
		IVTExcelReport.createCCAExcelSheet();
		ccaAndNCFiles = files.searchCCAFileForNCFile();
		return ccaAndNCFiles;
	}

	/*public static List<String> fetchEventIBMAndNCFiles() throws Exception {
		IVTExcelReport.createEventExcelSheet();
		ibmAndNCFiles = files.searchIBMFileForNCFile("IVTDataFilesEvent");	
		return ibmAndNCFiles;
	}*/

	public String propertyFileRead(String propFileName) throws Exception {
		FileReader fr = new FileReader(propFile);
		prop.load(fr);
		return prop.getProperty(propFileName);
	}

	public String[] splitStringValue(String Value, String delimiter) {
		String newVal[] = Value.split(delimiter);	
		return newVal;
	}

	public static void printUnMatchedReportInExcelSheet(String ibmTag, String ibmValue, String ncTag, String ncValue, String difference) throws Exception 
	{		
		IVTExcelReport.setCellValues(sheetName, IBMValue_row, ACCOUNT_NUMBER, ACCOUNTNUMBER);
		IVTExcelReport.setCellValues(sheetName, IBMValue_row, IBMTAG_NUMBER, ibmTag);
		IVTExcelReport.setCellValues(sheetName, IBMValue_row++, IBMVALUE_NUMBER, ibmValue);
		IVTExcelReport.setCellValues(sheetName, NCValue_row, NCTAG_NUMBER, ncTag);
		IVTExcelReport.setCellValues(sheetName, NCValue_row, NCVALUE_NUMBER, ncValue);
		IVTExcelReport.setCellValues(sheetName, NCValue_row++, DIFFERENCE_NUMBER, difference);
		IVTExcelReport.setCellValues(sheetName, flag_row++, FLAG_NUMBER, "NO");
	}

	public static void printMatchedReportInExcelSheet(String ibmTag, String ibmValue, String ncTag, String ncValue, String difference) throws Exception 
	{		
		IVTExcelReport.setCellValues(sheetName, IBMValue_row, ACCOUNT_NUMBER, ACCOUNTNUMBER);
		IVTExcelReport.setCellValues(sheetName, IBMValue_row, IBMTAG_NUMBER, ibmTag);
		IVTExcelReport.setCellValues(sheetName, IBMValue_row++, IBMVALUE_NUMBER, ibmValue);
		IVTExcelReport.setCellValues(sheetName, NCValue_row, NCTAG_NUMBER, ncTag);
		IVTExcelReport.setCellValues(sheetName, NCValue_row, NCVALUE_NUMBER, ncValue);
		IVTExcelReport.setCellValues(sheetName, NCValue_row++, DIFFERENCE_NUMBER, difference);
		IVTExcelReport.setCellValues(sheetName, flag_row++, FLAG_NUMBER, "YES");
	}
	public static void printEventUnMatchedReportInExcelSheet(String ibmTag, String ibmTagName, String ibmValue, String IBMVALUE_After_Tax1, String ncTag, String ncTagNames, String ncValue, String difference) throws Exception {
		int ACCOUNT_NUMBER = 0, IBMTAG_NUMBER = 1, IBM_TAGNAMES = 2, TAX_AtNC = 3, IBMVALUE_NUMBER = 4;
		int IBMVALUE_After_Tax = 5, NCTAG_NUMBER = 6, NC_TAGNAMES = 7, NCVALUE_NUMBER = 8;
		int DIFFERENCE_NUMBER = 9, FLAG_NUMBER = 10;
		String EventsheetName = "IBM_NC_EVENT_Tags";

		IVTExcelReport.setCellValues(EventsheetName, ++IBMValue_row, ACCOUNT_NUMBER, ACCOUNTNUMBER);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IBMTAG_NUMBER, ibmTag);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IBM_TAGNAMES, ibmTagName);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, TAX_AtNC, TAX_Val_At_NC);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IBMVALUE_NUMBER, ibmValue);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IBMVALUE_After_Tax, IBMVALUE_After_Tax1);
		IVTExcelReport.setCellValues(EventsheetName, ++NCValue_row, NCTAG_NUMBER, ncTag);
		IVTExcelReport.setCellValues(EventsheetName, NCValue_row, NC_TAGNAMES, ncTagNames);
		IVTExcelReport.setCellValues(EventsheetName, NCValue_row, NCVALUE_NUMBER, ncValue);
		IVTExcelReport.setCellValues(EventsheetName, NCValue_row, DIFFERENCE_NUMBER, difference);
		IVTExcelReport.setCellValues(EventsheetName, ++flag_row, FLAG_NUMBER, "NO");
	}

	public static void printEventMatchedReportInExcelSheet(String ibmTag, String ibmTagName, String ibmValue, String IBMVALUE_After_Tax1, String ncTag, String ncTagNames, String ncValue, String difference) throws Exception {
		int ACCOUNT_NUMBER = 0, IBMTAG_NUMBER = 1, IBM_TAGNAMES = 2, TAX_AtNC = 3, IBMVALUE_NUMBER = 4;
		int IBMVALUE_After_Tax = 5, NCTAG_NUMBER = 6, NC_TAGNAMES = 7, NCVALUE_NUMBER = 8;
		int DIFFERENCE_NUMBER = 9, FLAG_NUMBER = 10;
		String EventsheetName = "IBM_NC_EVENT_Tags";

		IVTExcelReport.setCellValues(EventsheetName, ++IBMValue_row, ACCOUNT_NUMBER, ACCOUNTNUMBER);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IBMTAG_NUMBER, ibmTag);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IBM_TAGNAMES, ibmTagName);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, TAX_AtNC, TAX_Val_At_NC);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IBMVALUE_NUMBER, ibmValue);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IBMVALUE_After_Tax, IBMVALUE_After_Tax1);
		IVTExcelReport.setCellValues(EventsheetName, ++NCValue_row, NCTAG_NUMBER, ncTag);
		IVTExcelReport.setCellValues(EventsheetName, NCValue_row, NC_TAGNAMES, ncTagNames);
		IVTExcelReport.setCellValues(EventsheetName, NCValue_row, NCVALUE_NUMBER, ncValue);
		IVTExcelReport.setCellValues(EventsheetName, NCValue_row, DIFFERENCE_NUMBER, difference);
		IVTExcelReport.setCellValues(EventsheetName, ++flag_row, FLAG_NUMBER, "Yes");
	}

	public static void printFilesReport(int File_Num, String FileNames, String File_Origin, String File_Modify_Date, String File_Missing_From) throws Exception {
		int FILE_NO = 0, IVT_FileName = 1, IVT_File_Origin = 2, File_Modified_Date = 3, File_Missing_From_Folder = 4;
		String EventsheetName = "IBM_NC_ERROR_FILES";

		IVTExcelReport.setCellValues(EventsheetName, ++IBMValue_row, FILE_NO, String.valueOf(File_Num));
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IVT_FileName, FileNames);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, IVT_File_Origin, File_Origin);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, File_Modified_Date, File_Modify_Date);
		IVTExcelReport.setCellValues(EventsheetName, IBMValue_row, File_Missing_From_Folder, File_Missing_From);
	}

	public void ERRORFILEMOVE() throws Exception {
		File dataSource = new File(propertyFileRead("File-Data_Error"));
		File ERROR_FILE_TO = new File(propertyFileRead("ERROR-FILE-TO"));
		String dataSourceFileName=null;
		Path ERROR_FILE_TOAlteredPath=null;

		File[] dataSourceList = dataSource.listFiles();
		File[] ERROR_FILE_TOList = ERROR_FILE_TO.listFiles();

		java.nio.file.Path dataSourcePathPath = dataSource.toPath();
		java.nio.file.Path ERROR_FILE_TOPath = ERROR_FILE_TO.toPath();


		if ((dataSource.exists())) {
			if (!(ERROR_FILE_TO.exists())) {
				System.out.println("ERROR_FILE_TO Folder does not exist...in the Path." + ERROR_FILE_TO.getAbsolutePath() + " Creating one.... Blank Folder");
				ERROR_FILE_TO.mkdir();
			} else  if ((ERROR_FILE_TO.exists())) {
				for (File dsl : dataSourceList) {
					if (dsl.isFile()) {
						try {
							dataSourceFileName= dsl.getName();
							ERROR_FILE_TOAlteredPath= Paths.get(ERROR_FILE_TO +"\\"+ dataSourceFileName);
							//  System.out.println(dataFileMatchedAlteredPath);
							BasicFileAttributes attrs = java.nio.file.Files.readAttributes(dsl.toPath(), BasicFileAttributes.class);
							System.out.println("Moving Files to Archive.....");
							java.nio.file.Files.move(dsl.toPath(), ERROR_FILE_TOAlteredPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ex) {
						}
					}
				}
			}
		} else if (!(dataSource.exists())) {
			System.out.println("dataSource Folder does not exist...in the Path." + dataSource.getAbsolutePath() + " Creating one.... Blank Folder");
			dataSource.mkdir();
		}
	}

	public void MatchedFILEMOVE() throws Exception {
		File dataSource = new File(propertyFileRead("File-Data_Matched"));
		File dataFileMatched = new File(propertyFileRead("File-Matched"));
		String dataSourceFileName=null;
		Path dataFileMatchedAlteredPath=null;

		File[] dataFileMatchedList = dataFileMatched.listFiles();
		File[] dataSourceList = dataSource.listFiles();


		java.nio.file.Path dataFileMatchedPath = dataFileMatched.toPath();
		java.nio.file.Path dataSourcePath = dataSource.toPath();

		if ((dataSource.exists())) {
			if (!(dataFileMatched.exists())) {
				System.out.println("ERROR_FILE_TO Folder does not exist...in the Path." + dataFileMatched.getAbsolutePath() + " Creating one.... Blank Folder");
				dataFileMatched.mkdir();
			} else  if ((dataFileMatched.exists())) {
				for (File dsl : dataSourceList) {
					if (dsl.isFile()) {
						try {
							dataSourceFileName= dsl.getName();
							dataFileMatchedAlteredPath= Paths.get(dataFileMatched +"\\"+ dataSourceFileName);
							//  System.out.println(dataFileMatchedAlteredPath);
							BasicFileAttributes attrs = java.nio.file.Files.readAttributes(dsl.toPath(), BasicFileAttributes.class);
							System.out.println("Moving Files to Archive.....");
							java.nio.file.Files.move(dsl.toPath(), dataFileMatchedAlteredPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ex) {
						}
					}
				}
			}
		} else if (!(dataSource.exists())) {
			System.out.println("dataSource Folder does not exist...in the Path." + dataSource.getAbsolutePath() + " Creating one.... Blank Folder");
			dataSource.mkdir();
		}
	}

	public void fetchFileNamesFromFolder(String folderName) throws Exception {
		File dataSource = new File(folderName);
		File folder = new File(dataSource.toString());
		File[] listOfFiles = folder.listFiles();
		Map<File, String> fileInFolderMap = new HashMap<>();
		List<File> subFolderInFolderList = new ArrayList<>();
		String fileName = null, foldersName = null, formattedDate = null;
		int folderCtr = 0, fileCtr = 0;
		IVTExcelReport.createMissingFileSheet();

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		FileTime fileDate;
		Date sysDate = new Date();
		FileTime sysTime = FileTime.fromMillis(sysDate.getTime());
		Date newDate = new Date(sysTime.toMillis());
		LocalDateTime ldt = LocalDateTime.ofInstant(sysTime.toInstant(), ZoneId.systemDefault());
		String File_Origin = propertyFileRead("IVTDataFiles");
		String File_Missing_From = dataSource.getAbsolutePath();

		for (File fil : listOfFiles)
			if (fil.isFile()) {
				BasicFileAttributes attrs = java.nio.file.Files.readAttributes(fil.toPath(), BasicFileAttributes.class);
				fileDate = attrs.creationTime();
				Date filDat = new Date(fileDate.toMillis());
				formattedDate = formatter.format(filDat);
				fileName = fil.getName();
				fileInFolderMap.put(fil, formattedDate);
			} else if (fil.isDirectory()) {
				subFolderInFolderList.add(fil);
			}
		for (File f : subFolderInFolderList) {
			System.out.println("file in Folder:- " + folderCtr + "::--" + f.getName());
		}

		for (Map.Entry<File, String> fm : fileInFolderMap.entrySet()) {
			//System.out.println("file in Folder:- " + fileCtr++ + "::--" + fm.getKey().getName() + " -->" + fm.getValue());
			printFilesReport(fileCtr++, fm.getKey().getName(), File_Origin, fm.getValue(), File_Missing_From);
		}
	}

	public void deleteFilesinFolder(String deleteFilesFolder) {
		File dataSource = new File(deleteFilesFolder);
		File folder = new File(dataSource.toString());
		File[] listOfFiles = folder.listFiles();
		List<File> fileInFolderList = new ArrayList<>();
		List<File> subFolderInFolderList = new ArrayList<>();
		String fileName = null, foldersName = null;
		int folderCtr = 0, fileCtr = 1;

		if (folder.exists()) {
			if (folder.length() > 0) {
				for (File fil : listOfFiles) {
					if (fil.isFile()) {
						fil.delete();
					} else if (!(fil.isFile())) {
						System.out.println("as this is not file, will not be deleted:-- " + fil);
					}
				}
			} else if (folder.length() <= 0) {
				System.out.println(deleteFilesFolder + ":::: is empty");
			}
		} else if (!(folder.exists())) {
			System.out.println("Folder does not exist:- " + deleteFilesFolder);
		}

	}

	public void deleteFolder(File deleteFolder) throws IOException {
		Path dirPath = Paths.get(String.valueOf(deleteFolder));
		File dataSource = new File(deleteFolder.toString());
		File folder = new File(dataSource.toString());
		File[] listOfFiles = folder.listFiles();
		List<File> fileInFolderList = new ArrayList<>();
		List<File> subFolderInFolderList = new ArrayList<>();
		String fileName = null, foldersName = null;
		int folderCtr = 0, fileCtr = 0;
		try {
			if (folder.exists()) {
				if (folder.isDirectory()) {

					System.out.println("Deleting Folder :- ..... " + deleteFolder);
					FileUtils.deleteDirectory(deleteFolder);
				} else if (folder.isFile()) {
					System.out.println(deleteFolder + ":::: is Not Folder to Delete, its a file");
				}
			} else if (!(folder.exists())) {
				System.out.println(deleteFolder + ":::: does not exist");
			}
		} catch (Exception e) {
			System.out.println("Exception :- Directory does not exist to Delete:- " + e);
		}
	}
}
