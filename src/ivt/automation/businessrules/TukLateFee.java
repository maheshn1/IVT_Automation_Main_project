package ivt.automation.businessrules;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ivt.automation.core.IVTBase;
import ivt.automation.report.IVTExcelReport;
import ivt.automation.utils.Files;

public class TukLateFee extends IVTBase {

	public static Double otcPrice = 0.0;
	public static String otcDate = "OTCDATE";
	public static String otcTypeName = "OTCTYPENAME Late Payment Fees";
	public static List<String> lateFeestagIBM = new ArrayList<>();
	public static List<String> LateFeestagNC = new ArrayList<>();


	public static void compareTukLateFee(String fileIBM, String fileNC) throws Exception {
		LinkedHashMap<String,String> lateFeestagAndValueIBM = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> lateFeestagAndValueNC = new LinkedHashMap<String,String>();
		lateFeestagIBM = OTCCommonTagsFunctionality.fetchOTCTypeNameDateAndPriceTag(fileIBM);


		for (String s1 : lateFeestagIBM) {
			lateFeestagAndValueIBM = IVTMultiTagCommonFunctionalities.extractTagNameAndValues(s1,",");
		}
		//System.out.println(lateFeestagAndValueIBM);

		LateFeestagNC =  OTCCommonTagsFunctionality.fetchOTCTypeNameDateAndPriceTag(fileNC);
		for (String s2 : LateFeestagNC) {
			lateFeestagAndValueNC = IVTMultiTagCommonFunctionalities.extractTagNameAndValues(s2,"\\|");
		}

		//System.out.println(lateFeestagAndValueNC);

		for(String tag : lateFeestagAndValueIBM.keySet()) {
			String ibmVal = lateFeestagAndValueIBM.get(tag);
			String ncval = lateFeestagAndValueNC.get(tag);
			if(!ibmVal.equals(ncval)) {
				System.out.println("Account Number " + Files.ACCOUNTNUMBER + "::Tag Mapping:" + tag + " IBM Value:: " + ibmVal
						+ " NC Value:: " + ncval);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, tag);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, ibmVal);
				IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tag);
				IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, ncval);
				IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "NO");
			}
			else
			{
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, ACCOUNT_NUMBER, Files.ACCOUNTNUMBER);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row, IBMTAG_NUMBER, tag);
				IVTExcelReport.setCellValues("IBMNCDiffReport", IBMValue_row++, IBMVALUE_NUMBER, ibmVal);
				IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row, NCTAG_NUMBER, tag);
				IVTExcelReport.setCellValues("IBMNCDiffReport", NCValue_row++, NCVALUE_NUMBER, ncval);
				IVTExcelReport.setCellValues("IBMNCDiffReport", flag_row++, FLAG_NUMBER, "YES");
			}
		}

	}



}
