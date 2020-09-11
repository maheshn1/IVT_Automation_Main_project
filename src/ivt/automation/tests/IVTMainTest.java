package ivt.automation.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.businessrules.Adj;
import ivt.automation.businessrules.IVTSingleTagCompareFiles;
import ivt.automation.businessrules.TukAirTimePlanTotal;
import ivt.automation.businessrules.TukCCATotal;
import ivt.automation.businessrules.TukCurrentSpendCap;
import ivt.automation.businessrules.TukLateFee;
import ivt.automation.businessrules.TukPaperFee;
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
			System.out.println(IVTBase.ACCOUNTNUMBER);
			
			IVTSingleTagCompareFiles.compareIBMAndNCSingleTags(IBM, NC);
			Adj.CompareADJTags(IBM,NC);
			TukPaperFee.compareTukPaperFee(IBM, NC);
			TukLateFee.compareTukLateFee(IBM, NC);
			TukCurrentSpendCap.compareSpendCap(IBM, NC);
			TukAirTimePlanTotal.compareAirTimePlanTotal(IBM,NC);
		}
		
		for (String str : IVTBase.fetchCCAAndNCFiles()) {
			List<String> ccalist = new ArrayList<>(); 
			
			String a[] = str.split("\\|");
			NC = a[0];
			
			String b[] = NC.split("\\\\");
			int len = b.length;
			IVTBase.ACCOUNTNUMBER = StringUtils.substringAfter(b[len-1], "_");
			System.out.println(IVTBase.ACCOUNTNUMBER);
			
			if(a.length==2) {
				CCA = a[1];
				TukCCATotal.compareTukCCATotal(CCA, NC);	
			}
			else {
				for(int i=1;i<a.length;i++) {
					ccalist.add(a[i]);
				}
				TukCCATotal.compareTukCCAListTotal(ccalist, NC);
			}
		}
	}
}


