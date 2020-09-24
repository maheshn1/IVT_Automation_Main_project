package ivt.automation.businessrules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.core.IVTBase;
import ivt.automation.utils.XlsxFile;

//Actual Comparison of two files
//Comparing method
//in general, we have to compare around 10k GMF files. hence values for file names and total number of GMF files should be input/fetched from Files.Java.
public class IVTSingleTagCompareFiles extends IVTBase {
	
	public String line = null;
	public List<String> singleTagsList = new ArrayList<String>();
	public LinkedHashMap<String,String> tagNameAndValueIBM = new LinkedHashMap<String,String>();
	public LinkedHashMap<String,String> tagNameAndValueNC = new LinkedHashMap<String,String>();

	public List<String> getIBMNCTagNames(String fileName) throws Exception, Exception {
		List<String> tempal = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while (((line = br.readLine()) != null)) {
			int count = singleTagsList.size();
			for (int i = 0; i < count; i++) {
				if (line.startsWith(singleTagsList.get(i))) {
					tempal.add(line);
					break;
				}
			}
		}
		br.close();
		return tempal;
	}	

	//Converts List to Map for single tag values which needs delimiter replacement
	public LinkedHashMap<String,String> convertList2Map(List<String> list) throws Exception {
		LinkedHashMap<String,String> tempLHM = new LinkedHashMap<>();
		for(String s : list){
			String key = StringUtils.substringBefore(s," ");
			String val = StringUtils.substringAfter(s," ");
			String Value = val.replaceAll("\\|", "");
			tempLHM.put(key,Value);
		}
		return tempLHM;
	}

	public void compareIBMAndNCSingleTags(String fileIBM, String fileNC) throws Exception {
		XlsxFile xlsxfile = new XlsxFile();
		//try {
		List<String> ibmlist = new ArrayList<>();		
		List<String> nclist = new ArrayList<>();
		double ibmDoubleValue =0.0;
		double ncDoubleValue =0.0;
		double diff =0.0;

		singleTagsList = xlsxfile.fetchTagNames(propertyFileRead("IBMNCTAG"),propertyFileRead("NC_IBM_Maps"));

		tagNameAndValueIBM.clear();		
		ibmlist = getIBMNCTagNames(fileIBM);
		tagNameAndValueIBM = convertList2Map(ibmlist);

		tagNameAndValueNC.clear();
		nclist = getIBMNCTagNames(fileNC);
		tagNameAndValueNC = convertList2Map(nclist);

		for (String tag : singleTagsList) {
			String IBMValue = tagNameAndValueIBM.get(tag);
			String NCValue = tagNameAndValueNC.get(tag);

			if (!IBMValue.equalsIgnoreCase(NCValue)) {

				if(!(tag.equalsIgnoreCase("ACCOUNTNO") || tag.equalsIgnoreCase("BILLDATE") || tag.equalsIgnoreCase("PAYMENTDUEDATE") 
						|| tag.equalsIgnoreCase("INVOICESTART") || tag.equalsIgnoreCase("INVOICEEND"))) {
					
					ibmDoubleValue = Double.parseDouble(IBMValue);
					ncDoubleValue = Double.parseDouble(NCValue);

					if(ibmDoubleValue > ncDoubleValue) {
						diff = ibmDoubleValue - ncDoubleValue;				
					}
					else {
						diff = ncDoubleValue - ibmDoubleValue;
					}
				}
				System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag:" + tag + " IBM Value:: " + IBMValue
						+ " NC Value:: " + NCValue);
				printUnMatchedReportInExcelSheet(tag, IBMValue, tag, NCValue, Double.toString(diff));
				diff = 0.0;

			} else {
				//	 System.out.println("Account Number "+ ACCOUNTNUMBER+"::Tag:"+tag+" IBM Value::"
				//		 +IBMValue+ " NC Value:: "+NCValue);
				diff = 0.0;
				printMatchedReportInExcelSheet(tag, IBMValue, tag, NCValue, Double.toString(diff));
			}

		}
		singleTagsList.clear();
		//} 
		/*catch (NullPointerException e) {
			System.out.println("Respective tag not found");
		}*/
	}
}
