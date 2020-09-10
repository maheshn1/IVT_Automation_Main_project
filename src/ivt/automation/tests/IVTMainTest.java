package ivt.automation.tests;

import ivt.automation.businessrules.Adj;
import ivt.automation.businessrules.IVTSingleTagCompareFiles;
import ivt.automation.businessrules.TukAirTimePlanTotal;
import ivt.automation.businessrules.TukCCATotal;
import ivt.automation.businessrules.TukCurrentSpendCap;
import ivt.automation.businessrules.TukLateFee;
import ivt.automation.businessrules.TukPaperFee;
import ivt.automation.utils.Files;

public class IVTMainTest {
	public static String IBM,NC,CCA;

	public static void main(String[] args) throws Exception {
		
		IVTSingleTagCompareFiles.fetchIBMAndNCFiles();

		for (String str : IVTSingleTagCompareFiles.ibmAndNCFiles) {
			String a[] = str.split("\\|");
			IBM = a[0];
			NC = a[1];
			CCA = a[2];
			//System.out.println(IBM+"\n"+NC+"\n"+CCA);
			//System.out.println(Files.ACCOUNTNUMBER);
			IVTSingleTagCompareFiles.compareIBMAndNCSingleTags(IBM, NC);
			Adj.CompareADJTags(IBM,NC);
			TukPaperFee.compareTukPaperFee(IBM, NC);
			TukLateFee.compareTukLateFee(IBM, NC);
			TukCurrentSpendCap.compareSpendCap(IBM, NC);
			TukAirTimePlanTotal.compareAirTimePlanTotal(IBM,NC);
			TukCCATotal.compareTukCCATotal(CCA, NC);
		}
	}
}


