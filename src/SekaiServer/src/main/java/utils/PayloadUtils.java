package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PayloadUtils {
	private boolean isServer;
	private GPS gps;
	private String id;
	private String ip;
	public final static String KEY_ID="ID";
	public final static String KEY_CLIENT="client";
	public final static String KEY_LATITUDE="lat";
	public final static String KEY_LONGITUDE="lon";
	public final static String KEY_CLIENT_POSITION="position";
	public final static String KEY_ISSERVER="isServer";
	public final static String KEY_IP="ip";
	public PayloadUtils(String jsonString) throws ParseException{
		JSONParser jsonParser = new JSONParser();
		JSONObject payloadJSON =(JSONObject)jsonParser.parse(jsonString);
		id = ((JSONObject)payloadJSON.get(KEY_CLIENT)).get(KEY_ID).toString();
		JSONObject positionJSON = (JSONObject) ((JSONObject)payloadJSON.get(KEY_CLIENT)).get(KEY_CLIENT_POSITION);
		float lat = Float.parseFloat(String.valueOf(positionJSON.get(KEY_LATITUDE)));
		float lon = Float.parseFloat(String.valueOf(positionJSON.get(KEY_LONGITUDE)));
		//isServer = (boolean) payloadJSON.get("isServer");
		isServer = Boolean.valueOf(payloadJSON.get(KEY_ISSERVER).toString());
		if (isServer == true)
			ip = payloadJSON.get(KEY_IP).toString();
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
	public String getIp() {
		return ip;
	}
	@Override
	public String toString(){
		return "lat:"+getLat()+", lon:"+getLon()+", id client="+getId();
	}
}