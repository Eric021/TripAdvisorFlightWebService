package com.tripAdvisorIR.places;

import java.util.ArrayList;
import java.util.List;

public class Island extends IranTouring {

	// returns subcategoryURL for sub sub categories
	public String getCategoryURL() {
		String island = "جزیره";
		return "http://hamgardi.com/list/show-places/Category-Tourism/"
				+ "CountryFilter-Iran/KeyWord-" + island + "/sort-review";
	}

	private final String tag = "جزایر ایران";

	public List<String> getTags() {
		List<String> tagSet = new ArrayList<>();
		tagSet.add(tag);
		tagSet.add(super.getTag());
		return tagSet;
	}
}
