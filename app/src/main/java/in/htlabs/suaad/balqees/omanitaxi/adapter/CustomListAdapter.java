package in.htlabs.suaad.balqees.omanitaxi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import in.htlabs.suaad.balqees.omanitaxi.R;
import in.htlabs.suaad.balqees.omanitaxi.app.AppController;
import in.htlabs.suaad.balqees.omanitaxi.model.VDetails;


public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<VDetails> vdetailsItems;
	VDetails vdetails;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<VDetails> vdetailsItems) {
		this.activity = activity;
		this.vdetailsItems = vdetailsItems;
	}

	@Override
	public int getCount() {
		return vdetailsItems.size();
	}

	@Override
	public Object getItem(int location) {
		return vdetailsItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.view_booking, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		TextView vb_tv_bdetails          = (TextView) convertView.findViewById(R.id.vb_tv_bdetails);

		// getting movie data for the row
		vdetails = vdetailsItems.get(position);


		// title
		vb_tv_bdetails.setText(vdetails.getBdetails());

		return convertView;
	}

}