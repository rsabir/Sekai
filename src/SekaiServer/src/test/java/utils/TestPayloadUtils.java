package utils;

import static org.junit.Assert.*;

import org.json.simple.parser.ParseException;
import org.junit.Test;
import utils.PayloadUtils;
import utils.GPS;

public class TestPayloadUtils {

	@Test
	public void test(){
		String payload = "{ \"client\":"+
				  "{\"ID\" : \"value\","+
				   "\"Position\" : "+
				    "    {\"lat\" : 26"+
				     "    \"lon\" : 22}"+
				  "},"+
				 "\"isServer\" : true"+
				"}";
		PayloadUtils payloadUtils;
		try {
			payloadUtils = new PayloadUtils(payload);
			assert(payloadUtils.getLat()==26);
			assert(payloadUtils.getLon()==22);
			assert(payloadUtils.isServer()==true);
			GPS gps = new GPS(26,22);
			GPS gpsJSON = payloadUtils.getGps();
			assert( gps.getLat()==gpsJSON.getLat() && gps.getLon()==gps.getLon() );
		} catch (ParseException e) {
			e.printStackTrace();
			assert(false);
		}
		
		
	}

}
