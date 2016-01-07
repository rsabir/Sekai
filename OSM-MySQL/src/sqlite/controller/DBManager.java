package sqlite.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import sqlite.DAO.MarquerDAO;
import sqlite.DAO.NodeDAO;
import sqlite.DAO.UserDAO;
import sqlite.entities.Marquer;
import sqlite.entities.Node;
import sqlite.entities.User;

public class DBManager {

    private Connexion DBconx = null;
    private MarquerDAO marquerDAO = null;
    private UserDAO userDAO = null;
    private NodeDAO nodeDAO = null;
    
    public DBManager(String serverName){
        DBconx = new Connexion(serverName);
        userDAO = new UserDAO(DBconx);
        nodeDAO = new NodeDAO(DBconx);    
        marquerDAO = new MarquerDAO(DBconx);
        DBconx.connect();
    }
    private boolean checkUser(String MAC){
    	ArrayList<String> macList = new ArrayList<String>(userDAO.getMacAddressList());
    	if (macList!=null) {
    	Iterator<String> iterator = macList.iterator();
		while (iterator.hasNext()) {
			String str=iterator.next();
			//System.out.println(str);
			if (str.equals(MAC)) return true;
		}
    	}
    	return false;
    }
	private boolean checkNode(float longitude, float latitude) {
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

		//addUser
		if (!checkUser(MAC)) {
			userID = userDAO.addUser(MAC,null);
		} else {
			userID = userDAO.getIDfromMAC(MAC);
		}
		if (userID==-1) {
			System.out.println("User registration error");
			return false;
		}
		
		//addNode
		if (!checkNode(longitude,latitude)) {
			nodeID = nodeDAO.addNode(longitude,latitude);
		}else {
			nodeID = nodeDAO.getIDfromLonLat(longitude,latitude);
		}
		if (nodeID==-1) {
			System.out.println("Node registration error");
			return false;
		}
		//addMarquer
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     Date dateobj = new Date();
		 return marquerDAO.addMarquer(userID, nodeID, df.format(dateobj.getTime()));

	}

	public LinkedList<Map<String, Comparable>> getAllData(){
		LinkedList<Map<String, Comparable>> clientList= new LinkedList();
		/**
		 * pour chaque user 
		 * chercher le marquer correspondant
		 * cad le plus récent
		 * constituer le Map
		 * constituter la liste
		 */
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
		
		return userDAO.getMacAddressList();
	}
	public LinkedList<Map<String, Comparable>> getClientDataToday(String MAC) {
		// TODO Auto-generated method stub
		/*
		 * avoir l'id a partir de la mac adresse
		 * aller chercher les marquers de la journée
		 * extraire leurs longitude latitude node id
		 * construire les client
		 * les renvoyer
		 * 
		 */
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
		// TODO Auto-generated method stub
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
