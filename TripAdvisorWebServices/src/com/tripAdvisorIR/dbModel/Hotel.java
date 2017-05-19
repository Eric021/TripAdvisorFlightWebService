package com.tripAdvisorIR.dbModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.gson.Gson;
import com.tripAdvisor.location.Location;
import com.tripAdvisorIR.dbModel.Hotel;
import com.tripAdvisorIR.dbModel.Room;

@XmlRootElement
public class Hotel implements Comparable<Hotel> {

	private String id;
	private String name;
	private List<LowestPrice> lowestPrices;
	@XmlTransient
	private String webUrl;
	private String lowestPrice;
	@XmlTransient
	private transient String description;
	@XmlTransient
	private transient String location;
	@XmlTransient
	private transient String email;
	@XmlTransient
	private transient double rating;
	@XmlTransient
	private transient List<Room> rooms = new ArrayList<Room>();
	// key: Website, value: link
	private Map<String, String> urls = new HashMap<>();
	private String link;
	
	// default constructor
	public Hotel() {

	}

	public Hotel(String id, String name, List<LowestPrice> lowestPrices) {
		this.id = id;
		this.name = name;
		this.lowestPrices = lowestPrices;
	}
	
	public Hotel(String id, String name, String webUrl, String link, String lowestPrice) {
		this.name = name;
		this.webUrl = webUrl;
		this.link = link;
		this.lowestPrice = lowestPrice;
		this.id = id;
	}

	// setter getter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Transient
	@XmlTransient
	public String getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	@XmlTransient
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<LowestPrice> getLowestPrices() {
		return lowestPrices;
	}

	public void setLowestPrices(List<LowestPrice> lowestPrices) {
		this.lowestPrices = lowestPrices;
	}

	// toString
	@Override
	public String toString() {
		return "نام= " + name + ", id= " + id + "لینک=" + link;
	}

	// toJson
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((webUrl == null) ? 0 : webUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Hotel other = (Hotel) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (webUrl == null) {
			if (other.webUrl != null) {
				return false;
			}
		} else if (!webUrl.equals(other.webUrl)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Hotel h) {

		int priceCompare = Integer.parseInt(h.lowestPrice);
		return Integer.parseInt(this.getLowestPrice()) - priceCompare;
	}

}
