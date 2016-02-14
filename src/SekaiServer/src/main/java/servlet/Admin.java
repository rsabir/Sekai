package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import constants.Mysql;
import constants.Urls;
import database.controller.Connexion;
import utils.Settings;
import utils.src.UserUtils;

@Controller
public class Admin {
	
	public final static int CODE_OK=0;
	public final static int CODE_ERROR_SAVING = -2;
	public final static int CODE_ERROR_PARAMETER = -1;
	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

	 @Autowired
	 public Admin(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
	       this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
	 }
	 
	@RequestMapping(method = RequestMethod.GET,value={"/ADMIN/index","/ADMIN/"})
	protected String getViewIndex()  {
		return "admin";
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String getViewRoot (HttpServletRequest request, HttpServletResponse response) {
	    return "redirect:/ADMIN/";
	}
	
	@RequestMapping(value="/ADMIN/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/Settings"})
	protected String getViewSettings()  {
		return "configSys";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/ServerConfig"})
	protected String getViewServerConfig(ModelMap model)  {
		model.addAttribute("ip", Urls.IP);
		model.addAttribute("configServer", Urls.CONFIGSERVER);
		return "getServerConfig";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/DatabaseConfig"})
	protected String getViewDatabaseConfig(ModelMap model)  {
		model.addAttribute("username", Mysql.USERNAME);
		model.addAttribute("port",Mysql.PORT);
		model.addAttribute("database",Mysql.DATABASE);
		return "databaseConfig";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/UserConfig"})
	protected String getUserConfig(ModelMap model)  {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); //get logged in username
		model.addAttribute("username", name);
		return "userConfig";
	}
	
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/Export"})
	protected String getViewExport()  {
		return "export";
	}
	
	
	@RequestMapping(method = RequestMethod.POST,value={"ADMIN/SetServerSettings"})
	@ResponseBody
	protected void postServerSettings(@RequestParam(value="ip") final String ip,
			@RequestParam(value="url_config") final String urlConfig, 
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResponse = new JSONObject();
		if (ip!=null && urlConfig !=null){
			Urls.CONFIGSERVER=urlConfig;
			Urls.IP = ip;
			Settings.saveFileSettings();
			jsonResponse.put("code",CODE_OK);
		}else{
			jsonResponse.put("code",CODE_ERROR_PARAMETER);
		}
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}
	
	@RequestMapping(method = RequestMethod.POST,value={"ADMIN/SetDatabaseSettings"})
	@ResponseBody
	protected void postDatabaseSettings(@RequestParam(value="username") final String username,
			@RequestParam(value="password") final String password, 
			@RequestParam(value="port") final String port, 
			@RequestParam(value="database") final String database, 
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResponse = new JSONObject();
		if (username!=null && password !=null && port !=null && database != null ){
			Mysql.PORT = (String) port;
			Mysql.PASSWORD = (String) password;
			Mysql.USERNAME = (String) username;
			Mysql.DATABASE = (String) database;
			Settings.saveFileSettings();
			jsonResponse.put("code",CODE_OK);
		}else{
			jsonResponse.put("code",CODE_ERROR_PARAMETER);
		}
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}
	
	@RequestMapping(method = RequestMethod.POST,value={"ADMIN/SetUser"})
	@ResponseBody
	protected void postUserSettings(@RequestParam(value="password") final String password, 
			HttpServletResponse response) throws IOException  {
		JSONObject jsonResponse = new JSONObject();
		if ( password !=null ){
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			ApplicationContext context =  new ClassPathXmlApplicationContext("utilsBeans.xml");
			UserUtils userUtils = (UserUtils) context.getBean("userUtils");
			try {	
				UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(user.getUsername());
				User updatedUser = new User(userDetails.getUsername(),password,userDetails.getAuthorities());
				inMemoryUserDetailsManager.updateUser(updatedUser);
				userUtils.changeUserPassword(user.getUsername(), password);
				jsonResponse.put("code",CODE_OK);
			} catch (Exception e) {
				e.printStackTrace();
				jsonResponse.put("code",CODE_ERROR_SAVING);
			}
		
		}else{
			jsonResponse.put("code",CODE_ERROR_PARAMETER);
		}
		response.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
	}
	
	
	
}
