package server;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import utils.ConfigUtils;

public class TestServerServer {

	public static String payload;
	static float[] gps;
	
	
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
		gps = new float[2];
		gps[0]=26;gps[1]=22;
		
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
