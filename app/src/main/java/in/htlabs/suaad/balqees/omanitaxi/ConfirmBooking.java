package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tapas on 6/22/2015.
 */
public class ConfirmBooking extends Activity implements View.OnClickListener {
   Button cb_btn_confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_booking);
        cb_btn_confirm=(Button)findViewById(R.id.cb_btn_confirm);
        cb_btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cb_btn_confirm:
                new CBook().execute();
                break;
        }
    }

    class CBook extends AsyncTask<String, String, String> {

        // Progress Dialog
        private ProgressDialog pDialog;
        private JSONParser jsonParser = new JSONParser();
        private static final String REGISTER_URL = "http://www.htlabs.in/student/taxibooking/confirmbooking.php";

        // JSON IDS:
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConfirmBooking.this);
            pDialog.setMessage("Sending the booking details to server...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();

                parameters.add(new BasicNameValuePair("user_id", UserLogin.user));
                parameters.add(new BasicNameValuePair("taxi_id", AvailableTaxi.taxiId));
                parameters.add(new BasicNameValuePair("from_place", TaxiBooking.from_place));
                parameters.add(new BasicNameValuePair("to_place", TaxiBooking.to_place));
                parameters.add(new BasicNameValuePair("date_of_booking", "2015-06-27"));
                parameters.add(new BasicNameValuePair("time_of_booking", "09:34:23"));
                parameters.add(new BasicNameValuePair("total_distance", TaxiBooking.tdis));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", parameters);

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Register  Successful!", json.toString());
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Register Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            super.onPostExecute(file_url);
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(ConfirmBooking.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

}
