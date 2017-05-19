package com.tripAdvisor.location;

import javax.xml.bind.annotation.XmlRootElement;

public class Location {
	private Coordinate coordinate;
	private String state;
	private String city;
	private String address;

	// constructors
	public Location(Coordinate coordinate, String state, String city, String address) {
		this.coordinate = coordinate;
		this.state = state;
		this.city = city;
		this.address = address;
	}

	public Location() {

	}

	// setter getter

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// toString
	@Override
	public String toString() {
		return "آدرس  مختصات=" + coordinate + ", استان=" + state + ", شهر=" + city + ", آدرس=" + address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
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
		Location other = (Location) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		return true;
	}

	

}
