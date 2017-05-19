package com.tripAdvisorIR.places;

import java.util.ArrayList;
import java.util.List;

import com.tripAdvisorIR.places.ZooAquarium;

public class Zoo extends ZooAquarium {
	
	public Zoo() {
	}
	
	// returns subcategoryURL for sub sub categories
		public String getCategoryURL() {
			return "http://hamgardi.com/list/KeyWord-" + "باغ" + " " + "وحش";
		}
		
		private final String tag = "باغ وحش های ایران";

		public List<String> getTags() {
			List<String> tagSet = new ArrayList<>();
			tagSet.add(tag);
			tagSet.add(super.getTag());
			return tagSet;
		}
}
