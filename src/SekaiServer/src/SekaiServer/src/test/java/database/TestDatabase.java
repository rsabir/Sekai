/**
 * 
 */
package database;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import database.DAO.PositionDAO;
import database.DAO.PositionDAOImpl;
import database.DAO.UserDAO;
import database.DAO.UserDAOImpl;
import database.entities.Position;
import database.entities.User;

/**
 * @author zakaria
 *
 */
public class TestDatabase {
	PositionDAO positionDAO = new PositionDAOImpl();
	UserDAO userDAO = new UserDAOImpl();
	

	
	@Before
	public void setUp() throws Exception { }
	@Test
	public void test() {
		try {
			//ClassPathXmlApplicationContext context=
				//	new ClassPathXmlApplicationContext(new String[]{"classpath:spring/application-config.xml"});
			
			
			User user= new User("111");
			Position position = new Position(11,11,user);
			user.addPosition(position);
			//userDAO.addUser(user);
			assertTrue(true);
		} catch (Exception e) { assertTrue(e.getMessage(),false);}}
}
