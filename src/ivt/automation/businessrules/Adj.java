package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class Adj extends IVTBase{

	public static String ibmNCAdj = "ADJ";

	public static void CompareADJTags(String ibmFile, String ncFile) throws Exception {
		
		LinkedHashMap<String,String> ibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> ncMapValue = new LinkedHashMap<>();
		List<String> ibmtags = new ArrayList<>();
		List<String> nctags = new ArrayList<>();
		List<String> ibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();
		double ibmAdjValue = 0.0;
		double ncAdjValue = 0.0;
		double diff = 0.0;

		ibmtags.add(ibmNCAdj);
		nctags.add(ibmNCAdj);

		ibmlist = IVTMultiTagCommonFunctionalities.getTagName(ibmFile,ibmtags);
		ibmMapvalue = IVTMultiTagCommonFunctionalities.convertList2MapMultiValues(ibmlist);

		nclist = IVTMultiTagCommonFunctionalities.getTagName(ncFile,nctags);
		ncMapValue =  IVTMultiTagCommonFunctionalities.convertList2MapMultiValues(nclist);

		ibmAdjValue = IVTMultiTagCommonFunctionalities.getOnlyValues(ibmMapvalue, 3, ",", ibmNCAdj);
		ncAdjValue =  IVTMultiTagCommonFunctionalities.getOnlyValues(ncMapValue, 3, "\\|", ibmNCAdj);

		if(ibmAdjValue!=ncAdjValue) {
			if(ibmAdjValue > ncAdjValue) {
				diff = ibmAdjValue - ncAdjValue;				
			}
			else {
				diff = ncAdjValue - ibmAdjValue;
			}
				
			System.out.println("Account Number " + ACCOUNTNUMBER + "::Tag Mapping:" + ibmNCAdj + " vs "+ibmNCAdj+" IBM Value:: " + ibmAdjValue
					+ " NC Value:: " + ncAdjValue);
			printUnMatchedReportInExcelSheet(ibmNCAdj, Double.toString(ibmAdjValue), ibmNCAdj, Double.toString(ncAdjValue), Double.toString(diff));
		}
		else
		{
			printMatchedReportInExcelSheet(ibmNCAdj, Double.toString(ibmAdjValue), ibmNCAdj, Double.toString(ncAdjValue), Double.toString(diff));
		}
	}
}
