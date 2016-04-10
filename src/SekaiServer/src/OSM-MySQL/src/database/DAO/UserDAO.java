package database.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.controller.Connexion;
import database.entities.User;


/**
 * Created by RABOUDI on 18/11/2015.
 */
public class UserDAO {
    
    public UserDAO(){
    }

    public ArrayList<String> getMacAddressList(){
		ArrayList<String> result = new ArrayList<String>();
			ResultSet rs =  Connexion.getInstance().query("select MACADDR from USER");
			if (rs != null) {
				try {
					while (rs.next()) {
						String macAddr = rs.getString("MACADDR");
						result.add(macAddr);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
				return result;
			} else
				return null;
    }

	/**
	 * 
	 * @param MAC
	 * @param name
	 * @return userID if success, -1 if failure
	 */
    public int getIDfromMAC(String MAC){
    	String selectStatement=new String();
		 selectStatement="select ID from USER where MACADDR = \""+MAC+"\"";
		 ResultSet resultSet = Connexion.getInstance().query(selectStatement);
		 if (resultSet!=null) {
		 try {
			 resultSet.next();
	         int userID= resultSet.getInt("ID");	
	         return  userID;
		  } catch (SQLException e) {
	          e.printStackTrace();
	      }
		 }
		  return -1;
    }
    
    public int addUser(String MAC, String name) {
		
		//verify the MAC address
		MAC.replace("-", ":");
		//if (!utils.DB.verifyMacAddr(MAC)) return -1;
		//prepare the insertion statement
		String insertStatement = new String();
		if (name == null) {
			insertStatement = "INSERT INTO USER(MACADDR)\n" + "Values (\""
					+ MAC + "\");";
		} else {
			insertStatement = "INSERT INTO USER(NAME,MACADDR)\n" + "Values (\""
					+ name + "\",\"" + MAC + "\");";
		}
		// execute the statement
		if ( Connexion.getInstance().update(insertStatement))
			// return the user ID
			return getIDfromMAC(MAC);
		else
			return -1;
	}
    public ArrayList<User> getUsers(){
    	String selectStatement=new String();
    	ArrayList<User> list= new ArrayList<User>();
		 selectStatement="select ID, MACADDR, NAME from USER;";
		 ResultSet resultSet = Connexion.getInstance().query(selectStatement);
		 if (resultSet!=null) {
		 try {
			 while (resultSet.next()) {
			// System.out.println("hiiii");
	         int userID= resultSet.getInt("ID");
	         String MACADDR = resultSet.getString("MACADDR");	
	         String NAME = resultSet.getString("NAME");	
	         list.add(new User(userID, NAME, MACADDR));
			 }
		  } catch (SQLException e) {
	          e.printStackTrace();
	          return null;
	      }
		 }
		 // System.out.println(list);
    	return list;
    }
}
