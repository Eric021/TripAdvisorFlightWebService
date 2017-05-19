package com.tripAdvisorIR.review;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class Review {

	private String id;
	private int userId;
	private boolean isLiked = true;
	private boolean disLiked = false;
	private boolean report = false;
	private int vote = 0;
	private ArrayList<Integer> votes=new ArrayList<>();
	private int review;
	private String date;
	private String comment;
	private boolean isConfirmed=true;

	public int ifIsLiked() {
		if (this.isLiked) {
			vote++;
			votes.add(this.vote);
		} else {
			this.disLiked = true;
			vote--;
			votes.add(this.vote);
		}
		return this.vote;
	}

	/*public void rating() {
		votes.size()
	}
	
	/*public boolean confirmCommnet()
	{
		
	}

	/*
	 * public void report() { if(!this.report) {
	 * 
	 * } }
	 */

	public void Date() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); // 2016/11/16 12:08:43

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
