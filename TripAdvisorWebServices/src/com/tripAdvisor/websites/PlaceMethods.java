package com.tripAdvisor.websites;

import java.awt.Image;

import org.jsoup.nodes.Document;

import com.tripAdvisor.location.Coordinate;

public interface PlaceMethods {
	String makeURL(int i);
	Coordinate findCoord(Document doc);
	String findAddress(Document doc);
	Image findImage();
	String findDescription(Document doc);
	double getLat(String coordString);
	double getLng(String coordString);
	String findLink();
	void findPlaces();
	String findName();
	int findAllPages();
	void startScrape();
	String findCity(Document doc);
	String findState(Document doc);
}
