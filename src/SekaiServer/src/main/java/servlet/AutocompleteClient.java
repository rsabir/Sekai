package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.controller.DBManager;

/**
 * Servlet implementation class autocompleteClient
 */
@WebServlet("/ADMIN/AutocompleteClient")
public class AutocompleteClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger("http");
	private DBManager dbManager= null;  
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutocompleteClient() {
        super();
        dbManager= new DBManager("server");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
		String parameter = request.getParameter("data");
		logger.debug("/ADMIN/AutocompleteClient was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName()+" and with the following parameter="+parameter);
		PrintWriter out = response.getWriter();
		JSONArray arrayObj = new JSONArray();

		ArrayList<String> macList = dbManager.getMACS();
		if (macList!=null){
			Iterator<String> macIt = macList.iterator();
			Pattern pattern;
			Matcher matcher;
			while (macIt.hasNext()){
				String MAC = macIt.next();
				pattern = Pattern.compile("^"+parameter);
				matcher = pattern.matcher(MAC);
				if (matcher.find()) arrayObj.add(MAC);
			}
		}
		out.println(arrayObj.toString());
		out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
