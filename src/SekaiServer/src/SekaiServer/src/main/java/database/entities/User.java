package database.entities;

/**
 * Created by RABOUDI on 18/11/2015.
 */
public class User {
    private int ID;
    private String Name;
    private String MacAddr;

    public String getMacAddr() {
        return MacAddr;
    }

    public void setMacAddr(String macAddr) {
        MacAddr = macAddr;
    }



    public User(int userID, String name, String macAddr) {
    	this.ID=userID;
    	this.Name=name;
    	this.MacAddr=macAddr;
    }

    public User(int userID, String macAddr) {
		// TODO Auto-generated constructor stub
    	this.ID=userID;
    	this.MacAddr=macAddr;
	}

	public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
