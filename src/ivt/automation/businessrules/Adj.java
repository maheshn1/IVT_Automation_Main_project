package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;

public class Adj extends IVTBase{

	public String ibmNCAdj = "ADJ";

	public void CompareADJTags(String ibmFile, String ncFile) throws Exception {
		
		IVTMultiTagCommonFunctionalities ivtMultiTagCommonFunction = new IVTMultiTagCommonFunctionalities();
		
		LinkedHashMap<String,String> ibmMapvalue = new LinkedHashMap<>();
		LinkedHashMap<String,String> ncMapValue = new LinkedHashMap<>();
		List<String> ibmtags = new ArrayList<>();
		List<String> nctags = new ArrayList<>();
		List<String> ibmlist = new ArrayList<>();
		List<String> nclist = new ArrayList<>();
		double ibmAdjValue = 0.0;
		double ncAdjValue = 0.0;
		double diff = 0.0;

		System.out.println("*********************Checking for ADJ***********************");
		
		ibmtags.add(ibmNCAdj);
		nctags.add(ibmNCAdj);

		ibmlist = ivtMultiTagCommonFunction.getTagName(ibmFile,ibmtags);
		ibmMapvalue = ivtMultiTagCommonFunction.convertList2MapMultiValues(ibmlist);

		nclist = ivtMultiTagCommonFunction.getTagName(ncFile,nctags);
		ncMapValue =  ivtMultiTagCommonFunction.convertList2MapMultiValues(nclist);

		ibmAdjValue = ivtMultiTagCommonFunction.getOnlyValues(ibmMapvalue, 3, ",", ibmNCAdj);
		ncAdjValue =  ivtMultiTagCommonFunction.getOnlyValues(ncMapValue, 3, "\\|", ibmNCAdj);

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
