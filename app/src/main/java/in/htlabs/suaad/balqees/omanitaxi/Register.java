
package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.OnClickListener;
import static android.widget.AdapterView.OnItemSelectedListener;
import static android.widget.AdapterView.OnTouchListener;


public class Register extends Activity implements OnTouchListener {

    Button su_btn_submit;
    EditText su_et_username, su_et_pwd, su_et_fname, su_et_lname, su_et_email, su_et_mobile;
    Boolean validate = false;
    String password, email, mobile;
    Spinner sp_city,sp_gender;

    String[] scity = {
            "Al Qabal",
            "Al Kamil Wal Wafi",
            "Bidiya",
            "Ibra",
            "Jalan Bani Bu Ali",
            "Jalan Bani Bu Hassan",
            "Mudhaybi",
            "Sur",
            "dima wa taian",
            "Wadi Bani Khalid" };

    String[] sgender={
       "Male",
        "Female"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup);
        su_btn_submit = (Button) findViewById(R.id.su_btn_submit);
        su_et_username = (EditText) findViewById(R.id.su_et_username);
        su_et_pwd = (EditText) findViewById(R.id.su_et_pwd);
        su_et_fname = (EditText) findViewById(R.id.su_et_fname);
        su_et_lname = (EditText) findViewById(R.id.su_et_lname);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        sp_gender = (Spinner) findViewById(R.id.sp_gender);
        su_et_email = (EditText) findViewById(R.id.su_et_email);
        su_et_mobile = (EditText) findViewById(R.id.su_et_mobile);
        su_btn_submit.setOnTouchListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scity);
        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sgender);
        sp_city.setAdapter(adapter);
        sp_gender.setAdapter(adapterr);

        sp_city.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = sp_city.getSelectedItemPosition();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        sp_gender.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = sp_gender.getSelectedItemPosition();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }
    @Override
    public boolean onTouch(View v, MotionEvent me) {
        switch (v.getId()) {
            case R.id.su_btn_submit:
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    validate = checkInput();
                }
                if(me.getAction()== MotionEvent.ACTION_UP){
                    if(validate){new RegisterUser().execute();}
                }
                break;
        }
        return false;
    }
    public boolean checkInput() {
        //username = su_et_username.getText().toString().toLowerCase();
        password = su_et_pwd.getText().toString();
       // fname = su_et_fname.getText().toString().toLowerCase();
       // lname = su_et_lname.getText().toString().toLowerCase();
       // gender = sp_gender.getSelectedItem().toString();
       // city = sp_city.getSelectedItem().toString();
        email = su_et_email.getText().toString();
        mobile = su_et_mobile.getText().toString();

        //email must have @
        if (!email.contains("@")) {
            su_et_email.setText("");
            su_et_email.requestFocus();
            su_et_email.setError("please enter correct email");
            Toast.makeText(getApplicationContext(), "please enter correct email", Toast.LENGTH_SHORT).show();
            return false;
        }
        //password greater than 6 chars
        if (password.length() < 6) {
            su_et_pwd.setText("");
            su_et_pwd.requestFocus();
            su_et_pwd.setError("please enter password greater than 6 characters");
            Toast.makeText(getApplicationContext(), "please enter password greater than 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
//mobile no should be of 8 digits
        if (mobile.length() > 8 || mobile.length() < 8) {
            su_et_mobile.setText("");
            su_et_mobile.requestFocus();
            su_et_mobile.setError("please enter mobile number of 8 digits");
            Toast.makeText(getApplicationContext(), "please enter mobile number of 8 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
             class RegisterUser extends AsyncTask<String, String, String> {

                // Progress Dialog
                private ProgressDialog pDialog;
                private JSONParser jsonParser = new JSONParser();
                private static final String REGISTER_URL = "http://www.htlabs.in/student/taxibooking/register.php";

                // JSON IDS:
                private static final String TAG_SUCCESS = "success";
                private static final String TAG_MESSAGE = "message";
                 boolean failure = false;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(Register.this);
                    pDialog.setMessage("Registering user to server...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... param) {
                    // TODO Auto-generated method stub
                    // Check for success tag
                    String username, password, fname, lname, city, gender, email, mobile;
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                        username = su_et_username.getText().toString();
                        password = su_et_pwd.getText().toString();
                        fname = su_et_fname.getText().toString();
                        lname = su_et_lname.getText().toString();
                        city= sp_city.getSelectedItem().toString();
                        gender =sp_gender.getSelectedItem().toString();
                        email = su_et_email.getText().toString();
                        mobile = su_et_mobile.getText().toString();

                        parameters.add(new BasicNameValuePair("username", username));
                        parameters.add(new BasicNameValuePair("password", password));
                        parameters.add(new BasicNameValuePair("fname", fname));
                        parameters.add(new BasicNameValuePair("lname", lname));
                        parameters.add(new BasicNameValuePair("city", city));
                        parameters.add(new BasicNameValuePair("gender", gender));
                        parameters.add(new BasicNameValuePair("email", email));
                        parameters.add(new BasicNameValuePair("mobile", mobile));

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
                        Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
                    }
                    Intent i = new Intent(Register.this,UserLogin.class);
                    startActivity(i);
                    finish();
                }
            }

        }


