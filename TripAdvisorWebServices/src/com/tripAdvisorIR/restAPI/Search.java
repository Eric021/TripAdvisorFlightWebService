package com.tripAdvisorIR.restAPI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.tripAdvisor.websites.Alibaba;
import com.tripAdvisor.websites.AriaBooking;
import com.tripAdvisor.websites.Eghamat24;
import com.tripAdvisor.websites.HotelYar;
import com.tripAdvisor.websites.IranHotelOnline;
import com.tripAdvisor.websites.PinTaPin;
import com.tripAdvisor.websites.Website;
import com.tripAdvisorIR.calendar.CalendarTool;
import com.tripAdvisorIR.dbModel.Hotel;
import com.tripAdvisorIR.errors.Error;
import com.tripAdvisorIR.info.Information;
import com.tripAdvisorIR.merge.CompareHotels;
import com.tripAdvisorIR.merge.MergeHotelsTest;
import com.tripAdvisor.websites.Ghasedak24;

import com.tripAdvisorIR.merge.MergeFlights;

import com.tripAdvisorIR.dbModel.Flight;

import com.tripAdvisorIR.merge.MergeHotels;

@Path("/search")
public class Search {
	private Information information = new Information();
	private HttpServletResponse response;
	private HttpServletRequest request;
	private static Set<Hotel> hotels = new HashSet<>();
	private static Set<Flight> flights = new HashSet<>();
	private static Set<Hotel> mergedSortedHotels = new HashSet<>();
	private static Set<Flight> mergedSortFlights = new HashSet<>();
	private static long startTime;
	private static long endTime;
	private Thread pintapinThread;
	private Thread hotelyarThread;
	private Thread ariabookingThread;
	private Thread alibabaThread;
	private Thread ghasedak24Thread;

