package ivt.automation.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import ivt.automation.core.IVTBase;

public class Files extends IVTBase{

	public static File directoryPath_ivt;	
	public static List<String> IBMAndNCFiles = new ArrayList<>();
	public static String ACCOUNTNUMBER = null;
	public static String CCAFILENAME = null;
	//All File Operations , To move different folders
	//Read IBM and NC Paths (from folders)
	//Count of Files both IBM and NC
	//Missing Files from both the folder
	//To get the occurrence of tagNames  --(Ex: to get the total boltons as arrayList and extracting only those values and sum it up)

	public static List<String> fetchFileFromivtDataFilesFolder(String FileNameBeginsWith) throws Exception {
		directoryPath_ivt = new File(propertyFileRead("IVT_Folder"));
		List<String> ivtFilesPath = new ArrayList<>();
		File ivtDataFiles_List[] = directoryPath_ivt.listFiles();

		for(File ivtFile : ivtDataFiles_List)       
		{       
			String fn = ivtFile.getName(); 
			fn=FilenameUtils.removeExtension(fn);
			String firstFilNam = StringUtils.substringBefore(fn, "_");
			String lastFilNam = StringUtils.substringAfterLast(fn,"_");           
			String regex="\\d+";   
			if((ivtFile.isFile())&&(firstFilNam.equals(FileNameBeginsWith))&&(lastFilNam.matches(regex)))
			{  
				ivtFilesPath.add(ivtFile.getName());               
			} 
			else if((ivtFile.isFile())&&(lastFilNam.contains(FileNameBeginsWith))) { 
				ivtFilesPath.add(ivtFile.getName());
			} 
		}                   
		return ivtFilesPath;
	}


	public static List<String> searchIBMFileForNCFile() throws Exception
	{
		List<String> NCFilesPath = new  ArrayList<String>();
		List<String> IBMFilesPath = new  ArrayList<String>();
		List<String> CCAFilesPath = new  ArrayList<String>();
		NCFilesPath = fetchFileFromivtDataFilesFolder("NC");                             
		IBMFilesPath = fetchFileFromivtDataFilesFolder("IBM");
		CCAFilesPath = fetchFileFromivtDataFilesFolder("CCA");
		String ivtFilePath=IVTBase.propertyFileRead("IVT_Folder");
		String ibmAcNo="",ncAcNo="", cca = "", ccaAcNo="";

		for(String ncFile : NCFilesPath) {
			ncAcNo=StringUtils.substringAfter(ncFile, "_");
			for(String ibmFile : IBMFilesPath) {
				ibmAcNo=StringUtils.substringAfter(ibmFile,"_");
				for(String ccaFile: CCAFilesPath) {					
					CCAFILENAME =StringUtils.substringBefore(ccaFile,".");
					cca = StringUtils.substringAfter(ccaFile,"_");
					ccaAcNo = StringUtils.substringBefore(cca,"_");
					if(ncAcNo.equals(ibmAcNo) && ibmAcNo.equals(ccaAcNo)) {
						ACCOUNTNUMBER = ibmAcNo;
						ibmFile=ivtFilePath.concat("IBM_"+ibmAcNo);
						ncFile=ivtFilePath.concat("NC_"+ncAcNo);
						ccaFile=ivtFilePath.concat("IBM_"+cca);
						String finalFiles = ibmFile+"|"+ncFile+"|"+ccaFile;
						IBMAndNCFiles.add(finalFiles);
					}
				}
			}
		}       
		for(String k : IBMAndNCFiles) {
			System.out.println(k);
		}
		return IBMAndNCFiles;

	}



}
