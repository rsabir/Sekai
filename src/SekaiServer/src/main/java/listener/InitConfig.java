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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.CommunicationsException;

import constants.FileNames;
import constants.Others;
import constants.PathsC;
import constants.Urls;

import static java.nio.file.StandardCopyOption.*;

/**
 * Created by RABOUDI on 22/11/2015.
 * CommunicationsException ConnectException : exception rised wher there is no mysql
 * SQLNestedException : exception rised when there is no database
 */
public class InitConfig implements ServletContextListener {
	
	private static Logger logger = LoggerFactory.getLogger(InitConfig.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent event) {
		String configPath = PathsC.PATHCONFIGJSON;
		Path p = Paths.get(configPath);
		if (Files.notExists(p)){
			String tmpPath = event.getServletContext().getRealPath(File.separator+"WEB-INF"
					+File.separator+FileNames.CONFIGJSON);
			
			File theDir = new File(PathsC.PATHCONFIG);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
			    logger.info("Creating directory: " + PathsC.PATHCONFIG);
			    boolean result = false;
			    Others.FIST_TIME=true;
			    try{
			        theDir.mkdir();
			        result = true;
			    } 
			    catch(SecurityException se){
			    	logger.error("Error occured while creating"+PathsC.PATHCONFIG);
			    	logger.error(se.getMessage());
					System.exit(-1);			        
			    }
			}
			logger.info("Creating the file: " + PathsC.PATHCONFIGJSON);
			p =  Paths.get(configPath);
			Path tmpPathP =  Paths.get(tmpPath);
			try {
				Files.copy(tmpPathP,p,REPLACE_EXISTING );
			} catch (IOException e) {
				logger.error("Error while creating the file: " + PathsC.PATHCONFIGJSON);
				logger.error(e.getMessage());
				System.exit(-1);	
			}
		}
		Path pathUsers = Paths.get(PathsC.PATHUSERS);
		if (Files.notExists(pathUsers)){
			String tmpPath = event.getServletContext().getRealPath(File.separator+"WEB-INF"
					+File.separator+FileNames.USERSPROPERTIES);
			Path tmpPathP =  Paths.get(tmpPath);
			logger.debug("Creating the file: " + PathsC.PATHUSERS);
			try {
				Files.copy(tmpPathP,pathUsers,REPLACE_EXISTING );
			} catch (IOException e) {
				logger.error("Error occured while creating the file: "+PathsC.PATHUSERS);
				logger.error(e.getMessage());
				System.exit(-1);	
			}
		}
//		Path pathLogSettings = Paths.get(PathsC.PATHLOGSETTINGS);
//		if (Files.notExists(pathLogSettings)){
//			String tmpPath = event.getServletContext().getRealPath(File.separator+"WEB-INF"
//					+File.separator+FileNames.LOGPROPERTIES);
//			Path tmpPathP =  Paths.get(tmpPath);
//			logger.debug("Creating the file: " + PathsC.PATHLOGSETTINGS);
//			try {
//				Files.copy(tmpPathP,pathLogSettings,REPLACE_EXISTING );
//			} catch (IOException e) {
//				logger.error("Error occured while creating the file: "+PathsC.PATHLOGSETTINGS);
//				logger.error(e.getMessage());
//				System.exit(-1);	
//			}
//		}
	}
}
