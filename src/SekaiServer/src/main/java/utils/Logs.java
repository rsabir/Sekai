package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.catalina.Context;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.LogsC;
import constants.Mysql;
import constants.Others;
import constants.PathsC;
import constants.Urls;

public class Logs {
	private File settingFile;
	private JSONObject settingJSON;
	private static Logs instance = null;
	private static Logger logger = LoggerFactory.getLogger(Logs.class);
	private static final String PARAMETER_LEVEL = "LEVEL_ROOT";
	private static final String PARAMETER_DIRECTORY = "DIRECTORY";
	private static final String PARAMETER_LOG_GENERAL_FILENAME = "LOG_GENERAL_FILENAME";
	private static final String PARAMETER_LOG_ERROR_FILENAME = "LOG_ERROR_FILENAME";
	private static final String PARAMETER_LOG_HTTP_FILENAME = "LOG_HTTP_FILENAME";
	private static Properties properties = null;

	
	
	private  Logs(){
		settingFile = new File(PathsC.PATHLOGSETTINGS);
	}
	public static synchronized Logs getInstance() {
		if (instance == null ) {
                instance = new Logs();
		}
		return instance;
	}
	public Logs load(){
		logger.info("Loading the properties of logs");
		if (settingFile.exists()){
			readSettings();
			if (properties != null){
				LogsC.LEVEL_ROOT = properties.getProperty(PARAMETER_LEVEL);
				PathsC.PATHERRORLOG = PathsC.PATHCONFIG+properties.getProperty(PARAMETER_LOG_ERROR_FILENAME);
				PathsC.PATHGENERALLOG =  PathsC.PATHCONFIG+properties.getProperty(PARAMETER_LOG_GENERAL_FILENAME);
				PathsC.PATHHTTPLOG =  PathsC.PATHCONFIG+properties.getProperty(PARAMETER_LOG_HTTP_FILENAME);
			}
		}
		return this;
	}
	public void saveSettings() throws FileNotFoundException, IOException{
		properties.setProperty(PARAMETER_LEVEL,LogsC.LEVEL_ROOT );
		properties.setProperty(PARAMETER_LOG_ERROR_FILENAME,PathsC.PATHERRORLOG );
		properties.setProperty(PARAMETER_LOG_GENERAL_FILENAME,PathsC.PATHGENERALLOG );
		properties.setProperty(PARAMETER_LOG_HTTP_FILENAME,PathsC.PATHHTTPLOG );
		properties.store(new FileOutputStream(settingFile),"Updating log properties");
	}
	public void readSettings(){
		InputStream input = null;
		try {
			input = new FileInputStream(PathsC.PATHLOGSETTINGS);
			properties  = new Properties();;
			properties.load(input);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
}
