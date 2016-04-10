package database.entities;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

/**
 * Created by HILI on 13/03/2016.
 */

@Entity
@Table
public class User {
	@Id
	@Column(name="id")
	private int idforindex;
	@Column
	private String userId;	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List <Position> userPositions;
	
	public User(){
		userPositions= new LinkedList<Position>();
	}
	public User(String userId){
		userPositions= new LinkedList<Position>();
		this.userId=userId;
	}
	
	public List<Position> getPositions() {
		return userPositions;
	}
	public void setPositions(LinkedList<Position> userPositions) {
		this.userPositions = userPositions;
	}
	
	public void removePosition(Position userPositions){
		this.userPositions.remove(userPositions);
		
	}
	public void addPosition(Position userPositions){
		this.userPositions.add(userPositions);
	}
	public int getIdforindex() {
		return idforindex;
	}
	public void setIdforindex(int idforindex) {
		this.idforindex = idforindex;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Position> getUserPositions() {
		return userPositions;
	}
	public void setUserPositions(LinkedList<Position> userPositions) {
		this.userPositions = userPositions;
	}
	public Position getLastPosition(){
		int max=0;
		Position positionMax = null; 
		for(int i=0;i<userPositions.size();i++){
			if(userPositions.get(i).getId()>max) {
				max=userPositions.get(i).getId();
				positionMax=userPositions.get(i);
			}
		}
		return positionMax;
		
	}
}
