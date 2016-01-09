package database.controller;

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
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.SQLNestedException;

import com.mysql.jdbc.CommunicationsException;

/**
 * Created by RABOUDI on 22/11/2015.
 * CommunicationsException ConnectException : exception rised wher there is no mysql
 * SQLNestedException : exception rised when there is no database
 */
public class InitListener implements ServletContextListener {
	Connection conn = null;
	
	public InitListener() {
		killApache("!!!!!!!!!!!!test");
		if (testMySQL(getMySQLPort())) {
		try {
			Context initialContext = new InitialContext();
			Context environmentContext = (Context) initialContext
			.lookup("java:comp/env");
			String dataResourceName = "jdbc/server";
			DataSource dataSource = (DataSource) environmentContext
			.lookup(dataResourceName);
			conn = dataSource.getConnection();
			Statement stm = conn.createStatement();
	         String createStatement = 
	        	 "CREATE TABLE IF NOT EXISTS USER   ("+
	        			    "ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
	        			    "NAME VARCHAR(30),"+
	        			   " MACADDR VARCHAR(30) UNIQUE NOT NULL"+
	        			");";
	         stm.executeUpdate(createStatement);
	         createStatement =
	        			"CREATE TABLE IF NOT EXISTS NODE ("+
	        			    "ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,"+
	        			    "LONGITUDE DOUBLE NOT NULL,"+
	        			    "LATITUDE DOUBLE NOT NULL"+
	        			")";
	         stm.executeUpdate(createStatement);
	         createStatement =
	        			"CREATE TABLE IF NOT EXISTS MARQUER ("+
	        			    "USERID INT NOT NULL REFERENCES USER (ID),"+
	        			    "NODEID INT NOT NULL REFERENCES Node (ID),"+
	        			    "DATEOFADD TIMESTAMP NOT NULL,"+
	        			    "PRIMARY KEY (USERID , NODEID, DATEOFADD)"+
	        			")";
	         stm.executeUpdate(createStatement);
		} catch (SQLNestedException  e) {
			System.err.println("!!! : Please create server database on mySQL or change your configuration");
		} catch (CommunicationsException e) {
			System.err.println("!!! : Nothing running on port 3306 or MySQL is stopped, please provide the port on configuration file");

			//chercher ou est mysql et changer la configuration du port
		
		}catch (SQLException e) {
			//create shema dans la base de données
			System.err.println("!!! : Please check your configuration for mysql and data sources");
		} catch (NamingException e){
			System.err.println("!!! : Please create 'server' database on mySQL or change your configuration");
		}
		} else {
			System.err.println("!!! : MySQL is not running on port 3306, please provide the port on configuration file");
			killApache("!!! : Connection to MySQL server could not be established");
		}
	}

	private boolean testMySQL(int port){
		boolean isUp;
		//C:\ProgramData\MySQL\MySQL Server 5.7/my.ini
		 try {
		        Socket socket = new Socket("127.0.0.1", port);
		        // Server is up
		        socket.close();
		        isUp=true;
		    } catch (ConnectException e) {
		    	isUp=false;
		    }
		    catch (IOException e)
		    {
		        // Server is down
		    	isUp=false;
		    	
		    }
		return isUp;
	}
	private void killApache(String string) {
		/**
		 * command on windows/Linux: 
		 * Tomcat7.exe stop
		 *  sh $CATALINA_HOME/bin/shutdown.sh
		 *  sh $CATALINA_HOME/bin/shutdown.bat
		 */
		System.err.println(string);
		ProcessBuilder pb = null;
		Process p;
		String workingDir = System.getProperty("user.dir");
		//createScript(workingDir);
		String scriptloc=workingDir+"\\tomcat-control.sh";
		String cmd="/bin/bash "+ scriptloc;
		pb = new ProcessBuilder(cmd, "stop");
		pb.directory(new File(workingDir));
		p = null;
        try {
            p = pb.start();
        } catch (IOException ex) {
        	System.err.println("!!! :the tomcat-control script have to be in the folowing directory:"+ workingDir);
        }
        
	}
	/**
	 * not used 
	 * creates an empty file
	 * @param workingDir
	 */
	private void createScript(String workingDir) {
		String script = " Hello ";
	    byte data[] = script.getBytes();
	    Path p = Paths.get(workingDir+"/tomcat-control.sh");
	    try {
	      OutputStream out = new BufferedOutputStream(
	      Files.newOutputStream(p, java.nio.file.StandardOpenOption.CREATE,
	    		  java.nio.file.StandardOpenOption.WRITE));
	      out.write(data);
	    } catch (IOException x) {
	    	System.err.println("!!! :the tomcat-control script");
	    }
		
	}

	private int getMySQLPort() {
		/**
		 * configuration files:
		 * /etc/my.cnf (on Unix/Linux)
		 * C:\WINNT\my.ini (on Windows)
		 */
		// TODO parse the configuration file
		return 3306;
	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		conn = null; // prevent any future access
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
