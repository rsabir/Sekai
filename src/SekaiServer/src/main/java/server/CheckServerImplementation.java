package server;

import java.util.ArrayList;

import server.prototype.CheckServer;

public class CheckServerImplementation implements CheckServer{

	public boolean isServer(String ip, ArrayList<String> servers) {
		for (int i=0;i<servers.size();i++){
			if (servers.get(i).equals(ip))
				return true;
		}
		return false;
	}

}
