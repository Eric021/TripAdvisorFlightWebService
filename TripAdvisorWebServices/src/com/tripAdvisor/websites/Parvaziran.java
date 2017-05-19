package com.tripAdvisor.websites;

import org.jsoup.Jsoup;

import java.io.IOException;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.tripAdvisorIR.info.Information;

public class Parvaziran extends Website{
	
	public Parvaziran(Information information) {
		super(information);
	}
	
	@Override
	public void startScrape() {
		String url = "http://parvaziran.ir/api/flight/getalloneway/"
				+ "?flightType=charter&source=THR"
				+ "&destination=KIH&personCount=1&flightDate=1396/02/25";
		
		try {
			Response response = Jsoup.connect(url).userAgent("Mozilla").method(Method.GET).ignoreContentType(true).execute();
			response.charset("UTF-8");
			System.out.println(response.body());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
