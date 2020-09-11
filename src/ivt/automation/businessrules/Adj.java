package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class Adj extends IVTBase{

	public static String ibmAdj = "ADJ";
	public static String ncAdj = "ADJ";

	public static void CompareADJTags(String ibmFile, String ncFile) throws Exception {

		List<String> ibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();
		LinkedHashMap<String,String> ibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> ncMapValue = new LinkedHashMap<>();
		List<String> ibmtags = new ArrayList<>();
		List<String> nctags = new ArrayList<>();
		double ibmAdjValue = 0.0;
		double ncAdjValue = 0.0;

		ibmtags.add(ibmAdj);
		nctags.add(ncAdj);

		ibmlist = IVTMultiTagCommonFunctionalities.getTagName(ibmFile,ibmtags);
		ibmMapvalue = IVTMultiTagCommonFunctionalities.convertList2MapMultiValues(ibmlist);

		nclist = IVTMultiTagCommonFunctionalities.getTagName(ncFile,nctags);
		ncMapValue =  IVTMultiTagCommonFunctionalities.convertList2MapMultiValues(nclist);

		ibmAdjValue = IVTMultiTagCommonFunctionalities.getOnlyValues(ibmMapvalue, 3, ",", ibmAdj);
		ncAdjValue =  IVTMultiTagCommonFunctionalities.getOnlyValues(ncMapValue, 3, "\\|", ncAdj);

		if(ibmAdjValue!=ncAdjValue) {
			System.out.println("Account Number " + Files.ACCOUNTNUMBER + "::Tag Mapping:" + ibmAdj + " vs "+ncAdj+" IBM Value:: " + ibmAdjValue
					+ " NC Value:: " + ncAdjValue);
			printUnMatchedReportInExcelSheet(ibmAdj, Double.toString(ibmAdjValue), ncAdj, Double.toString(ncAdjValue));
		}
		else
		{
			printMatchedReportInExcelSheet(ibmAdj, Double.toString(ibmAdjValue), ncAdj, Double.toString(ncAdjValue));
		}
	}
}
