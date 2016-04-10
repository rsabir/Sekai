package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.catalina.Context;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.Mysql;
import constants.Others;
import constants.PathsC;
import constants.Urls;

public class Settings {
	private File settingFile;
	private JSONObject settingJSON;
	private static Logger logger = LoggerFactory.getLogger(Settings.class);
	private static final String PARAMETER_URL_SETTING = "server_config";
	private static final String PARAMETER_MYSQL = "mysql";
	private static final String PARAMETER_MYSQL_USERNAME = "username";
	private static final String PARAMETER_MYSQL_PASSWORD = "password";
	private static final String PARAMETER_MYSQL_PORT = "port";
	private static final String PARAMETER_MYSQL_DATABASE = "database";
	private static final String PARAMETER_IP = "ip";
	
	public Settings(){
		settingFile = new File(PathsC.PATHSETTING);
	}
	public Settings init(){
		logger.info("Loading the settings from setting.json");
		checkDirectory();
		if (settingFile.exists()){
			readSettings();
			Urls.CONFIGSERVER = (String) settingJSON.get(PARAMETER_URL_SETTING);
			Urls.IP = (String) settingJSON.get(PARAMETER_IP);
			JSONObject mysqlJSON = (JSONObject) settingJSON.get(PARAMETER_MYSQL);
			Mysql.USERNAME = (String) mysqlJSON.get(PARAMETER_MYSQL_USERNAME);
			Mysql.PASSWORD = (String) mysqlJSON.get(PARAMETER_MYSQL_PASSWORD); 
			Mysql.PORT = (String) mysqlJSON.get(PARAMETER_MYSQL_PORT);
			Mysql.DATABASE = (String) mysqlJSON.get(PARAMETER_MYSQL_DATABASE);
		}else{
			logger.info("setting.json doesn't exist. Creating it ...");
			Others.FIST_TIME=true;
			saveFileSettings();
		}
		return this;
	}
	public void readSettings(){
		JSONParser parser = new JSONParser();
		try {

			settingJSON = (JSONObject) parser.parse(new FileReader(PathsC.PATHSETTING));

		} catch (FileNotFoundException e) {
			logger.error("Error occured while trying to read setting.json");
			logger.error(e.getMessage());
		}  catch (ParseException e) {
			logger.error(PathsC.PATHSETTING +" is not well formed");
			logger.error(e.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			logger.error("Error occured while trying to read setting.json");
			logger.error(e.getMessage());
		}

	}
	public static void saveFileSettings(){
		JSONObject settingJSON = new JSONObject();
		settingJSON.put(PARAMETER_URL_SETTING, Urls.CONFIGSERVER);
		settingJSON.put(PARAMETER_IP, Urls.IP);
		JSONObject mysqlJSON = new JSONObject();
		mysqlJSON.put(PARAMETER_MYSQL_PASSWORD, Mysql.PASSWORD);
		mysqlJSON.put(PARAMETER_MYSQL_USERNAME,Mysql.USERNAME);
		mysqlJSON.put(PARAMETER_MYSQL_PORT, Mysql.PORT);
		mysqlJSON.put(PARAMETER_MYSQL_DATABASE, Mysql.DATABASE);
		settingJSON.put(PARAMETER_MYSQL,mysqlJSON);
		try {

			FileWriter file = new FileWriter(PathsC.PATHSETTING,false);
			file.write(settingJSON.toString());
			file.flush();
			file.close();

		} catch (IOException e) {
			logger.error("Error occured while saving the file setting.json");
			logger.error(e.getMessage());
		}
	}
	private void checkDirectory(){
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
		    	logger.error("Error occured while creating directory: " + PathsC.PATHCONFIG);
		    	logger.error(se.getMessage());
				System.exit(-1);			        
		    }
		}
	}

	
	
	
	
	
}
