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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 5/22/2015.
 */
public class UserLogin extends Activity implements View.OnClickListener {

    Button l_btn_log;
    EditText l_edt_user,l_edt_pwd;
    TextView forgot_password,reg;
    Intent i;
    private ProgressDialog pDialog;
    public static String user=null;

     // JSON parser class
    JSONParser jparsor=new JSONParser();
    private static final String LOGIN_URL = "http://www.htlabs.in/student/taxibooking/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        l_btn_log = (Button) findViewById(R.id.l_btn_log);
        l_edt_user = (EditText) findViewById(R.id.l_edt_user);
        l_edt_pwd = (EditText) findViewById(R.id.l_edt_pwd);
        reg = (TextView) findViewById(R.id.reg);
        forgot_password = (TextView) findViewById(R.id.forgot_password);

        l_btn_log.setOnClickListener(this);
        reg.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l_btn_log:
                new AttemptLogin().execute();
                break;
            case R.id.forgot_password:
                i = new Intent(UserLogin.this, ForgetPassword.class);
                startActivity(i);
                break;
            case R.id.reg:
                i = new Intent(UserLogin.this, Register.class);
                startActivity(i);
                break;
        }
    }
    class AttemptLogin extends AsyncTask<String, String, String> {
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(UserLogin.this);
            pDialog.setMessage("Attempting for login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub // here Check for success tag
            int success;
            String username = l_edt_user.getText().toString();
            String password = l_edt_pwd.getText().toString();

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                Log.d("request!", "starting");
                JSONObject json = jparsor.makeHttpRequest( LOGIN_URL, "POST", params);

                // checking log for json response
                Log.d("Login attempt", json.toString());
                success = json.getInt(TAG_SUCCESS);
                UserLogin.user=json.getString(TAG_USER_ID);
                if (success == 1) {
                    Log.d("Successfully Login!", json.toString());
                    Intent ii = new Intent(UserLogin.this,Booking.class);
                    startActivity(ii);
                    finish();
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
                Toast.makeText(UserLogin.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}






