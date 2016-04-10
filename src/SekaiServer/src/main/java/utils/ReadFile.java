package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ReadFile {
	private static Logger logger = LoggerFactory.getLogger(ReadFile.class);
	public static String readFile(String filename){
		try {
			return readFile(new FileReader(filename));
		} catch (FileNotFoundException e) {
			logger.error("Error : File not found "+filename);
			logger.error(e.getMessage());
			return null;
		}
	}
	public static String readFile(FileReader file){
		BufferedReader br = new BufferedReader(file);
		String content = "";
		String sCurrentLine = "";
		try {
			while ((sCurrentLine = br.readLine()) != null) {
				content+="\n"+sCurrentLine;
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}
		return content;
	}
}
