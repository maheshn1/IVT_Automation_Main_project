package ivt.automation.tests;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.businessrules.IVTSingleTagCompareFiles;
import ivt.automation.businessrules.TukAirTimePlanTotal;
import ivt.automation.businessrules.TukLateFee;
import ivt.automation.businessrules.TukPaperFeeNew;
import ivt.automation.core.IVTBase;

public class IVTMainTest {
	public static String IBM,NC,CCA;
	
	public static void main(String[] args) throws Exception {
		
		for (String str : IVTBase.fetchIBMAndNCFiles()) {
			String a[] = str.split("\\|");
			IBM = a[0];
			NC = a[1];
			String b[] = IBM.split("\\\\");
			int len = b.length;
			IVTBase.ACCOUNTNUMBER = StringUtils.substringAfter(b[len-1], "_");
			//System.out.println(IVTBase.ACCOUNTNUMBER);
			
			//IVTSingleTagCompareFiles.compareIBMAndNCSingleTags(IBM, NC);
			TukPaperFeeNew.compareTukPaperFee(IBM, NC);	
			//TukLateFee.compareTukLateFee(IBM, NC);
			//TukAirTimePlanTotal.compareAirTimePlanTotal(IBM,NC);
		}
	}
}


