package com.tripAdvisor.location;

import javax.xml.bind.annotation.XmlRootElement;

public class Coordinate {
	private double lat;
	private double lng;

	// constructor
	public Coordinate() {

	}
	
	public Coordinate(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	//setter getter
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	// toString
	@Override
	public String toString() {
		return "[" + lat + " , " + lng + "]";
	}

	// hashcode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	// equals
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
			return false;
		return true;
	}
	
	
	// returns final part of Id of a place
	public String placeIdMaker(){
		String lat = (new Double(this.lat)).toString();
		String lng = (new Double (this.lng)).toString();
		lat = lat.substring(0,5);
		lng = lng.substring(0,5);
		return lat + "," + lng;
	}
	
}
