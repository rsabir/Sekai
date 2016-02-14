package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

import constants.PathsC;
import utils.src.UserUtils;

public class UserProperties implements UserUtils{


		public void changeUserPassword(String username,String password) throws Exception{
			Properties properties = new Properties();
			properties.setProperty(username, password+",ROLE_ADMIN,enabled");
			File userProperties = new File(PathsC.PATHUSERS);
			properties.store(new FileOutputStream(userProperties), null);
		}
}
