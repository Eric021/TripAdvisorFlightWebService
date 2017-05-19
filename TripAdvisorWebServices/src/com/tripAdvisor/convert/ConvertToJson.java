package com.tripAdvisor.convert;

import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tripAdvisor.websites.Hamgardi;
import com.tripAdvisorIR.places.HorseRiding;
import com.tripAdvisorIR.places.IntTagInfo;
import com.tripAdvisorIR.places.Place;
import com.tripAdvisorIR.search.Search;


public class ConvertToJson {
	private Set<Place> places = new HashSet<>();
	private Set<String> states = new HashSet<>();
	StringBuilder logBuilder = new StringBuilder("");

	public ConvertToJson() {
		getAllPlacesSets();
		getAllStatesSet();
	}

	public void getAllPlacesSets() {

		Set<Place> placesSet = Search.getHorseRidings();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getMountainClimbings();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getCinemas();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getTheaters();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getBallSports();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getWaterSports();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getGames();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getOtherGames();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getRacketSports();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getHandyCrafts();
		for (Place place : placesSet) {
			places.add(place);
		}
		
		
		// this is not usable , dont activate it
//		placesSet = Search.getHobbies();
//		for (Place place : placesSet) {
//			places.add(place);
//		}

		placesSet = Search.getIslands();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getNatures();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getReligouses();
		for (Place place : placesSet) {
			places.add(place);
		}

		placesSet = Search.getShoppingCenters();
		for (Place place : placesSet) {
			places.add(place);
		}

		// this is not usable, dont activate it
//		placesSet = Search.getStates();
//		for (Place place : placesSet) {
//			places.add(place);
//		}

		placesSet = Search.getMuseums();
		for (Place place : placesSet) {
			places.add(place);
		}
		
		// zoos has BS :) informations , dont activate it
//		placesSet = Search.getZoos();
//		for (Place place : placesSet) {
//			places.add(place);
//		}
		
		placesSet = Search.getAquariums();
		for (Place place : placesSet) {
			places.add(place);
		}

		System.out.println("places size:====" + places.size());

	}

	public void getAllStatesSet() {
		Hamgardi hamgardi = new Hamgardi(new HorseRiding(), IntTagInfo.horseRiding);
		states = hamgardi.getStateNames();
	}

	public void toJson() {
		
		// key: city, value:a list of Places of a city
		Map<String, Set<Place>> citiesPlaces = new HashMap<>();
		// key: state, value: set of cities of a state
		Map<String, Set<String>> statesCities = new HashMap<>();
		for (String state : states) {
			Set<String> cities = new HashSet<>();
			for (Place place : places) {
				if (place.getLocation().getState().equals(state)) {
					cities.add(place.getLocation().getCity());
				} else {
					continue;
				}
			}
			statesCities.put(state, cities);
			logBuilder.append("========== 1- toJson method: statesCities: ==========" + "\n");
			logBuilder.append("-----" + state + ":" + "-----" + "\n");
			logBuilder.append(statesCities.get(state) + "\n");
			for (String city : cities) {
				Set<Place> placesList = new HashSet<>();
				for (Place place : places) {
					if (place.getLocation().getCity().equals(city) && place.getLocation().getState().equals(state)) {
						placesList.add(place);
					}
				}
				citiesPlaces.put(city, placesList);
				logBuilder.append("========== 2- toJson method: citiesPlaces: ==========" + "\n");
				logBuilder.append("-----" + city + ":" + "-----" + "\n");
				logBuilder.append(citiesPlaces.get(city) + "\n");
			}
		}

		jsonMaker(states, statesCities, citiesPlaces);
	}

