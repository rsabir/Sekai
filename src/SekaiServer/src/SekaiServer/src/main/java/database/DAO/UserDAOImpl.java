package database.DAO;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import database.entities.Position;
import database.entities.User;



public class UserDAOImpl implements UserDAO {


	@PersistenceContext(unitName="pu-name", type=PersistenceContextType.EXTENDED)
	private EntityManager em;


	public void addUser(User user) {
		em.persist(user);

	}


	public User getUserById(String Id) {
		return em.find(User.class,Id);
	}

	@SuppressWarnings("unchecked")

	public List<User> getAllUsers() {
		Query q=em.createQuery("select u from user");
		return q.getResultList();
	}
	@SuppressWarnings("unchecked")

	public List<User> getAllUersByPosition(LinkedList<Position> positions){
		String q="select u from user u,position p where ";
		int i=0;
		for ( i=0;i<positions.size()-1;i++){
			q=q+" u.userid="+positions.get(i).getClient().getUserId()+" and";
		}
		q=q+"u.userid="+positions.get(i).getClient().getUserId();
		Query req=em.createQuery(q);

		return req.getResultList();
	}


	public void updateUser(User user) {
		em.merge(user);
		
	}

}
