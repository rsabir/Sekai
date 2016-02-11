package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import constants.Mysql;
import constants.Urls;
import database.controller.Connexion;
import utils.Settings;

/**
 * Servlet implementation class ConfigSys
 */
@Controller
@RequestMapping("/ADMIN/Settings")
public class ConfigSys extends HttpServlet {
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@RequestMapping(method = RequestMethod.GET)
	protected String doGet()  {
		return "configSys";
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	protected void doPost(@RequestParam(value="is_get") final int is_get,HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResponse = new JSONObject();
		if (is_get==1){
			jsonResponse.put("code",0);
			jsonResponse.put("url_config", Urls.CONFIGSERVER);
			jsonResponse.put("mysql_port", Mysql.PORT);
			jsonResponse.put("mysql_password", Mysql.PASSWORD);
			jsonResponse.put("mysql_username",Mysql.USERNAME);
			jsonResponse.put("mysql_database",Mysql.DATABASE);
		}else{
			Urls.CONFIGSERVER = (String) request.getParameter("url_config");
			Mysql.PORT = (String) request.getParameter("mysql_port");
			Mysql.PASSWORD = (String) request.getParameter("mysql_password");
			Mysql.USERNAME = (String) request.getParameter("mysql_username");
			Mysql.DATABASE = (String) request.getParameter("mysql_database");
			Settings.saveFileSettings();
			Connexion.destruct();
			jsonResponse.put("code",0);
		}
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}

}
