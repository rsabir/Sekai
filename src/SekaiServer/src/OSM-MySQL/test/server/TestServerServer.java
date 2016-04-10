package server;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import utils.ConfigUtils;
import utils.GPS;

public class TestServerServer {

	public static String payload;
	static GPS gps;
	
	
	public static void before(){
		TestServerClient.before();
		payload = "{ \"client\":"+
		  "{\"ID\" : \"value\","+
		   "\"Position\" : "+
		    "    {\"lat\" : 26"+
		     "    \"lon\" : 22}"+
		  "}"+
		 "\"isServer\" : true"+
		"}";
		gps = new GPS(26,22);
		
	}
	
	@Before
	public void beforeTest(){
		before();
	}
	
	@Test
	public void testNotifyAdd() {
		ArrayList <String> servers = ConfigUtils.findResponsibleServer(
				gps, ConfigUtils.parse(TestServerClient.jsonString)
				);
		JSONParser jsonparser = new JSONParser();
		try {
			assert(ServerServer.notifyAdd((JSONObject)jsonparser.parse(payload),servers)==1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

}
