package com.tripAdvisorIR.places;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class State extends IranTouring {

	// returns subcategoryURL for sub sub categories
	public String getCategoryURL() {
		return "http://hamgardi.com/wiki/iran/list/show-province";
	}

	private final String tag = "استان ها";

	public List<String> getTags() {
		List<String> tagSet = new ArrayList<>();
		tagSet.add(tag);
		tagSet.add(super.getTag());
		return tagSet;
	}
}
