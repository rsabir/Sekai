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

import constants.Mysql;
import constants.Urls;
import database.controller.Connexion;
import utils.Settings;

/**
 * Servlet implementation class ConfigSys
 */
@WebServlet(description = "Set Configuration", urlPatterns = { "/Settings" })
public class ConfigSys extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfigSys() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String redirectUrl = "/configSys.jsp";
		RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int is_get = Integer.parseInt((String) request.getParameter("is_get"));
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
