package com.tripAdvisor.websites;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Request;
import org.jsoup.nodes.Document;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tripAdvisorIR.calendar.CalendarTool;
import com.tripAdvisorIR.dbModel.Flight;
import com.tripAdvisorIR.info.Information;

public class AirplainTicket extends Website {

	public AirplainTicket(Information information) {
		super(information);
	}

	@Override
	public void startScrape() {
		Response response;
		String requsetBody = makeRequestBody();
		String url = makeURL();
		try {
			response = Jsoup.connect("https://airplaneticket.ir/flights/getFlights")
					.userAgent(userAgent)
					.header("Host", "airplaneticket.ir")
					.header("Accept", "application/json, text/javascript, */*; q=0.01")
					.referrer(url)
					.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
					.requestBody(requsetBody)
					.ignoreContentType(true)
					.method(Method.POST)
					.postDataCharset("UTF-8")
					.execute();
			Document document = response.parse();
			findFlights(document, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void findFlights(Document document,String link) {
		String flightsJson = document.body().text();
		System.out.println(flightsJson);
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		JsonElement jsonElement = jsonObject.get("FlightInfo");
		System.out.println(jsonElement);
		JsonObject[] flightsArray = gson.fromJson(jsonElement, JsonObject[].class);
		
		for (JsonObject flightJson : flightsArray) {
			String flightNumber = flightJson.get("FlightNumber").toString().replaceAll("\"", "");
			String airplane = flightJson.get("Airplane").toString().replaceAll("\"", "");
			String flightCompany = flightJson.get("Airline").toString().replaceAll("\"", "");
			String time =  flightJson.get("Departure").toString().replaceAll("\"", "");
			
			String date = flightJson.get("LeaveDateFa").toString().replaceAll("\"", "");
			String price = flightJson.get("price").toString().replaceAll("\"", "");
			String capacity = flightJson.get("ClassDetails").toString().replaceAll("\"", "");
			String isCharter = flightJson.get("SystemKeyName").toString().replaceAll("\"", "");
			String flightClass = flightJson.get("kind").toString().replaceAll("\"", "");
			Flight flight = new Flight(flightCompany, capacity, price, flightNumber, date, "airplainticket", link, time,
					airplane, isCharter, flightClass);
			System.out.println(flight.toJson());
			flights.add(flight);
		}
	}

	private String makeURL() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("https://airplaneticket.ir/flights/");
		stringBuilder.append(srcCityCode + "-" + destCityCode + "/");
		stringBuilder.append(getCheckInDate().replaceAll("/", "-"));
		if (Information.getIsRoundTrip()==true)
			stringBuilder.append( "/" + getCheckOutDate().replaceAll("/", "-"));
		return stringBuilder.toString();
	}

	private String makeRequestBody() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("from=[\"" + srcCityCode + "\"]");
		stringBuilder.append("&to=[\"" + destCityCode + "\"]");
		CalendarTool calendarTool = new CalendarTool();
		calendarTool.setIranianDate(getCheckInDate());
		int month = calendarTool.getIranianMonth();
		int day = calendarTool.getIranianDay();
		int year = calendarTool.getIranianYear();
		String date = "";

		if (day < 10)
			date += "0" + day + "/";
		else
			date += day + "/";

		if (month < 10)
			date += "0" + month + "/";
		else
			date += month + "/";

		date += year;
		stringBuilder.append("&depart=[\"" + date + "\"]");
		stringBuilder.append("&adl=" + getAdults());
		stringBuilder.append("&chd=" + getChildren());
		stringBuilder.append("&inf=" + getNewBorns());
		stringBuilder.append("&page=" + 1);
		stringBuilder.append("&filters={}");

		return stringBuilder.toString();
	}

}
