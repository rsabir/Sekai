package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import server.Server;
import server.ServerClient;
import server.ServerServer;
import utils.ConfigUtils;
import utils.PayloadUtils;
import utils.TmpClients;
import constants.Urls;

import database.controller.DBManager;

/**
 * Servlet implementation class SendGPS
 */
public class SendGPS extends HttpServlet {
	//private static final Logger LOGGER = Logger.getLogger(SendGPS.class);
	private static final long serialVersionUID = 1L;
	private static final String PAYLOADPARAMETER = "paylaod";
	private static final String URL = "http://localhost";
	private DBManager dbManager=null;

	public SendGPS() {
		dbManager = new DBManager("server");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Urls.IP = request.getLocalAddr();
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Urls.IP = request.getLocalAddr();
		TmpClients.getInstance();
		try {
			String payloadString = "";
			// 1. get received JSON data from request
//			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
//			if (br != null) {
//				payloadString = br.readLine();
//			}
			
			 StringBuffer jb = new StringBuffer();
			  String line = null;
			  try {
			    BufferedReader reader = request.getReader();
			    while ((line = reader.readLine()) != null)
			      jb.append(line);
			  } catch (Exception e) { /*report an error*/ }
			  payloadString = jb.toString();
			
			// String payloadString = request.getParameter(PAYLOADPARAMETER);
			String configString = ConfigUtils.getConfig(Urls.CONFIGSERVER);
			ArrayList<ArrayList<Object>> configList = ConfigUtils.parse(configString);
			Server.refresh(configList);
			// request.getAttribute(arg0)
			PayloadUtils payload = new PayloadUtils(payloadString);
			ArrayList<String> servers = ConfigUtils.findResponsibleServer(payload.getGps(), configList);
			request.setCharacterEncoding("utf8");
			response.setContentType("application/json");
			JSONObject jsonResponse = new JSONObject();
			if (!payload.isServer()) {
				if (servers.size() == 1 && ServerClient.isInCharge(servers)) {
					// TODO Verify
					if (dbManager.addData(payload.getId(), payload.getLon(), payload.getLat())){
						TmpClients.addRecentClient(payload);
						jsonResponse.put("code", 0);
						response.setStatus(HttpServletResponse.SC_OK);
					}
					else{
						jsonResponse.put("code", -1);
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					

				} else if (ServerClient.isInCharge(servers)) {
					JSONParser jsonParser = new JSONParser();
					ServerServer.notifyAdd((JSONObject) jsonParser.parse(payloadString), servers);
					dbManager.addData(payload.getId(), payload.getLon(), payload.getLat());
					TmpClients.addRecentClient(payload);
					response.setStatus(HttpServletResponse.SC_OK);
					jsonResponse.put("code", 0);
				} else if (servers.size()>0) {
					response.setStatus(HttpServletResponse.SC_SEE_OTHER);
					JSONArray jsonArray = new JSONArray();
					for (String server : servers) {
						JSONObject tmp = new JSONObject();
						tmp.put("IP", server);
						jsonArray.add(tmp);
					}
					jsonResponse.put("servers", jsonArray);
					if (servers.size()>0)
						response.setHeader("Location", servers.get(0));

				} else{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);					
				}
			}else{
				if (ServerClient.isInCharge(servers)) {
					response.setStatus(HttpServletResponse.SC_OK);
					dbManager.addData(payload.getId(), payload.getLon(), payload.getLat());
					TmpClients.addRecentClient(payload);
					jsonResponse.put("code", 0);
				}
			}
			PrintWriter out = response.getWriter();
			out.print(jsonResponse);
			

		} catch (ParseException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
