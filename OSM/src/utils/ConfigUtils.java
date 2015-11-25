package utils;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigUtils {
	
	public static String getConfig(String url) throws ParseException, IOException{	
		String response = HttpSendRequest.sendGET(url);
		return response;
		
		// TestConfigUtils.before();
		// return TestConfigUtils.jsonString;
	}
	
	public static ArrayList<ArrayList<Object>> parse(String configJson){
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonRoot = (JSONObject) parser.parse(configJson);
			ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>> ();
			JSONArray servers = (JSONArray) jsonRoot.get("servers");
			for (int i=0; i<servers.size(); i++){
				JSONObject tmpJSON = (JSONObject) servers.get(i);
				JSONObject tmpZone = (JSONObject) tmpJSON.get("zone");
				ArrayList<Object> tmp = new ArrayList<Object> ();
				tmp.add(0,tmpZone.get("maxlat"));
				tmp.add(1,tmpZone.get("minlat"));
				tmp.add(2,tmpZone.get("maxlon"));
				tmp.add(3,tmpZone.get("minlon"));
				tmp.add(4,tmpJSON.get("host"));
				result.add(tmp);
			}
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<String> findResponsibleServer(float[]gps,ArrayList<ArrayList<Object>> config){
		ArrayList<String> result = new ArrayList<String>();
		for (int i=0; i<config.size(); i++){
			ArrayList<Object> tmp = config.get(i);
			if (gps[0]>Float.parseFloat(tmp.get(0).toString()) || gps[0]<Float.parseFloat(tmp.get(1).toString())  || 
					gps[1]>Float.parseFloat(tmp.get(2).toString())  || gps[1]<Float.parseFloat(tmp.get(3).toString()) )
				continue;
			result.add(tmp.get(4).toString());
		}
		return result;
	}

}
