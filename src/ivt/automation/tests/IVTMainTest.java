package ivt.automation.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ivt.automation.businessrules.Adj;
import ivt.automation.businessrules.IVTSingleTagCompareFiles;
import ivt.automation.businessrules.TukAirTimePlanTotal;
import ivt.automation.businessrules.TukCCATotal;
import ivt.automation.businessrules.TukCurrentSpendCap;
import ivt.automation.businessrules.TukDiscountLineTotal;
import ivt.automation.businessrules.TukPaperFeeNew;
import ivt.automation.core.IVTBase;

public class IVTMainTest extends IVTBase{
	public static  String IBM,NC,CCA;
	
	public static void main(String[] args) throws Exception {
		
		IVTSingleTagCompareFiles ivtSingleFile = new IVTSingleTagCompareFiles();
		Adj adj = new Adj();
		TukPaperFeeNew tukPaperFee = new TukPaperFeeNew();
		TukCurrentSpendCap tukCurrentSpendCap = new TukCurrentSpendCap();
		TukAirTimePlanTotal airTime = new TukAirTimePlanTotal();
		TukCCATotal tukCCA = new TukCCATotal();
		TukDiscountLineTotal tukDiscountLine = new TukDiscountLineTotal();
		
		for (String str : fetchIBMAndNCFiles()) {
			String a[] = str.split("\\|");
			IBM = a[0];
			NC = a[1];
			String b[] = IBM.split("\\\\");
			int len = b.length;
			ACCOUNTNUMBER = StringUtils.substringAfter(b[len-1], "_");
			System.out.println(ACCOUNTNUMBER);
			
			ivtSingleFile.compareIBMAndNCSingleTags(IBM,NC);
			adj.CompareADJTags(IBM,NC);
			tukPaperFee.compareTukPaperFee(IBM, NC);
			tukCurrentSpendCap.compareSpendCap(IBM, NC);
			airTime.compareAirTimePlanTotal(IBM,NC);
			tukDiscountLine.compareDiscountLineTotal(IBM,NC);
		}
		
		for (String str : fetchCCAAndNCFiles()) {
			List<String> ccalist = new ArrayList<>(); 
			
			String a[] = str.split("\\|");
			NC = a[0];
			
			String b[] = NC.split("\\\\");
			int len = b.length;
			ACCOUNTNUMBER = StringUtils.substringAfter(b[len-1], "_");
			System.out.println(ACCOUNTNUMBER);
			
			if(a.length==2) {
				CCA = a[1];
				tukCCA.compareTukCCATotalSingleFile(CCA,NC);	
			}
			else {
				for(int i=1;i<a.length;i++) {
					ccalist.add(a[i]);
				}
				tukCCA.compareTukCCATotalMultiFile(ccalist, NC);
			}
		}
	}
}
