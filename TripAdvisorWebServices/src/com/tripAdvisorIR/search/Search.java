package com.tripAdvisorIR.search;

import java.util.Set;

import com.tripAdvisor.convert.ConvertToJson;
import com.tripAdvisor.websites.AirplainTicket;
import com.tripAdvisor.websites.Alibaba;
import com.tripAdvisor.websites.AriaBooking;
import com.tripAdvisor.websites.EGardesh;
import com.tripAdvisor.websites.Eghamat24;
import com.tripAdvisor.websites.EliGasht;
import com.tripAdvisor.websites.Hamgardi;
import com.tripAdvisor.websites.HotelYar;
import com.tripAdvisor.websites.IrTour;
import com.tripAdvisor.websites.IranHotelOnline;
import com.tripAdvisor.websites.Parvaziran;
import com.tripAdvisor.websites.PinTaPin;
import com.tripAdvisor.websites.Samtik;
import com.tripAdvisor.websites.Website;
import com.tripAdvisorIR.info.Information;
import com.tripAdvisorIR.places.Aquarium;
import com.tripAdvisorIR.places.BallSport;
import com.tripAdvisorIR.places.Cinema;
import com.tripAdvisorIR.places.Game;
import com.tripAdvisorIR.places.Handicraft;
import com.tripAdvisorIR.places.HorseRiding;
import com.tripAdvisorIR.places.IntTagInfo;
import com.tripAdvisorIR.places.Island;
import com.tripAdvisorIR.places.MountainClimbing;
import com.tripAdvisorIR.places.Museum;
import com.tripAdvisorIR.places.Nature;
import com.tripAdvisorIR.places.Other;
import com.tripAdvisorIR.places.Place;
import com.tripAdvisorIR.places.RacketSport;
import com.tripAdvisorIR.places.Religious;
import com.tripAdvisorIR.places.ShoppingCenter;
import com.tripAdvisorIR.places.Theater;
import com.tripAdvisorIR.places.WaterSport;
import com.tripAdvisorIR.places.Zoo;

public class Search {

	private Information information;
	private Hamgardi hamgardi;

	static Set<Place> mountainClimbings;
	static Set<Place> ballSports;
	static Set<Place> games;
	static Set<Place> horseRidings;
	static Set<Place> otherGames;
	static Set<Place> racketSports;
	static Set<Place> waterSports;
	static Set<Place> shoppingCenters;
	static Set<Place> handyCrafts;
	static Set<Place> states;
	static Set<Place> islands;
	static Set<Place> natures;
	static Set<Place> hobbies;
	static Set<Place> religouses;
	static Set<Place> cinemas;
	static Set<Place> theaters;
	static Set<Place> museums;
	static Set<Place> zoos;
	static Set<Place> aquariums;

	// constructor with field
	public Search(Information information) {
		this.information = information;
	}

	// getters for allSets

	public static Set<Place> getAquariums() {
		return aquariums;
	}

	public static Set<Place> getZoos() {
		return zoos;
	}

	public static Set<Place> getMuseums() {
		return museums;
	}

	public static Set<Place> getMountainClimbings() {
		return mountainClimbings;
	}

	public static Set<Place> getBallSports() {
		return ballSports;
	}

	public static Set<Place> getGames() {
		return games;
	}

	public static Set<Place> getHorseRidings() {

		return horseRidings;
	}

	public static Set<Place> getOtherGames() {
		return otherGames;
	}

	public static Set<Place> getRacketSports() {
		return racketSports;
	}

	public static Set<Place> getWaterSports() {
		return waterSports;
	}

	public static Set<Place> getShoppingCenters() {
		return shoppingCenters;
	}

	public static Set<Place> getHandyCrafts() {
		return handyCrafts;
	}

	public static Set<Place> getStates() {
		return states;
	}

	public static Set<Place> getIslands() {
		return islands;
	}

	public static Set<Place> getNatures() {
		return natures;
	}

	public static Set<Place> getHobbies() {
		return hobbies;
	}

	public static Set<Place> getReligouses() {
		return religouses;
	}

	public static Set<Place> getCinemas() {
		return cinemas;
	}

	public static Set<Place> getTheaters() {
		return theaters;
	}

