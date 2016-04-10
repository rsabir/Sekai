package servlet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import constants.PathsC;

/**
 * Servlet implementation class SetConfig
 */
@Controller
@RequestMapping("/ADMIN/SetConfig")
public class SetConfig {
	//private static final long serialVersionUID = 1L;
	private JSONObject responseJSON = null;
       

	@RequestMapping(method = RequestMethod.GET)
	protected String doGet() {
		String redirectUrl = "/setConfig";
		return redirectUrl;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean iserror=true;
		if (request.getParameter("response")!=null){
			String reponse = request.getParameter("response");
			if (isJSONValid(reponse)){
				try {
					FileWriter file = new FileWriter(PathsC.PATHCONFIGJSON);
					file.write(responseJSON.toJSONString());
					file.flush();
					file.close();
					iserror = false;
				} catch (IOException e) {
				}
			}
		}
		JSONObject jsonResponse = new JSONObject();
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		if (iserror){
			jsonResponse.put("code", -1);
		}else{
			jsonResponse.put("code",0);
		}
		out.print(jsonResponse);
	}
	
	public boolean isJSONValid(String test) {
	        JSONParser parser = new JSONParser();
	        try {
				responseJSON = (JSONObject) parser.parse(test);
			} catch (ParseException e) {
				return false;
			}
	    
	    return true;
	}


}
