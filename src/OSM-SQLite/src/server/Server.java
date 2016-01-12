package server;

import java.util.ArrayList;

import constants.Urls;

public class Server {
	private static float minLat;
	private static float maxLat;
	private static float minLgn;
	private static float maxLgn;
	
	
	public static void refresh(ArrayList<ArrayList<Object>> configList){
		for (int i=0; i<configList.size(); i++){
			ArrayList<Object> tmp = configList.get(i);
			if (tmp.get(4).toString().equals(Urls.IP)){
				maxLat = Float.parseFloat(tmp.get(0).toString());
				minLat = Float.parseFloat(tmp.get(1).toString());
				maxLgn = Float.parseFloat(tmp.get(2).toString()); 
				minLgn = Float.parseFloat(tmp.get(3).toString());
				return;
			}
		}
	}


	public static float getMinLat() {
		return minLat;
	}


	public static float getMaxLat() {
		return maxLat;
	}


	public static float getMinLgn() {
		return minLgn;
	}


	public static float getMaxLgn() {
		return maxLgn;
	}
	

}
