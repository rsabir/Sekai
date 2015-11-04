package server;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import utils.HttpSendRequest;

public class ServerServer {
	public static int notifyAdd(final JSONObject payload, ArrayList<String> servers){
		int compteur=0;
		for(final String server : servers){
			if (server.equals(ServerConstants.IP)){
				continue;
			}
			Thread t = new Thread(new Runnable(){
				public void run(){
					try {
						HttpSendRequest.sendPost(server,payload);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
			compteur++;
		}
		return compteur;
	}
	
	
	
}
