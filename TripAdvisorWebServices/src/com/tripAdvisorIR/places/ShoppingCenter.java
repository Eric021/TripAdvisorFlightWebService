package com.tripAdvisorIR.places;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingCenter extends ShoppingFashion {
	
	// returns subcategoryURL for sub sub categories
	public String getCategoryURL() {
		return "http://hamgardi.com/list/show-places/SubCategory-62";
	}
	
	private final String tag = "مراکز خرید";

	public List<String> getTags() {
		List<String> tagSet = new ArrayList<>();
		tagSet.add(tag);
		tagSet.add(super.getTag());
		return tagSet;
	}
}
