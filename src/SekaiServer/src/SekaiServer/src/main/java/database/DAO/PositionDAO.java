package database.DAO;


import java.util.Date;
import java.util.List;
import database.entities.Position;


/**
 * Created by HILI on 13/03/2016.
 */
public interface PositionDAO {

	public void addPosition(Position position);
	Position getPositionById(int Id);
	Position getLastPositionByUser(String userId);
	List<Position> getPositionByUser(String userId);
	List<Position> getPositionBydate(Date date1, Date date2);
	List<Position> getPositionBydateByUser(String IdUser,Date date1, Date date2);
}
