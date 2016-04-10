package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogConfigurationException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import constants.LogsC;
import constants.Mysql;
import constants.Others;
import constants.PathsC;
import constants.Urls;
import database.controller.Connexion;
import utils.Logs;
import utils.ReadFile;
import utils.Settings;
import utils.src.UserUtils;

@Controller
public class Admin {
	
	public final static int CODE_OK=0;
	public final static int CODE_ERROR_LOG_INITIALIZING = -3;
	public final static int CODE_ERROR_SAVING = -2;
	public final static int CODE_ERROR_PARAMETER = -1;
	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
	private static Logger httpLogger = LoggerFactory.getLogger("http");
	private static Logger logger = LoggerFactory.getLogger(Admin.class);
	
	 @Autowired
	 public Admin(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
	       this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
	 }
	 
	@RequestMapping(method = RequestMethod.GET,value={"/ADMIN/index","/ADMIN/"})
	protected String getViewIndex(HttpServletRequest request)  {
		httpLogger.info("/ADMIN/ was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		if (Others.FIST_TIME)
			return "redirect:/Start/Index";
		else	
			return "admin";
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String getViewRoot (HttpServletRequest request, HttpServletResponse response) {
	    return "redirect:/ADMIN/";
	}
	
	@RequestMapping(value="/ADMIN/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		httpLogger.info("/ADMIN/logout was requested by "+request.getRemoteAddr()+
				" with account "+request.getUserPrincipal().getName());
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/Settings"})
	protected String getViewSettings(HttpServletRequest request)  {
		httpLogger.info("/ADMIN/Settings was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		return "configSys";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/ServerConfig"})
	protected String getViewServerConfig(ModelMap model,HttpServletRequest request)  {
		httpLogger.info("/ADMIN/ServerConfig was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		model.addAttribute("ip", Urls.IP);
		model.addAttribute("configServer", Urls.CONFIGSERVER);
		return "getServerConfig";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/DatabaseConfig"})
	protected String getViewDatabaseConfig(ModelMap model,HttpServletRequest request)  {
		httpLogger.info("/ADMIN/DatabaseConfig was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		model.addAttribute("username", Mysql.USERNAME);
		model.addAttribute("port",Mysql.PORT);
		model.addAttribute("database",Mysql.DATABASE);
		return "databaseConfig";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/UserConfig"})
	protected String getUserConfig(ModelMap model,HttpServletRequest request)  {
		httpLogger.info("/ADMIN/UserConfig was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); //get logged in username
		model.addAttribute("username", name);
		return "userConfig";
	}
	
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/Export"})
	protected String getViewExport(HttpServletRequest request)  {
		httpLogger.info("/ADMIN/Export was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		return "export";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/getGeneralLog"})
	protected String getGeneralLogIndex(HttpServletRequest request)  {
		httpLogger.info("/ADMIN/getGeneralLog was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		return "generalLog";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/getHttpLog"})
	protected String getHttpLogIndex(HttpServletRequest request)  {
		httpLogger.info("/ADMIN/getHttpLog was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		return "httpLog";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/getErrorLog"})
	protected String getHttpErrorIndex(HttpServletRequest request)  {
		httpLogger.info("/ADMIN/getErrorLog was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		return "errorLog";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/getHttpLogFile"})
	@ResponseBody
	protected void getHttpLogFile(HttpServletRequest request,HttpServletResponse response) throws IOException  {
		httpLogger.info("/ADMIN/getHttpLogFile was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		response.setCharacterEncoding("utf8");
		PrintWriter out = response.getWriter();
		String content = ReadFile.readFile(PathsC.PATHHTTPLOG+".log");
		if (content==null)
			content="The file is empty";
		out.print(content);
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/getGeneralLogFile"})
	@ResponseBody
	protected void getGeneralFile(HttpServletRequest request,HttpServletResponse response) throws IOException  {
		httpLogger.info("/ADMIN/getGeneralLogFile was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		response.setCharacterEncoding("utf8");
		PrintWriter out = response.getWriter();
		String content = ReadFile.readFile(PathsC.PATHGENERALLOG+".log");
		if (content==null)
			content="The file is empty";
		out.print(content);
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/getErrorLogFile"})
	@ResponseBody
	protected void getErrorFile(HttpServletRequest request,HttpServletResponse response) throws IOException  {
		httpLogger.info("/ADMIN/getErrorLogFile was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		response.setCharacterEncoding("utf8");
		PrintWriter out = response.getWriter();
		String content = ReadFile.readFile(PathsC.PATHERRORLOG+".log");
		if (content==null)
			content="The file is empty";
		out.print(content);
	}
	@RequestMapping(method = RequestMethod.GET,value={"ADMIN/getLogSetting"})
	protected String getGeneralLogSettings(HttpServletRequest request,ModelMap model)  {
		httpLogger.info("/ADMIN/getLogSettings was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		model.addAttribute("level", LogsC.LEVEL_ROOT);
		return "logSettings";
	}
	
	
	@RequestMapping(method = RequestMethod.POST,value={"ADMIN/SetServerSettings"})
	@ResponseBody
	protected void postServerSettings(@RequestParam(value="ip") final String ip,
			@RequestParam(value="url_config") final String urlConfig, 
			HttpServletResponse response,HttpServletRequest request) throws ServletException, IOException {
		httpLogger.info("/ADMIN/SetServerSettings was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
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
			HttpServletResponse response,
			HttpServletRequest request) throws ServletException, IOException {
		httpLogger.info("/ADMIN/SetDatabaseSettings was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
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
			HttpServletResponse response,
			HttpServletRequest request) throws IOException  {
		httpLogger.info("/ADMIN/SetUser was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
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
	
	@RequestMapping(method = RequestMethod.POST,value={"ADMIN/SetLogs"})
	@ResponseBody
	protected void postServerSettings(@RequestParam(value="level") final String level,
			HttpServletResponse response,HttpServletRequest request) throws ServletException, IOException {
		httpLogger.info("/ADMIN/SetServerlevel was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		JSONObject jsonResponse = new JSONObject();
		if (level!=null && 
				(level.equals("TRACE") || level.equals("DEBUG")|| level.equals("INFO") || level.equals("WARN") 
						|| level.equals("ERROR") || level.equals("OFF"))){
			if (!level.equals(LogsC.LEVEL_ROOT)){
					LogsC.LEVEL_ROOT=level;
					Logs.getInstance().saveSettings();
					LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
					loggerContext.reset();
					ContextInitializer ci = new ContextInitializer(loggerContext);
				
					try {
						ci.autoConfig();
						jsonResponse.put("code",CODE_OK);
					} catch (JoranException e) {
						logger.error(e.getMessage());
						jsonResponse.put("code",CODE_ERROR_LOG_INITIALIZING);
					}
			}else{
				// level.equals(LogsC.LEVEL_ROOT) 
				jsonResponse.put("code",CODE_OK);
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
