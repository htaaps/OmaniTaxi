package in.htlabs.suaad.balqees.omanitaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 6/18/2015.
 */
public class AvailableTaxi extends FragmentActivity implements GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Taxi> taxiList = new ArrayList<Taxi>();
    private static final double SLAT=23.6000;
    private static final double SLON=58.5500;
    private Marker mIbra[];
    private Intent i;
    private static int selection=0;
    public static String taxiId=null;
    public static String pricePerKm=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_taxi);
        new GetAllLocations().execute();


    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    private void setUpMap() {
        Double lat,lon;
        mIbra=new Marker[taxiList.size()];

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(this);

        LatLng oman = new LatLng(SLAT,SLON);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oman, 8));
        for(int j=0;j<taxiList.size();j++){
            lat=Double.parseDouble(taxiList.get(j).getTLat());
            lon=Double.parseDouble(taxiList.get(j).getTLon());
            mIbra[j]=mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(taxiList.get(j).getTdName())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car)));
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap=null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.equals(mIbra[0])){
            this.taxiId=taxiList.get(0).getTId();
            this.pricePerKm=taxiList.get(0).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[1])){
            this.taxiId=taxiList.get(1).getTId();
            this.pricePerKm=taxiList.get(1).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[2])){
            this.taxiId=taxiList.get(2).getTId();
            this.pricePerKm=taxiList.get(2).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[3])){
            this.taxiId=taxiList.get(3).getTId();
            this.pricePerKm=taxiList.get(3).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[4])){
            this.taxiId=taxiList.get(4).getTId();
            this.pricePerKm=taxiList.get(4).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[5])){
            this.taxiId=taxiList.get(5).getTId();
            this.pricePerKm=taxiList.get(5).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[6])){
            this.taxiId=taxiList.get(6).getTId();
            this.pricePerKm=taxiList.get(6).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[7])){
            this.taxiId=taxiList.get(7).getTId();
            this.pricePerKm=taxiList.get(7).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[8])){
            this.taxiId=taxiList.get(8).getTId();
            this.pricePerKm=taxiList.get(8).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else if (marker.equals(mIbra[9])){
            this.taxiId=taxiList.get(9).getTId();
            this.pricePerKm=taxiList.get(9).getTPrice();
            i=new Intent(AvailableTaxi.this, TaxiBooking.class);
            startActivity(i);
        }
        else{
        }
        return false;
    }

    class GetAllLocations extends AsyncTask<String, String, String> {

        // Movies json url
        private static final String LOC_URL = "http://www.htlabs.in/student/taxibooking/alltaxi.php";

        private ProgressDialog pDialog;

        JSONParser jsonParser = new JSONParser();

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_POSTS = "posts";

        JSONArray posts=null;

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AvailableTaxi.this);
            pDialog.setMessage("Getting all the taxies...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOC_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                posts=json.getJSONArray(TAG_POSTS);

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    for(int j=0;j< posts.length();j++){
                        JSONObject obj=posts.getJSONObject(j);
                        Taxi c = new Taxi();
                        c.setTId(obj.getString("taxi_id"));
                        c.setTdName(obj.getString("driver_name"));
                        c.setTtype(obj.getString("type"));
                        c.setTPrice(obj.getString("price_per_km"));
                        c.setTLat(obj.getString("lat"));
                        c.setTLon(obj.getString("lon"));

                        taxiList.add(c);
                    }

                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(AvailableTaxi.this, file_url, Toast.LENGTH_LONG).show();
            }
            setUpMapIfNeeded();
        }

    }



}
