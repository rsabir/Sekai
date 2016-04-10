package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PayloadUtils {
	private boolean isServer;
	private GPS gps;
	private String id;
	public PayloadUtils(String jsonString) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		JSONObject payloadJSON =(JSONObject)jsonParser.parse(jsonString);
		id = ((JSONObject)payloadJSON.get("client")).get("ID").toString();
		JSONObject positionJSON = (JSONObject) ((JSONObject)payloadJSON.get("client")).get("Position");
		float lat = Float.parseFloat(String.valueOf(positionJSON.get("lat")));
		float lon = Float.parseFloat(String.valueOf(positionJSON.get("lon")));
		//isServer = (boolean) payloadJSON.get("isServer");
		isServer = Boolean.valueOf(payloadJSON.get("isServer").toString());
		gps = new GPS(lat,lon);
	}
	public GPS getGps() {
		return gps;
	}
	public boolean isServer() {
		return isServer;
	}
	public float getLon() {
		return gps.getLon();
	}
	public float getLat() {
		return gps.getLat();
	}
	public String getId() {
		return id;
	}
}