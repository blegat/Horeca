package com.horeca;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PresentationFragment extends Fragment {
	
	private TextView horeca_description = null;
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
        
		horeca_description = (TextView) view.findViewById(R.id.horeca_description);
		if (horeca.hasDescription()) {
			horeca_description.setText(horeca.getDescription());
		} else {
			view.findViewById(R.id.horeca_description_label).setVisibility(View.GONE);
			horeca_description.setVisibility(View.GONE);
		}
		return view;
    }
}
