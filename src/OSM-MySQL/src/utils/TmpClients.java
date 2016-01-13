package utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import database.controller.Connexion;

/**
 * This class was developed to reduce the resources taken in MySQL by /GetClients
 * @author rsabir
 *
 **/

public class TmpClients {
	private static Hashtable<String,Client> memory;
	private static ArrayList<String> clients;
	private static TmpClients instance;
	private static int RECENT_INTERVAL_SECONDS = 20000;
	
	private TmpClients(){
		TmpClients.memory=new Hashtable<String,Client>();
		TmpClients.clients = new ArrayList<String>();
	}
	public static TmpClients getInstance(){
		 if (TmpClients.instance == null) {
			 synchronized(TmpClients.class) {
	             if (TmpClients.instance == null) {
	            	   TmpClients.instance = new TmpClients();
	             }
	           }
		 }
		return TmpClients.instance;
	}
	
	public static ArrayList<Client> getRecentClients(){
		long now = (new Date()).getTime();
		ArrayList<Client> resultList = new ArrayList<Client>();
		Iterator<String> it = clients.iterator();
		while (it.hasNext()){
			String client = it.next();
			Client tmpClient = memory.get(client);
			if (now - tmpClient.getDate().getTime()< RECENT_INTERVAL_SECONDS){
				memory.put(tmpClient.getId(),tmpClient);
				resultList.add(tmpClient);
			}else{
				memory.remove(tmpClient.getId());
				it.remove();
			}
		}
		return resultList;
	}
	
	public static LinkedList<Map<String, Object>> getRecentClientsList(){
		LinkedList<Map<String, Object>> result = new LinkedList<Map<String, Object>> ();
		Iterator<Client> it = getRecentClients().iterator();
		while (it.hasNext()){
			Client tmpClient = it.next();
			Map client = new LinkedHashMap();
			client.put("lat",new Double(tmpClient.getLat()));
			client.put("lgn",new Double(tmpClient.getLon()));
			client.put("id", tmpClient.getId());
			result.add((Map<String, Object>) client);
		}
		return result;
	}
	
	public static Client getRecentClient(String id){
		return memory.get(id);
	}
	
	public static Map<String, Object> getRecentClientList(String id){
		Client client = getRecentClient(id);
		Map clientMap = new LinkedHashMap();
		if (client == null)
			return null;
		else{
			clientMap.put("lat",new Double(client.getLat()));
			clientMap.put("lgn",new Double(client.getLon()));
		}
		return clientMap;
	}
	
	public static void addRecentClient(PayloadUtils payload){
		addRecentClient(new Client(payload.getId(),payload.getLat(),payload.getLon(),new Date()));
	}
	
	public static void addRecentClient(Client a){
		if (!clients.contains(a.getId())){
			clients.add(a.getId());
		}
		memory.put(a.getId(),a);
	}
	
	public static int getSize(){
		return clients.size();
	}
}

