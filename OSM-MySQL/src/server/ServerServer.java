package server;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import constants.Urls;
import utils.HttpSendRequest;

public class ServerServer {
	public static int notifyAdd(final JSONObject payload, ArrayList<String> servers){
		int compteur=0;
		for(final String server : servers){
			if (server.equals(Urls.IP)){
				continue;
			}
			payload.remove("isServer");
			payload.put("isServer", true);
			final String url = "http://"+server+":8080/SendGPS";
			Thread t = new Thread(new Runnable(){
				public void run(){
					try {
						HttpSendRequest.sendPost(url,payload);
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
