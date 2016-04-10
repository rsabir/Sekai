package database.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.controller.Connexion;
import database.entities.Node;


/**
 * Created by RABOUDI on 18/11/2015.
 */
public class NodeDAO {
	private Logger logger = LoggerFactory.getLogger(NodeDAO.class);
    
	public NodeDAO() {
    }
	public int addNode(float longitude, float latitude) {
		logger.debug("Adding the location lat="+latitude+" lon="+longitude);
		String insertStatement=
			"INSERT INTO NODE (LONGITUDE,LATITUDE) Values(" +
			longitude +
			"," +
			latitude +
			");";
		logger.debug(insertStatement);
		if( Connexion.getInstance().update(insertStatement))
		return getIDfromLonLat(longitude,latitude);
		else return -1;
	}
	
	public int getIDfromLonLat(float longitude, float latitude) {
		String selectStatement=new String();
		logger.debug("Getting id of lat="+latitude+" lon="+longitude);
		 selectStatement="select ID from NODE " +
		 		"where LONGITUDE="+longitude+" and LATITUDE="+latitude+";";
		 logger.info(selectStatement);
		 ResultSet resultSet = Connexion.getInstance().query(selectStatement);
		 if (resultSet!=null) {
		 try {
			 resultSet.next();
	         int nodeID= resultSet.getInt("ID");	
	         return  nodeID;
		  } catch (SQLException e) {
			  logger.error("Error while getting id of lat="+latitude+" lon="+longitude);
	          logger.error(e.getMessage());
	      }
		 }
		  return -1;
	}
	public ArrayList<Node> getAllNodes() {
		logger.debug("Getting all locations from database");
		ArrayList<Node> result=new ArrayList<Node>();
		String statement = "select * from NODE";
		logger.info(statement);
    	ResultSet rs=  Connexion.getInstance().query(statement);
    	if (rs==null) return null;
        try {
        	while (rs.next()) {
            int nodeID= rs.getInt("ID");
            float longitude = rs.getFloat("LONGITUDE");
            float latitude = rs.getFloat("LATITUDE");
            Node node = new Node(nodeID,longitude,latitude);
            result.add(node);
        	}
        } catch (SQLException e) {
        	logger.error("Getting all locations from database");
            logger.error(e.getMessage());
            return null;
        }
		return result;
	}
	public Node getNodeByID(int nodeID) {
		logger.debug("Getting a location by it's id="+nodeID);
		String statement = "select * from NODE where ID="+nodeID;
		logger.info(statement);
    	ResultSet rs=  Connexion.getInstance().query(statement);
    	Node result = null;
    	if (rs==null) return null;
    	try {
    		rs.next();
			int id = rs.getInt("ID");
			float lon = rs.getFloat("LONGITUDE");
	    	float lat = rs.getFloat("LATITUDE");
	    	result = new Node(id,lon,lat);
		} catch (SQLException e) {
			logger.error("Error while getting a location by it's id="+nodeID);
			logger.error(e.getMessage());
		}
		return result;
	}
}