	private Website pintapin;
	private Website hotelyar;
	private Website ariaBooking;
	private Website alibaba;
	private Website ghasedak24;

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/hotels/items/")
	public Response getHotels(@QueryParam("city") String persianCityName, @QueryParam("check_in") String checkinDate,
			@QueryParam("nights") String nights, @QueryParam("adults") String adults,
			@QueryParam("children") String children) {
		boolean isDataValid = checkDataValidation(persianCityName, checkinDate, nights, adults, children);
		// add if statement to check validation of data enterd here !!!
		if (isDataValid) {
			information = new Information(persianCityName, checkinDate, Integer.parseInt(nights),
					Integer.parseInt(adults), Integer.parseInt(children));
			mergedSortedHotels.clear();
			searchHotels();
			return Response.status(200).entity(this.hotels).build();
		} else {
			return Response.status(200).entity(new Error(Error.Bad_Request_CODE, Error.Bad_Request_FA)).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/flights/items/")
	public Response getFlights(@QueryParam("srcCity") String srcPersianCityName,
			@QueryParam("dstCity") String dstPersianCityName, @QueryParam("check_in") String checkinDate,
			@QueryParam("check_out") String checkOutDate, @QueryParam("adults") String adults,
			@QueryParam("children") String children, @QueryParam("newBorn") String newBorn) {

		Information.setIsRoundTrip(true);
		if (srcPersianCityName==null /*|| dstPersianCityName.equals(null) || checkinDate.equals(null)
				|| checkOutDate.equals(null) || adults.equals(null)*/) {
			return Response.status(206).entity(new Error(Error.Partial_Content_CODE, Error.Partial_Content_FA)).build();
		}

		if (checkOutDate != null) {

			boolean isvalid = checkFlightDataValidation(srcPersianCityName, dstPersianCityName, checkinDate,
					checkOutDate, adults, children ,newBorn);
			if (isvalid) {
				information = new Information(srcPersianCityName, dstPersianCityName, checkinDate, checkOutDate,
						Integer.parseInt(adults), 0, 0);
				mergedSortFlights.clear();
				searchFlights();

				return Response.status(200).entity(this.flights).build();
			}
		}
		if (checkOutDate==null) {
			Information.setIsRoundTrip(false);
			boolean isvalid = checkFlightDataValidation(srcPersianCityName, dstPersianCityName, checkinDate,
					checkOutDate, adults, children , newBorn);
			if (isvalid) {

				information = new Information(srcPersianCityName, dstPersianCityName, checkinDate,
						Integer.parseInt(adults), Integer.parseInt(children), Integer.parseInt(newBorn));
				mergedSortFlights.clear();
				searchFlights();
				return Response.status(200).entity(this.flights).build();
			}
		}
		return Response.status(200).entity(new Error(Error.Bad_Request_CODE, Error.Bad_Request_FA)).build();
	}

	@POST
	@Path("/info")
	@Consumes("application/x-www-form-urlencoded")
	public void info(@FormParam("faCity") String faCity, @FormParam("enCity") String enCity,
			@FormParam("dateIn") String dateIn, @FormParam("nights") int nights,
			@Context final HttpServletResponse response, @Context final HttpServletRequest request) {
		startTime = System.currentTimeMillis();
		information.setEnglishCityName(enCity);
		information.setPersianCityName(faCity);
		information.setCheckinDate(dateIn);
		information.setNights(nights);
		this.request = request;
		this.response = response;
		mergedSortedHotels.clear();
		searchHotels();
	}

	private boolean checkDataValidation(String persianCityName, String checkinDate, String nights, String adults,
			String children) {
		final String[] enNumsArray = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		final String[] faNumsArray = { "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹", "۰" };
		try {
			for (String enNumber : enNumsArray)
				if (persianCityName.contains(enNumber))
					return false;
			for (String faNumber : faNumsArray)
				if (persianCityName.contains(faNumber))
					return false;
			if (Integer.parseInt(children) < 0)
				return false;
			if (Integer.parseInt(adults) < 0)
				return false;
			if (Integer.parseInt(nights) <= 0)
				return false;
			if (checkinDateValidation(checkinDate) != 0)
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean checkFlightDataValidation(String srcPersianCityName, String dstPersianCityName, String checkinDate,
			String checkOutDate, String adults, String children, String newBorn) {
		final String[] enNumsArray = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		final String[] faNumsArray = { "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹", "۰" };
		try {
			for (String enNumber : enNumsArray) {
				if (srcPersianCityName.contains(enNumber)) {
					/*
					 * return Response.status(203).entity( new
					 * Error(Error.Non_Autherative_Information_CODE,
					 * Error.Non_Autherative_Information_FA)) .build();
					 */
					return false;
				}
			}
			for (String faNumber : faNumsArray) {
				if (srcPersianCityName.contains(faNumber)) {
					return false;
				}
			}
			if (Integer.parseInt(children) < 0)
				return false;

			if (Integer.parseInt(adults) < 0)
				return false;
			if (checkinDateValidation(checkinDate) != 0)
				return false;

			if (Information.getIsRoundTrip() == true) {
				if (checkoutDateValidation(checkinDate) != 0)
					return false;
			}
			if (Information.getIsRoundTrip() == false) {
				checkOutDate = null;
				return true;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private int checkinDateValidation(String checkinDate) {
		try {
			LocalDate localDate = LocalDate.now();
			String[] dateParts = DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate).split("/");
			CalendarTool calendarTool = new CalendarTool(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]),
					Integer.parseInt(dateParts[2]));
			int thisYear = calendarTool.getIranianYear();
			int thisMonth = calendarTool.getIranianMonth();
			int thisday = calendarTool.getIranianDay();

			if (!checkinDate.contains("/"))
				return 1;
			int counter = 0;
			for (int i = 0; i < checkinDate.length(); i++) {
				if (checkinDate.charAt(i) == '/')
					counter++;
			}
			if (counter != 2)
				return 1;
			String[] parts = checkinDate.split("/");
			if (Integer.parseInt(parts[0]) < thisYear)
				return 1;
			if (Integer.parseInt(parts[1]) < thisMonth | Integer.parseInt(parts[1]) > 13)
				return 1;
			if (Integer.parseInt(parts[1]) == thisMonth)
				if (Integer.parseInt(parts[2]) < thisday)
					return 1;
			if (Integer.parseInt(parts[2]) > 31)
				return 1;
			return 0;
		} catch (Exception e) {
			return 1;
		}

	}

	private int checkoutDateValidation(String checkoutDate) {
		try {
			LocalDate localDate = LocalDate.now();
			String[] dateParts = DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate).split("/");
			CalendarTool calendarTool = new CalendarTool(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]),
					Integer.parseInt(dateParts[2]));
			int thisYear = calendarTool.getIranianYear();
			int thisMonth = calendarTool.getIranianMonth();
			int thisday = calendarTool.getIranianDay();

			if (!checkoutDate.contains("/"))
				return 1;
			int counter = 0;
			for (int i = 0; i < checkoutDate.length(); i++) {
				if (checkoutDate.charAt(i) == '/')
					counter++;
			}
			if (counter != 2)
				return 1;
			String[] parts = checkoutDate.split("/");
			if (Integer.parseInt(parts[0]) < thisYear)
				return 1;
			if (Integer.parseInt(parts[1]) < thisMonth | Integer.parseInt(parts[1]) > 13)
				return 1;
			if (Integer.parseInt(parts[1]) == thisMonth)
				if (Integer.parseInt(parts[2]) < thisday)
					return 1;
			if (Integer.parseInt(parts[2]) > 31)
				return 1;
			return 0;
		} catch (Exception e) {
			return 1;
		}

	}

	public void searchHotels() {
		List<Hotel> pintapinHotels = null;
		List<Hotel> hotelyarHotels = null;
		List<Hotel> ariabookingHotels = null;
		// List<Hotel> eghamat24Hotels = null;
		// List<Hotel> iranHotelOnlineHotels = null;
		try {
			pintapin = new PinTaPin(information);
			pintapinThread = new Thread(pintapin);
			pintapinThread.start();

			hotelyar = new HotelYar(information);
			hotelyarThread = new Thread(hotelyar);
			hotelyarThread.start();

			ariaBooking = new AriaBooking(information);
			ariabookingThread = new Thread(ariaBooking);
			ariabookingThread.start();

			// searchEghamat24();
			// searchIranHotelOnline();
			pintapinThread.join();
			hotelyarThread.join();
			ariabookingThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		pintapinHotels = pintapin.getHoltes();
		ariabookingHotels = ariaBooking.getHoltes();
		hotelyarHotels = hotelyar.getHoltes();

		// eghamat24Hotels and iranHotelOnlineHotels did not added to method
		gatherAllResultsToOneSet(pintapinHotels, hotelyarHotels, ariabookingHotels);
		mergeResults();
		// sendRedirect();
		// getHotels();
	}

	public void searchFlights() {

		System.out.println("entered search flights");
		List<Flight> ghasedak24Flights = null;
		List<Flight> alibabaFlights = null;

		alibaba = new Alibaba(information);
		alibabaThread = new Thread(alibabaThread);
		alibaba.run();

		ghasedak24 = new Ghasedak24(information);
		ghasedak24Thread = new Thread(ghasedak24Thread);
		ghasedak24.run();
		// ghasedak24Thread = new Thread(ghasedak24Thread);
		// ghasedak24Thread.start();

		// ghasedak24Thread.join();
		// alibabaThread.join();

		ghasedak24Flights = ghasedak24.getFlights();
		// alibabaFlights = alibaba.getFlights();

		gatherAllFlightResultsToOneSet(ghasedak24Flights, alibabaFlights);
		mergeFlight();
	}

	private void sendResponse() throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
	}

	private void sendRedirect() {
		try {
			response.sendRedirect(("http://localhost:8080/TripAdviserJsoup/rest/search/hotels/items"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void gatherAllResultsToOneSet(List<Hotel> pintapinHotels, List<Hotel> hotelyarHotels,
			List<Hotel> ariabookingHotels) {
		if (pintapinHotels != null) {
			for (Hotel hotel : pintapinHotels) {
				this.hotels.add(hotel);
			}
		}

		if (hotelyarHotels != null) {
			for (Hotel hotel : hotelyarHotels) {
				this.hotels.add(hotel);
			}
		}

		if (ariabookingHotels != null) {
			for (Hotel hotel : ariabookingHotels) {
				this.hotels.add(hotel);
			}
		}
	}

	private void gatherAllFlightResultsToOneSet(List<Flight> ghasedak24Flights, List<Flight> alibabaFlights) {
		if (alibabaFlights != null) {
			for (Flight flight : alibabaFlights) {
				this.flights.add(flight);
			}
		}

		if (ghasedak24Flights != null) {
			for (Flight flight : ghasedak24Flights) {
				this.flights.add(flight);
			}
		}
	}

	private void searchPinTaPin() throws InterruptedException {

	}

	private void searchHotelyar() throws InterruptedException {

	}

	private void searchAriaBooking() throws InterruptedException {

	}

	private List<Hotel> searchEghamat24() throws InterruptedException {
		Website eghamat24 = new Eghamat24(information);
		Thread eghamat24Thread = new Thread(eghamat24);
		eghamat24Thread.start();
		eghamat24Thread.join();
		return eghamat24.getHoltes();
	}

	private List<Hotel> searchIranHotelOnline() throws InterruptedException {
		Website iranHotelOnline = new IranHotelOnline(information);
		Thread iranHotelOnlineThread = new Thread(iranHotelOnline);
		iranHotelOnlineThread.start();
		iranHotelOnlineThread.join();
		return iranHotelOnline.getHoltes();
	}

	private void mergeResults() {
		long startTime = System.currentTimeMillis();
		MergeHotels mergeHotels = new MergeHotels(hotels);
		mergeHotels.startMerge();
		this.hotels.clear();
		this.hotels = mergeHotels.getMergedHotels();
		long endTime = System.currentTimeMillis();
	}

	private void mergeFlight() {
		long startTime = System.currentTimeMillis();
		MergeFlights mergeFlights = new MergeFlights(flights);
		mergeFlights.startMerge();
		this.flights.clear();
		this.flights = mergeFlights.getMergedFlights();
		long endTime = System.currentTimeMillis();
		System.err.println("merge time: " + (endTime - startTime));
	}

	private void sort() {
		List<Hotel> hotelsList = new ArrayList<>();
		for (Hotel hotel : hotels) {
			hotelsList.add(hotel);
		}
		Collections.sort(hotelsList, new CompareHotels());

		for (Hotel hotel : hotelsList) {
			mergedSortedHotels.add(hotel);
		}
	}

	private void mergeSort() {
		MergeHotelsTest mergeHotels = new MergeHotelsTest();
		List<Hotel> hotelsList = new ArrayList<>();
		for (Hotel hotel : hotels) {
			hotelsList.add(hotel);
		}
		mergeHotels.mergeSort(hotelsList);
		File file = new File("/home/amir/Desktop/mergeLog.log");
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			for (Hotel hotel : mergeHotels.getMergedSortedHotels()) {
				System.out.println(hotel.toJson());
				writer.append(hotel.toJson() + "\n\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("new comparator result");
		Collections.sort(hotelsList, new CompareHotels());
		for (Hotel hotel : hotelsList) {
			System.out.println(hotel.toJson());
		}

		System.out.println("merge method in Search class ended");
	}

}