	public void searchFlightsOnWeb() {
//		searchSamtik();
//		searchAlibaba();
		searchAirplaneTicket();
//		searchIRTour();
//		searchParvaziran();
	}
	
	private void searchParvaziran() {
		System.out.println("===== parvaziran.ir datas : =====");
		Parvaziran parvaziran = new Parvaziran(information);
		parvaziran.startScrape();
	}
	
	private void searchIRTour() {
		System.out.println("===== IrTour.ir datas : =====");
		IrTour irTour = new IrTour(information);
		irTour.startScrape();
	}
	
	private void searchAirplaneTicket() {
		AirplainTicket airplainTicket = new AirplainTicket(information);
		airplainTicket.startScrape();
	}
	
	private void searchAlibaba() {
		Alibaba alibaba = new Alibaba(information);
		alibaba.startScrape();
	}

	private void searchSamtik() {
		Samtik samtik = new Samtik(information);
		samtik.startScrape();
	}

	// search methods
	public void searchHotelsOnWeb() {
		// searchPinTaPin();
		// searchEghamat24();
		// searchAriaBooking();
		// searchHotelYar();
		searchIranHotelOnline();
		// searchEGardesh();
		// searchEliGasght();
	}

	private void searchIranHotelOnline() {
		System.out.println("==== IranHotelOnlie datas: ====");
		IranHotelOnline iranHotelOnline = new IranHotelOnline(information);
		iranHotelOnline.startScrape();
	}

	private void searchAriaBooking() {
		System.out.println("==== AriaBooking datas: ====");
		AriaBooking ariaBooking = new AriaBooking(information);
		ariaBooking.startScrape();
	}

	public void searchPlacesOnWeb() {
		searchHamgardi();
		convertToJsonPlaces();
	}

	private void convertToJsonPlaces() {
		ConvertToJson convertToJson = new ConvertToJson();
		convertToJson.toJson();
	}

	private void searchHamgardi() {
		searchSportGame();
		searchArtCulture();
		searchMuseum();
		searchZooAquarium();
		searchTrip();
		searchShoppingFashion();
		searchIranTouring();

	}

	private void searchZooAquarium() {
		System.out.println("=== Hamgardi.com: ZooAquarium ===");
		// Place zoo = new Zoo();
		// hamgardi = new Hamgardi(zoo,IntTagInfo.zoo);
		// hamgardi.startScrape();
		// zoos = hamgardi.getPlaces();
		// System.out.println("zoos size: " + zoos.size());

		Place aquarium = new Aquarium();
		hamgardi = new Hamgardi(aquarium, IntTagInfo.aquarium);
		hamgardi.startScrape();
		aquariums = hamgardi.getPlaces();
		System.out.println("aquarium size: " + aquariums.size());
	}

	private void searchMuseum() {
		System.out.println("=== Hamgardi.com: Museums ===");
		Place museum = new Museum();
		hamgardi = new Hamgardi(museum, IntTagInfo.museums);
		hamgardi.startScrape();
		museums = hamgardi.getPlaces();
		System.out.println("museums size: " + museums.size());
	}

	private void searchSportGame() {
		System.out.println("=== Hamgardi.com: SportGame ===");
		Place mountainClimbing = new MountainClimbing();
		hamgardi = new Hamgardi(mountainClimbing, IntTagInfo.mountainClimbing);
		hamgardi.startScrape();
		mountainClimbings = hamgardi.getPlaces();
		System.out.println("mountainClimbings size: " + mountainClimbings.size());

		Place horseRiding = new HorseRiding();
		hamgardi = new Hamgardi(horseRiding, IntTagInfo.horseRiding);
		hamgardi.startScrape();
		horseRidings = hamgardi.getPlaces();
		System.out.println("horseRidings size: " + horseRidings.size());

		Place ballSport = new BallSport();
		hamgardi = new Hamgardi(ballSport, IntTagInfo.ballSport);
		hamgardi.startScrape();
		ballSports = hamgardi.getPlaces();
		System.out.println("ball sports size: " + ballSports.size());

		Place game = new Game();
		hamgardi = new Hamgardi(game, IntTagInfo.game);
		hamgardi.startScrape();
		games = hamgardi.getPlaces();
		System.out.println("games size: " + games.size());

		Place other = new Other();
		hamgardi = new Hamgardi(other, IntTagInfo.otherGames);
		hamgardi.startScrape();
		otherGames = hamgardi.getPlaces();
		System.out.println("others size: " + otherGames.size());

		Place racketSport = new RacketSport();
		hamgardi = new Hamgardi(racketSport, IntTagInfo.racketSport);
		hamgardi.startScrape();
		racketSports = hamgardi.getPlaces();
		System.out.println("racketSports size: " + racketSports.size());

		Place waterSport = new WaterSport();
		hamgardi = new Hamgardi(waterSport, IntTagInfo.waterSport);
		hamgardi.startScrape();
		waterSports = hamgardi.getPlaces();
		System.out.println("water sports size: " + waterSports.size());
	}

