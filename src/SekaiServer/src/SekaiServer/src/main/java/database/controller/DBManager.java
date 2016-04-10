package database.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;
import database.DAO.PositionDAO;
import database.DAO.PositionDAOImpl;
import database.DAO.UserDAO;
import database.DAO.UserDAOImpl;
import database.entities.Position;
import database.entities.User;


public class DBManager {

	private UserDAO userDAO = null;
	private PositionDAO positionDao;

	public DBManager(String serverName){
		userDAO = new UserDAOImpl();
		positionDao = new PositionDAOImpl();
	}

	public boolean addData(String Id, float lng,float lat){

		
		User user= new User(Id);
		Position position= new Position(lat,lng,user);
		userDAO.addUser(user);
		positionDao.addPosition(position);
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public LinkedList<Map<String, Comparable>> getAllData(){
		LinkedList<Map<String, Comparable>> clientList= new LinkedList();
		/**
		 * pour chaque user 
		 * chercher le marquer correspondant
		 * cad le plus r�cent
		 * constituer le Map
		 * constituter la liste
		 */


		LinkedList<User> users= new LinkedList<User>();
		users.addAll(userDAO.getAllUsers());
		Position position;
		Map client ;
		User user;
		if(users!=null){
			for(int i=0;i<users.size();i++){
				user=users.get(i);
				position=user.getLastPosition();
				if(position!=null){
					client = new LinkedHashMap();
					client.put("lat", position.getLat());
					client.put("lgn", position.getLng());
					client.put("id",user.getUserId());
					clientList.add(client);
				}
			}
			System.out.println(clientList);
			return clientList;
		}
		return null;


	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getClientData(String Id){
		Map client = new LinkedHashMap();

		User user= userDAO.getUserById(Id);
		if(user!=null){
			Position position = user.getLastPosition();
			if(position!=null){
				client.put("lat",position.getLat());
				client.put("lgn",position.getLng());
				client.put("id", user.getUserId());
			}
			return client;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public LinkedList<Map<String, Comparable>> getClientDataToday(String Id) {
		// TODO Auto-generated method stub
		/*
		 * avoir l'id a partir de la mac adresse
		 * aller chercher les marquers de la journ�e
		 * extraire leurs longitude latitude node id
		 * construire les client
		 * les renvoyer
		 * 
		 */
		User user = userDAO.getUserById(Id);

		if(user!=null){
			Date date1 = null,date2;
			Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			int day= localCalendar.get(Calendar.DATE);
			int month =localCalendar.get(Calendar.MONTH) ;
			int year= localCalendar.get(Calendar.YEAR);
			try {
				date1= sdf.parse(day+"/"+month+"/"+year);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			date2= new Date(date1.getTime() + (1000 * 60 * 60 * 24));
			LinkedList <Position> p = new LinkedList<Position>();
			p.addAll(positionDao.getPositionBydateByUser(user.getUserId(), date1, date2));
			if(p!=null){
				LinkedList<Map<String, Comparable>> result = new LinkedList<Map<String, Comparable>>();
				Map<String, Comparable> client= new LinkedHashMap<String, Comparable>();
				for(int i=0;i<p.size();i++){
					client.put("lat", p.get(i).getLat());
					client.put("lon", p.get(i).getLng());
					result.add(client);
				}
				return result;
			}

		}

		return null;
	}
	@SuppressWarnings("rawtypes")
	public LinkedList< Map<String, Comparable>> getClientDataYesterday(String Id) {
		User user = userDAO.getUserById(Id);

		if(user!=null){
			Date date1 = null,date2;
			Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			int day= localCalendar.get(Calendar.DATE);
			int month =localCalendar.get(Calendar.MONTH) ;
			int year= localCalendar.get(Calendar.YEAR);
			try {
				date1= sdf.parse(day+"/"+month+"/"+year);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			date2= new Date(date1.getTime() - (1000 * 60 * 60 * 24));
			LinkedList <Position> p = new LinkedList<Position>();
			p.addAll(positionDao.getPositionBydateByUser(user.getUserId(), date1, date2));
			if(p!=null){
				LinkedList<Map<String, Comparable>> result = new LinkedList<Map<String, Comparable>>();
				Map<String, Comparable> client= new LinkedHashMap<String, Comparable>();
				for(int i=0;i<p.size();i++){
					client.put("lat", p.get(i).getLat());
					client.put("lon", p.get(i).getLng());
					result.add(client);
				}
				return result;
			}

		}

		return null;

	}
}
