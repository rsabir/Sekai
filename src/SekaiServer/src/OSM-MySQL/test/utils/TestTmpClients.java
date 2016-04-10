package utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTmpClients {

	private TmpClients tmpClients;
	@Before
	public void beforeTest(){
		tmpClients = TmpClients.getInstance(); 
	}
	
	@After
	public void afterTest(){
		tmpClients.clean();
	}
	
	@Test
	public void testaddRecentClient() {
		TmpClients.addRecentClient(new Client("a",3.2,4.5,new Date()));
		tmpClients.addRecentClient(new Client("b",3.2,4.6,new Date()));
		assert (tmpClients.getSize()==2);
	}
	
	@Test
	public void testgetRecentClient() {
		tmpClients.getInstance().addRecentClient(new Client("a",3.2,4.5,new Date()));
		ArrayList<Client> clients = tmpClients.getRecentClients();
		assert (clients.size()==1);
		tmpClients.addRecentClient(new Client("b",3.2,4.6,new Date()));
		clients = tmpClients.getRecentClients();
		assert (clients.size()==2);
		clients = tmpClients.getRecentClients();
		assert (clients.size()==2);
		clients = tmpClients.getRecentClients();
		assert (clients.size()==2);
		clients = tmpClients.getRecentClients();
		assert (clients.size()==2);
		Client client0 = clients.get(0);
		Client client1 = clients.get(1);
		assert(client0.getLat()==3.2 && client1.getLat()==3.2 );
		if (client0.getId() == "a"){
			System.out.println(client1.getLon());
			System.out.println(client0.getLon());
			assert(client0.getLon()==4.5);
			assert(client1.getLon()==4.6);
		}else{
			assert(client1.getLon()==4.5);
			assert(client0.getLon()==4.6);
		}
		tmpClients.addRecentClient(new Client("a",3.2,4.6,new Date()));
		clients = tmpClients.getRecentClients();
		assert (clients.size()==2);
		assert(client0.getLat()==3.2 && clients.get(1).getLat()==3.2 );
		assert(client1.getLon()==4.6 && clients.get(0).getLon()==4.6 );
		for (int i=0 ; i<20 ; i++){
			tmpClients.addRecentClient(new Client("a"+i,3.2,4.5,new Date()));
		}
		assert(tmpClients.getSize() == 20+2);
	}
	
	@Test
	public void testgetRecentList() {
		tmpClients.getInstance().addRecentClient(new Client("a",3.2,4.5,new Date()));
		LinkedList<Map<String, Object>> clients = tmpClients.getRecentClientsList();
		assert (clients.size()==1);
		tmpClients.addRecentClient(new Client("b",3.2,4.6,new Date()));
		clients = tmpClients.getRecentClientsList();
		assert (clients.size()==2);
		clients = tmpClients.getRecentClientsList();
		assert (clients.size()==2);
		clients = tmpClients.getRecentClientsList();
		assert (clients.size()==2);
		clients = tmpClients.getRecentClientsList();
		assert (clients.size()==2);
	}

}
