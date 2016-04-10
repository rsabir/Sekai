package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import constants.Mysql;
import constants.Others;
import constants.Urls;
import database.controller.Connexion;
import utils.Settings;
import utils.src.UserUtils;

@Controller
public class FirstConfig {
	
	public final static int CODE_OK=0;
	public final static int CODE_ERROR_SAVING = -2;
	public final static int CODE_ERROR_PARAMETER = -1;
	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
	private Logger logger = LoggerFactory.getLogger(FirstConfig.class);
	private Logger httpLogger = LoggerFactory.getLogger("http");

	 @Autowired
	 public FirstConfig(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
	       this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
	 }
	 
	@RequestMapping(method = RequestMethod.GET,value={"/Start/Index"})
	protected String getViewIndex(HttpServletRequest request)  {
		httpLogger.info("/Start/Index was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		return "Start/firstpage";
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"/Start/ChangePassword"})
	protected String getChangePassword(ModelMap model,HttpServletRequest request)  {
		httpLogger.info("/Start/ChangePassword was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); //get logged in username
		model.addAttribute("username", name);
		return "Start/changePassword";
	}
	
	@RequestMapping(method = RequestMethod.POST,value={"/Start/ChangePassword"})
	@ResponseBody
	protected void ChangePassword(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="password") final String password,
			@RequestParam(value="confirmPassword") final String confirmPassword) throws IOException  {
		httpLogger.info("/Start/ChangePassword was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName()+" and with the following parameter password="+password
				+" confirmPassword="+confirmPassword);
		JSONObject jsonResponse = new JSONObject();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); //get logged in username
	    if ( password !=null && confirmPassword != null && password.equals(confirmPassword)){
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
				logger.error("Error occured while changing Password");
				logger.error(e.getMessage());
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
	
	@RequestMapping(method = RequestMethod.GET,value={"/Start/BasicConfig"})
	protected String getBasicConfig(ModelMap model,HttpServletRequest request)  {
		httpLogger.info("/Start/BasicConfig was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		model.addAttribute("ip", Urls.IP);
		model.addAttribute("configServer", Urls.CONFIGSERVER);
		return "Start/basicConfig";
	}
	
	
	@RequestMapping(method = RequestMethod.GET,value={"/Start/ConfigJSON"})
	protected String getConfigJSON(HttpServletRequest request) {
		httpLogger.info("/Start/ConfigJSON was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		String redirectUrl = "/Start/setConfig";
		return redirectUrl;
	}
	
	@RequestMapping(method = RequestMethod.GET,value={"/Start/Congrat"})
	protected String getCongratView(HttpServletRequest request) {
		httpLogger.info("/Start/congrat was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		String redirectUrl = "/Start/congrat";
		return redirectUrl;
	}
	
	@RequestMapping(method = RequestMethod.POST,value={"/Start/finish"})
	@ResponseBody
	protected void setFinished(HttpServletRequest request) {
		httpLogger.info("/Start/finish was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		Others.FIST_TIME = false;
	}	
	
}
