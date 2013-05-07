package com.horeca;

import java.util.Date;

import com.horeca.HorecaListActivity.HorecaListCursorAdapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class HoraireFragment extends Fragment {
	
	private ListView ouvertures_list = null;
	private Horeca horeca = null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
         Bundle savedInstanceState) {
		
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// Get the horeca from which to take the ouvertures
		// specified by the HorecaListActivity calling this activity
		Bundle b = getActivity().getIntent().getExtras();
		long horeca_id = b.getLong("horeca_id");
		horeca = new Horeca(horeca_id, db);
		
		// Get the ouvertures
		Cursor cursor = Ouverture.getAllOuverturesInHoreca(db, horeca);
		
		View view = inflater.inflate(R.layout.horaire_view, container, false);
		
		ouvertures_list = (ListView) view.findViewById(R.id.ouvertures_list);
		ouvertures_list.setAdapter(new HoraireCursorAdapter(getActivity(), cursor));
		
		db.close();
		
		ouvertures_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
        		Intent i = new Intent(getActivity(), OuvertureActivity.class);
        		// Tell the PlatActivity which plat to display
        		// by giving its id
        		i.putExtra("_id", id);
        		startActivity(i);
			}
		});
		//return getListView();
        return view;
	}
	
	public class HoraireCursorAdapter extends CursorAdapter {
		public HoraireCursorAdapter(Context context, Cursor c) {
			super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
		}
	 
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			Ouverture ouverture = new Ouverture(cursor);
			TextView range = (TextView) view.findViewById(R.id.ouverture_item_range);
			range.setText("From " + ouverture.getDebut() + " to " + ouverture.getFin() + ".");
		}
	 
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.horaire_item, parent, false);
			bindView(v, context, cursor);
			return v;
		}
	}
	
}