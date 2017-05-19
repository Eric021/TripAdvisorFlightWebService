package com.tripAdvisor.websites;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.select.Elements;

import com.tripAdvisorIR.dbModel.Hotel;
import com.tripAdvisorIR.dbModel.Room;
import com.tripAdvisorIR.info.Information;

public class HotelYar extends Website implements Runnable{
	private static Map<String, String> idCityMap = new HashMap<>();
	Document document;
	private String hotelName;
	// constructors
	public HotelYar() {

	}

	// constructor with fields
	public HotelYar(Information information) {
		super(information);
		try {
			getCityCode();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		startScrape();
	}
	
	public void startScrape() {

		OutputSettings settings = new OutputSettings();
		settings.charset("UTF-8");
		String useragent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0";
		try {

			String body = makeReqBody();
			String url = makeURL();

			document = Jsoup.connect(url).timeout(2 * 1000).header("Host", "hotelyar.com").userAgent(useragent)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Language", "en-US,en;q=0.5").header("Accept-Encoding", "gzip, deflate")
					.referrer("http://hotelyar.com").header("Content-Type", "application/x-www-form-urlencoded")
//					.header("Content-Length", "153")
					.header("Connection", "keep-alive")
					.header("Upgrade-Insecure_request", "1").followRedirects(true).method(Method.POST).requestBody(body)
					.post();

			Response response = null;
			String responseBody = "";
			int counter = 0;
			while (true) {
				counter += 5;
				body = makeReqBody(counter);
				System.out.println(counter);
				response = Jsoup.connect("http://hotelyar.com/cleanSearchContinue.php").header("Host", "hotelyar.com")
						.userAgent(useragent).header("Accept", "*/*").header("Accept-Language", "en-US,en;q=0.5")
						.header("Accept-Encoding", "gzip, deflate")
						.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
						.header("X-Requested-With", "XMLHttpRequest").header("Content-Length", "150").referrer(url)
						.header("Connection", "keep-alive").requestBody(body).method(Method.POST).execute();
				if ((!response.body().contains("<div")) | response.body() == null)
					break;
				response.charset("UTF-8");
				responseBody = responseBody + "\n\n" + response.body();

			}
			document.select("#divContainer").get(0).append(responseBody);

			findHotels(document);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String makeReqBody(int i) {
		String reqBody = "difference=" + getNights() + "&fromDate=" + getCheckInDate() + "&city="
				+ getCityCode().replaceAll("-", "") + "&orderBy=0&offset=" + i;
		return reqBody;
	}

	private String makeReqBody() {
		String reqBody = "";
		String[] dateParts = getCheckInDate().split("/");
		reqBody = "city=" + getCityCode().replaceAll("-", "") + "&slcHotel=0&fromDate=" + dateParts[0] + "%2F"
				+ dateParts[1] + "%2F" + dateParts[2] + "&difference=" + getNights() + "&group=1&";
		return reqBody;
	}

	private String getCityCode() throws NullPointerException {
		idCityMap.clear();
		String cityCode = "";
		File file = new File("/home/amir/TripAdviserJsoup/TripAdviserJsoup/txtFiles/cityidForHotelyar.txt");
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				if (scanner.next().equals(getPersianCityName())) {
					cityCode = scanner.next();
					break;
				}
			}
			scanner.close();

			if (cityCode.equals("")) {
				throw new NullPointerException("null for city code");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return cityCode;
	}

	// returns url of hotelyar.com
	public String makeURL() {
		return "http://hotelyar.com/city/" + getCityCode().replaceAll("-", "") + "/هتلهای-" + getPersianCityName();
	}

	// finds hotels from hotelyar.com
	private void findHotels(Document document) {
		String hotelsBoxesCssSelector = "body > div.col-sm-offset-0.col-xs-12.col-sm-12.col-md-12 >"
				+ "div.content.container > table > tbody > tr > td div.panel.panel-default";

		Elements hotelsBoxes = document.select(hotelsBoxesCssSelector);
		for (Element hotelBox : hotelsBoxes) {

			Hotel hotel = null;
			try {
				// gets hotelNameInfo for every Hotel
				Element hotelNameInfo = getHotelNameInfo(hotelBox);
				// gets name for every hotel
				String name = getName(hotelNameInfo);
				// gets Link for every Hotel
				String link = getLink(hotelNameInfo);
				// gets address(location) for every Hotel
				String location = ""; // getLocation(hotelNameInfo);
				// gets rooms from a Hotel
				this.hotelName = name;
				List<Room> rooms = getHotelRooms(getRoomTags(hotelBox));
				String lowestPrice = findLowestPrice(rooms);
				String id = getPersianCityName() + "-" + name;
				hotel = new Hotel(id, name, "hotelyar", link, lowestPrice);
				hotels.add(hotel);
				System.out.println(hotel.toJson());
			} catch (Exception e) {
				// e.printStackTrace();
				continue;
			}

			try {
				writeOutputToFile(hotel);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void writeOutputToFile(Hotel hotel) throws IOException {
		File file = new File("/home/amir/TripAdviserJsoup/resultHotelyar.log");
		FileWriter writer = new FileWriter(file);
		writer.append(hotel.toJson() + "\n\n");
		writer.flush();
		writer.close();
	}

	private String findLowestPrice(List<Room> rooms) throws ArrayIndexOutOfBoundsException {
		int[] prices = new int[rooms.size()];
		int priceArrayCounter = 0;
		for (Room room : rooms) {
			if (room.getPrice().equals(" تومان ")) {
				continue;
			}
			try {
				prices[priceArrayCounter] = Integer.parseInt(
						room.getPrice().replaceAll(",", "").replaceAll(" تومان ", "").replaceAll(" ", "").trim());
				priceArrayCounter++;
			} catch (Exception e) {
				continue;
			}
		}
		int lowestPrice;
		try {
			lowestPrice = prices[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException();
		}
		for (int i = 0; i < prices.length; i++) {
			if (prices[i] < lowestPrice) {
				lowestPrice = prices[i];
			}
		}
		return String.valueOf(lowestPrice / getNights());
	}

	// returns List<Room> from Hotel
	private List<Room> getHotelRooms(Elements hotelRoomsTags) {
		List<Room> rooms = new ArrayList<>();
		for (Element roomTag : hotelRoomsTags) {
			// String type = getType(roomTag);
			int peopleNumber;
			int extraPeople;
			boolean breakfast;
			boolean lunchDinner;
			String price;
			boolean isAvilable = true;
			try {
				peopleNumber = getPeopleNumber(roomTag);
				extraPeople = getExtraPeople(roomTag);
				breakfast = getBreakfast(roomTag);
				lunchDinner = false;
				price = getPrice(roomTag);
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			} catch (IndexOutOfBoundsException e) {
				continue;
			}

			String id = getEnglishCityName() + "-" + hotelName + "-" + peopleNumber;
			rooms.add(new Room(id, "", isAvilable, price, lunchDinner, breakfast, peopleNumber, extraPeople));
		}
		return rooms;
	}

	// returns price(string) of room from Hotel
	private String getPrice(Element roomTag) {
		int size = roomTag.getElementsByTag("td").size();
		Element lastTag = roomTag.select("td:nth-child(" + size + ")").get(0);

		String price;
		if (lastTag.children().size() > 0) {
			price = lastTag.child(0).attr("price").trim();
			price = price.substring(0, price.length() - 1);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
		return price + " تومان ";
	}

	// returns true if room has breakfast for free
	private boolean getBreakfast(Element roomTag) {
		String hasBreakfast = roomTag.select(":nth-child(1) > td:nth-child(5) > small").attr("alt").trim();
		if (!hasBreakfast.equals("صبحانه ندارد")) {
			return true;
		} else {
			return false;
		}
	}

	// returns extraPeopleNumber (int) for a room from Hotel
	private int getExtraPeople(Element roomTag) {
		String extPeopleStr = roomTag.select("td:nth-child(4)").get(0).text().trim();
		if (extPeopleStr.equals("-")) {
			return 0;
		} else {
			return new Integer(extPeopleStr).intValue();
		}
	}

	// returns peopleNumber (int) for a room from Hotel
	private int getPeopleNumber(Element roomTag) {
		return new Integer(roomTag.select("td:nth-child(3)").get(0).text().trim());
	}

	// returns Type of a Room from Hotel
	private String getType(Element roomTag) {
		return roomTag.select("td.text-right > b").get(0).text().trim();
	}

	// returns Tags for rooms of a Hotel
	private Elements getRoomTags(Element hotelBox) {
		return hotelBox.select("form > table > tbody > tr");
	}

	// returns Element containig hotel name information
	private Element getHotelNameInfo(Element hotelBox) {
		String hotelNameInfoSelector = "div > div.col-xs-12.col-sm-12.col-md-8 > h3 > a";
		return hotelBox.select(hotelNameInfoSelector).get(0);
	}

	private String getName(Element hotelNameInfo) {
		return hotelNameInfo.text().trim();
	}

	private String getLink(Element hotelNameInfo) {
		String link = hotelNameInfo.attr("href").trim();
		if (link.contains(" ")) {
			link = link.replace(" ", "%20");
		}
		return link;
	}

	private String getLocation(Element hotelAddressInfo) {
		return null;
	}

	// fils form based on customer informations
	private void fillForm(Document document) {
		FormElement form = (FormElement) document.select("#srch").get(0);
		Element selectNights = form.getElementsByAttributeValue("name", "difference").get(0);
		String nightsOptionValueToBeSelected = "option" + getNights();
		Elements selectNightOptions = selectNights.getElementsByTag("option");
		Element selectCity = form.getElementsByAttributeValue("name", "city").get(0);
		Elements selectCityOptions = selectCity.getElementsByTag("option");
		form.select("#datepicker").get(0).attr("value", getCheckInDate());

		for (Element option : selectCityOptions) {
			if (option.attr("city_name").equals(getPersianCityName())) {
				option.attr("selected", "selected");
			} else {
				option.removeAttr("selected");
			}
		}

		for (Element option : selectNightOptions) {
			if (option.attr("value").equals(nightsOptionValueToBeSelected)) {
				option.attr("selected", "selected");
			} else {
				option.removeAttr("selected");
			}
		}

		try {
			Document doc = form.submit().method(Method.POST).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Integer correctIdValue(String stringId) {
		if (stringId.startsWith("-")) {
			return Integer.parseInt(stringId.substring(1));
		} else if (stringId.equals(" ") | stringId.equals("")) {
			return null;
		} else {
			return Integer.parseInt(stringId);
		}
	}
}
