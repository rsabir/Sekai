package database.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.Mysql;

public class Connexion {
	 private static volatile Connexion instance = null;
	 private static Connection connection = null;
		//private Statement statement = null;
	private static Context context = null;
	private static DataSource datasource = null;
	private static Logger logger = LoggerFactory.getLogger(Connection.class);

	 private Connexion() throws SQLException{
		 super();
		 // Get the context and create a connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:"+Mysql.PORT+"/"+Mysql.DATABASE,Mysql.USERNAME,Mysql.PASSWORD);
		logger.info("Connection To Database succeded");
	 }

public final static Connexion getInstance() {
	 if (Connexion.instance == null) {
		 synchronized(Connexion.class) {
             if (Connexion.instance == null) {
               try {
            	   Connexion.instance = new Connexion();
               } catch (SQLException e) {
				return null;
               }
             }
           }
	 }
	return Connexion.instance;
	
}
public static Connection getMysqlConnection(){
	return connection;
}
public static void destruct(){
	Connexion.instance = null;
}
public ResultSet query(String request) {
	 ResultSet resultat = null;
      try {
      	Statement statement = connection.createStatement();
          resultat = statement.executeQuery(request);
      } catch (SQLException e) {
          logger.error("Error while doing the request : " + request);
          logger.error(e.getMessage());
          return null;
      }
     // close();
      return resultat;
}

public boolean update(String request) {

	 try {
    	Statement statement = connection.createStatement();
        statement.executeUpdate(request);
    } catch (SQLException e) {
        logger.error("Error while doing the request : " + request);
        logger.error(e.getMessage());
        return false;
    }
    
    //close();
    return true;
}
}

