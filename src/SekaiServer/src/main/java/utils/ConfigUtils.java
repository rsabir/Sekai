package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.SchedulingConfiguration;

import constants.Urls;

public class ConfigUtils {
	
	private static String configString; 
	private static long lastTimeGotten = 0;
	private final static long INTERVAL_UPDATE = 5000;
	private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
	
	public static String getConfig() throws ParseException, IOException{
		return getConfig(Urls.CONFIGSERVER);
	}
	public static String getConfig(String url) throws ParseException, IOException{
		long now = (new Date()).getTime();
		if ( now - lastTimeGotten > ConfigUtils.INTERVAL_UPDATE){
			logger.debug("Getting the config from"+url);
			configString = HttpSendRequest.sendGET(url);
			lastTimeGotten = now;
		}
		return configString;	
	}
	
	/**
	 * 
	 * @param configJson le configuration en json mode string  
	 * @return ArrayList composé ainsi :
	 * [ [23,   22,   0.3,    0.1,  "192.169.1.2"] , [...] ]
	 * 	   ^     ^     ^       ^             ^
	 * 	maxLat minLat  maxLgn minLgn      IP
 	 */
	public static ArrayList<ArrayList<Object>> parse(String configJson){
		logger.debug("Parsing the config : "+configJson);
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
			logger.error("Error occured while parsing the config"+configJson);
			logger.error(e.getMessage());
			return null;
		}
	}
	/**
	 * 
	 * @param gps un tableau [lat,lgn] contenant les coordonnées du point
	 * @param config l'ArrayList retourné par la fonction parse
	 * @return ArrayList contenant tout les ip des serveurs reponsables du point
	 */
	public static ArrayList<String> findResponsibleServer(GPS gps,ArrayList<ArrayList<Object>> config){
		ArrayList<String> result = new ArrayList<String>();
		for (int i=0; i<config.size(); i++){
			ArrayList<Object> tmp = config.get(i);
			if (gps.getLat()>Float.parseFloat(tmp.get(0).toString()) || gps.getLat()<Float.parseFloat(tmp.get(1).toString())  || 
					gps.getLon()>Float.parseFloat(tmp.get(2).toString())  || gps.getLon()<Float.parseFloat(tmp.get(3).toString()) )
				continue;
			result.add(tmp.get(4).toString());
		}
		return result;
	}
	
	public static ArrayList<String> getServers(ArrayList<ArrayList<Object>> config){
		ArrayList<String> result = new ArrayList<String>();
		for (int i=0; i<config.size(); i++){
			ArrayList<Object> tmp = config.get(i);
			result.add(tmp.get(4).toString());
		}
		return result;
	}
	

}
