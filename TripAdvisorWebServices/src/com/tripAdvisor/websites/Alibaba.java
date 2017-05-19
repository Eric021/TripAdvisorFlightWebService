package com.tripAdvisor.websites;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tripAdvisorIR.dbModel.Flight;
import com.tripAdvisorIR.info.Information;

public class Alibaba extends Website {
	private Map<String, String> cityCode = new HashMap<String, String>();
	private String srcCityCode;
	private String destCityCode;
	private Document document;

	public Alibaba() {

	}

	public Alibaba(Information information) {
		super(information);
		try {
			readCityCodesFromFile();
		} catch (FileNotFoundException | NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startScrape() {
		Response response;
		String referrer = makeReferrer();
		try {
			String searchFlightURL = makeSearchFlightURL(srcCityCode, destCityCode, getCheckInDate());
			response = Jsoup.connect(searchFlightURL).userAgent(userAgent).header("Host", "http://www.alibaba.ir")
					.header("Accept", "application/json, text/javascript, */*; q=0.01")
					.header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "en-US,en;q=0.5")
					.header("X-Requested-With", "XMLHttpRequest").header("Connection", "Keep-Alive").referrer(referrer)
					.ignoreContentType(true).method(Method.GET).execute();
			String[] responseParts = response.body().split("RequestId\":");
			String[] idParts = responseParts[1].split(",");
			String id = idParts[0];
			System.out.println("id is : " + id);
			String getFlightURL = makeGetFlightURL(id, srcCityCode, destCityCode, false, getCheckInDate());
			Document doc = Jsoup.connect(getFlightURL).userAgent(userAgent).ignoreContentType(true).get();
			// System.out.println(doc.select("body").text());
			findFlights(doc, referrer);

			// find datas for roundTrip
			if (Information.getIsRoundTrip() == true) {
				System.out.println("round trip data:");
				searchFlightURL = makeSearchFlightURL(destCityCode, srcCityCode, getCheckOutDate());
				response = Jsoup.connect(searchFlightURL).userAgent(userAgent).header("Host", "http://www.alibaba.ir")
						.header("Accept", "application/json, text/javascript, */*; q=0.01")
						.header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "en-US,en;q=0.5")
						.header("X-Requested-With", "XMLHttpRequest").referrer(referrer)
						.header("Connection", "Keep-Alive").ignoreContentType(true).method(Method.GET).execute();
				responseParts = response.body().split("RequestId\":");
				idParts = responseParts[1].split(",");
				id = idParts[0];
				System.out.println("id is : " + id);
				getFlightURL = makeGetFlightURL(id, destCityCode, srcCityCode, true, getCheckOutDate());
				doc = Jsoup.connect(getFlightURL).userAgent(userAgent).ignoreContentType(true).get();
				findFlights(doc, referrer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void findFlights(Document doc, String link) {
		String[] flightsStringParts = doc.select("body").text().split("AvailableFlights\":");
		String[] flightsJsonParts = flightsStringParts[1].split(",\"FaranegarAvailableCount\"");
		String flightsJson = flightsJsonParts[0];
		Gson gson = new Gson();
		JsonObject[] flightsArray = gson.fromJson(flightsJson, JsonObject[].class);
		for (JsonObject flightJson : flightsArray) {
			String flightCompany = flightJson.get("AirLine").toString().replaceAll("\"", "");
			String airplane = flightJson.get("Aircraft").toString().replaceAll("\"", "");
			String time = flightJson.get("LeaveTime").toString().replaceAll("\"", "");
			String date = flightJson.get("LeaveDateFa").toString().replaceAll("\"", "");
			String flightNumber = flightJson.get("FlightNumber").toString().replaceAll("\"", "");
			String price = flightJson.get("price").toString().replaceAll("\"", "");

			System.out.println(price.length() + "+++++++++++++++++++++++++***************************");
			String capacity = flightJson.get("ClassDetails").toString().replaceAll("\"", "");
			String isCharter = flightJson.get("SystemKeyName").toString().replaceAll("\"", "");
			String flightClass = flightJson.get("kind").toString().replaceAll("\"", "");

			Flight flight = new Flight(flightCompany, capacity, price, flightNumber, date, "alibaba", link, time,
					airplane, isCharter, flightClass);
			System.out.println(flight.toJson());
			flights.add(flight);
		}
	}

	private String makeGetFlightURL(String id, String srcCode, String destCode, boolean isReturn, String dateFrom) {
		String baseURL = "https://www.alibaba.ir/api/GetFlight";
		StringBuilder getFlightURL = new StringBuilder(baseURL);
		getFlightURL.append("?id=" + id);
		getFlightURL.append("&last=0");
		getFlightURL.append("&ffrom=" + srcCode);
		getFlightURL.append("&fto=" + destCode);
		getFlightURL.append("&datefrom=" + dateFrom);
		getFlightURL.append("&count=" + (getAdults() + getChildren() + getNewBorns()));
		getFlightURL.append("&interval=1");
		getFlightURL.append("&isReturn=" + isReturn);
		getFlightURL.append("&isNew=true");
		return getFlightURL.toString();
	}

	private String makeSearchFlightURL(String srcCode, String destCode, String dateFrom) {
		String baseURL = "https://www.alibaba.ir/api/searchFlight";
		String from = "?ffrom=" + srcCode;
		String to = "&fto=" + destCode;
		String passengersCount = "&adult=" + getAdults() + "&child=" + getChildren() + "&infant=" + getNewBorns();
		String searchFlightURL = baseURL + from + to + "&datefrom=" + dateFrom + passengersCount;
		return searchFlightURL;
	}

	private String makeReferrer() {
		String baseUrl = "https://www.alibaba.ir/flights/";
		String checkInDate = getCheckInDate().replaceAll("/", "-");
		String checkOutDate = getCheckOutDate().replaceAll("/", "-");
		String passengersCount = getAdults() + "-" + getChildren() + "-" + getNewBorns();
		String referrer = baseUrl + srcCityCode + "-" + destCityCode + "/" + checkInDate + "/";
		if (Information.getIsRoundTrip() == true)
			referrer += checkOutDate + "/";
		referrer += passengersCount;
		return referrer;
	}

	private String makeURL() {
		String baseUrl = "https://www.alibaba.ir/";
		String planeFa = "هواپیما";
		String ticketFa = "بلیط";
		String toFa = "به";
		String dateIn = getCheckInDate().replaceAll("/", "");

		if (Information.getIsRoundTrip() == true) { // two-way ticket
			// String urlPart1 = baseUrl + srcCityCode + "-" + destCityCode +
			// "-" + dateIn + getAdults() + "-" + getChildren() + "-" +
			// getNewBorns() +
			return null;
		} else { // one-way ticket
			String urlPart1 = baseUrl + srcCityCode + "-" + destCityCode + "-" + dateIn + "-" + getAdults() + "-"
					+ getChildren() + "-" + getNewBorns() + "/";
			String urlPart2 = ticketFa + "-" + planeFa + "-" + getSrcPersianCityName() + "-" + toFa + "-"
					+ getPersianCityName();
			return urlPart1 + urlPart2;
		}
	}

	private void readCityCodesFromFile() throws FileNotFoundException, NoSuchElementException {
		File cityCodeFile = new File("/home/eric/Alibaba.txt");
		Scanner scanner = new Scanner(cityCodeFile);
		while (scanner.hasNextLine()) {
			if (scanner.next().equals(getPersianCityName())) {
				this.destCityCode = scanner.next();
				break;
			}
		}
		scanner.close();
		scanner = new Scanner(cityCodeFile);
		while (scanner.hasNextLine()) {
			if (scanner.next().equals(getSrcPersianCityName())) {
				this.srcCityCode = scanner.next();
				break;
			}
		}

		scanner.close();
		System.out.println("src code: " + srcCityCode);
		System.out.println("dest code: " + destCityCode);
		if (srcCityCode == null || destCityCode == null) {
			String err = "گزینه ای برای " + "'" + getSrcPersianCityName() + "'" + "یا" + "'" + getPersianCityName()
					+ "'" + " پیدا نشد";
			throw new NoSuchElementException(err);
		}

	}

}
