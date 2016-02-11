package utils;

import java.util.Date;

public class Client{
	private double lat;
	private double lon;
	private String id;
	private Date date;
	
	public Client(String id,double lat,double lon,Date date){
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.date = date;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}
}