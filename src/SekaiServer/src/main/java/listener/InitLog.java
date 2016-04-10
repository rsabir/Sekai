package listener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.ConnectException;
import java.net.Socket;
import java.net.URLDecoder;
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
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.apache.tomcat.dbcp.dbcp.SQLNestedException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.CommunicationsException;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import constants.FileNames;
import constants.Others;
import constants.PathsC;
import constants.Urls;

import static java.nio.file.StandardCopyOption.*;


public class InitLog implements ServletContextListener {
	

	public InitLog(){
		checkConfigDirecotry();
		Path pathLogSettings = Paths.get(PathsC.PATHLOGSETTINGS);
		if (Files.notExists(pathLogSettings)){
			
			try {
				String tmpPath = getPath();
				tmpPath += File.separator+"WEB-INF"+File.separator+FileNames.LOGPROPERTIES;
				Path tmpPathP =  Paths.get(tmpPath);
				System.out.println("creating the file: "+PathsC.PATHLOGSETTINGS);
				Files.copy(tmpPathP,pathLogSettings,REPLACE_EXISTING );
			} catch (IOException e) {
				// We can't use logger;
				System.out.println("Error occured while creating the file: "+PathsC.PATHLOGSETTINGS);
				e.printStackTrace();
				System.exit(-1);	
			}
		}
	}
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent event) {
		/*
		Path pathLogSettings = Paths.get(PathsC.PATHLOGSETTINGS);
		if (Files.notExists(pathLogSettings)){
			String tmpPath = event.getServletContext().getRealPath(File.separator+"WEB-INF"
					+File.separator+FileNames.LOGPROPERTIES);
			Path tmpPathP =  Paths.get(tmpPath);
			try {
				System.out.println("creating the file: "+PathsC.PATHLOGSETTINGS);
				Files.copy(tmpPathP,pathLogSettings,REPLACE_EXISTING );
				LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
				loggerContext.reset();
				ContextInitializer ci = new ContextInitializer(loggerContext);
//				Logger logger = LoggerFactory.getLogger(InitLog.class);
//				logger.debug("Creating the file: " + PathsC.PATHLOGSETTINGS);
			} catch (IOException e) {
				// We can't use logger;
				System.out.println("Error occured while creating the file: "+PathsC.PATHLOGSETTINGS);
				e.printStackTrace();
				System.exit(-1);	
			}
		}
		*/
	}
	public void checkConfigDirecotry(){
		File theDir = new File(PathsC.PATHCONFIG);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("Creating directory: " + PathsC.PATHCONFIG);
		    boolean result = false;
		    Others.FIST_TIME=true;
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		    	System.out.println("Error occured while creating directory: " + PathsC.PATHCONFIG);
		    	System.out.println(se.getMessage());
				System.exit(-1);			        
		    }
		}
	}
	public String getPath() throws UnsupportedEncodingException {
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String fullPath = URLDecoder.decode(path, "UTF-8");
		String pathArr[] = fullPath.split("/WEB-INF/classes/");
		//System.out.println(fullPath);
		//System.out.println(pathArr[0]);
		fullPath = pathArr[0];
		
		return fullPath;
	}
}
