package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PayloadUtils {
	private boolean isServer;
	private float lon;
	private float lat;
	private float[] gps;
	private String id;
	public PayloadUtils(String jsonString) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		JSONObject payloadJSON =(JSONObject)jsonParser.parse(jsonString);
		id = (String) ((JSONObject)payloadJSON.get("client")).get("ID");
		JSONObject poisitionJSON = (JSONObject) ((JSONObject)payloadJSON.get("client")).get("Position");
		lat = Float.parseFloat(String.valueOf(poisitionJSON.get("lat")));
		lon = Float.parseFloat(String.valueOf(poisitionJSON.get("lon")));
		isServer = (boolean) payloadJSON.get("isServer");
		gps = new float[2];
		gps[0]=lat;gps[1]=lon;
	}
	public float[] getGps() {
		return gps;
	}
	public boolean isServer() {
		return isServer;
	}
	public float getLon() {
		return lon;
	}
	public float getLat() {
		return lat;
	}
	public String getId() {
		return id;
	}
}
