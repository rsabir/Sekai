package server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import constants.Urls;
import utils.ConfigUtils;

public class TestServerClient {

	public static String jsonString;
	 
	public static void before(){
		Urls.IP="1.2.3.4";
		jsonString =  "{\"servers\":["+
	             "{"+
	                "\"zone\":{"+
	                       "\"maxlat\": 30,"+
	                       "\"minlat\": 0,"+
	                      " \"maxlon\": 40,"+
	                       "\"minlon\": 20"+
	                "},"+
	                "\"host\":\"1.2.3.4\""+
	             "},{"+
	                "\"zone\":{"+
	                       "\"maxlat\": 100,"+
	                       "\"minlat\": 30,"+
	                       "\"maxlon\": 20,"+
	                       "\"minlon\": 0"+
	                "},"+
	                "\"host\":\"1.2.3.5\""+
	                "},{"+
	                "\"zone\":{"+
	                       "\"maxlat\": 30,"+
	                       "\"minlat\": 25,"+
	                       "\"maxlon\": 25,"+
	                       "\"minlon\": 20"+
	                "},"+
	                "\"host\":\"1.2.3.6\""+
	             "}]}";
	}
	
	@Before
	public void beforeTest(){
		before();
	}
	
	@Test
	public void testgetZoneInfo() {
		ServerClient.adressConfig = "http://10.161.61.0";
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					ServerClient.getZoneInfo();
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
			
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					ServerClient.getZoneInfo("http://10.161.61.0");
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
			
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testIsInCharge(){
		float[] gps = {25,25};
		ArrayList <String> servers = ConfigUtils.findResponsibleServer(gps, ConfigUtils.parse(jsonString));
		assert(ServerClient.isInCharge(servers)==true);
		gps[0]=150;
		assert(ServerClient.isInCharge(servers)==false);
		gps[0]=40;gps[1]=15;
	}

}
