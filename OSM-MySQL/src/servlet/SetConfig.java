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

import constants.PathsC;

/**
 * Servlet implementation class SetConfig
 */
@WebServlet("/SetConfig")
public class SetConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONObject responseJSON = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetConfig() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String redirectUrl = "/setConfig.jsp";
		RequestDispatcher rd = request.getRequestDispatcher(redirectUrl);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
