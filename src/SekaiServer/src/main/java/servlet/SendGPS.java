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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import server.Server;
import server.ServerClient;
import server.ServerServer;
import server.prototype.CheckServer;
import utils.ConfigUtils;
import utils.PayloadUtils;
import utils.TmpClients;
import utils.src.UserUtils;
import constants.Urls;

import database.controller.DBManager;

/**
 * Servlet implementation class SendGPS
 */

@Controller
@RequestMapping("/SendGPS")
public class SendGPS{
	//private static final Logger LOGGER = Logger.getLogger(SendGPS.class);
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SendGPS.class);
	private static Logger httpLogger = LoggerFactory.getLogger("http");
	private DBManager dbManager=null;

	public SendGPS() {
		dbManager = new DBManager("server");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
			
			  httpLogger.debug("/SendGPS was called the following data : "+
						payloadString+" and by the following IP="+request.getRemoteAddr());
			  String configString = ConfigUtils.getConfig(Urls.CONFIGSERVER);
			  ArrayList<ArrayList<Object>> configList = ConfigUtils.parse(configString);
			  Server.refresh(configList);
			  // request.getAttribute(arg0)
			  PayloadUtils payload = new PayloadUtils(payloadString);
			  ArrayList<String> serversResponsible = ConfigUtils.findResponsibleServer(payload.getGps(), configList);
			  request.setCharacterEncoding("utf8");
			  response.setContentType("application/json");
			  JSONObject jsonResponse = new JSONObject();
			  if (!payload.isServer()) {
				if (serversResponsible.size() == 1 && ServerClient.isInCharge(serversResponsible)) {
					if (dbManager.addData(payload.getId(), payload.getLon(), payload.getLat())){
						TmpClients.addRecentClient(payload);
						jsonResponse.put("code", 0);
						response.setStatus(HttpServletResponse.SC_OK);
					}
					else{
						jsonResponse.put("code", -1);
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					

				} else if (ServerClient.isInCharge(serversResponsible)) {
					JSONParser jsonParser = new JSONParser();
					ServerServer.notifyAdd((JSONObject) jsonParser.parse(payloadString), serversResponsible);
					dbManager.addData(payload.getId(), payload.getLon(), payload.getLat());
					TmpClients.addRecentClient(payload);
					response.setStatus(HttpServletResponse.SC_OK);
					jsonResponse.put("code", 0);
				} else if (serversResponsible.size()>0) {
					response.setStatus(HttpServletResponse.SC_SEE_OTHER);
					JSONArray jsonArray = new JSONArray();
					for (String server : serversResponsible) {
						JSONObject tmp = new JSONObject();
						tmp.put("IP", server);
						jsonArray.add(tmp);
					}
					jsonResponse.put("servers", jsonArray);
					response.setHeader("Location", serversResponsible.get(0));

				} else{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);					
				}
			}else{
				ArrayList<String> servers= ConfigUtils.getServers(configList);
				ApplicationContext context =  new ClassPathXmlApplicationContext("serverBeans.xml");
				CheckServer checkServer = (CheckServer) context.getBean("checkServer");
				if (checkServer.isServer(payload.getIp(), servers)){
					if (ServerClient.isInCharge(serversResponsible)) {
						response.setStatus(HttpServletResponse.SC_OK);
						dbManager.addData(payload.getId(), payload.getLon(), payload.getLat());
						TmpClients.addRecentClient(payload);
						jsonResponse.put("code", 0);
					}
				}else{
					jsonResponse.put("code", -1);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					logger.error("Bad Server Request by "+request.getRemoteAddr());
				}
			}
			PrintWriter out = response.getWriter();
			out.print(jsonResponse);
			

		} catch (ParseException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.error("Bad Request by "+request.getRemoteAddr());
		}
	}

}
