package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

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

/**
 * Created by admin on 5/26/2015.
 */
public class ViewBooking extends Activity implements View.OnClickListener {

    TextView vb_bookingid, vb_from, vb_to, vb_date, vb_time, vb_total;
    String booking_id, from_, to_, date_, time_, total_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_booking);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog()
                .build());
        vb_bookingid = (TextView) findViewById(R.id.vb_bookingid);
        vb_from = (TextView) findViewById(R.id.vb_from);
        vb_to = (TextView) findViewById(R.id.vb_to);
        vb_date = (TextView) findViewById(R.id.vb_date);
        vb_time = (TextView) findViewById(R.id.vb_time);
        vb_total = (TextView) findViewById(R.id.vb_total);
        JSONObject json = null;
        String str = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();
        HttpPost myConnection = new HttpPost("http://www.htlabs.in/student/taxibooking/view.php");
        try {
            response = myClient.execute(myConnection);
            str = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            JSONArray jArray = new JSONArray(str);
            json = jArray.getJSONObject(0);
            vb_bookingid.setText(json.getString("bokking_id"));
            vb_from.setText(json.getString("from_"));
            vb_to.setText(json.getString("to_"));
            vb_date.setText(json.getString("date_"));
            vb_time.setText(json.getString("time_"));
            vb_total.setText(json.getString("total_price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}

