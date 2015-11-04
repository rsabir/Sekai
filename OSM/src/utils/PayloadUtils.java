package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PayloadUtils {
	private boolean isServer;
	private int lon;
	private int lat;
	private int[] gps;
	private String id;
	PayloadUtils(String jsonString) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		JSONObject payloadJSON =(JSONObject)jsonParser.parse(jsonString);
		id = (String) ((JSONObject)payloadJSON.get("client")).get("ID");
		JSONObject poisitionJSON = (JSONObject) ((JSONObject)payloadJSON.get("client")).get("Position");
		lat = (int) poisitionJSON.get("lat");
		lon = (int) poisitionJSON.get("lon");
		gps = new int[2];
		gps[0]=lat;gps[1]=lon;
	}
	public int[] getGps() {
		return gps;
	}
	public boolean isServer() {
		return isServer;
	}
	public int getLon() {
		return lon;
	}
	public int getLat() {
		return lat;
	}
	public String getId() {
		return id;
	}
}
