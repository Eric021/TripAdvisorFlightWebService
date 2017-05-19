package com.tripAdvisorIR.places;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tripAdvisor.location.*;
import com.tripAdvisorIR.review.Review;

/**
 * @author amir
 *
 */
public class Place {

	private String id;
	private String name;
	private Location location;
	private Image image = null;
	private String description;
	private String report = null;
	private int vote;
	private List<Review> review = null;
	private List<String> typeOfAttraction;
	private int intTag;
	private String phoneNumber;
	private String generalTag;
	private String detailedTag;

	// constructors
	public Place() {

	}

	public Place(String name, Image image, Location location, String description, String report, int vote,
			List<Review> review, List<String> typeOfAttraction) {
		setId(name, location);
		this.name = name;
		this.image = image;
		this.location = location;
		this.description = description;
		this.report = report;
		this.vote = vote;
		this.review = review;
		this.typeOfAttraction = typeOfAttraction;
	}

	public Place(int intTag) {
		this.intTag = intTag;
	}

	// method for calling all needed items for scraping
	public void allSetters(String name, Location location, Set<String> typesOfAttraction) {
		setId(name, location);

	}

	// setter getters

	public int getIntTag() {
		return intTag;
	}

	public String getId() {
		return id;
	}

	public void setId(String name, Location location) {
		this.id = location.getCity() + "-" + name + "-";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public List<Review> getReview() {
		return review;
	}

	public void setReview(List<Review> review) {
		this.review = review;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	

	public String getGeneralTag() {
		return generalTag;
	}

	public void setGeneralTag(String generalTag) {
		this.generalTag = generalTag;
	}

	public String getDetailedTag() {
		return detailedTag;
	}

	public void setDetailedTag(String detailedTag) {
		this.detailedTag = detailedTag;
	}

	public String getNewTag(int intTag) {
		String[] tagsArray = new String[19];

		tagsArray[0] = "ورزش، سرگرمی و بازی , ورزش توپی";
		tagsArray[1] = "ورزش، سرگرمی و بازی , کوهنوردی";
		tagsArray[2] = "ورزش، سرگرمی و بازی , بازی";
		tagsArray[3] = "ورزش، سرگرمی و بازی , ورزش های آبی";
		tagsArray[4] = "ورزش، سرگرمی و بازی , ورزش های راکتی";
		tagsArray[5] = "ورزش، سرگرمی و بازی , سوارکاری";
		tagsArray[6] = "ورزش، سرگرمی و بازی , ورزش ها و بازی های دیگر";
		tagsArray[7] = "فرهنگ و هنر , فیلم و سینما";
		tagsArray[8] = "فرهنگ و هنر , تئاتر و نمایش";
		tagsArray[9] = "خرید و مد , صنایع دستی";
		tagsArray[10] = "خرید و مد , مراکز خرید";
		tagsArray[11] = "سفر , اماکن مذهبی";
		tagsArray[12] = "سفر , سرگرمی";
		tagsArray[13] = "سفر , جاذبه های طبیعت";
		tagsArray[14] = "ایران گردی , استان ها";
		tagsArray[15] = "ایران گردی , جزایر ایران";
		tagsArray[16] = "موزه , موزه های ایران";
		tagsArray[17] = "باغ وحش و آکواریوم , باغ وحش های ایران";
		tagsArray[18] = "باغ وحش و آکواریوم , آکواریوم های ایران";

		return tagsArray[intTag];
	}

	public void setTypeOfAttraction(List<String> typeOfAttraction) {
		System.out.println("setter typeOfAttraction:" + typeOfAttraction);
		this.typeOfAttraction = typeOfAttraction;
	}

	// toString
	@Override
	public String toString() {
		return toJson();
	}

	// returns subcategoryURL for sub sub categories
	public String getCategoryURL() {
		return null;
	}

	protected String getTag() {
		return null;
	}

	public List<String> getTags() {
		List<String> asghar = new ArrayList<>();
		return asghar;
	}

	public String toJson() {
		String name = getName();
		double x = getLocation().getCoordinate().getLat();
		double y = getLocation().getCoordinate().getLng();
		String tag = getNewTag(getIntTag());
		String phone = getPhoneNumber();
		
		StringBuilder jsonBuilder = new StringBuilder("");
		jsonBuilder.append("{" + "\n");
		jsonBuilder.append("\t" + "\"name\":\"" + name + "\",\n");
		jsonBuilder.append("\t" + "\"type\":\"Point\",\n");
		jsonBuilder.append("\t" + "\"formInstance\": \n");
		jsonBuilder.append("\t" + "{" + "\n");
		jsonBuilder.append("\t" + "\"score\": 0,\n");
		jsonBuilder.append("\t" + "\"tags\" : \"" + tag + "\",\n");
		jsonBuilder.append("\t" + "\"phoneNumber\" : \"" + phone + "\"\n");
		jsonBuilder.append("\t" + "}," + "\n");
		jsonBuilder.append("\t" + "\"point\" :\n" + "\t" + "{" + "\n");
		jsonBuilder.append("\t" + "\"type\" : \"point\"," + "\n");
		jsonBuilder.append("\t" + "\"coordinates\" : [ " + x + ", " + y + " ]" + "\n");
		
		jsonBuilder.append("\t" + "}" + "\n" + "}");
		
		return jsonBuilder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	
}
