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

import constants.Mysql;
import constants.PathsC;
import constants.Urls;

public class Settings {
	private File settingFile;
	private JSONObject settingJSON;
	private static final String PARAMETER_URL_SETTING = "server_config";
	private static final String PARAMETER_MYSQL = "mysql";
	private static final String PARAMETER_MYSQL_USERNAME = "username";
	private static final String PARAMETER_MYSQL_PASSWORD = "password";
	private static final String PARAMETER_MYSQL_PORT = "port";
	private static final String PARAMETER_MYSQL_DATABASE = "database";
	
	public Settings(){
		settingFile = new File(PathsC.PATHSETTING);
	}
	public Settings init(){
		checkDirectory();
		if (settingFile.exists()){
			readSettings();
			Urls.CONFIGSERVER = (String) settingJSON.get(PARAMETER_URL_SETTING);
			JSONObject mysqlJSON = (JSONObject) settingJSON.get(PARAMETER_MYSQL);
			Mysql.USERNAME = (String) mysqlJSON.get(PARAMETER_MYSQL_USERNAME);
			Mysql.PASSWORD = (String) mysqlJSON.get(PARAMETER_MYSQL_PASSWORD); 
			Mysql.PORT = (String) mysqlJSON.get(PARAMETER_MYSQL_PORT);
			Mysql.DATABASE = (String) mysqlJSON.get(PARAMETER_MYSQL_DATABASE);
			//new SetContext(mysql_username,mysql_password,mysql_port,context);
		}else{
			saveFileSettings();
		}
		return this;
	}
	public void readSettings(){
		JSONParser parser = new JSONParser();
		try {

			settingJSON = (JSONObject) parser.parse(new FileReader(PathsC.PATHSETTING));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  catch (ParseException e) {
			System.out.println(PathsC.PATHSETTING +" is not well formed");
			System.out.println(e);
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void saveFileSettings(){
		JSONObject settingJSON = new JSONObject();
		settingJSON.put(PARAMETER_URL_SETTING, Urls.CONFIGSERVER);
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
			e.printStackTrace();
		}
	}
	private void checkDirectory(){
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
	}

	
	
	
	
	
}
