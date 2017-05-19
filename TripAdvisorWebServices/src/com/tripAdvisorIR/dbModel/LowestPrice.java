package com.tripAdvisorIR.dbModel;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class LowestPrice {
	private String price;
	private String host;
	private String link;
	private String flightCompany;
	private String capacity;
	private String flightNumber;
	private String flightClass;
	private String isCharter;
	private String date;
	private String lowestPrice;

	public LowestPrice(String price, String host, String link) {
		this.price = price;
		this.host = host;
		this.link = link;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		
		this.price = price;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@XmlTransient
	public String getFlightCompany() {
		return flightCompany;
	}

	public void setFlightCompany(String flightCompany) {
		this.flightCompany = flightCompany;
	}

	@XmlTransient
	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	@XmlTransient
	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	@XmlTransient
	public String getFlightClass() {
		return flightClass;
	}

	public void setFlightClass(String flightClass) {
		this.flightClass = flightClass;
	}

	@XmlTransient
	public String getIsCharter() {
		return isCharter;
	}

	public void setIsCharter(String isCharter) {
		this.isCharter = isCharter;
	}

	@XmlTransient
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@XmlTransient
	public String getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

}
