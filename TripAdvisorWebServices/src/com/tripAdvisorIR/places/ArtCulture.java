package com.tripAdvisorIR.places;

import java.util.Set;

public class ArtCulture extends Place {

	private final String tag = "فرهنگ و هنر";

	public ArtCulture() {
	}

	protected String getTag() {
		return tag;
	}

	// returns subcategoryURL for sub sub categories
	public String getCategoryURL() {
		return null;
	}

}
