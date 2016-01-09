package servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class GetCLients
 */
@WebServlet("/GetClients")
public class GetClients extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DBManager dbManager= null;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetClients() {
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
	 * @throws IOException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	private void returnError(JSONObject jsonResponse,JSONParser jsonParser, HttpServletResponse response) throws IOException {
		Map obj=new LinkedHashMap();
		obj.put("code",new Integer(-1));
		obj.put("statut","Error on client List grep");
		//System.out.println("client"+clientList);
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResponse = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		String all = request.getParameter("all");
		LinkedList<Map<String, Comparable>> clientList=
			new LinkedList();
		if (all.equals("1")){
			clientList=dbManager.getAllData();
			if (clientList==null)
				returnError(jsonResponse, jsonParser, response); //
		}else{
			String client= request.getParameter("client");
			clientList=new LinkedList<Map<String, Comparable>>();
			clientList.add(dbManager.getClientData(client));
			//System.out.println(clientList);
			if (clientList==null); 
		}
		Map obj=new LinkedHashMap();
		obj.put("code",new Integer(0));
		obj.put("clients",clientList);
		//System.out.println("client"+clientList);
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
