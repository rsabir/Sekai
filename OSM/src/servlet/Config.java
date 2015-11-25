package servlet;

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
		String jsonString =  "{\"servers\":["+
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

}