	private void searchShoppingFashion() {
		System.out.println("=== Hamgardi.com: ShoppingFashion ===");
		Place shoppingCenter = new ShoppingCenter();
		hamgardi = new Hamgardi(shoppingCenter, IntTagInfo.shoppingCenter);
		hamgardi.startScrape();
		shoppingCenters = hamgardi.getPlaces();
		System.out.println("shopping centers size: " + shoppingCenters.size());

		Place handyCraft = new Handicraft();
		hamgardi = new Hamgardi(handyCraft, IntTagInfo.handyCraft);
		hamgardi.startScrape();
		hamgardi.getPlaces();
		handyCrafts = hamgardi.getPlaces();
		System.out.println("handy crafts size: " + handyCrafts.size());
	}

	private void searchIranTouring() {
		System.out.println("=== Hamgardi.com: IranTouring ===");
		// Place state = new State();
		// hamgardi = new Hamgardi(state, IntTagInfo.iranStates);
		// hamgardi.getStateNames();
		// hamgardi.startScrape();
		// states = hamgardi.getPlaces();
		// System.out.println("states size: " + states.size());

		Place island = new Island();
		hamgardi = new Hamgardi(island, IntTagInfo.iranIslands);
		hamgardi.startScrape();
		islands = hamgardi.getPlaces();
		System.out.println("islands size: " + islands.size());
	}

	private void searchTrip() {
		System.out.println("=== Hamgardi.com: Trip ===");
		Place nature = new Nature();
		hamgardi = new Hamgardi(nature, IntTagInfo.nature);
		hamgardi.startScrape();
		natures = hamgardi.getPlaces();
		System.out.println("natures size: " + natures.size());

		// this part should not bet activated never
		// Place hobby = new Hobby();
		// hamgardi = new Hamgardi(hobby, IntTagInfo.hobby);
		// hamgardi.startScrape();
		// hobbies = hamgardi.getPlaces();
		// System.out.println("hobbies size: " + hobbies.size());

		Place religious = new Religious();
		hamgardi = new Hamgardi(religious, IntTagInfo.religious);
		hamgardi.startScrape();
		religouses = hamgardi.getPlaces();
		System.out.println("reliouguses place: " + religouses.size());
	}

	private void searchArtCulture() {
		System.out.println("=== Hamgardi.com: ArtCulture ===");
		Place cinema = new Cinema();
		hamgardi = new Hamgardi(cinema, IntTagInfo.cinema);
		hamgardi.startScrape();
		cinemas = hamgardi.getPlaces();
		System.out.println("cinemas size: " + cinemas.size());

		Place theater = new Theater();
		hamgardi = new Hamgardi(theater, IntTagInfo.theater);
		hamgardi.startScrape();
		theaters = hamgardi.getPlaces();
		System.out.println("theaters size: " + theaters.size());
	}

	private void searchEghamat24() {
		System.out.println("==== data from eghamat24.com ====");
		Eghamat24 eghamat24 = new Eghamat24(information);
	}

	private void searchHotelYar() {
		System.out.println("==== data from hotelyar.com ====");
		HotelYar hotelYar = new HotelYar(information);
	}

	private void searchPinTaPin() {
		System.out.println("PinTaPin.com datas:");
		PinTaPin pinTaPin = new PinTaPin(information);
	}

	private void searchEGardesh() {
		System.out.println("EGardesh.com datas:");
		EGardesh eGardesh = new EGardesh(information);
	}

	private void searchEliGasht() {
		System.out.println("EliGasht datas:");
		EliGasht iranHotelOnline = new EliGasht();
	}
}
