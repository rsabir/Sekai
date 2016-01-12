package sqlite.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sqlite.controller.Connexion;
import sqlite.entities.User;

/**
 * Created by RABOUDI on 18/11/2015.
 */
public class UserDAO {
private Connexion DBconx=null;
    
    public UserDAO(Connexion DBconx){
        this.DBconx=DBconx;
        DBconx.connect();
    }

    public ArrayList<String> getMacAddressList(){
    	ArrayList<String> result=new ArrayList<String>();
    	ResultSet rs= DBconx.query("select MACADDR from User");
        try {
        	while (rs.next()) {
            String macAddr = rs.getString("MACADDR");
            result.add(macAddr);
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return result;
    	
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
		 ResultSet resultSet =DBconx.query(selectStatement);
		 try {
	         int userID= resultSet.getInt("ID");	
	         return  userID;
		  } catch (SQLException e) {
	          e.printStackTrace();
	      }
		  return -1;
    }
    public int addUser(String MAC, String name) {
		
		//verify the MAC address
		MAC.replace("-", ":");
		if (!utils.DB.verifyMacAddr(MAC)) return -1;
		//prepare the insertion statement
		String insertStatement= new String();
		if (name==null){
			//System.out.println("lol2");
		 insertStatement =
             "INSERT INTO User(MACADDR)\n"+
             		"Values (\"" +
             		MAC +
             		"\");" ;
		}
		else {
			//System.out.println("lol1");
		 insertStatement = 
			 "INSERT INTO User(NAME,MACADDR)\n" +
			 "Values (\"" +
			 name +
			 "\",\"" +
			 MAC +
			 "\");" ;
		}
		//execute the statement
		 DBconx.update(insertStatement);
		 //return the user ID
		return getIDfromMAC(MAC);
	}
    public ArrayList<User> getUsers(){
    	String selectStatement=new String();
    	ArrayList<User> list= new ArrayList<User>();
		 selectStatement="select ID, MACADDR, NAME from USER;";
		 ResultSet resultSet =DBconx.query(selectStatement);
		 try {
			 while (resultSet.next()) {
			// System.out.println("hiiii");
	         int userID= resultSet.getInt("ID");
	         String MACADDR = resultSet.getString("MACADDR");	
	         String NAME = resultSet.getString("NAME");	
	         list.add(new User(userID, NAME, MACADDR));
	         //list.add(new User(userID, MACADDR));
			 }
		  } catch (SQLException e) {
	          e.printStackTrace();
	      }
		 // System.out.println(list);
    	return list;
    }
}
