package sqlite.entities;

/**
 * Created by RABOUDI on 18/11/2015.
 */
public class Node {
    private int ID;
    private float longitude;
    private float latitude;

    public Node(int nodeID, float longitude, float latitude) {
        this.latitude = latitude;
        this.ID = nodeID;
        this.longitude = longitude;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}
