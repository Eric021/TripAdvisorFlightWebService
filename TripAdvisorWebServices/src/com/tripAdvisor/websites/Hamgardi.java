package com.tripAdvisor.websites;

import java.awt.Image;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.tripAdvisor.location.Coordinate;
import com.tripAdvisor.location.Location;
import com.tripAdvisorIR.info.Detail;
import com.tripAdvisorIR.places.Place;

public class Hamgardi extends Website implements PlaceMethods {

	private final String categoryURL;
	private Document document;
	private Element placeElement;
	private Place place;
	private int intTag;
	private String country;
	Set<Place> places = new HashSet<>();

	// Constructor
	public Hamgardi(Place place, int intTag) {
		this.categoryURL = place.getCategoryURL();
		this.place = place;
		this.intTag = intTag;
	}
	
	public Hamgardi() {
		categoryURL = null;
	}

	// getter for List<Place> places
	public Set<Place> getPlaces() {
		return places;
	}
	
	public void printPlaces() {
		for (Place place : places) {
			System.out.println(place);
		}
	}

	// return state names of Iran
	/*
	 public Set<String> getStateNames() {
		Set<String> stateNames = new HashSet<>();
		try {
			String url = "http://hamgardi.com/wiki/iran/list/page-1/show-province";
			String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";
			this.document = Jsoup.connect(url).userAgent(userAgent).get();
			Element stateNamesEelements = document.select("#OpenLayers_Layer_Markers_29").get(0);
			for (int i = 0; i < stateNamesEelements.children().size(); i++) {
				String name = stateNamesEelements.child(i).attr("href").trim();
				stateNames.add(name);
			}
			System.out.println(stateNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stateNames;
	}
	*/

