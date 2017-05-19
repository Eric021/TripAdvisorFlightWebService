package com.tripAdvisor.websites;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
//import org.elasticsearch.search.SearchHits;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.select.Elements;

import com.tripAdvisorIR.dbModel.Flight;
import com.tripAdvisorIR.info.Information;
import com.tripAdvisorIR.restAPI.Search;;

public class Ghasedak24 extends Website {

	private Document document;
	String url;

	public Ghasedak24(Information information) {
		super(information);
		// try {
		// readCityCodesFromFile();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		//
		// System.out.println("FileNotFoundException");
		// } catch (NoSuchElementException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// System.out.println("NoSuchElementException");
		// }
	}

	@Override
	public void startScrape() {
		System.out.println("entered start scrap");
		OutputSettings settings = new OutputSettings();
		settings.charset("UTF-8");
		String userAgent = "Mozilla/5.0(X11; Ubuntu; Linux x86_64;rv:53.0)Gecko/20100101 Firefox/53.0";
		url = makeURL();
		System.out.println(url);
		try {
			this.document = Jsoup.connect(url).header("host", "ghasedak24.com").userAgent(userAgent)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Language", "en-US,en;q=0.5").header("Accept-Encoding", "gzip,deflate,br")
					.referrer("https://ghasedak24.com/").header("Upgrade-Insecure-Requests", "1")
					.header("Connection", "keep-alive").followRedirects(true).method(Method.GET).get();

			Response response = null;
			String responseBody = "";

			findFlights(document);
		} catch (IOException e) {
			System.out.println("Connect to " + url + " >>>> ERROR ");
			e.printStackTrace();
		}
	}

	private void findFlights(Document doc) {
		Element flightBox;
		String flightBoxAddress = "body > div.result-wrap > div.container > div.row > div.col-md-12 > div.panel.panel-default > div#search-results > div.list-group-item > div.row > div.col-md-9.col-sm-9.col-xs-12 > div#search-results";
		flightBox = doc.select(flightBoxAddress).get(0);
		for (int i = 0; i < flightBox.children().size(); i++) {
			flightDetails(flightBox.child(i));
		}
	}

	private void flightDetails(Element element) {
		String flightCompany, capacity, price, flightNumber, flightClass, isCharter, date, time;
		flightCompany = element
				.select("div > div.panel-body > div.row > div.col-md-2.col-sm-2.col-xs-6 > p.text-center").text()
				.trim();
		date = element.select("div > div.panel-body > div.row > div.col-md-2.col-sm-2.col-xs-4 > p").text().trim();
		capacity = element.select("div > div.panel-body > div.row > div.col-md-2.col-sm-2.col-xs-3 > p.text-center")
				.text().trim();
		price = element
				.select("div > div.panel-body > div.row > div.col-md-2.col-sm-2.col-xs-4 > p.text-center > span.price")
				.text().trim();
		String link = makeURL();
		String host = "ghasedak24";
		String[] parts = flightCompany.split(" ");
		if (flightCompany.contains("ایر")) {
			String part0 = parts[0];
			String part1 = parts[1];
			String part2 = parts[2];
			String part3 = parts[3];
			flightCompany = part0 + " " + part1;
			flightNumber = part2;
			isCharter = part3;

		} else {
			String part0 = parts[0];
			String part1 = parts[1];
			String part2 = parts[2];
			flightCompany = part0;
			flightNumber = part1;
			isCharter = part2;
		}

		System.out.println("date******" + date);
		String[] parts1 = date.split(" ");

		String part0 = parts1[0];
		String part1 = parts1[1];
		String part2 = parts1[2];
		System.out.println(part0 + " ******************** " + part1 + " ********** " + part2 + " ******* ");
		date = part0;
		time = part1;
		flightClass = part2;

		System.out.println(price + " " + flightClass + " " + capacity + " " + time + " " + date + " " + isCharter + " "
				+ flightNumber + " " + flightCompany);

		String id = flightCompany + " " + flightNumber + " " + flightClass + capacity;

		Flight flight = new Flight();
		flight.setId(id);
		flight.setFlightCompany(flightCompany);
		flight.setFlightNumber(flightNumber);
		flight.setIsCharter(isCharter);
		flight.setDate(date);
		flight.setTime(time);
		flight.setCapacity(capacity);
		flight.setFlightClass(flightClass);
		flight.setPrice(price + "0");
		flight.setLink(link);
		flight.setHost(host);
		// have doubt it works ok or not
		// flight.setLowestPrice(price);
		flights.add(flight);

	}

	private String makeURL() {

		// String urlPart = null;
		if (Information.getIsRoundTrip() == false) {
			String baseUrl = "https://ghasedak24.com/search/flight/";
			String dateIn = getCheckInDate().replaceAll("/", "-");
			String urlPart = baseUrl + srcCityCode + "-" + destCityCode + "/" + dateIn + "/" + getAdults() + "-"
					+ getChildren() + "-" + getNewBorns();
			return urlPart;
		} else if (Information.getIsRoundTrip() == true) {

			String baseUrl = "https://ghasedak24.com/search/flight/";
			String dateIn = getCheckInDate().replaceAll("/", "-");
			String dateOut = getCheckOutDate().replaceAll("/", "-");
			String urlPart = baseUrl + srcCityCode + "-" + destCityCode + "/" + dateIn + "/" + dateOut + "/"
					+ getAdults() + "-" + getChildren() + "-" + getNewBorns();
			return urlPart;
		}
		return "not found url";
	}

	// reads the cities from html file I made and checks wether they exist or
	// not
	// private void readCityCodesFromFile() throws FileNotFoundException,
	// NoSuchElementException {
	// File cityCodeFile = new File("/home/eric/Alibaba.txt");
	// Scanner scanner = new Scanner(cityCodeFile);
	// System.out.println("src " + getSrcPersianCityName());
	// System.out.println("des " + getPersianCityName());
	//
	// while (scanner.hasNextLine()) {
	// if (scanner.next().equals(getPersianCityName())) {
	// this.destCityCode = scanner.next();
	// break;
	// } else {
	// continue;
	// }
	// }
	// scanner.close();
	// System.out.println(this.srcCityCode);
	// Scanner scanner2 = new Scanner(cityCodeFile);
	// while (scanner2.hasNextLine()) {
	// if (scanner2.next().equals(getSrcPersianCityName())) {
	// this.srcCityCode = scanner2.next();
	// break;
	// } else {
	// continue;
	// }
	// }
	// System.out.println(this.srcCityCode);
	// scanner2.close();
	// System.out.println("src code: " + srcCityCode);
	// System.out.println("dest code: " + destCityCode);
	// if (srcCityCode == null || destCityCode == null) {
	// String err = "گزینه ای برای " + "'" + getSrcPersianCityName() + "'" +
	// "یا" + "'" + getPersianCityName()
	// + "'" + " پیدا نشد";
	// throw new NoSuchElementException(err);
	// }
	// }
}
