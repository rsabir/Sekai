package sqlite.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sqlite.controller.Connexion;
import sqlite.entities.Marquer;
import sqlite.entities.Node;
import sqlite.entities.User;

/**
 * Created by RABOUDI on 18/11/2015.
 */
public class MarquerDAO {
    private Connexion DBconx=null;
    
    public MarquerDAO(Connexion DBconx){
        this.DBconx=DBconx;
        //DBconx.connect();
    }

    private User getUser(int userID){
        ResultSet rs= DBconx.query("select ID, NAME, MACADDR from USER where ID="+ userID);
        try {
            String name = rs.getString("NAME");
            String macAddr = rs.getString("MACADDR");
            return new User(userID,name,macAddr);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Node getNode(int nodeID){
        ResultSet rs= DBconx.query("select ID, " +
                "LONGITUDE, LATITUDE from Node where ID="+ nodeID);
        try {
            long longitude = rs.getLong("LONGITUDE");
            long latitude = rs.getLong("LATITUDE");
            return new Node(nodeID,longitude,latitude);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
 
	public boolean addMarquer(int userID, int nodeID, String date) {
		// String insertStatement = "INSERT INTO Marquer(USERID,NODEID,DATEOFADD)\n"
			//	+ "Values (" + userID + "," + nodeID + ",\"" + date + "\");\n";

		String insertStatement = "INSERT INTO MARQUER(USERID,NODEID,DATEOFADD)\n"
				+ "Values (" + userID + "," + nodeID + ",NOW());\n";
		if (DBconx.update(insertStatement))
			return true;
		return false;
	}

	public Marquer getRecentMarquer(int userId) {
		Marquer result=null;
		//System.out.println(userId);
		String queryStatement="Select * from MARQUER where " +
				"time_to_sec(NOW())-time_to_sec(dateofadd)<20 and " +
				"userid = "+userId; // 3600 pour avoir l'heure de france
		ResultSet resultSet =DBconx.query(queryStatement);
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
				System.out.println("date problem");
			}
			  }
			 } catch (SQLException e) {
				 return null;
		}
		
		return result;
	}
	
	public ArrayList<Marquer> getMarquersToday(int userID) {

		ResultSet resultSet = DBconx
				.query("Select * from marquer where DATEDIFF(NOW(),dateofadd)=0 AND userid = "
						+ userID);
		ArrayList<Marquer> li = new ArrayList<Marquer>();
		if (resultSet == null)
			return null;
		try {
			// System.out.println(resultSet.next());
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
					System.out.println("date problem");
				}
			}
		} catch (Exception e) {
			return null;
		}
		return li;
	}

	public ArrayList<Marquer> getMarquersYesterday(int userID) {
		ResultSet resultSet = DBconx
		.query("Select * from MARQUER where DATEDIFF(NOW(),dateofadd)=0 AND userid = "
				+ userID);
		if (resultSet == null)
			return null;
        ArrayList<Marquer> li=new ArrayList<Marquer>();
        try {
        	//System.out.println(resultSet.next());
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
					System.out.println("date problem");
				}
				
				
            }
        } catch (Exception e) {
        	return null;
		}
		return li;
	}


}
