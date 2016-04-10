package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

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


import database.controller.DBManager;

/**
 * Servlet implementation class GetHistory
 */
@WebServlet("/GetHistory")
public class GetHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private DBManager dbManager= null;  
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetHistory() {
        super();
        dbManager= new DBManager("server");
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResponse = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		String client = request.getParameter("client");
		int optionChoosed = Integer.parseInt(request.getParameter("optionSelected"));
		LinkedList<Map<String, Comparable>> clientList= null;
		if (optionChoosed==0){
			// his position right now (is not used/never sent)
		}else if (optionChoosed==1){
			// his position of today
			clientList = new LinkedList<Map<String, Comparable>>(dbManager.getClientDataToday(client));
			if (clientList==null);
			/*for (int i=0;i<100;i++){
				LinkedHashMap<String, Comparable> client2 = new LinkedHashMap<String, Comparable>();
				client2.put("lat",40.74173+i*0.001);
				client2.put("lgn",-74.22569+i*0.001);
				clientList.add(client2);
			}*/
		}else{
			// his position of yesterday
			clientList = new LinkedList<Map<String, Comparable>>(dbManager.getClientDataYesterday(client));
			if (clientList==null);
			/*for (int i=0;i<100;i++){
			LinkedHashMap<String, Comparable> client2 = new LinkedHashMap<String, Comparable>();
			client2.put("lat",40.74173+i*0.0001);
			client2.put("lgn",-74.22569+i*0.0001);
			clientList.add(client2);
			}*/
		}
		//TODO get from the database the latlng of the day
		Map<String, Serializable> obj=new LinkedHashMap<String, Serializable>();
		obj.put("code",new Integer(0));
		obj.put("history",clientList);
		String jsonText = JSONValue.toJSONString(obj);			
		try {
			jsonResponse = (JSONObject) jsonParser.parse(jsonText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}

}
