package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class TukLateFee extends IVTBase {

	public static String otcDate = "OTCDATE";
	public static String otcTypeName = "OTCTYPENAME Late Payment Fees";
	public static List<String> lateFeestagIBM = new ArrayList<>();
	public static List<String> LateFeestagNC = new ArrayList<>();

	public static void compareTukLateFee(String fileIBM, String fileNC) throws Exception {
		lateFeestagIBM = OTCCommonTagsFunctionality.fetchOTCTypeNameDateAndPriceTag(fileIBM);
		LateFeestagNC =  OTCCommonTagsFunctionality.fetchOTCTypeNameDateAndPriceTag(fileNC);

		for(int i = 0;i<lateFeestagIBM.size();i++) {

			LinkedHashMap<String, String> ibm = new LinkedHashMap<>();			
			ibm = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(lateFeestagIBM.get(i));
			

			for(int j = 0;j<LateFeestagNC.size();j++) {
				LinkedHashMap<String, String> nc = new LinkedHashMap<>();
				nc = IVTMultiTagCommonFunctionalities.convertStr2MapWithDelim(LateFeestagNC.get(j));
				

				if(i==j) {
					for(String tag : ibm.keySet()) {
							String ibmVal = ibm.get(tag);
							String ncVal = nc.get(tag);

						if(!ibmVal.equals(ncVal)) {
							System.out.println("Account Number " + Files.ACCOUNTNUMBER + "::Tag Mapping for Late Payment Fees:" + tag + " IBM Value:: " + ibmVal
									+ " NC Value:: " + ncVal);
							IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
							IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, tag);
							IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, ibmVal);
							IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tag);
							IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, ncVal);
							IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "NO");
						}
						else
						{
							IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
							IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, tag);
							IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, ibmVal);
							IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tag);
							IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, ncVal);
							IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "YES");
						}
					}
				}				
				nc.clear();
			}
			ibm.clear();
		}
	}
	
	



}
