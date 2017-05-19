package com.tripAdvisor.websites;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tripAdvisorIR.calendar.CalendarTool;
import com.tripAdvisorIR.dbModel.Hotel;
import com.tripAdvisorIR.info.Information;

public class AriaBooking extends Website implements Runnable{

	private Document document;

	public AriaBooking() {

	}

	public AriaBooking(Information information) {
		super(information);
	}
	
	@Override
	public void run() {
		startScrape();
	}

	public void startScrape() {
		String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0";
		try {
			String url = makeURL();
			System.out.println("Connecting to page: " + 1);
			document = Jsoup.connect(url + "&page=1")
					.header("Host", "ariabooking.ir")
					.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Language", "en-US,en;q=0.5")
					.header("Accept-Encoding", "gzip, deflate")
					.referrer("http://ariabooking.ir/")
					.header("Connection", "keep-alive")
					.userAgent(userAgent)
					.header("Upgrade-Insecure-Requests", "1")
					.followRedirects(true)
					.timeout(2*1000)
					.get();
			int pageNumber = numberOfPages();
			findHotels();
			for (int i = 2; i <= pageNumber; i++) {
				System.out.println("Connecting to page: " + i);
				document = Jsoup.connect(url + "&page=" + i)
						.header("Host", "ariabooking.ir")
						.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
						.header("Accept-Language", "en-US,en;q=0.5")
						.header("Accept-Encoding", "gzip, deflate")
						.referrer("http://ariabooking.ir/")
						.header("Connection", "keep-alive")
						.userAgent(userAgent)
						.header("Upgrade-Insecure-Requests", "1")
						.followRedirects(true)
						.timeout(2*1000)
						.get();
				findHotels();
			}
			
			System.out.println("number of hotels added to List: " + hotels.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void findHotels() throws IOException {
		Elements hotelsBoxes = document.select("div.hotel_info.row");
		for (Element hotelBox : hotelsBoxes) {
			try {
				String name = findName(hotelBox);
				String lowestPrice = findLowestPrice(hotelBox);
				String link = findLink(hotelBox);
				String id = getPersianCityName() + "-" + name; 
				Hotel hotel = new Hotel(id, name, "ariabooking", link, lowestPrice);
				hotels.add(hotel);
			} catch (Exception e) {
				continue;
			}
		}
		
	}

	private String findLink(Element hotelBox) {
		CalendarTool calendar = new CalendarTool();
		calendar.setIranianDate(getCheckInDate());
		String[] linkParts = hotelBox.select("div.hotel_main_info > div.hotel_title > a").get(0).attr("href").trim().split("check_in1=");
		String linkPart1 = linkParts[0];
		String linkPart2 = "check_in1=" + calendar.getIranianDay() + " " + calendar.getIranianYear() + " " +  calendar.getIranianMonthStr();
		String linkPart3 = "&check_in2=" + getCheckInDate().replaceAll("/", "-") + "&number_night=" + getNights();
		
		int dayTo = calendar.getIranianDay() + getNights();
		int monthTo = calendar.getIranianMonth();
		int yearTo = calendar.getIranianYear();

		boolean isLeapYear = (calendar.getIranianYear() % 4 == 0 && calendar.getIranianYear() % 100 != 0) || (calendar.getIranianYear() % 400 == 0);

		if (dayTo >= 29) {
			if (calendar.getIranianMonth() <= 6 && calendar.getIranianMonth() >= 1) {
				if (dayTo > 31) {
					dayTo = dayTo - 31 - 1;
					monthTo += 1;
				} else if (calendar.getIranianMonth() >= 7 && calendar.getIranianMonth() <= 11) {
					if (dayTo > 30) {
						dayTo = dayTo - 30 - 1;
						monthTo += 1;
					}
				} else {
					if (isLeapYear) {
						if (dayTo > 30) {
							dayTo = dayTo - 30 - 1;
							yearTo += 1;
							monthTo = 1;
						}
					} else {
						if (dayTo > 29) {
							dayTo = dayTo - 29 - 1;
							monthTo = 1;
							yearTo = yearTo + 1;
						}
					}
				}
			}
		}
		calendar.setIranianDate(yearTo, monthTo, dayTo);
		String linkPart4 = "&check_out1=" + calendar.getIranianDate().replaceAll("/", "-");
		String linkPart5 = "&check_out2=" + calendar.getIranianDay() + " " + calendar.getIranianYear() + " " + calendar.getIranianMonthStr();
		String link = linkPart1 + linkPart2 + linkPart3  + linkPart4 + linkPart5 ;
		return link; 
	}

	private String findLowestPrice(Element hotelBox) {
		String priceCssSelector = "div.hotel_main_info > div.hotel_price > span.farsi > b";
		Element temp = hotelBox.select(priceCssSelector).get(0);
		if (temp.children().size() > 0) {
			hotelBox.select("div.hotel_main_info > div.hotel_price > span.farsi > b > del").get(0).remove();
		}
		int price;
		String priceString = hotelBox.select(priceCssSelector).get(0).text().replaceAll(",", "");//.substring(2);

		price = Integer.parseInt(priceString) / 10;

		return String.valueOf(price);
	}

	private String findName(Element hotelBox) throws IOException {
		String name = hotelBox.select("div.hotel_main_info > div.hotel_title > a").get(0).text().trim();
		String[] nameParts = name.split(" ");
		
		if (!name.contains(getPersianCityName())) {
			throw new IOException(name + " does not equals " + getPersianCityName());
		} else {
			if (nameParts[nameParts.length-1].equals(getPersianCityName())) {
				return name;				
			} else {
				throw new IOException(nameParts[nameParts.length-1] + " does not equals " + getPersianCityName());
			}
		}
	}

	private int numberOfPages() {
		int counter = 0;
		for (int i = 0; i < document.select("#pageingHolder").get(0).getElementsByTag("a").size(); i++) {
			if (!document.select("#pageingHolder").get(0).child(i).text().equals("")) {
				counter++;
			}
		}
		return counter;
	}

	private String makeURL() {
		String url = "http://ariabooking.ir/hotel_search.php?keywords=";
		String dateIn = getCheckInDate().replaceAll("/", "-");
		url = url + getPersianCityName() + "&check_in2=" + dateIn + "&num_night_index=" + getNights();
		return url;
	}

}
