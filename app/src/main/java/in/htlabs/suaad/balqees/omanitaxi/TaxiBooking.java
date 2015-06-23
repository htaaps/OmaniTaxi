package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 6/18/2015.
 */
public class TaxiBooking extends FragmentActivity implements AdapterView.OnItemClickListener,View.OnClickListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAIL = "/details";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBrg9SF5B1TPpfrLLwuXMnWfzI9Yr82fls";

    AutoCompleteTextView tb_ac_tv_to,tb_ac_tv_from;

    ArrayList<LatLng> markerPoints;

    static ArrayList<String> resultIdList = null;
    String placeId;

    static Double toLongitude=0.0,toLatitude=0.0;

    TextView tb_tv_price,tb_tv_distance;

    Button tb_btn_booking;

    public static String to_place,from_place,tdis,tcost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_booking);
        tb_ac_tv_to = (AutoCompleteTextView) findViewById(R.id.tb_ac_tv_to);
        tb_ac_tv_from = (AutoCompleteTextView) findViewById(R.id.tb_ac_tv_from);

        tb_tv_distance=(TextView)findViewById(R.id.tb_tv_distance);
        tb_tv_price=(TextView)findViewById(R.id.tb_tv_price);

        tb_btn_booking=(Button)findViewById(R.id.tb_btn_booking);
        tb_btn_booking.setOnClickListener(this);

        tb_ac_tv_to.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_place));
        tb_ac_tv_from.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_place));

        tb_ac_tv_to.setOnItemClickListener(this);
        tb_ac_tv_from.setOnItemClickListener(this);

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        setUpMapIfNeeded();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_btn_booking:
                this.to_place=tb_ac_tv_to.getText().toString();
                this.from_place=tb_ac_tv_from.getText().toString();
                this.tdis=tb_tv_distance.getText().toString();
                this.tcost=tb_tv_price.getText().toString();

                Intent i= new Intent(TaxiBooking.this,ConfirmBooking.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String str = (String) adapterView.getItemAtPosition(position);
                placeId=resultIdList.get(position);
                Toast.makeText(this, placeId, Toast.LENGTH_SHORT).show();
                new GetMarker().execute();
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:om");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            resultIdList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                resultIdList.add(predsJsonArray.getJSONObject(i).getString("place_id"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tb_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    private void setUpMap() {

        LatLng oman = new LatLng(MapsActivity.SLAT,MapsActivity.SLON);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oman, 8));

        mMap.setMyLocationEnabled(true);


    }


    class GetMarker extends AsyncTask<String, String, String> {

        // Movies json url
        private String MAR_URL = "https://maps.googleapis.com/maps/api/place/details/json?key=";

        private ProgressDialog pDialog;

        JSONParser jsonParser = new JSONParser();

        JSONArray posts=null;

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TaxiBooking.this);
            pDialog.setMessage("Plesae wait plotting in map...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            MAR_URL=MAR_URL+API_KEY+"&placeid="+placeId;

            int success;
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            Log.d("request!", "starting");
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(MAR_URL, "POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());


            try {
                   // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(json.toString());
                JSONObject result = jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
                toLongitude  = result.getDouble("lng");
                toLatitude =  result.getDouble("lat");

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Cannot process JSON results", e);
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
                Toast.makeText(TaxiBooking.this, file_url, Toast.LENGTH_LONG).show();
            }

            LatLng point=new LatLng(toLatitude,toLongitude);

            // Already two locations
            if(markerPoints.size()>1){
                markerPoints.clear();
                mMap.clear();

            }

            // Adding new item to the ArrayList
            markerPoints.add(point);
            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();

            // Setting the position of the marker
            options.position(point);

            /**
             * For the start location, the color of marker is GREEN and
             * for the end location, the color of marker is RED.
             */
            if(markerPoints.size()==1){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else if(markerPoints.size()==2){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

            // Add new marker to the Google Map Android API V2
            mMap.addMarker(options);

            // Checks, whether start and end locations are captured
            if(markerPoints.size() >= 2) {
                LatLng origin = markerPoints.get(0);
                LatLng dest = markerPoints.get(1);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while dow url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
            Toast.makeText(TaxiBooking.this,DirectionsJSONParser.dis+" "+DirectionsJSONParser.time,Toast.LENGTH_LONG).show();
            tb_tv_distance.setText(DirectionsJSONParser.dis);
            Float distance = Float.parseFloat(DirectionsJSONParser.dis.substring(0, DirectionsJSONParser.dis.indexOf("km")));
            Float cost=distance*Float.parseFloat(AvailableTaxi.pricePerKm);
            tb_tv_price.setText(cost.toString());
        }
    }
}
