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

import sqlite.controller.DBManager;

/**
 * Servlet implementation class autocompleteClient
 */
@WebServlet("/AutocompleteClient")
public class AutocompleteClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private DBManager dbManager= null;  
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutocompleteClient() {
        super();
        dbManager= new DBManager("server");
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
		String parameter = request.getParameter("string");
		PrintWriter out = response.getWriter();
		JSONArray arrayObj = new JSONArray();
		//TODO to change 

		System.out.println(parameter);//le parametre est null
		ArrayList<String> macList = dbManager.getMACS();
		Iterator<String> macIt = macList.iterator();
		 Pattern pattern;
		 Matcher matcher;
		while (macIt.hasNext()){
			String MAC = macIt.next();
			pattern = Pattern.compile("^"+parameter);
			matcher = pattern.matcher(MAC);
			if (matcher.find()) arrayObj.add(MAC);
		}
		arrayObj.add("test"); //a supprimer une fois le parametre sera corrigé
		out.println(arrayObj.toString());
		out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
