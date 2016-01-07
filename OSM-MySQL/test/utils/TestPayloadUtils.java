package utils;

import static org.junit.Assert.*;

import org.json.simple.parser.ParseException;
import org.junit.Test;

public class TestPayloadUtils {

	@Test
	public void test(){
		String payload = "{ \"client\":"+
				  "{\"ID\" : \"value\","+
				   "\"Position\" : "+
				    "    {\"lat\" : 26"+
				     "    \"lon\" : 22}"+
				  "}"+
				 "\"isServer\" : true"+
				"}";
		PayloadUtils payloadUtils;
		try {
			payloadUtils = new PayloadUtils(payload);
			assert(payloadUtils.getLon()==22);
			assert(payloadUtils.getLat()==26);
			assert(payloadUtils.isServer()==true);
			float[] gps = {22,26};
			float[] gpsJSON = payloadUtils.getGps();
			assert( gps[0]==gpsJSON[0] && gps[1]==gpsJSON[1] );
		} catch (ParseException e) {
			e.printStackTrace();
			assert(false);
		}
		
		
	}

}
