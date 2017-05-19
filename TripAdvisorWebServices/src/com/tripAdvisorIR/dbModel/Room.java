package com.tripAdvisorIR.dbModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Room {
	private String id;
	private String type;
	private boolean isAvailable;
	private String price;
	private boolean lunchDinner;
	private boolean breakfast;
	private int peopleNumber;
	private int extraPeople;

	public Room(String id, String type, boolean isAvailable, String price, boolean lunchDinner, boolean breakfast,
			 int peopleNumber, int extraPeople) {
		this.id = id;
		this.type = type;
		this.isAvailable = isAvailable;
		this.price = price;
		this.lunchDinner = lunchDinner;
		this.breakfast = breakfast;
		this.peopleNumber = peopleNumber;
		this.extraPeople = extraPeople;
	}

	public Room() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getisAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return this.price;
	}

	public boolean isLunchDinner() {
		return lunchDinner;
	}

	public void setLunchDinner(boolean lunchDinner) {
		this.lunchDinner = lunchDinner;
	}

	public boolean isBreakfast() {
		return breakfast;
	}

	public void setBreakfast(boolean breakfast) {
		this.breakfast = breakfast;
	}

	public int getPeopleNumber() {
		return peopleNumber;
	}

	public void setPeopleNumber(int peopleNumber) {
		this.peopleNumber = peopleNumber;
	}

	public int getExtraPeople() {
		return extraPeople;
	}

	public void setExtraPeople(int extraPeople) {
		this.extraPeople = extraPeople;
	}
}
