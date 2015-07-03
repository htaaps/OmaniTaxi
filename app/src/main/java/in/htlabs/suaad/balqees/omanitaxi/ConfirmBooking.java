package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tapas on 6/22/2015.
 */
public class ConfirmBooking extends Activity implements View.OnClickListener {
   Button cb_btn_confirm;
   TextView cb_tv_from,cb_tv_to,cb_tv_tprice,cb_tv_tdistance,cb_tv_date,cb_tv_time;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_booking);
        cb_btn_confirm=(Button)findViewById(R.id.cb_btn_confirm);
        cb_tv_from =(TextView)findViewById(R.id.cb_tv_from);
        cb_tv_to=(TextView)findViewById(R.id.cb_tv_to);
        cb_tv_tprice=(TextView)findViewById(R.id.cb_tv_tprice);
        cb_tv_tdistance=(TextView)findViewById(R.id.cb_tv_tdistance);
        cb_tv_date=(TextView)findViewById(R.id.cb_tv_date);
        cb_tv_time=(TextView)findViewById(R.id.cb_tv_time);

        cb_tv_from.setText(TaxiBooking.from_place);
        cb_tv_to.setText(TaxiBooking.to_place);
        cb_tv_tprice.setText(TaxiBooking.tcost);
        cb_tv_tdistance.setText(TaxiBooking.tdis);

        cb_btn_confirm.setOnClickListener(this);
        cb_tv_date.setOnClickListener(this);
        cb_tv_time.setOnClickListener(this);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                cb_tv_date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                cb_tv_time.setText(hourOfDay + ":" + minute);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cb_btn_confirm:
                sendSMSMessage();
                new CBook().execute();
                break;
            case R.id.cb_tv_date:
                datePickerDialog.show();
                break;
            case R.id.cb_tv_time:
                timePickerDialog.show();
                break;
        }
    }

    protected void sendSMSMessage() {
        Log.i("Send SMS", AvailableTaxi.d_gsm);

        String phoneNo = AvailableTaxi.d_gsm;
        String msg = " I want to travel from "+TaxiBooking.from_place+" to "+TaxiBooking.to_place+" on "
                +cb_tv_date.getText().toString()+ "at "+cb_tv_time.getText().toString();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
                parameters.add(new BasicNameValuePair("date_of_booking",cb_tv_date.getText().toString()));
                parameters.add(new BasicNameValuePair("time_of_booking", cb_tv_time.getText().toString()));
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
            Intent i = new Intent(ConfirmBooking.this,Booking.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
