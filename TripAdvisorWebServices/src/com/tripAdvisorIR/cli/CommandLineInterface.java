package com.tripAdvisorIR.cli;

import java.util.Scanner;

import com.tripAdvisor.websites.EGardesh;
import com.tripAdvisor.websites.Eghamat24;
import com.tripAdvisor.websites.EliGasht;
import com.tripAdvisor.websites.HotelYar;
import com.tripAdvisor.websites.PinTaPin;
import com.tripAdvisorIR.info.Information;
import com.tripAdvisorIR.search.Search;

public class CommandLineInterface {
	private Information information = new Information();
		
	public void run() {
		askUserInfo();
		Search search = new Search(information);
		search.searchFlightsOnWeb();
//		search.searchHotelsOnWeb();
//		search.searchPlacesOnWeb();
	}
	
	// asks user informations:
	private void askUserInfo(){

		Scanner scanner = new Scanner(System.in);
		System.out.println("enter your current persian city: ");
		information.setSrcPersianCityName(scanner.next());
		System.out.println("enter destination persian city name: ");
		information.setPersianCityName(scanner.next());
		System.out.println("enter destination english city name: ");
		information.setEnglishCityName(scanner.next());
		System.out.println("do you need a two-way ticket? y/n: ");
		String ans = scanner.next();
		if (ans.equals("y") | ans.equals("Y")) {
			Information.setIsRoundTrip(true);
		} else {
			Information.setIsRoundTrip(false);
		}
		System.out.println("enter checkin date. ex: 1396/02/15");
		information.setCheckinDate(scanner.next());
		System.out.println("how many nights you need to reserve?");
		information.setNights(scanner.nextInt());
		System.out.println("how many adults you are?");
		information.setAdults(scanner.nextInt());
		System.out.println("how many kids you have?");
		information.setChildren(scanner.nextInt());
		System.out.println("how many newborn you have? ");
		information.setNewBorns(scanner.nextInt());
		
		scanner.close();
	}
	
	
}	
