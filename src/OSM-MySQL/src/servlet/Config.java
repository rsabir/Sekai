package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import constants.PathsC;
import constants.Urls;

/**
 * Servlet implementation class Config
 */
@WebServlet("/Config")
public class Config extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Config() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = PathsC.PATHCONFIGJSON;
		String jsonString = readFile( path ); 
		//"{\"servers\":["+
//	             "{"+
//	                "\"zone\":{"+
//	                       "\"maxlat\": 44.8448769,"+
//	                       "\"minlat\": 44.8305549,"+
//	                      " \"maxlon\": -0.5275520000000142,"+
//	                       "\"minlon\": -0.6563579999999547"+
//	                "},"+
//	                "\"host\":\"172.20.0.43\""+
//	             "},{"+
//		             "\"zone\":{"+
//		                 "\"maxlat\": 44.8448769,"+
//		                 "\"minlat\": 44.8305549,"+
//		                " \"maxlon\": -0.6,"+
//		                 "\"minlon\": -0.7"+
//                 	"},"+
//                 	"\"host\":\"172.20.0.45\""+
//	                "},{"+
//	                "\"zone\":{"+
//	                 "\"maxlat\": 44.9448769,"+
//	                 "\"minlat\": 44.8448769,"+
//	                " \"maxlon\": -0.6,"+
//	                 "\"minlon\": -0.7"+
//            	"},"+
//            		"\"host\":\"172.20.0.46\""+
//            		"},{"+
//	                "\"zone\":{"+
//	                 "\"maxlat\": 44.9448769,"+
//	                 "\"minlat\": 44.8448769,"+
//	                " \"maxlon\": -0.7,"+
//	                 "\"minlon\": -0.8"+
//            	"},"+
//            		"\"host\":\"172.20.0.47\""+
//	             "}]}";
//		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonResponse;
		try {
			jsonResponse = (JSONObject) jsonParser.parse(jsonString);
			request.setCharacterEncoding("utf8");
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonResponse);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static String readFile(String filename) {
	    String result = "";
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(filename));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	        result = sb.toString();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}

}