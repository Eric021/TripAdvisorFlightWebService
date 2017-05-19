package com.tripAdvisorIR.places;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Other extends SportGame {
	// returns subcategoryURL for sub sub categories
	public String getCategoryURL() {
		return "http://hamgardi.com/list/show-places/SubCategory-56";
	}

	private final String tag = "ورزش ها و بازی های دیگر";

	public List<String> getTags() {
		List<String> tagSet = new ArrayList<>();
		tagSet.add(tag);
		tagSet.add(super.getTag());
		return tagSet;
	}
}
