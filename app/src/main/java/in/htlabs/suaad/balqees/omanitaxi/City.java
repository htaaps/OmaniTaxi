package in.htlabs.suaad.balqees.omanitaxi;

/**
 * Created by Tapas on 6/16/2015.
 */
public class City {
    private String city_id,city_name,city_lat,city_lon;
    public City(){
    }

    public City(String city_id,String city_name,String city_lat,String city_lon){
        this.city_id=city_id;
        this.city_name=city_name;
        this.city_lat=city_lat;
        this.city_lon=city_lon;
    }

    public String getCId() {
        return city_id;
    }

    public void setCId(String city_id) {
        this.city_id = city_id;
    }

    public String getCName() {
        return city_name;
    }

    public void setCName(String city_name) {
        this.city_name = city_name;
    }

    public String getCLat() {
        return city_lat;
    }

    public void setCLat(String city_lat) {
        this.city_lat = city_lat;
    }

    public String getCLon() {  return city_lon;    }

    public void setCLon(String city_lon) {   this.city_lon = city_lon;   }
}