	private void jsonMaker(Set<String> states, Map<String, Set<String>> statesCities,
			Map<String, Set<Place>> citiesPlaces) {
		StringBuilder builder = new StringBuilder();
		int allPlacesAddedCounter = 0;
		builder.append("[");

		int stateCounter = 0;
		for (String state : states) {
			String persianStateName = "استان" + " " + state;
			logBuilder.append("========== 3- jsonMaker method: foreach-> state:states ==========" + "\n");
			logBuilder.append("-----" + persianStateName + ":" + "-----" + "\n");
			builder.append("\n" + "{\n" + "\"name\" :\"" + persianStateName + "\"," + "\n");
			builder.append("\t\t\"type\" : \"parent\"" + "," + "\n");
			builder.append("\t\t\"subLayers\" :" + "\n");
			builder.append("\t\t[" + "\n");

			Set<String> cities = new HashSet<>();
			cities = statesCities.get(state);
			logBuilder.append("========== 4- jsonMaker method: statesCities: ==========" + "\n");
			logBuilder.append("-----" + cities + ":" + "-----");
			int cityCounter = 0;
			for (String city : cities) {
				String cityPersian = "شهر" + " " + city;
				logBuilder.append("========== 5- jsonMaker method: foreach-> city:cities ==========" + "\n");
				logBuilder.append("-----" + cityPersian + ":" + "-----" + "\n");
				builder.append("{" + "\n" + "\"name\" : \"" + cityPersian + "\"" + "," + "\n");
				builder.append("\"type\" : \"parent\"" + "," + "\n");
				builder.append("\"subLayers\" :" + "\n");
				builder.append("[" + "\n");
				// hotelJson
				builder.append("{" + "\n");
				builder.append("\"type\" : \"vector\"" + "," + "\n");
				builder.append("\"name\" : \"هتل ها\"" + "," + "\n");
				builder.append("\"vectorObjectsType\" : \"Point\"" + "," + "\n");
				builder.append("\"formSchemaKey\" : \"Hotel\"" + "," + "\n");
				builder.append("\"vectorObjects\" :" + "\n");
				builder.append("[]" + "\n");
				builder.append("}" + "," + "\n");
				// resturant json
				builder.append("{" + "\n" + "\"type\" : \"vector\"" + "," + "\n");
				builder.append("\"name\" : \"رستوران ها\"" + "," + "\n");
				builder.append("\"vectorObjectsType\" : \"Point\"" + "," + "\n");
				builder.append("\"formSchemaKey\" : \"Restaurant\"" + "," + "\n");
				builder.append("\"vectorObjects\" :" + "\n");
				builder.append("[]" + "\n");
				builder.append("}" + "," + "\n");
				// ThingsToDo json
				builder.append("{" + "\n" + "\"type\" : \"vector\"" + "," + "\n");
				builder.append("\"name\" : \"جاذبه های گردشگری\"" + "," + "\n");
				builder.append("\"vectorObjectsType\" : \"Point\"" + "," + "\n");
				builder.append("\"formSchemaKey\" : \"Things_To_Do\"" + "," + "\n");
				builder.append("\"vectorObjects\" :" + "\n");
				builder.append("[" + "\n");

				Set<Place> placeList = new HashSet<>();
				placeList = citiesPlaces.get(city);
				logBuilder.append("========== 6- jsonMaker method: citiesPlaces: ==========" + "\n");
				logBuilder.append("-----" + "places.size = "  + places.size() + ":" + "-----");
				int placesCounter = 0;
				for (Place place : placeList) {
					logBuilder.append("========== 7- jsonMaker method: foreach-> place:places ==========" + "\n");
					logBuilder.append("-----" + place.getName() + ":" + "-----" + "\n");
					builder.append(place.toJson());
					placesCounter++;
					if (placesCounter < placeList.size()) {
						builder.append(",");
					}
					builder.append("\n");
					allPlacesAddedCounter++;
				}
				builder.append("]" + "\n");
				builder.append("}");
				builder.append("\n");
				builder.append("]");
				builder.append("\n");
				builder.append("}");
				cityCounter++;

				if (cityCounter < cities.size()) {
					builder.append(",");
				}

			}

			builder.append("]" + "\n");
			builder.append("}");
			// builder.append("}");
			stateCounter++;
			if (stateCounter < states.size()) {
				builder.append(",");
			}
			builder.append("\n");
		}
		builder.append("\n" + "]");

		File file = new File("/home/amir/json.json");
		File logFile = new File("/home/amir/log.log");
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(file);
			fileWriter.append(builder.toString());
			fileWriter.close();
			fileWriter = new FileWriter(logFile);
			fileWriter.append(logBuilder);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("all places size: " + places.size());
		System.out.println("writing to file finished");
		System.out.println(allPlacesAddedCounter + " places added to json.json file...!");

	}
}
