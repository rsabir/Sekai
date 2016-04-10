package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.Server;
import utils.ConfigUtils;

/**
 * Servlet implementation class GetLatLgn
 */
@WebServlet("/ADMIN/GetLatLgn")
public class GetLatLgn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(GetLatLgn.class);
	private static Logger httpLogger = LoggerFactory.getLogger("http");
    private boolean is_error = false;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLatLgn() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		httpLogger.info("/Start/GetLatLgn was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		ExecutorService executor = Executors.newSingleThreadExecutor();
	    JSONObject jsonResponse = new JSONObject();
	    Future<String> future = executor.submit(new Task());
	    is_error=false;
	    
	    try {
	          executor.invokeAll(Arrays.asList(new Task()), 6, TimeUnit.SECONDS); // Timeout of 10 minutes.
	    } catch (InterruptedException e) {
	    	logger.error("Error occured while requesting /Start/GetLatLgn by "+request.getRemoteAddr()+" with account "+
					request.getUserPrincipal().getName());
	    	logger.error(e.getMessage());
	    	future.cancel(true);
	    	jsonResponse.put("code", -1);
	    }
	    if (jsonResponse.get("code")==null && is_error==false){
	    	jsonResponse.put("code", new Integer(0));
	    	jsonResponse.put("maxLat", new Float(Server.getMaxLat()));
	    	jsonResponse.put("maxLgn", new Float(Server.getMaxLgn()));
	    	jsonResponse.put("minLat", new Float(Server.getMinLat()));
	    	jsonResponse.put("minLgn", new Float(Server.getMinLgn()));
	    }else if (jsonResponse.get("code")==null){
	    	jsonResponse.put("code", new Integer(-1));
	    }
	    response.setCharacterEncoding("utf8");
	    response.setContentType("application/json");
	    executor.shutdownNow();
	    PrintWriter out = response.getWriter();
	    out.print(jsonResponse);
	    }

	class Task implements Callable<String> {
	    public String call() throws Exception {
	    	String configString;
			try {
				configString = ConfigUtils.getConfig();
				ArrayList<ArrayList<Object>> configList = ConfigUtils.parse(configString);
				Server.refresh(configList);		
			} catch (ParseException e) {
				logger.error("Error occured while requesting /Start/GetLatLgn");
		    	logger.error(e.getMessage());
				is_error=true;
				return "error";
			} catch (IOException e) {
				logger.error("Error occured while requesting /Start/GetLatLgn");
		    	logger.error(e.getMessage());
				is_error=true;
				return "error";
			}
			return "Ready";
	    }
	}

		

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}


