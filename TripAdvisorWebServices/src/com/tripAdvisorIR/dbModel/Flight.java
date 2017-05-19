package com.tripAdvisorIR.dbModel;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.google.gson.Gson;
import com.tripAdvisorIR.merge.CompareFlights;

public class Flight implements Comparable<Flight> {

	// id + " " + flightCompany + " " + capacity + " " + price + " " +
	// flightNumber + " " + date + " " + link + " " + time
	private String id;
	private String flightCompany;
	private String capacity;
	private String price;
	private String flightNumber;
	private String date;
	private String host;
	private String link;
	private String time;
	private String type; // airplane type
	private String isCharter;
	private String flightClass;

	private transient String classSaloon;
	private transient List<LowestPrice> lowestPrices;
	private transient String lowestPrice;
	private transient String name;

	public Flight() {

	}

	public Flight(String flightCompany, String capacity, String price, String flightNumber, String date, String host,
			String link, String time, String airplane, String isCharter, String flightClass) {
		this.flightCompany = flightCompany;
		this.capacity = capacity;
		this.price = price;
		this.flightNumber = flightNumber;
		this.date = date;
		this.host = host;
		this.link = link;
		this.time = time;
		this.type = airplane;
		this.isCharter = isCharter;
		this.flightClass = flightClass;
		this.id = flightCompany + " " + flightNumber + " " + flightClass;
		this.name = flightCompany + " " + flightNumber;
	}

	public Flight(String flightCompany, String capacity, String price, String flightNumber, String date, String host,
			String link, String time) {
		this.flightCompany = flightCompany;
		this.capacity = capacity;
		this.price = price;
		this.flightNumber = flightNumber;
		this.date = date;
		this.host = host;
		this.link = link;
		this.time = time;
		this.id = flightCompany + " " + flightNumber;
	}

	public Flight(String flightCompany, String capacity, String price, String flightNumber, String flightClass,
			String isCharter, String date, String lowestPrice, List<LowestPrice> lowestPrices, String host, String link,
			String time) {
		this.flightCompany = flightCompany;
		this.capacity = capacity;
		this.price = price;
		this.flightNumber = flightNumber;
		this.flightClass = flightClass;
		this.isCharter = isCharter;
		this.date = date;
		this.lowestPrice = lowestPrice;
		this.lowestPrices = lowestPrices;
		this.link = link;
		this.host = host;
		this.setTime(time);
	}

	// public Flight(String id, String name, String webUrl, String link, String
	// lowestPrice) {
	// this.setName(name);
	// this.webUrl = webUrl;
	// this.setLink(link);
	// this.lowestPrice = lowestPrice;
	// this.setId(id);
	// }

	// public Flight(String time, String capacity, String flightNumber, String
	// classSaloon, String price, String type,
	// String webUrl) {
	// super();
	// this.time = time;
	// this.capacity = capacity;
	// this.flightNumber = flightNumber;
	// this.classSaloon = classSaloon;
	// this.price = price;
	// this.type = type;
	// this.webUrl = webUrl;
	// }

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	@XmlTransient
	public String getClassSaloon() {
		return classSaloon;
	}

	public void setClassSaloon(String classSaloon) {
		this.classSaloon = classSaloon;
	}

	@XmlTransient
	public String getPrice() {
		CompareFlights cf = new CompareFlights();

		// cf.compare(flight1, flight2);

		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@XmlTransient
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlTransient
	public String getHost() {
		return host;
	}

	public void setHost(String webUrl) {
		this.host = webUrl;
	}

	public List<LowestPrice> getLowestPrices() {
		return lowestPrices;
	}

	public void setLowestPrices(List<LowestPrice> lowestPrices) {
		this.lowestPrices = lowestPrices;
	}

	@XmlTransient
	public String getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	@Override
	public int compareTo(Flight o) {
		int priceCompare = Integer.parseInt(o.lowestPrice);
		return Integer.parseInt(this.getLowestPrice()) - priceCompare;
	}

	@XmlTransient
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@XmlTransient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public String getId() {
		id = flightCompany + " " + capacity + " " + price + " " + flightNumber + " " + date + " " + link + " " + time;
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlightCompany() {
		return flightCompany;
	}

	public void setFlightCompany(String flightCompany) {
		this.flightCompany = flightCompany;
	}

	public String getFlightClass() {
		return flightClass;
	}

	public void setFlightClass(String flightClass) {
		this.flightClass = flightClass;
	}

	public String getIsCharter() {
		return isCharter;
	}

	public void setIsCharter(String isCharter) {
		this.isCharter = isCharter;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	@Override
	public String toString() {
		return id + " " + flightCompany + " " + capacity + " " + price + " " + flightNumber + " " + date + " " + link
				+ " " + time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capacity == null) ? 0 : capacity.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((flightClass == null) ? 0 : flightClass.hashCode());
		result = prime * result + ((flightCompany == null) ? 0 : flightCompany.hashCode());
		result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((isCharter == null) ? 0 : isCharter.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Flight other = (Flight) obj;
		if (capacity == null) {
			if (other.capacity != null)
				return false;
		} else if (!capacity.equals(other.capacity))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (flightClass == null) {
			if (other.flightClass != null)
				return false;
		} else if (!flightClass.equals(other.flightClass))
			return false;
		if (flightCompany == null) {
			if (other.flightCompany != null)
				return false;
		} else if (!flightCompany.equals(other.flightCompany))
			return false;
		if (flightNumber == null) {
			if (other.flightNumber != null)
				return false;
		} else if (!flightNumber.equals(other.flightNumber))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (isCharter == null) {
			if (other.isCharter != null)
				return false;
		} else if (!isCharter.equals(other.isCharter))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

}
