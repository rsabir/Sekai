package database.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.DAO.MarquerDAO;
import database.DAO.NodeDAO;
import database.DAO.UserDAO;
import database.entities.Marquer;
import database.entities.Node;
import database.entities.User;


public class DBManager {

	private static Logger logger = LoggerFactory.getLogger(DBManager.class);
    private MarquerDAO marquerDAO = null;
    private UserDAO userDAO = null;
    private NodeDAO nodeDAO = null;
    
    public DBManager(String serverName){
        userDAO = new UserDAO();
        nodeDAO = new NodeDAO();    
        marquerDAO = new MarquerDAO();
    }
    private boolean checkUser(String MAC){
    	logger.debug("Checking if user "+MAC+" exists");
    	ArrayList<String> macList = new ArrayList<String>(userDAO.getMacAddressList());
    	if (macList!=null) {
	    	Iterator<String> iterator = macList.iterator();
			while (iterator.hasNext()) {
				String str=iterator.next();
				if (str.equals(MAC)) return true;
			}
    	}
    	return false;
    }
	private boolean checkNode(float longitude, float latitude) {
		logger.debug("Checking if position "+longitude+":"+latitude+" exists");
		ArrayList<Node> nodeList = new ArrayList<Node>(nodeDAO.getAllNodes());
		if (nodeList!=null) {
			Iterator<Node> iterator = nodeList.iterator();
			while (iterator.hasNext()) {
				Node n=iterator.next();
				//System.out.println(n);
	    		if ((n.getLatitude()==latitude)&&(n.getLongitude()==longitude)) return true;
	    	}
		}
		return false;
		
	}

	public boolean addData(String MAC, float longitude,float latitude){
		int userID;
		int nodeID;
		
		String string_logger = "entry user "+MAC+" with the following gps "+longitude+":"+latitude;
		logger.debug("Adding "+string_logger );

		//addUser
		if (!checkUser(MAC)) {
			userID = userDAO.addUser(MAC,null);
		} else {
			userID = userDAO.getIDfromMAC(MAC);
		}
		if (userID==-1) {
			logger.error("Failed while registring "+string_logger+" caused by User ID");
			return false;
		}
		
		//addNode
		if (!checkNode(longitude,latitude)) {
			nodeID = nodeDAO.addNode(longitude,latitude);
		}else {
			nodeID = nodeDAO.getIDfromLonLat(longitude,latitude);
		}
		if (nodeID==-1) {
			logger.error("Failed while registring "+string_logger+" caused by Location");
			return false;
		}
		//addMarquer
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     Date dateobj = new Date();
		 return marquerDAO.addMarquer(userID, nodeID, df.format(dateobj.getTime()));
	}

	public LinkedList<Map<String, Comparable>> getAllData(){
		logger.debug("Getting recent locations of all users");
		LinkedList<Map<String, Comparable>> clientList= new LinkedList();
		ArrayList<User> userList = new ArrayList<User>();
		userList.addAll(userDAO.getUsers());
		if (userList!=null) {
		Iterator<User> userIt = userList.iterator();
		while (userIt.hasNext()){
			User user=userIt.next();;
			Marquer marquer = marquerDAO.getRecentMarquer(user.getID());
			if (marquer==null) continue;
			Node node = nodeDAO.getNodeByID(marquer.getPosition());
			if (node==null) continue;
			Map client = new LinkedHashMap();
			client.put("lat",node.getLatitude());
			client.put("lgn",node.getLongitude());
			client.put("id", user.getMacAddr());
			clientList.add(client);
			}
			System.out.println(clientList);
			return clientList;
			}
			return null;
				//System.out.println("User "+user.getMacAddr()+" does not have a GPS (SKIP)");

	}
	public Map getClientData(String MACID){
		logger.debug("Gettting recent location of the user "+MACID);
		Map client = new LinkedHashMap();
		int userID = userDAO.getIDfromMAC(MACID);
		if (userID==-1) return null;
		Marquer marquer = marquerDAO.getRecentMarquer(userID);
		if (marquer==null) return null;
		Node node = nodeDAO.getNodeByID(marquer.getPosition());
		if (node==null) return null;
		client.put("lat",node.getLatitude());
		client.put("lgn",node.getLongitude());
		client.put("id", MACID);
		return client;
	}
	public ArrayList<String> getMACS() {
		// TODO Auto-generated method stub
		logger.debug("Getting ID of all users");
		return userDAO.getMacAddressList();
	}
	
	public LinkedList<Map<String, Comparable>> getClientHistory(String MAC,String date) throws ParseException {
		logger.debug("Getting history of the client "+MAC+" at"+date);
		LinkedList<Map<String, Comparable>> result = new LinkedList<Map<String, Comparable>>();
		int userID = userDAO.getIDfromMAC(MAC);
		if (userID==-1) return null;
		ArrayList<Marquer> marquerList = marquerDAO.getMarquersGivenDay(userID, date);
		if (marquerList!=null) {
			Iterator<Marquer> marquerIt = marquerList.iterator();
			while (marquerIt.hasNext()){
				Marquer marquer = marquerIt.next();
				Node node = nodeDAO.getNodeByID(marquer.getPosition());
				if (node==null) return null;
				Map<String, Comparable> client= new LinkedHashMap<String, Comparable>();
				client.put("lat", node.getLatitude());
				client.put("lon", node.getLongitude());
				result.add(client);
			}
		return result;
		}
		return null;		
	}
	
	public LinkedList<Map<String, Comparable>> getClientDataToday(String MAC) {
		logger.debug("Getting the today's history of the user"+MAC);
		LinkedList<Map<String, Comparable>> result = new LinkedList<Map<String, Comparable>>();
		int userID = userDAO.getIDfromMAC(MAC);
		if (userID==-1) return null;
		ArrayList<Marquer> marquerList = marquerDAO.getMarquersToday(userID);
		if (marquerList!=null) {
		Iterator<Marquer> marquerIt = marquerList.iterator();
		while (marquerIt.hasNext()){
			Marquer marquer = marquerIt.next();
			Node node = nodeDAO.getNodeByID(marquer.getPosition());
			if (node==null) return null;
			Map<String, Comparable> client= new LinkedHashMap<String, Comparable>();
			client.put("lat", node.getLatitude());
			client.put("lon", node.getLongitude());
			result.add(client);
		}
		/*client2.put("lat",40.74173+i*0.001);
		client2.put("lgn",-74.22569+i*0.001);
		clientList.add(client2);*/
		return result;
		}
		return null;
	}
	public LinkedList< Map<String, Comparable>> getClientDataYesterday(String MAC) {
		logger.debug("Getting the yesterday's history of the user"+MAC);
		LinkedList<Map<String, Comparable>> result = new LinkedList<Map<String, Comparable>>();
		int userID = userDAO.getIDfromMAC(MAC);
		if (userID == -1) return null;
		ArrayList<Marquer> marquerList = marquerDAO.getMarquersYesterday(userID);
		if (marquerList==null) return null;
		Iterator<Marquer> marquerIt = marquerList.iterator();
		while (marquerIt.hasNext()){
			Marquer marquer = marquerIt.next();
			Node node = nodeDAO.getNodeByID(marquer.getPosition());
			if (node==null) return null;
			Map<String, Comparable> client= new LinkedHashMap<String, Comparable>();
			client.put("lat", node.getLatitude());
			client.put("lon", node.getLongitude());
			result.add(client);
			
		}
		return result;
	}

}
