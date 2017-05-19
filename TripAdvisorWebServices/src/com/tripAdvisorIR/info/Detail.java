package com.tripAdvisorIR.info;

import com.tripAdvisor.location.Location;

public class Detail {

	private Location location;
	private String discription;
	private int vote;
	
	public Detail(Location location, String discription) {
		this.location = location;
		this.discription = discription;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String description) {
		this.discription = description;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	
	
	
}
