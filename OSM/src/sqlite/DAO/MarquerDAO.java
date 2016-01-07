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
        ResultSet rs= DBconx.query("select ID, NAME, MACADDR from User where ID="+ userID);
        try {
            String name = rs.getString("NAME");
            String macAddr = rs.getString("MACADDR");
            return new User(userID,name,macAddr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
        }
        return null;
    }
 
	public void addMarquer(int userID, int nodeID, String date) {
		String insertStatement = "INSERT INTO Marquer(USERID,NODEID,DATEOFADD)\n" +
				"Values (" +
				userID +
				"," +
				nodeID +
				",\"" +
				date +
				"\");\n" ;
		   DBconx.update(insertStatement);
			
	}

	public Marquer getRecentMarquer(int userId) {
		Marquer result=null;
		//System.out.println(userId);
		String queryStatement="select * from Marquer where userID="+
		userId+
		"  and strftime('%s','now') +3600 - strftime('%s',DATEOFADD)<20 order by DATEOFADD DESC limit 1"; // 3600 pour avoir l'heure de france
		ResultSet resultSet =DBconx.query(queryStatement);
		try {
		if (resultSet.next()) {
			int id = resultSet.getInt("userID");
		    int node = resultSet.getInt("nodeID");
		    String dateStr = resultSet.getString("dateofadd");
		    SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    Date dateOfAdd = null;
			  
			try {
				dateOfAdd = fd.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		    result=new Marquer(id,node,dateOfAdd);
			  }
			 } catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return result;
	}
	public Marquer getRecentMarquerClient(int userId) {
		Marquer result=null;
		//System.out.println(userId);
		String queryStatement="select * from Marquer where userID="+
		userId+
		"  order by DATEOFADD DESC limit 1"; // 3600 pour avoir l'heure de france
		ResultSet resultSet =DBconx.query(queryStatement);
		try {
			  if (resultSet.next()) {
			int id = resultSet.getInt("userID");
		    int node = resultSet.getInt("nodeID");
		    String dateStr = resultSet.getString("dateofadd");
		    SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    Date dateOfAdd = null;
			  
			try {
				dateOfAdd = fd.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    result=new Marquer(id,node,dateOfAdd);
			  }
			 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	public ArrayList<Marquer> getMarquersToday(int userID) {
		// TODO Auto-generated method stub
		/*
		 * select sur ceux qui ont la date d'aujourd'hui
		 * 
		 */
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DATE, 1);  // number of days to add
//		String dt = sdf.format(c.getTime());
		/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date myDate = null;
		try {
			myDate = df.parse(dt);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		df.applyPattern("yyyy-MM-dd HH:mm:ss");
		String myDateString =df.format(myDate);
		System.out.println(dt+"  "+myDateString);*/
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    // Date dateobj = new Date();
		ResultSet resultSet =DBconx.query("select * from marquer where userID = "+userID
				+" And date(dateofadd)=date('now') ");
        ArrayList<Marquer> li=new ArrayList<Marquer>();
        try {
        	//System.out.println(resultSet.next());
            while (resultSet.next()) {
            	System.out.println(resultSet.next());
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
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
				
            }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return li;
	}

	public ArrayList<Marquer> getMarquersYesterday(int userID) {
		ResultSet resultSet =DBconx.query(" SELECT * FROM Marquer where userid= "+userID
				+" and strftime('%s',date('now'))-strftime('%s',dateofadd)>=3600*24 and strftime('%s',date('now'))-strftime('%s',dateofadd)<=3600*48 ");
        ArrayList<Marquer> li=new ArrayList<Marquer>();
        try {
        	//System.out.println(resultSet.next());
            while (resultSet.next()) {
            	System.out.println(resultSet.next());
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
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
				
            }
        } catch (Exception e) {
			//e.printStackTrace();
		}
		return li;
	}


}
