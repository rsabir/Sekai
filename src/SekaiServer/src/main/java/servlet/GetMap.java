package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import constants.Urls;


@Controller
@RequestMapping("/ADMIN/GetMap")
public class GetMap {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(GetMap.class);
	private static Logger httpLogger = LoggerFactory.getLogger("http");


	@RequestMapping(method = RequestMethod.GET)
	protected String doGet(HttpServletRequest request) throws  IOException {
		httpLogger.info("/Start/GetLatLgn was requested by "+request.getRemoteAddr()+" with account "+
				request.getUserPrincipal().getName());
		String redirectUrl = "/getMap";
		return redirectUrl;
	}
}
