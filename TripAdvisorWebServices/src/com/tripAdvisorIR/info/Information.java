package com.tripAdvisorIR.info;

import com.tripAdvisorIR.calendar.CalendarTool;



public class Information {
	
	private String englishCityName;
	private String persianCityName;
	private String checkinDate;
	private String checkOutDate;
	private int nights;
	private int adults;
	private int children;
	private String srcPersianCityName;
	private static boolean isRoundTrip = false;
	private int newBorns;
	
	public Information() {
	
	}

	public Information(String persianCityName , String checkinDate, int nights, int adults, int children) {
		this.persianCityName = persianCityName;
		this.checkinDate = checkinDate;
		this.nights = nights;
		this.adults = adults;
		this.children = children;
	}
	
//	public Information(String englishCityName, String persianCityName, String srcPersianCityName ,String checkinDate, int nights, int adults,
//			int children) {
//		this.englishCityName = englishCityName;
//		this.persianCityName = persianCityName;
//		this.checkinDate = checkinDate;
//		this.nights = nights;
//		this.adults = adults;
//		this.children = children;
//		this.srcPersianCityName = srcPersianCityName;
//	}
	
	
	//ONE WAY FLIGHTS 
	public Information(String srcPersianCityName, String persianCityName , String checkinDate,String checkOutDate, int adults, int children,
			int newBorn) {
		this.persianCityName = persianCityName;
		this.srcPersianCityName = srcPersianCityName;
		this.checkinDate = checkinDate;
		this.setCheckOutDate(checkOutDate);
		this.adults = adults;
		this.children = children;
		this.newBorns = newBorn;
	}
	
	//TWO WAY FLIGHTS
	public Information(String srcPersianCityName, String persianCityName , String checkinDate, int adults, int children,
			int newBorn) {
		this.persianCityName = persianCityName;
		this.srcPersianCityName = srcPersianCityName;
		this.checkinDate = checkinDate;
		this.adults = adults;
		this.children = children;
		this.newBorns = newBorn;
	}
	public String getChechoutDate() {
		CalendarTool calendarTool = new CalendarTool();
		calendarTool.setIranianDate(checkinDate);
		calendarTool.nextDay(nights);
		int day = calendarTool.getIranianDay();
		int month = calendarTool.getIranianMonth();
		String checkoutDate = calendarTool.getIranianYear() + "/";
		if (month < 10) {
			checkoutDate += "0"+month + "/";
		} else {
			checkoutDate += month + "/";
		}
		if (day < 10) {
			checkoutDate += "0" + day;
		} else {
			checkoutDate += day;
		}
		return checkoutDate;
	}

	public String getEnglishCityName() {
		return englishCityName;
	}

	public void setEnglishCityName(String englishCityName) {
		this.englishCityName = englishCityName;
	}

	public String getPersianCityName() {
		return persianCityName;
	}

	public void setPersianCityName(String persianCityName) {
		this.persianCityName = persianCityName;
	}

	public String getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(String checkinDate) {
		this.checkinDate = checkinDate;
	}

	public int getNights() {
		return nights;
	}

	public void setNights(int nights) {
		this.nights = nights;
	}

	public int getAdults() {
		return adults;
	}

	public void setAdults(int adults) {
		this.adults = adults;
	}

	public int getChildren() {
		return children;
	}

	public void setChildren(int children) {
		this.children = children;
	}

	public String getSrcPersianCityName() {
		return srcPersianCityName;
	}

	public void setSrcPersianCityName(String srcPersianCityName) {
		this.srcPersianCityName = srcPersianCityName;
	}
	
	public static Boolean getIsRoundTrip() {
		return isRoundTrip;
	}

	public static void setIsRoundTrip(Boolean isRoundTrip) {
		Information.isRoundTrip = isRoundTrip;
	}
	
	public int getNewBorns() {
		return newBorns;
	}

	public void setNewBorns(int newBorns) {
		this.newBorns = newBorns;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	
	
}
