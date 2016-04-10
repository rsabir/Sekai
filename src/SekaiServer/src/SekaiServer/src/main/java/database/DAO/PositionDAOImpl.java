package database.DAO;


import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import database.entities.Position;





public class PositionDAOImpl implements PositionDAO{
	
	@PersistenceContext(unitName="pu-name", type=PersistenceContextType.EXTENDED)
	private EntityManager em;
	

	public void addPosition(Position position) {
		em.persist(position);
		
	}


	public Position getPositionById(int Id) {
		return em.find(Position.class,Id);
	}

	@SuppressWarnings("unchecked")

	public List<Position> getPositionByUser(String userId) {
		Query q = em.createQuery("select p from position where p.userid=:x");
		q.setParameter("x", userId);
		
		return q.getResultList();
	}


	public List<Position> getPositionBydate(Date date1, Date date2) {
		Query q = em.createQuery("select p from position where p.date>=:d1"
				+ "and p.date <=:d2");
		q.setParameter("d1", date1);
		q.setParameter("d2", date2);
		return null;
	}
	@SuppressWarnings("unchecked")

	public List<Position> getPositionBydateByUser(String IdUser,Date date1, Date date2) {
		Query q = em.createQuery("select p from position where p.date>=:d1"
				+ "and p.date <=:d2 and p.userId=:userId");
		q.setParameter("d1", date1);
		q.setParameter("d2", date2);
		q.setParameter("userId", IdUser);
		return q.getResultList();
	}


	public Position getLastPositionByUser(String userId) {
		Query q = em.createQuery("select p from position where p.userid=:x order by p.Id desc");
		q.setParameter("x", userId);
		
		return (Position) q.getResultList().get(0);
	}
	
	

}
