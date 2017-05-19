package com.tripAdvisor.websites;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tripAdvisorIR.calendar.CalendarTool;
import com.tripAdvisorIR.dbModel.Hotel;
import com.tripAdvisorIR.dbModel.Room;
import com.tripAdvisorIR.info.Information;

public class PinTaPin extends Website {

	private Document document;
	private String hotelName;

	// constructors
	public PinTaPin() {

	}

	public PinTaPin(Information information) {
		super(information);
	}

	protected void startScrape() {
		try {
			String url = makeURL();
			document = Jsoup.connect(url).userAgent("Mozilla").followRedirects(true).get();
			int pageNumber = findPageNumber(document);
			findHotels();
			for (int i = 2; i <= pageNumber; i++) {
				document = Jsoup.connect(url + "&page=" + i).userAgent("Mozilla").get();
				findHotels();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// finds Hotel
	private void findHotels() throws HttpStatusException {
		Elements hotels = document.select(".hotels-data").get(0).getElementsByTag("li");
		for (Element hotelElem : hotels) {
			Element hotelElement = hotelElem.child(0);
			// String address = findAddress(hotelElement);
			String name = findName(hotelElement);
			String lowestPrice = findLowestPrice(hotelElement);
			this.hotelName = name;
			String link = "";
			try {
				link = findLink(hotelElement);
			} catch (ParseException e) {
				continue;
			}
			List<Room> rooms = null;
			// try {
			// rooms = findRooms(webUrl);
			// } catch (IOException e) {
			// continue;
			// }
			String id = getPersianCityName() + "-" + name;
			Hotel hotel = new Hotel(id, name, "pintapin", link, lowestPrice);
			System.out.println(hotel);
			addHotel(hotel);
		}
	}

	private String findLowestPrice(Element hotelElement) {
		String priceCssSelector = "div.hotel-details > div.price-part > div.price > div.new-price > span";
		int price = Integer.parseInt(hotelElement.select(priceCssSelector).text().replaceAll(",", "").trim());
		return String.valueOf(price / getNights());
	}

	// finds List<Room> of a Hotel
	private List<Room> findRooms(String link) throws IOException, HttpStatusException {
		Document doc = null;
		doc = Jsoup.connect(link).userAgent("Mozilla").followRedirects(true).get();
		List<Room> rooms = new ArrayList<>();
		int roomsSize;
		try {
			roomsSize = findNumberOfRooms(doc);
		} catch (NullPointerException e) {
			roomsSize = 0;
		}
		Element hotelRooms = doc.getElementById("hotel-rooms-list-section");
		for (int i = 0; i < roomsSize; i++) {
			Element hotelRoom = hotelRooms.child(i).child(0);
			String price = findRoomPrice(hotelRoom);
			boolean breakfast = hasBreakfast(hotelRoom);
			boolean lunchDinner = false;
			int peopleNumber = findPeopleNumber(hotelRoom);
			int extraPeople = 0;
			String type = findRoomType(hotelRoom);
			boolean isAvailable = true;
			String id = hotelNameRoom(doc) + "-" + type + "-" + peopleNumber;
			Room room = new Room(id, type, isAvailable, price, lunchDinner, breakfast, peopleNumber, extraPeople);

			rooms.add(room);
		}

		return rooms;
	}

	// findsHotelName from RoomPage
	private String hotelNameRoom(Document doc) {
		return doc.select("h1.title > a:nth-child(1)").text().trim();
	}

	// finds type of a Room from a Hotel
	private String findRoomType(Element hotelRoom) {
		return hotelRoom.select("div.name > h4").text().trim();
	}

	// finds number of people in Room
	private int findPeopleNumber(Element hotelRoom) {
		// System.out.println(hotelRoom.select("div.name >
		// div.space").outerHtml());
		return Integer.parseInt(hotelRoom.select("div.name > div.space").get(0).child(1).text().trim());
	}

	// has return boolean for breakfast
	private boolean hasBreakfast(Element hotelroom) {
		String hasBreakfastString = hotelroom.select("div.detail > div.include > div > span.breakfast").text().trim();

		if (hasBreakfastString.contains("صبحانه")) {
			return true;
		} else {
			return false;
		}

	}

	// finds room's price
	private String findRoomPrice(Element hotelRoom) {
		return hotelRoom.select("div.detail > div.total-price > div.total > div.low-price > span.span-prices")
				.attr("content").trim();
	}

	// finds number of rooms
	private int findNumberOfRooms(Document doc) throws NullPointerException {
		return doc.getElementById("hotel-rooms-list-section").children().size();
	}

	// finds link of a Hotel
	private String findLink(Element hotelElement) throws ParseException {
		String dateFrom = getDateFrom();
		String dateTo = getDateTo();
		String reserveFa = "رزرو";
		String hotelFa = "هتل";
		String baseUrl = "https://www.pintapin.com/" + reserveFa + "-" + hotelFa + "/" + getPersianCityName() + "/"
				+ getHotelNameForRoomLink();
		String link = baseUrl + "?adults=" + getAdults() + "&date_from=" + dateFrom + "&date_to=" + dateTo
				+ "&children_count=" + getChildren();
		return link;
	}

	//
	private String getHotelNameForRoomLink() {
		String removable = "-" + getPersianCityName();
		return hotelName.replaceAll(" ", "-").replace(removable, "");
	}

	// finds name of a Hotel
	private String findName(Element hotelElement) {
		return hotelElement.select("div.detail > h3").text().trim();
	}

	// finds Address of a Hotel
	private String findAddress(Element hotelElement) {
		return hotelElement.child(1).select("div.detail > address.address").text().trim();
	}

	// finds page numbers in firstPage
	private int findPageNumber(Document document) {
		int hotelNumbers = Integer.parseInt(document.select(".hotels-total > span:nth-child(1)").text());
		int pages = (int) Math.floor((hotelNumbers / 12) + 1);
		return pages;
	}

	// makes url based on user informations
	private String makeURL() {
		try {
			String dateFrom = getDateFrom();
			String dateTo = getDateTo();
			String reserveFa = "رزرو";
			String hotelFa = "هتل";
			String baseUrl = "https://www.pintapin.com/" + reserveFa + "-" + hotelFa + "/" + getPersianCityName();
			String url = baseUrl + "?adults=" + getAdults() + "&date_from=" + dateFrom + "&date_to=" + dateTo
					+ "&children_count=" + getChildren();
			return url;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// calculates dateFrom
	private String getDateFrom() throws ParseException {

		CalendarTool calendar = new CalendarTool();
		String dateFrom = getCheckInDate();
		String[] parts = dateFrom.split("/");
		int year = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]);
		int day = Integer.parseInt(parts[2]);
		calendar.setIranianDate(year, month, day);
		String dateIn = calendar.getGregorianDate();
		return dateIn.replaceAll("/", "-");
	}

	// calculates dateTo :
	private String getDateTo() throws ParseException {

		String dateFrom = getCheckInDate();
		int nights = getNights();
		String[] dateParts = dateFrom.split("/");
		int yearFrom = Integer.parseInt(dateParts[0]);
		int monthFrom = Integer.parseInt(dateParts[1]);
		int dayFrom = Integer.parseInt(dateParts[2]);

		int dayTo = dayFrom + nights;
		int monthTo = monthFrom;
		int yearTo = yearFrom;

		boolean isLeapYear = (yearFrom % 4 == 0 && yearFrom % 100 != 0) || (yearFrom % 400 == 0);

		if (dayTo >= 29) {
			if (monthFrom <= 6 && monthFrom >= 1) {
				if (dayTo > 31) {
					dayTo = dayTo - 31 - 1;
					monthTo += 1;
				} else if (monthFrom >= 7 && monthFrom <= 11) {
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
		CalendarTool calendarTool = new CalendarTool();
		calendarTool.setIranianDate(yearTo, monthTo, dayTo);
		return calendarTool.getGregorianDate().replaceAll("/", "-");
	}

}
