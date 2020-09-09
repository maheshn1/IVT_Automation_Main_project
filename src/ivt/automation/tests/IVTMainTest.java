package ivt.automation.tests;

import ivt.automation.businessrules.IVTSingleTagCompareFiles;
import ivt.automation.businessrules.TukLateFee;
import ivt.automation.businessrules.TukPaperFee;
import ivt.automation.utils.Files;

public class IVTMainTest {
	public static String IBM;
	public static String NC;

	public static void main(String[] args) throws Exception {
		TukLateFee tt = new TukLateFee();
		IVTSingleTagCompareFiles.fetchIBMAndNCFiles();

		for (String str : IVTSingleTagCompareFiles.ibmAndNCFiles) {
			String a[] = str.split("\\|");
			IBM = a[0];
			NC = a[1];
			//System.out.println(IBM+"\n"+NC);
			//System.out.println(Files.ACCOUNTNUMBER);
			//IVTSingleTagCompareFiles.compareIBMAndNCTags(IBM, NC);
			//TukPaperFee.compareTukPaperFee(IBM, NC);
			tt.compareTukLateFee(IBM, NC);
		}
	}
}


