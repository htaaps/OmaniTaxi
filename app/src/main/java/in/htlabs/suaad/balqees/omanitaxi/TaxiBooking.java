package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 6/18/2015.
 */
public class TaxiBooking extends FragmentActivity implements AdapterView.OnItemClickListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAIL = "/details";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBrg9SF5B1TPpfrLLwuXMnWfzI9Yr82fls";

    AutoCompleteTextView tb_ac_tv_to,tb_ac_tv_from;


    static ArrayList<String> resultIdList = null;
    String placeId;

    static Double toLongitude=0.0,toLatitude=0.0,fromLongitude=0.0,fromLatitude=0.0;

    boolean loc=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_booking);
        tb_ac_tv_to = (AutoCompleteTextView) findViewById(R.id.tb_ac_tv_to);
        tb_ac_tv_from = (AutoCompleteTextView) findViewById(R.id.tb_ac_tv_from);

        tb_ac_tv_to.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_place));
        tb_ac_tv_from.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_place));

        tb_ac_tv_to.setOnItemClickListener(this);
        tb_ac_tv_from.setOnItemClickListener(this);

        setUpMapIfNeeded();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//        switch(view.getId()){
//            case R.layout.list_place:
                String str = (String) adapterView.getItemAtPosition(position);
                placeId=resultIdList.get(position);
                Toast.makeText(this, placeId, Toast.LENGTH_SHORT).show();
                new GetMarker().execute();
//                break;
//            case R.id.tb_ac_tv_from:

//                break;
//       }
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
        Double lat,lon;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

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
            mMap.addMarker(new MarkerOptions().position(new LatLng(toLatitude, toLongitude)).title("Marker"));
        }

    }
}
