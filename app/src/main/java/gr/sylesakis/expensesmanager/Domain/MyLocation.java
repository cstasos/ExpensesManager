package gr.sylesakis.expensesmanager.Domain;

/**
 * Created by SakaroS on 22/12/2016.
 */

public class MyLocation {

    private int ID;
    private String locName;
    private String locLat;
    private String locLng;

    public MyLocation() { }

    public MyLocation(MyLocation mloc){
        this.ID = mloc.getID();
        this.locName = new String(mloc.getLocName());
        this.locLat = new String(mloc.getLocLat());
        this.locLng = new String(mloc.getLocLng());
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = new String(locName);
    }

    public String getLocLat() {
        return locLat;
    }

    public void setLocLat(String locLat) {
        this.locLat = new String(locLat);
    }

    public String getLocLng() {
        return locLng;
    }

    public void setLocLng(String locLng) {
        this.locLng = new String(locLng);
    }

    @Override
    public String toString() {
        return "MyLocation{" +
                "ID=" + ID +
                ", locName='" + locName + '\'' +
                ", locLat='" + locLat + '\'' +
                ", locLng='" + locLng + '\'' +
                '}';
    }
}
