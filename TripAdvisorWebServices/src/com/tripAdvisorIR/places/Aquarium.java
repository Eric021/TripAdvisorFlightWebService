package com.tripAdvisorIR.places;

import java.util.ArrayList;
import java.util.List;

public class Aquarium extends ZooAquarium {
	
	public Aquarium() {

	}
	
	public String getCategoryURL() {
		return "http://hamgardi.com/list/KeyWord-" + "آکواریوم";
	}
	
	private final String tag = "آکواریوم های ایران";

	public List<String> getTags() {
		List<String> tagSet = new ArrayList<>();
		tagSet.add(tag);
		tagSet.add(super.getTag());
		return tagSet;
	}
}
