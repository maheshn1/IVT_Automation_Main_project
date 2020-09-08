package ivt.automation.tests;

import ivt.automation.businessrules.IVTSingleTagCompareFiles;
import ivt.automation.businessrules.TukPaperFee;

public class IVTMainTest {
	public static String IBM;
	public static String NC;

	public static void main(String[] args) throws Exception {
		IVTSingleTagCompareFiles.fetchIBMAndNCFiles();

		for (String str : IVTSingleTagCompareFiles.ibmAndNCFiles) {
			String a[] = str.split("\\|");
			IBM = a[0];
			NC = a[1];
			System.out.println(IBM+"\n"+NC);

			//IVTSingleTagCompareFiles.compareIBMAndNCTags(IBM, NC);
			TukPaperFee.tukPaperFeeCompare(IBM, NC);
		}
	}
}


