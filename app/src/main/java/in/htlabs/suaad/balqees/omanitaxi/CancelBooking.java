package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.htlabs.suaad.balqees.omanitaxi.adapter.CancelListAdapter;
import in.htlabs.suaad.balqees.omanitaxi.app.AppController;
import in.htlabs.suaad.balqees.omanitaxi.model.VDetails;

/**
 * Created by admin on 5/26/2015.
 */
public class CancelBooking extends Activity implements View.OnClickListener{
    // Log tag
    private static final String TAG = ViewBooking.class.getSimpleName();

    // Movies json url
    private static String url = null;
    private ProgressDialog pDialog;
    private List<VDetails> vdetailsList = new ArrayList<VDetails>();
    private ListView listView;
    private CancelListAdapter adapter;

    Button cb_btn_delete;
    EditText cb_ed_booking;
    // JSON parser class
    JSONParser jparsor=new JSONParser();
    private static final String LOGIN_URL = "http://www.htlabs.in/student/taxibooking/deletebooking.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_booking);
        listView = (ListView) findViewById(R.id.list_bview);
        cb_btn_delete=(Button)findViewById(R.id.cb_btn_delete);
        cb_ed_booking=(EditText)findViewById(R.id.cb_ed_booking);
        cb_btn_delete.setOnClickListener(this);

        adapter = new CancelListAdapter(this, vdetailsList);

        url ="http://www.htlabs.in/student/taxibooking/viewbooking.php"+"?user_id="+UserLogin.user;

        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Creating volley request obj
        JsonArrayRequest prodReq = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                hidePDialog();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        JSONArray pArray = obj.getJSONArray("posts");

                        for (int j = 0; j < pArray.length(); j++) {

                            String bid= pArray.getJSONObject(j).getString("booking_id")+"from "+pArray.getJSONObject(j).getString("from_place")
                                    +"to "+pArray.getJSONObject(j).getString("to_place");

                            VDetails item = new VDetails();
                            item.setBdetails(bid);

                            // adding movie to movies array
                            vdetailsList.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(prodReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_btn_delete:
                new DeleteBooking().execute();
                break;
        }

    }
    class DeleteBooking extends AsyncTask<String, String, String> {

        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CancelBooking.this);
            pDialog.setMessage("Check booking id from server...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            int success;
            String booking_id = cb_ed_booking.getText().toString();
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("booking_id", booking_id));
                params.add(new BasicNameValuePair("user_id", UserLogin.user));
                Log.d("request!", "starting");
                JSONObject json = jparsor.makeHttpRequest( LOGIN_URL, "POST", params);

                // checking log for json response
                Log.d("Check attempt", json.toString());
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Delete Successfully !", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    return json.getString(TAG_MESSAGE);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String message) {
            pDialog.dismiss();
            if (message != null){
                Toast.makeText(CancelBooking.this, message, Toast.LENGTH_LONG).show();
            }
            Intent i = new Intent(CancelBooking.this,Booking.class);
            startActivity(i);
            finish();
        }
    }
}
