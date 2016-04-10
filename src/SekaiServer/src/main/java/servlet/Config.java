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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.PathsC;
import constants.Urls;

/**
 * Servlet implementation class Config
 */
@WebServlet("/Config")
public class Config extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(Config.class);
	private static Logger httpLogger = LoggerFactory.getLogger("http");
       
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
		String path = PathsC.PATHCONFIGJSON;
		String jsonString = readFile( path ); 
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonResponse;
		httpLogger.debug("/Config was requested by "+request.getRemoteAddr());
		try {
			jsonResponse = (JSONObject) jsonParser.parse(jsonString);
			request.setCharacterEncoding("utf8");
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonResponse);
		} catch (ParseException e) {
			logger.error("Error happened while requesting /Config");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private static String readFile(String filename) {
	    String result = "";
	    logger.debug("Reading the file "+filename);
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
	    	logger.error("Error occured while reading the file "+filename);
	    	logger.error(e.getMessage());
	    }
	    return result;
	}

}