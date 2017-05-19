package com.tripAdvisorIR.places;

import com.tripAdvisorIR.places.Place;

public class ZooAquarium extends Place {
	
	private final String tag = "باغ وحش و آکواریوم";

	public ZooAquarium() {
	
	}
	
	protected String getTag() {
		return tag;
	}

	// returns subcategoryURL for sub sub categories
	public String getCategoryURL() {
		return null;
	}
}
