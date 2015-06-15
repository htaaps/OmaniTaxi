package in.htlabs.suaad.balqees.omanitaxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by admin on 5/22/2015.
 */
public class Booking extends Activity  implements View.OnClickListener {
    ImageButton bl_btn_nbooking,bl_btn_view,bl_btn_cancel;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_booking);
        bl_btn_nbooking=(ImageButton)findViewById(R.id.bl_btn_nbooking);
        bl_btn_view=(ImageButton)findViewById(R.id.bl_btn_view);
        bl_btn_cancel=(ImageButton)findViewById(R.id.bl_btn_cancel);

        bl_btn_nbooking.setOnClickListener(this);
        bl_btn_view.setOnClickListener(this);
        bl_btn_cancel.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bl_btn_nbooking:
                i = new Intent(Booking.this, MapsActivity.class);
                startActivity(i);
                break;
            case R.id.bl_btn_view:
                 i = new Intent(Booking.this, ViewBooking.class);
                startActivity(i);
                break;
            case R.id.bl_btn_cancel:
                i = new Intent(Booking.this, CancelBooking.class);
                startActivity(i);
                break;

        }

    }
}
