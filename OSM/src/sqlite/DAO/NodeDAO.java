package sqlite.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import sqlite.controller.Connexion;
import sqlite.entities.Node;
import sqlite.entities.User;

/**
 * Created by RABOUDI on 18/11/2015.
 */
public class NodeDAO {
    private Connexion DBconx=null;
    public NodeDAO(Connexion DBconx) {
        this.DBconx=DBconx;
    }
	public int addNode(float longitude, float latitude) {
		String insertStatement=
			"INSERT INTO NODE (LONGITUDE,LATITUDE) Values(" +
			longitude +
			"," +
			latitude +
			");";
		 DBconx.update(insertStatement);
		return getIDfromLonLat(longitude,latitude);
	}
	
	public int getIDfromLonLat(float longitude, float latitude) {
		String selectStatement=new String();
		 selectStatement="select ID from NODE " +
		 		"where LONGITUDE=="+longitude+" and LATITUDE=="+latitude+";";
		 ResultSet resultSet =DBconx.query(selectStatement);
		 try {
	         int nodeID= resultSet.getInt("ID");	
	         return  nodeID;
		  } catch (SQLException e) {
	          e.printStackTrace();
	      }
		  return -1;
	}
	public ArrayList<Node> getAllNodes() {
		ArrayList<Node> result=new ArrayList<Node>();
    	ResultSet rs= DBconx.query("select * from Node");
        try {
        	while (rs.next()) {
            int nodeID= rs.getInt("ID");
            float longitude = rs.getFloat("LONGITUDE");
            float latitude = rs.getFloat("LATITUDE");
            Node node = new Node(nodeID,longitude,latitude);
            result.add(node);
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return result;
	}
	public Node getNodeByID(int nodeID) {
		// TODO Auto-generated method stub
    	ResultSet rs= DBconx.query("select * from Node where ID="+nodeID);
    	Node result = null;
    	try {
			int id = rs.getInt("ID");
			float lon = rs.getFloat("LONGITUDE");
	    	float lat = rs.getFloat("LATITUDE");
	    	result = new Node(id,lon,lat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return result;
	}
}
