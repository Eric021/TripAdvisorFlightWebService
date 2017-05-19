package com.tripAdvisorIR.merge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tripAdvisorIR.dbModel.Flight;
import com.tripAdvisorIR.dbModel.LowestPrice;

public class MergeFlights {

	private Set<Flight> flights = new HashSet<>();
	private Set<Flight> mergedFlights = new HashSet<>();
	LowestPrice lowestPrice;

	public MergeFlights(Set<Flight> flights) {
		this.flights = flights;
	}

	public void startMerge() {
		mergePrices();
	}

	private void mergePrices() {
		Set<String> flightsID = new HashSet<>();
		for (Flight flight : flights) {
			flightsID.add(flight.getId());
		}

		for (String flightId : flightsID) {
			Flight mergedFlight = new Flight();
			List<LowestPrice> lowestPrices = new ArrayList<>();
			lowestPrices.clear();
			String flightCompany;
			String flightNumber;
			String flightClass;
			String isCharter;
			String date;
			String capacity;
			String time;
			String price;

			for (Flight flight : flights) {

				if (flightId.equals(flight.getId())) {
					flightNumber = flight.getFlightNumber();
					flightCompany = flight.getFlightCompany();
					flightClass = flight.getFlightClass();
					isCharter = flight.getIsCharter();
					date = flight.getDate();
					capacity = flight.getCapacity();
					time = flight.getTime();

					// price = flight.getPrice();

					mergedFlight.setFlightCompany(flightCompany);
					mergedFlight.setFlightNumber(flightNumber);
					mergedFlight.setFlightClass(flightClass);
					mergedFlight.setDate(date);
					mergedFlight.setCapacity(capacity);
					mergedFlight.setIsCharter(isCharter);
					mergedFlight.setDate(date);
					mergedFlight.setTime(time);

					// mergedFlight.setPrice(price);

					price = flight.getPrice();
					if (price.equals(0)) {
						continue;
					}

					String link = flight.getLink();
					String host = flight.getHost();
					this.lowestPrice = new LowestPrice(price, host, link);
					lowestPrices.add(lowestPrice);
				}

				
//				for (int i = 0; i < lowestPrices.size(); i++) {
//					CompareFlights Compare = new CompareFlights();
//					Compare.compare(this.lowestPrice, lowestPrices.get(i));
//				}

				mergedFlight.setLowestPrices(lowestPrices);

			}

			mergedFlights.add(mergedFlight);
		}
	}

	public Set<Flight> getMergedFlights() {
		return mergedFlights;
	}
}
