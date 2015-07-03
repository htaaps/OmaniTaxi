package in.htlabs.suaad.balqees.omanitaxi;

/**
 * Created by admin on 6/18/2015.
 */
public class Taxi {

    private String taxi_id,driver_name,type,price_per_km,lat,lon,d_gsm;
    public Taxi(){
    }

    public Taxi(String taxi_id, String driver_name, String type, String price_per_km, String lat, String lon,String d_gsm){
        this.taxi_id=taxi_id;
        this.driver_name=driver_name;
        this.type=type;
        this.price_per_km=price_per_km;
        this.lat=lat;
        this.lon=lon;
        this.d_gsm=d_gsm;
    }


    public String getDgsm() {
        return d_gsm;
    }

    public void setDgsm(String d_gsm) {
        this.d_gsm = d_gsm;
    }

    public String getTId() {
        return taxi_id;
    }

    public void setTId(String taxi_id) {
        this.taxi_id = taxi_id;
    }

    public String getTdName() {
        return driver_name;
    }

    public void setTdName(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getTtype() {
        return type;
    }

    public void setTtype(String type) {
        this.type = type;
    }

    public String getTPrice() {
        return price_per_km;
    }

    public void setTPrice(String price_per_km) {
        this.price_per_km = price_per_km;
    }

    public String getTLat() {
        return lat;
    }

    public void setTLat(String lat) {
        this.lat = lat;
    }

    public String getTLon() {
        return lon;
    }

    public void setTLon(String lon) {
        this.lon = lon;
    }


}
