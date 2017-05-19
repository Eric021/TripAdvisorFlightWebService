package com.tripAdvisor.websites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tripAdvisorIR.dbModel.Hotel;
import com.tripAdvisorIR.dbModel.Room;
import com.tripAdvisorIR.info.Information;

public class Eghamat24 extends Website {
	private String hotelName;

	// default Constructor
	public Eghamat24() {

	}

	// constructor with fields of superclass
	public Eghamat24(Information information) {
		super(information);
		startScrape();
	}

	// starts scraping
	public void startScrape() {
		String url = makeURL();
		String queryString ="";// makeQueryString();
		Document document;
		try {
			document = Jsoup.connect(url).userAgent("Mozilla").get();
			System.out.println("titleeeee" + document.title());
			Response response = Jsoup.connect(queryString)
					.method(Method.GET)
					.header("Host", "www.eghamat24.com")
					.userAgent("Mozilla/5.0")
					.header("Accept", "*/*")
					.header("Accept-Language","en-US,en;q=0.5")
					.header("Accept-Encoding", "gzip, deflate, br")
					.header( "X-Requested-With" , "XMLHttpRequest")
					.referrer("https://www.eghamat24.com/search/Mashhad/96-02-18/3")
//					.header("Cookie", "_ga=GA1.2.1740610233.1490256384;"
//							+ "24-Session=9e1eadb6f1c29778cfdee500794fbabf42ce81ba;"
//							+ "_gid=GA1.2.1850395080.1494056782")
					.header("Connection", "keep-alive").execute();
			findHotels(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// makes url
	public String makeURL() {
		String checkinDate = getDateIn();
		System.out.println(
				"https://www.eghamat24.com/search/" + getEnglishCityName() + "/" + checkinDate + "/" + getNights());
		return "https://www.eghamat24.com/search/" + getEnglishCityName() + "/" + checkinDate + "/" + getNights();

	} // end of makeURL

	private String getDateIn() {
		String dateIn = getCheckInDate();
		dateIn = dateIn.substring(2);
		dateIn = dateIn.replaceAll("/", "-");
		return dateIn;
	}

	// finds hotels
	private void findHotels(Document document) {

		Element hotelBox;
		String hotelBoxCssSelector = "body > main > div.r_box.page_box.search_page > div > div "
				+ "> div.page_content_box > div.page_content_box_main";
		hotelBox = document.select(hotelBoxCssSelector).get(0);
		for (int i = 0; i < hotelBox.children().size(); i++) {

			try {

				// gets hotelNameInfo for every Hotel
				Element hotelNameInfo = getHotelNameInfo(hotelBox.child(i));
				// gets name for every hotel
				String name = getName(hotelNameInfo);
				// gets Link for every Hotel
				String link = getLink(hotelNameInfo);
				// gets address(location) for every Hotel
				String location = getLocation(hotelNameInfo);
				// gets rooms from a Hotel
				this.hotelName = name;
				List<Room> rooms = getHotelRooms(getRoomTags(hotelBox.child(i)));
				String id = getPersianCityName() + "-" + name;
				String lowestPrice = findLowestPrice(rooms);
				Hotel hotel = new Hotel(id, name, "eghamat24", link, lowestPrice);
				System.out.println(hotel);
				addHotel(hotel);

			} catch (NumberFormatException e) {
				continue;
			}

		}

	} // end of findHotels

	private String findLowestPrice(List<Room> rooms) {
		double[] prices = new double[rooms.size()];
		int counter = 0;
		for (Room room : rooms) {
			prices[counter] = Double.parseDouble(room.getPrice().replaceAll(",", ""));
			counter++;
		}
		double lowestPrice = prices[0];
		for (int i = 0; i < prices.length; i++) {
			if (prices[i] < lowestPrice) {
				lowestPrice = prices[i];
			}
		}
		return String.valueOf(lowestPrice);
	}

	// returns Element for link and name informations
	private Element getHotelNameInfo(Element hotelBoxChild) {
		return hotelBoxChild.child(0);
	}

	// returns name of a hotel
	private String getName(Element hotelNameInfo) {
		return hotelNameInfo.select("div.hotel_name_box > a").get(0).attr("title").trim();
	}

	// returns weblink of a hotel
	private String getLink(Element hotelNameInfo) {
		String link = hotelNameInfo.select("div.hotel_name_box > a").get(0).attr("href").trim();
		if (link.contains(" ")) {
			link = link.replace(" ", "%20");
		}
		return link;
	}

	// returns List of Rooms for a Hotel
	private List<Room> getHotelRooms(Elements hotelRoomsTags) throws NumberFormatException {
		List<Room> rooms = new ArrayList<>();
		int i = 000;
		for (Element roomTag : hotelRoomsTags) {
			int peopleNumber = getPeopleNumber(roomTag);
			String price = getPrice(roomTag);
			String type = getType(roomTag);
			boolean isAvilable = getIsAvilable(roomTag);
			boolean breakfast = getBreakfast(roomTag);
			boolean lunchDinner = getLunchDinner(roomTag);

			String id = getEnglishCityName() + "-" + hotelName + "-" + peopleNumber + i;
			i++;
			rooms.add(new Room(id, type, isAvilable, price, lunchDinner, breakfast, peopleNumber, 0));
		}
		return rooms;
	}

	// returns true if the room from Hotel is avilablle
	private boolean getIsAvilable(Element roomTag) {
		String avilableText = roomTag.getElementsByTag("section").get(5).select("a > p").text();
		if (avilableText.contains("رزرو")) {
			return false;
		} else {
			return true;
		}
	}

	// returns capacity of a room from a Hotel
	private int getPeopleNumber(Element roomTag) {
		return roomTag.getElementsByTag("section").get(0).getElementsByTag("span").size();
	}

	// returns price of a room from Hotel
	private String getPrice(Element roomTag) throws NumberFormatException {
		String price = roomTag.getElementsByTag("section").get(4).select("div.hotel_room_price_new > p").text().trim();
		price = price.replaceAll("۰", "0");
		price = price.replaceAll("۱", "1");
		price = price.replaceAll("۲", "2");
		price = price.replaceAll("۳", "3");
		price = price.replaceAll("۴", "4");
		price = price.replaceAll("۵", "5");
		price = price.replaceAll("۶", "6");
		price = price.replaceAll("۷", "7");
		price = price.replaceAll("۸", "8");
		price = price.replaceAll("۹", "9");
		if (price.equals("") | price.equals(" ")) {
			throw new NumberFormatException("Not Found Price");
		} else {
			return price.replaceAll("تومان", "").trim();
		}
	}

	// returns boolean for breakfast of a room from Hotel
	private boolean getBreakfast(Element roomTag) {
		String hasBreakfast = roomTag.getElementsByTag("section").get(3).child(0).attr("class");
		if (hasBreakfast.equals("icon-correct.signal"))
			return true;
		else
			return false;
	}

	private boolean getLunchDinner(Element roomTag) {
		String hasLunchDinner = roomTag.getElementsByTag("section").get(3).child(1).attr("class");
		if (hasLunchDinner.equals("icon-close-2"))
			return false;
		else
			return true;
	}

	// returns type of room from Hotel
	private String getType(Element roomTag) {
		return roomTag.getElementsByTag("section").get(1).text().trim();
	}

	// returns address(location) of a hotel
	private String getLocation(Element hotelNameInfo) {
		return hotelNameInfo.select("div.hotel_other_info > p.hotel_address").get(0).text().trim();
	}

	// returns tags of rooms of a Hotel
	private Elements getRoomTags(Element hotelBoxChild) {
		return hotelBoxChild.select("div.hotel_box_reserve > div").get(0).child(0).child(1).select("div.tr");
	}
}
