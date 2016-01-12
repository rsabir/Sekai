package server;

import java.io.IOException;
import java.util.ArrayList;

import constants.Urls;
import utils.HttpSendRequest;

public class ServerClient{
	public static String adressConfig;
	public final static String getConfig="/get";

	public static String getZoneInfo() throws IOException{
		return getZoneInfo(adressConfig);
	}
	public static String getZoneInfo(String address) throws IOException{
		return HttpSendRequest.sendGET(address+getConfig); 
	}
	
	public static boolean isInCharge(ArrayList<String>serversInCharge){
		for (int i=0;i<serversInCharge.size();i++)
			if (serversInCharge.get(i).equals(Urls.IP))
				return true;
		return false;
	}
	
	
}
