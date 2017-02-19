package gr.sylesakis.expensesmanager.Form;

/**
 * Created by SakaroS on 22/12/2016.
 */

public class LocationForm {

    private String locationName;
    private String locLatitude;
    private String locLongtitude;

    public LocationForm() { }

    public LocationForm(String locationName, String locLatitude, String locLongtitude) {
        this.locationName = new String(locationName);
        this.locLatitude = new String(locLatitude);
        this.locLongtitude = new String(locLongtitude);
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = new String(locationName);
    }

    public String getLocLatitude() {
        return locLatitude;
    }

    public void setLocLatitude(String locLatitude) {
        this.locLatitude = new String(locLatitude);
    }

    public String getLocLongtitude() {
        return locLongtitude;
    }

    public void setLocLongtitude(String locLongtitude) {
        this.locLongtitude = new String(locLongtitude);
    }
}
