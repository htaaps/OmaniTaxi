package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class ForgetPassword extends Activity implements View.OnTouchListener {
    EditText fg_et_email, fg_et_newpassword, fg_et_confirmpassword;
    Button fg_btn_change;
    boolean validate = false;
    String password, npassword, email;
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jparsor = new JSONParser();
    private static final String LOGIN_URL = "http://www.htlabs.in/student/taxibooking/forgetpassword.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        fg_et_email = (EditText) findViewById(R.id.fg_et_email);
        fg_et_newpassword = (EditText) findViewById(R.id.fg_et_newpassword);
        fg_et_confirmpassword = (EditText) findViewById(R.id.fg_et_confirmpassword);
        fg_btn_change = (Button) findViewById(R.id.fg_btn_change);
        fg_btn_change.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        switch (v.getId()) {
            case R.id.fg_btn_change:
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    validate = checkInput();
                }
                if (me.getAction() == MotionEvent.ACTION_UP) {
                    if (validate) {
                        new ForgetPwd().execute();
                    }
                }

                break;
        }
        return false;
    }

    public boolean checkInput() {
        password = fg_et_newpassword.getText().toString();
        npassword = fg_et_confirmpassword.getText().toString();
        email = fg_et_email.getText().toString();
        //email must have @
        if (!email.contains("@")) {
            fg_et_email.setText("");
            fg_et_email.requestFocus();
            Toast.makeText(getApplicationContext(), "please enter correct email", Toast.LENGTH_SHORT).show();
            return false;
        }

        //password greater than 6 chars
        if (password.length() < 6) {
            fg_et_newpassword.setText("");
            fg_et_confirmpassword.setText("");
            fg_et_newpassword.requestFocus();
            Toast.makeText(getApplicationContext(), "please enter password greater than 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        //repassword greater than 6 characters
        if (npassword.length() < 6) {
            fg_et_confirmpassword.setText("");
            fg_et_confirmpassword.setText("");
            fg_et_confirmpassword.requestFocus();
            Toast.makeText(getApplicationContext(), "please enter password greater than 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        //password matching
        if(!password.equals(npassword)){
            Toast.makeText(getApplicationContext(), "password do not match", Toast.LENGTH_SHORT).show();
            fg_et_newpassword.setText("");
            fg_et_confirmpassword.setText("");
            fg_et_newpassword.requestFocus();
            return false;
        }

        else {

            return true;
        }
    }

    public class ForgetPwd extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ForgetPassword.this);
            pDialog.setMessage("Attempting for connect to server...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub // here Check for success tag
            int success;
            String password = fg_et_newpassword.getText().toString();
            String cpassword = fg_et_confirmpassword.getText().toString();
            String email = fg_et_email.getText().toString();
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("password", password));
                Log.d("request!", "starting");
                JSONObject json = jparsor.makeHttpRequest(LOGIN_URL, "POST", params);

                // checking log for json response
                Log.d("Login attempt", json.toString());
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully Login!", json.toString());
                    Intent ii = new Intent(ForgetPassword.this, UserLogin.class);
                    startActivity(ii);
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {
            pDialog.dismiss();
            if (message != null) {
                Toast.makeText(ForgetPassword.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}


