package in.htlabs.suaad.balqees.omanitaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<City> cityList = new ArrayList<City>();
    private Marker mCity[];
    public static final double SLAT=23.6000;
    public static final double SLON=58.5500;
    private Intent i;
    public static int selection=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        new GetAllLocations().execute();

    }

    @Override
    protected void onPause() {
        super.onResume();
        mMap=null;
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
        mCity=new Marker[cityList.size()];

        mMap.setOnMarkerClickListener(this);

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mMap.setMyLocationEnabled(true);

        LatLng oman = new LatLng(SLAT,SLON);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oman, 8));

        for(int j=0;j<cityList.size();j++){
            lat=Double.parseDouble(cityList.get(j).getCLat());
            lon=Double.parseDouble(cityList.get(j).getCLon());
            mCity[j]=mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(cityList.get(j).getCName()));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(mCity[0])){
            selection=1;
        }
        else if(marker.equals(mCity[1])){
            selection=2;
            i=new Intent(MapsActivity.this, AvailableTaxi.class);
                startActivity(i);
        }
        else if(marker.equals(mCity[2])){
            selection=3;
        }
        else if(marker.equals(mCity[3])){
            selection=4;
        }
        else if(marker.equals(mCity[4])){
            selection=5;
        }
        else if(marker.equals(mCity[5])){
            selection=6;
        }
        else if(marker.equals(mCity[6])){
            selection=7;
        }
        else if(marker.equals(mCity[7])){
            selection=8;
        }
        else{
            selection=0;
        }
        return false;
    }

    class GetAllLocations extends AsyncTask<String, String, String> {

        // Movies json url
        private static final String LOC_URL = "http://www.htlabs.in/student/taxibooking/citydetails.php";

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
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Getting all the cities...");
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
                        City c=new City();
                        c.setCId(obj.getString("city_id"));
                        c.setCName(obj.getString("city_name"));
                        c.setCLat(obj.getString("city_lat"));
                        c.setCLon(obj.getString("city_lon"));

                        cityList.add(c);
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
                Toast.makeText(MapsActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
            setUpMapIfNeeded();
        }

    }
}
