package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.htlabs.suaad.balqees.omanitaxi.adapter.CustomListAdapter;
import in.htlabs.suaad.balqees.omanitaxi.app.AppController;
import in.htlabs.suaad.balqees.omanitaxi.model.VDetails;

/**
 * Created by admin on 5/26/2015.
 */
public class ViewBooking extends Activity {
    // Log tag
    private static final String TAG = ViewBooking.class.getSimpleName();

    // Movies json url
    private static String url = null;
    private ProgressDialog pDialog;
    private List<VDetails> vdetailsList = new ArrayList<VDetails>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_booking_list);
        listView = (ListView) findViewById(R.id.list);

        adapter = new CustomListAdapter(this, vdetailsList);

        url = "http://www.htlabs.in/student/taxibooking/viewbooking.php"+"?user_id=1";

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
}
