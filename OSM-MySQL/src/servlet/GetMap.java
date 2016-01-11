package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import constants.Urls;

/**
 * Servlet implementation class getMap
 */
@WebServlet(description = "servlet used to show all users in the map", urlPatterns = { "/GetMap" })
public class GetMap extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMap() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Urls.IP.equals("0.0.0.0")){
			Urls.IP = request.getLocalAddr();
		}
		String redirectUrl = "/getMap.jsp";
		RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
		rd.forward(request, response);
	}

	/**
	 * @throws UnsupportedEncodingException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		/*
		JSONObject jsonResponse = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		String all = request.getParameter("all");
		LinkedList<Map<String, Comparable>> clientList= new LinkedList();
		if (all.equals("1")){
			Map<String, Comparable> client2 = new LinkedHashMap();
			client2.put("lat",40.74173);
			client2.put("lgn",-74.22569);
			client2.put("id", "user1");
			clientList.add(client2);
			Map client1 = new LinkedHashMap();
			client1.put("lat",40.75);
			client1.put("lgn",-74.22569); //40.74173, -74.22569
			client1.put("id", "user2");
			clientList.add(client1);
		}else{
			String client= request.getParameter("client");
			if (client.equals("user1")){
				Map client1 = new LinkedHashMap();
				client1.put("lat",40.74173);
				client1.put("lgn",-74.22569);
				client1.put("id", "user1");
				clientList.add(client1);
			}else if(client.equals("user2")){
				Map client1 = new LinkedHashMap();
				client1.put("lat",40.75);
				client1.put("lgn",-74.22569);
				client1.put("id", "user2");
				clientList.add(client1);
			}
		}
		Map obj=new LinkedHashMap();
		obj.put("code",new Integer(0));
		obj.put("clients",clientList);
		String jsonText = JSONValue.toJSONString(obj);

		
			
		try {
			jsonResponse = (JSONObject) jsonParser.parse(jsonText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
*/		
	}

}
