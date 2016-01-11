package listener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.ConnectException;
import java.net.Socket;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.apache.tomcat.dbcp.dbcp.SQLNestedException;

import com.mysql.jdbc.CommunicationsException;

import constants.PathsC;
import constants.Urls;

import static java.nio.file.StandardCopyOption.*;

/**
 * Created by RABOUDI on 22/11/2015.
 * CommunicationsException ConnectException : exception rised wher there is no mysql
 * SQLNestedException : exception rised when there is no database
 */
public class InitConfig implements ServletContextListener {
	
	public InitConfig() {
		
		
			}

	
		@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String configPath = PathsC.PATHCONFIGJSON;
		Path p = Paths.get(configPath);
		if (Files.notExists(p)){
			String tmpPath = event.getServletContext().getRealPath(File.separator+"WEB-INF"+File.separator+"config.json");
			
			File theDir = new File(PathsC.PATHCONFIG);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
			    System.out.println("creating directory: " + PathsC.PATHCONFIG);
			    boolean result = false;

			    try{
			        theDir.mkdir();
			        result = true;
			    } 
			    catch(SecurityException se){
			    	System.out.println("Please run the server as root");
					System.exit(-1);			        
			    }
			}
			System.out.println("creating directory: " + PathsC.PATHCONFIGJSON);
			p =  Paths.get(configPath);
			Path tmpPathP =  Paths.get(tmpPath);
			try {
				Files.copy(tmpPathP,p,REPLACE_EXISTING );
			} catch (IOException e) {
				System.out.println("Please run the server as root");
				System.exit(-1);	
			}
		}
	}
}
