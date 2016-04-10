package listener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.ConnectException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.apache.tomcat.dbcp.dbcp.SQLNestedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.CommunicationsException;
import com.sun.naming.internal.ResourceManager;

import constants.Mysql;
import utils.Logs;
import utils.Settings;

/**
 * Created by RABOUDI on 22/11/2015.
 * CommunicationsException ConnectException : exception rised wher there is no mysql
 * SQLNestedException : exception rised when there is no database
 */
public class InitListener implements ServletContextListener {
	private Connection conn = null;
	private static Logger logger = LoggerFactory.getLogger(InitListener.class);
	Settings settings;
	
	public InitListener() {
	}

	private boolean testMySQL(String port){
		boolean isUp;
		logger.info("Testing if the server is running under the port:"+port); 
		 try {
		        Socket socket = new Socket("127.0.0.1", Integer.valueOf(port));
		        // Server is up
		        socket.close();
		        isUp=true;
		    } catch (Exception e) {
		    	logger.error("The server is not running under the port:"+port);
		    	isUp=false;
		    }
		return isUp;
	}
	
	
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("Clossing the connection with the database");
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Error occured while closing the connection with the database");
				logger.error(e.getMessage());
			}
		}
		conn = null; 
	}

	public void contextInitialized(ServletContextEvent event) {

		settings = (new Settings()).init();
		Logs.getInstance().load();
		
		if (testMySQL(Mysql.PORT)) {
		try {
			 final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
			Class.forName(JDBC_DRIVER);
			String username = Mysql.USERNAME;
			String password = Mysql.PASSWORD;
			
			
			database.controller.Connexion connectionTester = database.controller.Connexion.getInstance(); 
			if (connectionTester == null){
				Connection con =  DriverManager.getConnection("jdbc:mysql://localhost:"+Mysql.PORT+"/",Mysql.USERNAME,Mysql.PASSWORD);
				String statement = "CREATE DATABASE "+Mysql.DATABASE;
				logger.debug(statement);
				int Result=con.createStatement().executeUpdate(statement);
				connectionTester = database.controller.Connexion.getInstance(); 
			} 
			
			conn = connectionTester.getMysqlConnection();
			Statement stm = conn.createStatement();
	         String createStatement = 
	        	 "CREATE TABLE IF NOT EXISTS USER   ("+
	        			    "ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
	        			    "NAME VARCHAR(30),"+
	        			   " MACADDR VARCHAR(30) UNIQUE NOT NULL"+
	        			");";
	         logger.debug(createStatement);
	         stm.executeUpdate(createStatement);
	         createStatement =
	        			"CREATE TABLE IF NOT EXISTS NODE ("+
	        			    "ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
	        			    "LONGITUDE DOUBLE NOT NULL,"+
	        			    "LATITUDE DOUBLE NOT NULL"+
	        			")";
	         logger.debug(createStatement);
	         stm.executeUpdate(createStatement);
	         createStatement =
	        			"CREATE TABLE IF NOT EXISTS MARQUER ("+
	        			    "USERID INT NOT NULL REFERENCES USER (ID),"+
	        			    "NODEID INT NOT NULL REFERENCES Node (ID),"+
	        			    "DATEOFADD TIMESTAMP NOT NULL,"+
	        			    "PRIMARY KEY (USERID , NODEID, DATEOFADD)"+
	        			")";
	         logger.debug(createStatement);
	         stm.executeUpdate(createStatement);
		} catch (CommunicationsException e) {
			logger.error("!!! : Nothing running on port "+Mysql.PORT+" or MySQL is stopped, please provide the correct port on configuration file");
			System.exit(1);
		
		}catch (SQLException e) {
			logger.error("!!! : Please check your configuration for mysql and data sources (username,password,port)");
			logger.error(e.getMessage());
			System.exit(1);
		} catch (ClassNotFoundException e) {
			logger.error("!!! : Error while loading the driver of mysql");
			logger.error(e.getMessage());
		}
		} else {
			logger.error("!!! : The database server is not running on port "+Mysql.PORT+", please provide the port on configuration file");
			System.exit(1);
		}

	}
}
