package com.horeca;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

public class PresentationFragment extends Fragment implements AdapterView.OnClickListener {
	private TextView distance_label = null;
	private TextView distance = null;
	private TextView horeca_numtel = null;
	private TextView horeca_horaire = null;
	private TextView horeca_description = null;
	private TextView horeca_pricerange = null;
	private TextView favorite_label = null;
	private ImageButton favorite = null;
	private Horeca horeca = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.presentation_view, container, false);
        
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// Get the horeca from which to take the plats
		// specified by the HorecaListActivity calling this activity
		Bundle b = getActivity().getIntent().getExtras();
		horeca = new Horeca(b.getLong("horeca_id"), db);
		
		// close db
		 db.close();
		
		// it sets the title because it is the default tab
		getActivity().setTitle(horeca.getName());
		
		GPSTracker gps = new GPSTracker(getActivity());
		distance = (TextView) view.findViewById(R.id.horeca_distance);
		if (gps.getLocation() != null) {
			// I cast it to long to get less precision decimals
			distance.setText(String.valueOf(Utils.distanceToString(horeca.getDistance(gps))));
		} else {
			distance.setVisibility(View.GONE);
			distance_label = (TextView) view.findViewById(R.id.horeca_distance_label);
			distance_label.setVisibility(View.GONE);
		}
		
		horeca_numtel = (TextView) view.findViewById(R.id.horeca_numtel);
		horeca_numtel.setText(horeca.getNumtel());

		horeca_horaire = (TextView) view.findViewById(R.id.horeca_horaire);
		if (horeca.hasHoraire()) {
			horeca_horaire.setText(horeca.getHoraire());
		} else {
			view.findViewById(R.id.horeca_horaire_label).setVisibility(View.GONE);
			horeca_horaire.setVisibility(View.GONE);
		}
		
		horeca_description = (TextView) view.findViewById(R.id.horeca_description);
		if (horeca.hasDescription()) {
			horeca_description.setText(horeca.getDescription());
		} else {
			view.findViewById(R.id.horeca_description_label).setVisibility(View.GONE);
			horeca_description.setVisibility(View.GONE);
		}
		
		horeca_pricerange = (TextView) view.findViewById(R.id.horeca_pricerange);
		horeca_pricerange.setText("You can eat here from " + horeca.getMinPrice() + " € to " + horeca.getMaxPrice() + " €.");
		
		favorite_label = (TextView) view.findViewById(R.id.horeca_is_favorite);
		favorite = (ImageButton) view.findViewById(R.id.favorite);
		Log.i("fav", favorite.toString());
		return view;
    }
    
    public void onResume() {
    	super.onResume();
    	if (User.isSignedIn()) {
    		if (horeca.isFavorite()) {
    			favorite.setImageResource(R.drawable.star_on);
    		} else {
    			favorite.setImageResource(R.drawable.star_off);
    		}
    		favorite.setOnClickListener(this);
    		favorite_label.setVisibility(View.VISIBLE);
    		favorite.setVisibility(View.VISIBLE);
    	} else {
    		favorite_label.setVisibility(View.GONE);
    		favorite.setVisibility(View.GONE);
    	}
    }
    
	public void onClick(View v){
		if (horeca.isFavorite()) {
			favorite.setImageResource(R.drawable.star_off);
			MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
			SQLiteDatabase db = sqliteHelper.getReadableDatabase();
			horeca.removeFavorite(db);
			db.close();
		} else {
			MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
			SQLiteDatabase db = sqliteHelper.getReadableDatabase();
			favorite.setImageResource(R.drawable.star_on);
			horeca.setFavorite(db);
			db.close();
		}
	}
}
