package database.DAO;


import java.util.LinkedList;
import java.util.List;

import database.entities.Position;
import database.entities.User;

/**
 * Created by HILI on 13/03/2016.
 */

public interface UserDAO {

	public void addUser(User user);
	public User getUserById(String Id);
	public List<User> getAllUsers();
	public List<User> getAllUersByPosition(LinkedList<Position> positions);
	public void updateUser(User user);
	
}
