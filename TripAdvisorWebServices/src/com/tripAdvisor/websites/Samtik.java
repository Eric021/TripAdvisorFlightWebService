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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tripAdvisorIR.dbmanager.SamtikCityCodes;
import com.tripAdvisorIR.info.Information;

public class Samtik extends Website {

	private Map<String, String> cityCode = new HashMap<String, String>();
	private String srcCityCode;
	private String destCityCode;
	private Document document;
	
	public Samtik() {

	}

	public Samtik(Information information) {
		super(information);
		try {
			readCityCodesFromFile();
		} catch (FileNotFoundException | NoSuchElementException e) {
			e.printStackTrace();
		}
	}
	
	public void startScrape(){
		String url = makeURL();
		System.out.println(url);
		try {
			this.document = Jsoup.connect(url).get();
			Thread.sleep(200);
			
			
			System.out.println("connected to " + url);
			findFlights();
		} catch (IOException | InterruptedException e) {
			System.out.println("Connect to " + url + " >>>> ERROR ");
			e.printStackTrace();
		}
	}
	
	private void findFlights(){
		System.out.println(document);
		Elements flighsBoxes = document.select("div.list-tickets > div.tab-content > div > div.row > div.col-xs-12 > article");
		System.out.println(flighsBoxes);
		
		for (Element flightBox : flighsBoxes) {
			String flightTitle = flightBox.select("div:nth-child(1) > h3:nth-child(2)").text().trim();
			String flightNumber = flightBox.select("div:nth-child(1) > small:nth-child(3)").text().trim();
			
			System.out.println(flightTitle + ": " + flightNumber);
		}
		
	}
	
	private String makeURL(){
		String baseUrl = "https://www.samtik.com/flight/";
		String planeFa = "هواپیما";
		String ticketFa = "بلیط";
		String toFa = "به";
		String dateIn = getCheckInDate().replaceAll("/", "");
		
		if (Information.getIsRoundTrip()==true) { // two-way ticket
//			String urlPart1 = baseUrl + srcCityCode + "-" + destCityCode + "-" + dateIn + getAdults() + "-" + getChildren() + "-" + getNewBorns() + 
			return null;
		} else { // one-way ticket			
			String urlPart1 = baseUrl + srcCityCode + "-" + destCityCode + "-" + dateIn + "-" + getAdults() + "-" + getChildren() + "-" + getNewBorns() + "/";
			String urlPart2 = ticketFa + "-" + planeFa + "-" + getSrcPersianCityName() + "-" + toFa + "-" + getPersianCityName();
			return urlPart1 + urlPart2;
		}
	}
	
	private void readCityCodesFromFile() throws FileNotFoundException, NoSuchElementException {
		File cityCodeFile = new File("/home/amir/TripAdvisor/TripAdviserJsoup/txtFiles/SamkitCityCodes.txt");
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

	
	
	/*
	 * writes founded cities and codes to a file
	 */
	/*private void writeToFile() throws FileNotFoundException {
		File cityCodeFile = new File("/home/amir/TripAdvisor/TripAdviserJsoup/txtFiles/SamkitCityCodes.txt");
		Formatter formatter = new Formatter(cityCodeFile);

		Set<String> keys = new HashSet<>();
		keys = cityCode.keySet();
		for (String key : keys) {
			String value = cityCode.get(key);
			formatter.format(key + " " + value + "\n");
		}

		formatter.close();
	}*/
	

	/*
	 * searches samtik.com for codes of cities
	 */
	/*public void getCityCodesFromSite() {
		Document doc;
		try {
			 doc = Jsoup.connect("http://samtik.com").get();
			 File file = new File("/home/amir/samtik.html");
			 doc = Jsoup.parse(file, "UTF-8", "");
			 scrapeCityCodes(doc);
			 SamtikCityCodes.writeToDB(cityCode);
			 writeToFile();
			readCityCodesFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	

	/*
	 * searches samtik.com html file to obtain cities codes
	 */
	/*private void scrapeCityCodes(Document doc) {

		// System.out.println(doc);
		String selector = "#domesticFlights";
		Elements elements = doc.select(selector).get(0).child(0).child(0).child(1).children();
		System.out.println(elements);
		for (Element element : elements) {
			String key = element.text().trim();

			String[] parts = element.attr("data-latin").trim().split("-");
			String key2 = parts[0].trim();
			String value = element.attr("value").trim();
			if (!key.equals("") && !key.equals(" ")) {
				if (!value.equals("") && !value.equals(" ")) {
					System.out.println("****** key2: " + key2);
					System.out.println("****** value: " + value);
					cityCode.put(key2, value);
				}
			}
		}
	}*/
}
