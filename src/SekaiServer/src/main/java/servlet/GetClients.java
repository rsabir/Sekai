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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.Urls;


import database.controller.DBManager;
import utils.TmpClients;

/**
 * Servlet implementation class GetCLients
 */
@WebServlet("/ADMIN/GetClients")
public class GetClients extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(GetClients.class);
	private static Logger httpLogger = LoggerFactory.getLogger("http");
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
			logger.error("Error occured while requesting");
		}
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		httpLogger.info("/Start/GetClients was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		TmpClients.getInstance();
		JSONObject jsonResponse = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		String all = request.getParameter("all");
		LinkedList<Map<String, Object>> clientList=
			new LinkedList();
		if (all.equals("1")){
			clientList = TmpClients.getRecentClientsList();
			if (clientList==null)
				returnError(jsonResponse, jsonParser, response); //
		}else{
			String client= request.getParameter("client");
			Map<String, Object> lastClient = TmpClients.getRecentClientList(client);
			if (lastClient!=null)
				clientList.add(lastClient);
		}
		Map obj=new LinkedHashMap();
		obj.put("code",new Integer(0));
		obj.put("clients",clientList);
		String jsonText = JSONValue.toJSONString(obj);
		try {
			jsonResponse = (JSONObject) jsonParser.parse(jsonText);
		} catch (ParseException e) {
			
		}
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}

}
