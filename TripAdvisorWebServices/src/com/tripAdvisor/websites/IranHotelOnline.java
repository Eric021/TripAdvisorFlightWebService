package com.tripAdvisor.websites;

import java.io.IOException;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.tripAdvisorIR.info.Information;

public class IranHotelOnline extends Website {
	public IranHotelOnline(Information information) {
		super(information);
	}
	
	@Override
	public void startScrape() {
		try {
//			Document document = Jsoup.connect("https://www.iranhotelonline.com/Persian/Cities/")
//					.header("Host", "www.iranhotelonline.com")
//					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0")
//					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//					.header("Accept-Language", "en-US,en;q=0.5")
//					.header("Accept-Encoding", "gzip, deflate, br")
//					.referrer("https://www.iranhotelonline.com/")
//					.header("Connection", "keep-alive")
//					.header("Upgrade-Insecure-Requests", "1")
//					.method(Method.GET)
//					.get();
			
			Response response = Jsoup.connect("https://www.iranhotelonline.com/Persian/Cities/")
					.header("Host", "www.iranhotelonline.com")
					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Language", "en-US,en;q=0.5")
					.header("Accept-Encoding", "gzip, deflate, br")
					.referrer("https://www.iranhotelonline.com/")
					.header("Connection", "keep-alive")
					.header("Upgrade-Insecure-Requests", "1")
					.method(Method.GET)
					.execute();
			
			System.out.println(response.body());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
