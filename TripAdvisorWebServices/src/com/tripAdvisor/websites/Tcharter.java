package com.tripAdvisor.websites;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tripAdvisorIR.dbModel.Flight;
import com.tripAdvisorIR.info.Information;

public class Tcharter extends Website {

	private Map<String, String> cityCode = new HashMap<String, String>();
	private String srcCityCode;
	private String destCityCode;
	private Document document;
	private Set<Flight> flights;
	
	public Tcharter() {

	}

	public Tcharter(Information information) {
		super(information);
		try {
			readCityCodesFromFile();
		} catch (FileNotFoundException | NoSuchElementException e) {
			e.printStackTrace();
		}
	}
	
	protected void startScrape () {
		try {
			document = Jsoup.connect(makeURL()).userAgent("Mozilla").get();
			findFlights();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void findFlights() {
		flights = new HashSet<>();
		Element flightsBox = findFlightBox(document);
		
	}
	
	private Element findFlightBox(Document document) {
		return document.select("#dates").get(0);
	}
	
	private String makeURL() {
		String baseUrl = "https://tcharter.ir/tickets/dates/";
		if (Information.getIsRoundTrip()==true) { // two-way ticket
//			String urlPart1 = baseUrl + srcCityCode + "-" + destCityCode + "-" + dateIn + getAdults() + "-" + getChildren() + "-" + getNewBorns() + 
			return null;
		} else { // one-way ticket			
			return baseUrl + srcCityCode + "-" + destCityCode;
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
	
}
