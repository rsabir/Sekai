package utils;

public class GPS{
	private float lon;
	private float lat;
	public GPS(float lat, float lon){
		this.lat = lat;
		this.lon = lon;
	}
	public float getLon() {
		return lon;
	}
	public float getLat() {
		return lat;
	}
}