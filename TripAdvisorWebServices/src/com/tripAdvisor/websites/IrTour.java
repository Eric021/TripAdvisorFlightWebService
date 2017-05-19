package com.tripAdvisor.websites;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tripAdvisorIR.dbModel.Flight;
import com.tripAdvisorIR.info.Information;

public class IrTour extends Website {
	
	private String srcCityCode;
	private String destCityCode;
	
	public IrTour(Information information) {
		super(information);
		try {
			readCityCodesFromFile();
		} catch (FileNotFoundException | NoSuchElementException e) {
			e.printStackTrace();
		}
	}
	
	public void startScrape() {
		String url = "http://irtour.ir/fa/Flight/SearchFlight";
		String url2 = "http://irtour.ir";
		String requestBody = makeRequestBody(url2);
		String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0";
//		String reqBody = "From=THR&To=KIH&StartDate=1396%2F02%2F25"
//				+ "&EndDate=&passenger%5B0%5D.Key=adt&passenger%5B0%5D.Value=1"
//				+ "&passenger%5B1%5D.Key=chd&passenger%5B1%5D.Value=0"
//				+ "&passenger%5B2%5D.Key=inf&passenger%5B2%5D.Value=0"
//				+ "&Fare=Business%2CEconomy";
		
		try {
			Response response = Jsoup.connect(url2)
					.userAgent(userAgent)
					.header("Host","irtour.ir")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Language", "en-US,en;q=0.5")
					.header("Accept-Encoding", "gzip, deflate")
					.referrer("http://irtour.ir/")
					.header("Connection", "keep-alive")
					.followRedirects(true)
					.requestBody(requestBody)
					.method(Method.POST)
					.execute();
			
			Document document = response.parse();
			
			File file = new File("/home/amir/Desktop/testIRTOUR.html");
			FileWriter writer = new FileWriter(file);
			writer.append(document.outerHtml());
			writer.close();
			findFlights(document, url + "?" + requestBody);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void findFlights(Document document, String url) {
		Element flightsBox = findFlightsBox(document);
		for (int i = 0; i< flightsBox.getElementsByAttributeValue("class", "flight-panel").size(); i++) {
			Element flightBox = flightsBox.getElementsByAttributeValue("class", "flight-panel").get(i);
			String flightNumber = findFlightNumber(flightBox);
			String[] flightCompanyParts = findCompany(flightBox).split("،");
			String flightCompany = flightCompanyParts[0].trim();
			String type = flightCompanyParts[1].trim();
			String time = findTime(flightBox);
			String price = findPrice(flightBox);
			String capacity = findCapacity(flightBox);
			
			Flight flight = new Flight(flightCompany, capacity, price, flightNumber, getCheckInDate(), "IrTour", url, time);
			flights.add(flight);
			System.out.println(flight.toString());
			
//			System.out.println("flight number: " + flightNumber);
//			System.out.println("flight company: " + flightCompany);
//			System.out.println("airplaneType: " + type);
//			System.out.println("flight time: " + time);
//			System.out.println("flight price: " + price);
//			System.out.println("flight capacity: " + capacity);
//			System.out.println("flights URL: " + url);
			System.out.println();
		}
	}
	
	private String makeRequestBody(String url) {
		StringBuilder reqBody = new StringBuilder();
		reqBody.append("?From=" + this.srcCityCode);
		reqBody.append("&To=" + this.destCityCode);
		reqBody.append("&StartDate=" + getCheckInDate());//.replaceAll("/", "%2F"));
		reqBody.append("&EndDate=");
		if (Information.getIsRoundTrip())
			reqBody.append(getCheckOutDate());//.replaceAll("/", "%2F"));
		reqBody.append("&passenger[0].Key=adt&passenger[0].Value=" + getAdults());
		reqBody.append("&passenger[1].Key=chd&passenger[1].Value=" + getChildren());
		reqBody.append("&passenger[2].Key=inf&passenger[2].Value=" + getNewBorns());
		reqBody.append("&Fare=Business,Economy");
		
		return reqBody.toString();
	}
	
	private String findCapacity(Element flightBox) {
		String capacity = flightBox.select("div.fp-body > div.text-center > div.row").get(0).child(1).child(1).child(1).child(1).text().trim();
		capacity = capacity.replace("صندلی موجود", "").trim();
		return capacity;
	}
	
	private String findPrice(Element flightBox) {
		String price = flightBox.select("div.fp-body > div.text-center > div.row").get(0).child(1).child(1).child(1).child(0).text().trim();
		price = price.replace("تومان", "");
		price = price.replaceAll(",", "").trim();
		return price;
	}
	
	private String findTime(Element flightBox) {
		return flightBox.select("div.fp-body > div.text-center > div.row").get(0).child(0).child(0).child(0).child(0).text().trim();
	}
	
	private String findCompany(Element flightBox) {
		return flightBox.select("div.fp-body > div.row > div.col-xs-9.col-sm-9.col-md-9.col-lg-9").get(0).child(0).text();
	}
	
	private String findFlightNumber(Element flightBox) {
		String flightNumber = flightBox.select("div.fp-body > div.row > div.col-xs-9.col-sm-9.col-md-9.col-lg-9").get(0).child(1).text();
		String[] flightNumParts = flightNumber.split(" ");
		flightNumber = flightNumParts[2];
		return flightNumber;
	}
	
	private Element findFlightsBox(Document document) {
		String cssSelector = "body > div#Airinegrid.container-fluid > div#pagebody.row > div.col-sm-10.col-md-10.col-lg-10 > div#grid"
				+ " > div#Departurelist > div#gridrslt > div.row";
		String cssSelector2 = "body";
		System.out.println("+=-+-+_=_=_=-+_+-+-+-+-+-=_=-+_+-+_");
		System.out.println(document.outerHtml());
		System.out.println("+=-+-+_=_=_=-+_+-+-+-+-+-=_=-+_+-+_");
		return document.select(cssSelector).get(0);
	}
	
	private void readCityCodesFromFile() throws FileNotFoundException, NoSuchElementException {
		File cityCodeFile = new File("/home/amir/TripAdviserJsoup/TripAdviserJsoup/txtFiles/SamkitCityCodes.txt");
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
