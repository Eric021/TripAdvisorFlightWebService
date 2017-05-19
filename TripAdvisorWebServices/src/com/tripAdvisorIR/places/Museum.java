package com.tripAdvisorIR.places;

import java.util.ArrayList;
import java.util.List;

public class Museum extends ArtCulture {
	// returns subcategoryURL for sub sub categories
	public String getCategoryURL() {
		return "http://hamgardi.com/list/KeyWord-" + "موزه";
	}
	
	private final String tag = "موزه";

	public List<String> getTags() {
		List<String> tagSet = new ArrayList<>();
		tagSet.add(tag);
		tagSet.add(super.getTag());
		return tagSet;
	}
}
