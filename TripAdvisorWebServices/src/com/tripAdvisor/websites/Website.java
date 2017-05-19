package com.tripAdvisor.websites;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import com.tripAdvisorIR.dbModel.Flight;
import com.tripAdvisorIR.dbModel.Hotel;
import com.tripAdvisorIR.info.Information;

public abstract class Website implements Runnable {
	private Information information;
	protected final static String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0";
	public static List<Hotel> hotels = new ArrayList<>();
	public static List<Flight> flights = new ArrayList<>();
	protected String srcCityCode;
	protected String destCityCode;

	// constructors
	public Website() {

	}

	public Website(Information information) {
		this.information = information;
		try {
			readCityCodesFromFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		startScrape();
	}

	// setter getter for List<Hotel>
	public List<Hotel> getHoltes() {
		return hotels;
	}

	public void setHotels(List<Hotel> hotels) {
		this.hotels = hotels;
	}

	// setter getter for list flights
	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	// add Hotel to List<Hotel>
	protected void addHotel(Hotel hotel) {
		hotels.add(hotel);
	}

	// setter getter
	protected String getPersianCityName() {
		return information.getPersianCityName();
	}

	protected String getCheckInDate() {
		return information.getCheckinDate();
	}

	protected Integer getNights() {
		return information.getNights();
	}

	protected String getEnglishCityName() {
		return information.getEnglishCityName();
	}

	protected int getChildren() {
		return information.getChildren();
	}

	protected int getAdults() {
		return information.getAdults();
	}

	protected String getSrcPersianCityName() {
		return information.getSrcPersianCityName();
	}

	// protected boolean getIsRoundTrip(){
	// return information.isRoundTrip();
	// }

	protected int getNewBorns() {
		return information.getNewBorns();
	}

	protected String getCheckOutDate() {
		return information.getChechoutDate();
	}

	protected void startScrape() {

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
