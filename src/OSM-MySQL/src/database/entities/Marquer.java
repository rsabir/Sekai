package database.entities;

import java.util.Date;

/**
 * Created by RABOUDI on 18/11/2015.
 */
public class Marquer {
    private Date DateOfAdd;
    private int position;
    private int user;

    public Marquer(int user, int position, Date dateOfAdd) {
        DateOfAdd = dateOfAdd;
        this.position = position;
        this.user = user;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }



    public Date getDateOfAdd() {
        return DateOfAdd;
    }

    public void setDateOfAdd(Date dateOfAdd) {
        DateOfAdd = dateOfAdd;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }




}
