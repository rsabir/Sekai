package utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestConfigUtils {

	static String jsonString;
	@Before
	public void beforeTests(){
		before();
	}
	
	
	public static void before(){
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
	
	@Test
	public void testParse() {
		ArrayList<ArrayList<Object>> result = ConfigUtils.parse(jsonString);
		assert(result.size()==3);
		/*assert((int)result.get(0).get(0)==30);
		assert((int)result.get(0).get(1)==0);
		assert((int)result.get(0).get(2)==40);
		assert((int)result.get(0).get(3)==20);
		assert ( ( (String) result.get(0).get(4) ).equals("1.2.3.4") );
		assert((int)result.get(1).get(0)==100);
		assert((int)result.get(1).get(1)==30);
		assert((int)result.get(1).get(2)==20);
		assert((int)result.get(1).get(3)==0);
		assert ( ( (String) result.get(1).get(4) ).equals("1.2.3.5") );		*/
	}

	@Test
	public void testFindResponsibleServer(){
		ArrayList<ArrayList<Object>> result = ConfigUtils.parse(jsonString);
		float[] gps = {1,21};
		assert(ConfigUtils.findResponsibleServer(gps, result).size()==1);
		assert(ConfigUtils.findResponsibleServer(gps, result).get(0).equals("1.2.3.4"));
		gps[1] = 1;
		assert(ConfigUtils.findResponsibleServer(gps, result).size()==0);
		gps[0]=35;gps[1]=12;
		assert(ConfigUtils.findResponsibleServer(gps, result).size()==1);
		assert(ConfigUtils.findResponsibleServer(gps, result).get(0).equals("1.2.3.5"));
		gps[0]=26;gps[1]=22;
		assert(ConfigUtils.findResponsibleServer(gps, result).size()==2);
		assert(ConfigUtils.findResponsibleServer(gps, result).get(0).equals("1.2.3.4"));
		assert(ConfigUtils.findResponsibleServer(gps, result).get(0).equals("1.2.3.6"));
	}	

}
