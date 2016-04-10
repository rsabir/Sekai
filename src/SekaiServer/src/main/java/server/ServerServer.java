package server;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.Urls;
import utils.HttpSendRequest;
import utils.PayloadUtils;

public class ServerServer {
	private static Logger logger = LoggerFactory.getLogger(ServerServer.class);
	public static int notifyAdd(final JSONObject payload, final ArrayList<String> servers){
		logger.info("Send request "+payload+" to "+servers);
		int compteur=0;
		for(final String server : servers){
			if (server.equals(Urls.IP)){
				continue;
			}
			payload.remove(PayloadUtils.KEY_ISSERVER);
			payload.put(PayloadUtils.KEY_ISSERVER, true);
			payload.put(PayloadUtils.KEY_IP,Urls.IP);
			final String url = "http://"+server+"/"+Urls.PATHSENDGPS;
			Thread t = new Thread(new Runnable(){
				public void run(){
					try {
						HttpSendRequest.sendPost(url,payload);
					} catch (Exception e) {
						logger.error("Error while sending request "+payload+" to "+servers);
						logger.error(e.getMessage());
					}
				}
			});
			t.start();
			compteur++;
		}
		return compteur;
	}
	
	
	
}
