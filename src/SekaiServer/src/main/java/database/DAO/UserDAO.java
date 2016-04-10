package database.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.controller.Connexion;
import database.entities.User;


/**
 * Created by RABOUDI on 18/11/2015.
 */
public class UserDAO {
    
	private static Logger logger = LoggerFactory.getLogger(UserDAO.class);
    
    public ArrayList<String> getMacAddressList(){
    	String messageDebug = "etting ID of all users"; 
    	logger.debug("G"+messageDebug);
		ArrayList<String> result = new ArrayList<String>();
		String statement = "select MACADDR from USER";
		logger.info(statement);
		ResultSet rs =  Connexion.getInstance().query(statement);
		if (rs != null) {
			try {
				while (rs.next()) {
					String macAddr = rs.getString("MACADDR");
					result.add(macAddr);
				}
			} catch (SQLException e) {
				logger.error("Error while g"+messageDebug);
				logger.error(e.getMessage());
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
    	String messageDebug = "etting id database of user from it's ID="+MAC;
    	logger.debug("G"+messageDebug);
    	String selectStatement="select ID from USER where MACADDR = \""+MAC+"\"";
    	logger.info(selectStatement);
		ResultSet resultSet = Connexion.getInstance().query(selectStatement);
		if (resultSet!=null) {
			try {
				resultSet.next();
		        int userID= resultSet.getInt("ID");	
		        return  userID;
			} catch (SQLException e) {
				logger.error("Error while g"+messageDebug);
				logger.error(e.getMessage());
		    }
		}
		return -1;
    }
    
    public int addUser(String MAC, String name) {
		
		logger.debug("Adding the user with the name="+name+" and id="+MAC);
    	//prepare the insertion statement
		String insertStatement = new String();
		if (name == null) {
			insertStatement = "INSERT INTO USER(MACADDR)\n" + "Values (\""
					+ MAC + "\");";
		} else {
			insertStatement = "INSERT INTO USER(NAME,MACADDR)\n" + "Values (\""
					+ name + "\",\"" + MAC + "\");";
		}
		logger.info(insertStatement);
		// execute the statement
		if ( Connexion.getInstance().update(insertStatement))
			// return the user ID
			return getIDfromMAC(MAC);
		else
			return -1;
	}
    public ArrayList<User> getUsers(){
    	String messageDebug = "etting all users from database";
    	logger.debug("G"+messageDebug);
    	String selectStatement=new String();
    	ArrayList<User> list= new ArrayList<User>();
		 selectStatement="select ID, MACADDR, NAME from USER;";
		 logger.info(selectStatement);
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
	          logger.error("Error while g"+messageDebug);
			  logger.error(e.getMessage());
	          return null;
	      }
		 }
    	return list;
    }
}
