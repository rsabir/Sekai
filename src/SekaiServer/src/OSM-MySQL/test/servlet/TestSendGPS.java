package servlet;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import server.TestServerServer;
import utils.HttpSendRequest;

public class TestSendGPS {

	@Test
	public void testSendByClient() {
		TestServerServer.before();
		String jsonString = TestServerServer.payload;
		JSONParser jsonParser = new JSONParser();
		/*
		try {
			HttpSendRequest.sendPost("http://localhost:8080/OSM/SendGPS",(JSONObject)jsonParser.parse(jsonString));
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

}
