package ivt.automation.businessrules;

import java.util.ArrayList;

import ivt.automation.core.IVTBase;

public class Adj {

	public static ArrayList<String> ADJBusinessRule(String IBMValue, String NCValue) {
		String newIBMValues[] = IVTBase.splitStringValue(IBMValue,",");
		String newNCValues[] =IVTBase.splitStringValue(NCValue,"\\|");
		ArrayList<String> d = new ArrayList<String>();
		for(int i =0;i<newIBMValues.length;i++) {
			if(i==3) {
				d.add(newIBMValues[i]);
			}
		}
		for(int i =0;i<newNCValues.length;i++) {
			if(i==3) {
				d.add(newNCValues[i]);				
			}
		}
	return d;
	}
	
	
}
