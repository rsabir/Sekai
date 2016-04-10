package database.entities;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Created by HILI on 13/03/2016.
 */
@Entity
@Table
public class Position {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int Id;
	@Column
	private float lat;
	@Column
	private float lng;
	@Column(name="date")
	private Date dateOfAdd= new Date();
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;


	public Position(){

	}
	public Position(float lat, float lng, User user){
		this.lat=lat;
		this.lng=lng;
		this.user=user;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
	public Date getDateOfAdd() {
		return dateOfAdd;
	}
	public void setDateOfAdd(Date dateOfAdd) {
		this.dateOfAdd = dateOfAdd;
	}
	public User getClient() {
		return user;
	}
	public void setClientId(User user) {
		this.user = user;
	}

}
