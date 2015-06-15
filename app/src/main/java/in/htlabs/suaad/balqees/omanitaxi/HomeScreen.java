package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by admin on 5/23/2015.
 */
public class HomeScreen extends Activity  implements View.OnClickListener{
    ImageButton hs_btn_booking,hs_btn_about,hs_btn_contact;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        hs_btn_booking=(ImageButton)findViewById(R.id.hs_btn_booking);
        hs_btn_about=(ImageButton)findViewById(R.id.hs_btn_about);
        hs_btn_contact=(ImageButton)findViewById(R.id.hs_btn_contact);

        hs_btn_booking.setOnClickListener(this);
        hs_btn_about.setOnClickListener(this);
        hs_btn_contact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.hs_btn_booking:
                i = new Intent(HomeScreen.this, UserLogin.class);
                startActivity(i);
                break;
            case R.id.hs_btn_about:
                i = new Intent(HomeScreen.this, About.class);
                startActivity(i);
                break;
            case R.id.hs_btn_contact:
                i = new Intent(HomeScreen.this, Contact.class);
                startActivity(i);
                break;
        }
    }
}