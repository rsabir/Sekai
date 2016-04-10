package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document ;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class SetContext {
	private static Logger logger = LoggerFactory.getLogger(SetContext.class); 
	String mysql_username;
	String mysql_password;
	String mysql_port;
	ServletContext context;
	public SetContext(String username_mysql,String password_mysql, String port_mysql,ServletContext context){
		mysql_username = username_mysql;
		mysql_password = password_mysql;
		mysql_port = port_mysql;
		this.context = context;
		createXML();
	}
	public SetContext(){
	}
	private void createXML(){
		logger.info("Creating/Updating the context.xml");
		String path = System.getProperty("catalina.home")+File.separator+"conf"+File.separator+"context.xml";
		
		String contextApp = context.getRealPath("/META-INF/context.xml");
		System.out.println(context.getRealPath(File.separator));
		File fXmlFile = new File(contextApp);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.newDocument();			
			doc.getDocumentElement().normalize();
			Element contextElement = doc.getDocumentElement();
			Element watchedRessource = (Element) contextElement.getElementsByTagName("WatchedResource").item(0);
			Element loader = (Element) contextElement.getElementsByTagName("Loader").item(0);
			Element ressource = (Element) contextElement.getElementsByTagName("Resource").item(0);
			if (ressource!=null){
				String name = ressource.getAttribute("name");
				String auth = ressource.getAttribute("auth");
				String driverClassName = ressource.getAttribute("driverClassName");
				String type = ressource.getAttribute("type");
				String url = "jdbc:mysql://localhost:"+mysql_port+"/osm";
				ressource = document.createElement("Resource");
				ressource.setAttribute("name", "jdbc/osm");
				ressource.setAttribute("auth", auth);
				ressource.setAttribute("driverClassName", driverClassName);				
				ressource.setAttribute("type", type);
				ressource.setAttribute("username", mysql_username);
				ressource.setAttribute("password", mysql_password);
				ressource.setAttribute("url", url);
			}
			contextElement = document.createElement("Context");
			
			contextElement.appendChild(document.importNode(watchedRessource, true));
			contextElement.appendChild(document.importNode(loader, true));
			contextElement.appendChild(ressource);
			document.appendChild(contextElement);
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(path));
			transformer.transform(source, result);
			result = new StreamResult(new File(contextApp));
			transformer.transform(source, result);
			
			logger.info("Context was successfully modified");
			
			
		} catch (Exception e){
			logger.error("Error while creating/updating context.xml");
			logger.error(e.getMessage());
		}
	}
}
