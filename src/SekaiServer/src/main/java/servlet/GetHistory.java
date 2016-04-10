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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.Urls;


import database.controller.DBManager;

/**
 * Servlet implementation class GetHistory
 */
@WebServlet("/ADMIN/GetHistory")
public class GetHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(GetHistory.class);
	private static Logger httpLogger = LoggerFactory.getLogger("http");
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
		String date = request.getParameter("date");
		httpLogger.info("/Start/GetHistory was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName()+" and with the following parameter client="+client
				+" date="+date);
		LinkedList<Map<String, Comparable>> clientList= null;
		
		try {
			clientList = new LinkedList<Map<String, Comparable>>(dbManager.getClientHistory(client,date));
		} catch (java.text.ParseException e1) {
			logger.error("Error while requesting /Start/GetHistory by "+request.getRemoteAddr()+" with account "+
					request.getUserPrincipal().getName()+" and with the following parameter client="+client
					+" date="+date);
			logger.error(e1.getMessage());
		}
		
		Map<String, Serializable> obj=new LinkedHashMap<String, Serializable>();
		obj.put("code",new Integer(0));
		obj.put("history",clientList);
		String jsonText = JSONValue.toJSONString(obj);			
		try {
			jsonResponse = (JSONObject) jsonParser.parse(jsonText);
		} catch (ParseException e) {
			logger.error("Error while requesting /Start/GetHistory by "+request.getRemoteAddr()+" with account "+
					request.getUserPrincipal().getName()+" and with the following parameter client="+client
					+" date="+date);
			logger.error(e.getMessage());
		}
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}

}
