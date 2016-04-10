package database.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.controller.Connexion;
import database.entities.Marquer;
import database.entities.Node;
import database.entities.User;
import utils.DateUtils;


/**
 * Created by RABOUDI on 18/11/2015.
 */
public class MarquerDAO {
    
	private static Logger logger = LoggerFactory.getLogger(MarquerDAO.class);
    public MarquerDAO(){
    }

    private User getUser(int userID){
    	logger.debug("Getting infos of userID with ID database = "+userID);
    	String queryStatement= "select ID, NAME, MACADDR from USER where ID="+ userID;
    	logger.info(queryStatement);
    	ResultSet rs=  Connexion.getInstance().query(queryStatement);
        try {
            String name = rs.getString("NAME");
            String macAddr = rs.getString("MACADDR");
            return new User(userID,name,macAddr);
        } catch (SQLException e) {
            logger.error("Fail at getting infos of the user"+userID);
            logger.error(e.getMessage());
            return null;
        }
    }
    private Node getNode(int nodeID){
    	logger.debug("Getting the location entry with id="+nodeID);
    	String queryStatement = "select ID, LONGITUDE, LATITUDE from NODE where ID="+ nodeID;
    	logger.debug(queryStatement);
        ResultSet rs=  Connexion.getInstance().query(queryStatement);
        try {
            long longitude = rs.getLong("LONGITUDE");
            long latitude = rs.getLong("LATITUDE");
            return new Node(nodeID,longitude,latitude);
        } catch (SQLException e) {
        	logger.error("Fail while getting the location entry with id="+nodeID);
        	logger.error(e.getMessage());
            return null;
        }
    }
 
	public boolean addMarquer(int userID, int nodeID, String date) {
		logger.debug("Adding a marquer entery : userID="+userID+" nodeID="+nodeID+" date="+date );
		String insertStatement = "INSERT INTO MARQUER(USERID,NODEID,DATEOFADD)\n"
				+ "Values (" + userID + "," + nodeID + ",NOW());\n";
		logger.info(insertStatement);
		if ( Connexion.getInstance().update(insertStatement))
			return true;
		return false;
	}

	public Marquer getRecentMarquer(int userId) {
		logger.debug("Getting the recent marquer of the user with ID of database="+userId);
		Marquer result=null;
		String queryStatement="Select * from MARQUER where " +
				"time_to_sec(NOW())-time_to_sec(dateofadd)<20 and " +
				"userid = "+userId; 
		logger.info(queryStatement);
		ResultSet resultSet = Connexion.getInstance().query(queryStatement);
		if (resultSet==null) return null;
		try {
			if (resultSet.next()) {
				int id = resultSet.getInt("userID");
			    int node = resultSet.getInt("nodeID");
			    String dateStr = resultSet.getString("dateofadd");
			    SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			    Date dateOfAdd = null;
				  
				try {
					dateOfAdd = fd.parse(dateStr);
				    result=new Marquer(id,node,dateOfAdd);
				} catch (ParseException e) {
					logger.error("Error while getting a recent marquer");
					logger.error(e.getMessage());
				}
			}
		} catch (SQLException e) {
			logger.error("Error while getting a recent marquer");
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public ArrayList<Marquer> getMarquersGivenDay(int userID, String date) throws ParseException{
		logger.debug("Getting marquer of the user with id database="+userID+" and of "+date);
		date = DateUtils.formatDate(date,"dd/MM/yyyy", "yyyy-MM-dd");
		String statement = "Select * from MARQUER "+
					" WHERE userid = "+ userID+
					" AND DATE(dateofadd) = '"+date+"'";
		logger.info(statement);
		ResultSet resultSet = Connexion.getInstance()
				.query(statement);
		ArrayList<Marquer> resultList = new ArrayList<Marquer>();
		if (resultSet == null)
			return null;
		try {
			while (resultSet.next()) {
				int user = resultSet.getInt("USERID");
				int nodeID = resultSet.getInt("NODEID");
				String dateOfAddStr = resultSet.getString("DATEOFADD");
				SimpleDateFormat fd = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date dateOfAdd = null;
				try {
					dateOfAdd = fd.parse(dateOfAddStr);
					Marquer marq = new Marquer(user, nodeID, dateOfAdd);
					resultList.add(marq);
				} catch (ParseException e) {
					logger.error("Fail while getting marquer of the user with id "
							+ "database="+userID+" and of "+date);
					logger.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("Fail while getting marquer of the user with id "
					+ "database="+userID+" and of "+date);
			logger.error(e.getMessage());
			return null;
		}
		return resultList;		
	}
	
	public ArrayList<Marquer> getMarquersToday(int userID) {
		logger.debug("Getting marquer of the user with id database="+userID+" and of today");
		String queryStatement = "Select * from MARQUER where DATEDIFF(NOW(),dateofadd)=0 AND userid = "
				+ userID;
		logger.info(queryStatement);
		ResultSet resultSet = Connexion.getInstance()
				.query(queryStatement);
		ArrayList<Marquer> li = new ArrayList<Marquer>();
		if (resultSet == null)
			return null;
		try {
			while (resultSet.next()) {
				int user = resultSet.getInt("USERID");
				int nodeID = resultSet.getInt("NODEID");
				String dateOfAddStr = resultSet.getString("DATEOFADD");
				SimpleDateFormat fd = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date dateOfAdd = null;
				try {
					dateOfAdd = fd.parse(dateOfAddStr);
					Marquer marq = new Marquer(user, nodeID, dateOfAdd);
					li.add(marq);
				} catch (ParseException e) {
					logger.error("Error while getting marquer of the user with id database="+userID+" and of today");
					logger.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("Error while getting marquer of the user with id database="+userID+" and of today");
			logger.error(e.getMessage());
			return null;
		}
		return li;
	}

	public ArrayList<Marquer> getMarquersYesterday(int userID) {
		logger.debug("Getting marquer of the user with id database="+userID+" and of yesterday");
		String statement = "Select * from MARQUER where DATEDIFF(NOW(),dateofadd)=0 AND userid = "
				+ userID;
		logger.info(statement);
		ResultSet resultSet =  Connexion.getInstance()
				.query(statement);
		
		if (resultSet == null)
			return null;
        ArrayList<Marquer> li=new ArrayList<Marquer>();
        try {
            while (resultSet.next()) {
                int user= resultSet.getInt("USERID");
                int nodeID= resultSet.getInt("NODEID");
                String dateOfAddStr= resultSet.getString("DATEOFADD");
                SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateOfAdd = null;
				try {
					dateOfAdd = fd.parse(dateOfAddStr);
					Marquer marq=new Marquer(user,nodeID,dateOfAdd);
					li.add(marq);
				} catch (ParseException e) {
					logger.error("Error while getting marquer of the user with id database="+userID+" and of yesterday");
					logger.error(e.getMessage());
				}
				
				
            }
        } catch (Exception e) {
        	logger.error("Error while getting marquer of the user with id database="+userID+" and of yesterday");
			logger.error(e.getMessage());
        	return null;
		}
		return li;
	}


}