	// methods to scrape Data from Hamgardi.com
	@Override
	public void startScrape() {
		String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";
		try {
			this.document = Jsoup.connect(categoryURL).userAgent(userAgent).get();
			int allPages = findAllPages();

			for (int pageNumber = 1; pageNumber <= allPages; pageNumber++) {
				String url = makeURL(pageNumber);
				this.document = Jsoup.connect(url).userAgent(userAgent).get();
				findPlaces();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void findPlaces() {
		Element placesBox = getPlaceBoxElement();

		for (int i = 0; i < placesBox.children().size() ; i++) {
			this.placeElement = getPlaceElement(placesBox, i);
			String name = findName();
			if (name.equals("") | name.equals(" ")) {
				continue;
			}
			
			try {
				place = new Place(intTag);
				place.setName(findName());
				String[] parts = place.getNewTag(intTag).split(",");
				place.setGeneralTag(parts[0]);
				place.setDetailedTag(parts[1]);
				String link = findLink();
				place.setImage(findImage());
				Detail detail = findDetails(link);
				place.setId(place.getName(), detail.getLocation());
				place.setLocation(detail.getLocation());
				place.setDescription(detail.getDiscription());
				place.setPhoneNumber(findPhoneNumber());
				if (this.country.equals("ایران")) {
					places.add(place);
				} else {
					continue;
				}
				System.out.println(place.toJson());
				System.out.println("place No. " + places.size() + " added.");
			} catch (Exception e) {
				continue;
			}
			
		}
	}

	// returns all places Box Element
	private Element getPlaceBoxElement() {
		return document.select("#TopMainContent_TopMainContent_DivItems").get(0);
	}

	// return every place's element
	private Element getPlaceElement(Element placesBox, int i) {
		return placesBox.child(i);
	}

	@Override
	public String makeURL(int pageNumber) {
		System.out.println("\n" + categoryURL + "/page-" + pageNumber + "\n");
		return categoryURL + "/page-" + pageNumber;
	}
	
	private Detail findDetails(String link) throws Exception {
		
			Document doc = Jsoup.connect(link).userAgent("Mozilla").get();
			String address = findAddress(doc);
			String city = findCity(doc);
			String state = findState(doc);
			Coordinate coordinate = findCoord(doc);
			Location location = new Location(coordinate, state, city, address);
			findCountry(doc);
//			int vote = findVote(doc);
			return new Detail(location, findDescription(doc));


	}
	
//	private int findVote(Document doc) {
//		return 0;
//	}

	private String findPhoneNumber(){
		String phoneNumber = "";
		String phoneNoSelector = "div.contact-info > div.row-fluid > div.span3 > span";
		try {
			phoneNumber = placeElement.select(phoneNoSelector).text().trim();
		} catch (Exception e) {
			phoneNumber = "";
		}
		return phoneNumber;
	}

	@Override
	public Coordinate findCoord(Document doc) {
		double lat = 0.0;
		double lng = 0.0;
		String coordString = doc.select("#GoogleMapControl").attr("data-center");
		lat = getLat(coordString);
		lng = getLng(coordString);

		return new Coordinate(lat, lng);
	}
	
	public void findCountry(Document doc){
		this.country = doc.select(".span10 > p:nth-child(2) > a:nth-child(1)").text().trim();
	}

	@Override
	public String findCity(Document doc) {
		return doc.select(".span10 > p:nth-child(2) > a:nth-child(3)").text().trim();
	}

	@Override
	public String findState(Document doc) {
		return doc.select(".span10 > p:nth-child(2) > a:nth-child(2)").text().trim();
	}

	@Override
	public String findAddress(Document doc) {
		return doc.select(".span10 > p:nth-child(2)").text().trim();
	}

	@Override
	public Image findImage() {
		return null;
	}

	@Override
	public String findDescription(Document doc) {
		return doc.select(".review").text().trim();
	}

	@Override
	public double getLat(String coordString) {
		String[] parts = coordString.split(",");
		return new Double(parts[0]);
	}

	@Override
	public double getLng(String coordString) {
		String[] parts = coordString.split(",");
		return new Double(parts[1]);
	}

	@Override
	public String findLink() {
		String url = "http://hamgardi.com" + placeElement.select("h5 > a").attr("href");
		return url;
	}

	@Override
	public String findName() {
		String name = placeElement.select("h5 > a > span.persian_title").text().trim();
		name = name.replaceAll("\"", "");
		try {
			int pipeIndex = name.indexOf("|");
			name = name.substring(0,pipeIndex-1);
		} catch (Exception e) {

		}
		return name.trim(); 
	}

	@Override
	public int findAllPages() {
		String seletor = "#TopMainContent_TopMainContent_ListPagingControl1_ul > li.pagingItemCount";
		String number = document.select(seletor).text();
		String[] parts = number.split(" ");
		number = parts[0].trim();
		int num = Integer.parseInt(number);
		int res = (int) (Math.floor(num / 30) + 1);
		return res;

	}
	
	public Set<String> getStateNames() {

		//Set<String> stateNames = new HashSet<>();
		/*
		 * try { String url =
		 * "http://hamgardi.com/wiki/iran/list/page-1/show-province"; String
		 * userAgent =
		 * "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21"
		 * ; this.document = Jsoup.connect(url).userAgent(userAgent).get();
		 * System.out.println("title = " + document.title()); Element
		 * stateNamesEelements =
		 * document.select("#OpenLayers_Layer_Markers_29 > div").get(0);
		 * System.out.println(stateNamesEelements.outerHtml());
		 * 
		 * for (int i = 0; i < stateNamesEelements.children().size(); i++) {
		 * String name = stateNamesEelements.child(i).attr("href").trim();
		 * stateNames.add(name); } System.out.println(stateNames); } catch
		 * (Exception e) { e.printStackTrace(); } return stateNames;
		 */

		Set<String> state = new HashSet<>();
		state.add("آذربایجان غربی");
		state.add("آذربایجان شرقی");
		state.add("اردبیل");
		state.add("اصفهان");
		state.add("البرز");
		state.add("ایلام");
		state.add("بوشهر");
		state.add("تهران");
		state.add("چهارمحال و بختیاری");
		state.add("خراسان جنوبی");
		state.add("خراسان رضوی");
		state.add("خراسان شمالی");
		state.add("خوزستان");
		state.add("زنجان");
		state.add("سمنان");
		state.add("سیستان و بلوچستان");
		state.add("فارس");
		state.add("قزوین");
		state.add("قم");
		state.add("کردستان");
		state.add("کرمان");
		state.add("کرمانشاه");
		state.add("کهگیلویه و بویر احمد");
		state.add("گلستان");
		state.add("گیلان");
		state.add("لرستان");
		state.add("مازندران");		
		state.add("مرکزی");
		state.add("هرمزگان");
		state.add("همدان");
		state.add("یزد");

		return state;
	}

}
