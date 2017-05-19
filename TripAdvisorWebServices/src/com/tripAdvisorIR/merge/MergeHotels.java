package com.tripAdvisorIR.merge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tripAdvisorIR.dbModel.Hotel;
import com.tripAdvisorIR.dbModel.LowestPrice;

public class MergeHotels {
	private Set<Hotel> hotels = new HashSet<>();
	private Set<Hotel> mergedHotels = new HashSet<>();

	public MergeHotels(Set<Hotel> hotels) {
		this.hotels = hotels;
	}

	public void startMerge() {
		mergePrices();
	}

	private void merge(Hotel hotel1, Hotel hotel2) {
		int price1 = new Integer(hotel1.getLowestPrice()).intValue();
		int price2 = new Integer(hotel2.getLowestPrice()).intValue();
		if (price1 < price2) {
			mergedHotels.add(hotel1);
		} else if (price2 < price1) {
			mergedHotels.add(hotel2);
		} else {
			mergedHotels.add(hotel1);
		}

	}

	private void mergePrices() {
		Set<String> hotelNames = new HashSet<>();
		for (Hotel hotel : hotels) {
			hotelNames.add(hotel.getName());
		}

		for (String hotelName : hotelNames) {
			Hotel mergedHotel = new Hotel();
			List<LowestPrice> lowestPrices = new ArrayList<>();
			String id = null;
			for (Hotel hotel : hotels) {
				if (hotelName.equals(hotel.getName())) {
					if (id == null) {
						id = hotel.getId();
					}
					mergedHotel.setId(id);
					mergedHotel.setName(hotelName);
					String price;
					try {
						price = hotel.getLowestPrice();
						if (price.equals("0") | price == null) 
							continue;
					} catch (Exception e) {
						continue;
					}
					String link = hotel.getLink();
					String host = hotel.getWebUrl();
					lowestPrices.add(new LowestPrice(price, host, link));
				}
				mergedHotel.setLowestPrices(lowestPrices);
				
			}
			mergedHotels.add(mergedHotel);
		}
	}

	public Set<Hotel> getMergedHotels() {
		return mergedHotels;
	}

}
