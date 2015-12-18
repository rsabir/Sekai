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
    }
    private boolean checkUser(String MAC){
    	ArrayList<String> macList = new ArrayList<String>(userDAO.getMacAddressList()); 
    	Iterator<String> iterator = macList.iterator();
		while (iterator.hasNext()) {
			String str=iterator.next();
			//System.out.println(str);
			if (str.equals(MAC)) return true;
		}
    	return false;
    }
	private boolean checkNode(float longitude, float latitude) {
		ArrayList<Node> nodeList = new ArrayList<Node>(nodeDAO.getAllNodes());
		Iterator<Node> iterator = nodeList.iterator();
		while (iterator.hasNext()) {
			Node n=iterator.next();
			//System.out.println(n);
    		if ((n.getLatitude()==latitude)&&(n.getLongitude()==longitude)) return true;
    	}
		return false;
	}
	/*
	private boolean checkMarquer(int userID, int nodeID) {
		// TODO Auto-generated method stub
		ArrayList<Marquer> marquerList = new ArrayList<Marquer>(marquerDAO.getAllMarquers());
		Iterator<Marquer> iterator = marquerList.iterator();
		while (iterator.hasNext()) {
			Marquer m=iterator.next();
			//System.out.println(m);
    		if ((m.getUser()==userID)&&(m.getPosition()==nodeID)) return true;
    	}
		return false;
	}
	*/
	public void addData(String MAC, float longitude,float latitude){
		int userID;
		int nodeID;
		//System.out.println(MAC);
		//System.out.println(longitude);
		//System.out.println(latitude);
		
		//addUser
		if (!checkUser(MAC)) {
			userID = userDAO.addUser(MAC,null);
		} else {
			userID = userDAO.getIDfromMAC(MAC);
		}
		if (userID==-1) {
			System.out.println("User registration error");
			return;
		}
		
		//addNode
		if (!checkNode(longitude,latitude)) {
			nodeID = nodeDAO.addNode(longitude,latitude);
		}else {
			nodeID = nodeDAO.getIDfromLonLat(longitude,latitude);
		}
		//addMarquer
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     Date dateobj = new Date();
		 marquerDAO.addMarquer(userID, nodeID, df.format(dateobj.getTime()));
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
		ArrayList<User> userList = new ArrayList<User>(userDAO.getUsers());
		Iterator<User> userIt = userList.iterator();
		while (userIt.hasNext()){
			User user=userIt.next();
			//System.out.println("all"+user.getID());
			Marquer marquer = marquerDAO.getRecentMarquer(user.getID());
			if (marquer!=null) {
			Node node = nodeDAO.getNodeByID(marquer.getPosition());
			Map client = new LinkedHashMap();
			client.put("lat",node.getLatitude());
			client.put("lgn",node.getLongitude());
			client.put("id", user.getMacAddr());
			clientList.add(client);
			} else {
				System.out.println("User "+user.getMacAddr()+" does not exist");
			}
		}
		//System.out.println(clientList);
		return clientList;
	}
	public Map getClientData(String MACID){
		Map client = new LinkedHashMap();
		int userID = userDAO.getIDfromMAC(MACID);
		Marquer marquer = marquerDAO.getRecentMarquer(userID);
		Node node = nodeDAO.getNodeByID(marquer.getPosition());
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
		ArrayList<Marquer> marquerList = marquerDAO.getMarquersToday(userID);
		Iterator<Marquer> marquerIt = marquerList.iterator();
		while (marquerIt.hasNext()){
			Marquer marquer = marquerIt.next();
			Node node = nodeDAO.getNodeByID(marquer.getPosition());
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
	public LinkedList< Map<String, Comparable>> getClientDataYesterday(String MAC) {
		// TODO Auto-generated method stub
		LinkedList<Map<String, Comparable>> result = new LinkedList<Map<String, Comparable>>();
		int userID = userDAO.getIDfromMAC(MAC);
		ArrayList<Marquer> marquerList = marquerDAO.getMarquersYesterday(userID);
		Iterator<Marquer> marquerIt = marquerList.iterator();
		while (marquerIt.hasNext()){
			Marquer marquer = marquerIt.next();
			Node node = nodeDAO.getNodeByID(marquer.getPosition());
			Map<String, Comparable> client= new LinkedHashMap<String, Comparable>();
			client.put("lat", node.getLatitude());
			client.put("lon", node.getLongitude());
			result.add(client);
			
		}
		return result;
	}

}
