package ivt.automation.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import ivt.automation.core.IVTBase;

public class Files extends IVTBase{

	public static File directoryPath_ivt;
	
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
		List<String> IBMAndNCFiles = new ArrayList<>();

		NCFilesPath = fetchFileFromivtDataFilesFolder("NC");                             
		IBMFilesPath = fetchFileFromivtDataFilesFolder("IBM");

		String ivtFilePath=IVTBase.propertyFileRead("IVT_Folder");
		String ibmAcNo="",ncAcNo="";

		for(String ncFile : NCFilesPath) {
			ncAcNo=StringUtils.substringAfter(ncFile, "_");
			for(String ibmFile : IBMFilesPath) {
				ibmAcNo=StringUtils.substringAfter(ibmFile,"_");
				if(ncAcNo.equals(ibmAcNo)) {
					ibmFile=ivtFilePath.concat("IBM_"+ibmAcNo);
					ncFile=ivtFilePath.concat("NC_"+ncAcNo);
					String finalFiles = ibmFile+"|"+ncFile;
					IBMAndNCFiles.add(finalFiles);
				}
			}
		}       
		/*for(String k : IBMAndNCFiles) {
			System.out.println(k);
		}*/
		return IBMAndNCFiles;
	}

	public static List<String> searchCCAFileForNCFile() throws Exception
	{
		String ivtFilePath=IVTBase.propertyFileRead("IVT_Folder");
		List<String> NCFilesPath = new  ArrayList<String>();
		List<String> CCAFilesPath = new  ArrayList<String>();
		List<String> CCAAndNCFiles = new ArrayList<>();
		
		NCFilesPath = fetchFileFromivtDataFilesFolder("NC");  
		CCAFilesPath = fetchFileFromivtDataFilesFolder("CCA");        
		String ncAcNo="",cca="", ccaIbmAcNo="", finalFiles="";

		for(String ncFile : NCFilesPath) {
			ncAcNo=StringUtils.substringAfter(ncFile, "_");
			ncFile=ivtFilePath.concat("NC_"+ncAcNo);
			for(String ccaFile : CCAFilesPath) { //IBM_1234_12_CCA
				cca = StringUtils.substringAfter(ccaFile,"_");  //1234_12_CCA.txt
				ccaIbmAcNo = StringUtils.substringBefore(cca,"_"); //1234
				if(ncAcNo.equals(ccaIbmAcNo)) {					
					ccaFile = ivtFilePath.concat("IBM_"+cca);
					finalFiles = finalFiles+"|"+ccaFile;
				}
			}
			String finalFileList = ncFile+finalFiles;
			CCAAndNCFiles.add(finalFileList);
			finalFiles="";			
		}       
		/*for(String k : CCAAndNCFiles) {
			System.out.println(k);
		}*/
		return CCAAndNCFiles;
	}
}
