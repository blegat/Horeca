package com.horeca;

import java.util.Date;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class ImageHorecaFragment extends Fragment implements ViewBinder {
	
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
		/*db.query(HorecaContract.Ouverture.TABLE_NAME,
				HorecaContract.Ouverture.COLUMN_NAMES,
				HorecaContract.Ouverture.HORECA_ID + " == ?",
				new String[]{((Long) horeca_id).toString()},
				null, null, null);*/
		
		View view = inflater.inflate(R.layout.horaire_view, container, false);
		
		// Display the horaire in a list
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), //this context
				android.R.layout.simple_list_item_1, //id of the item layout used by default for the individual rows (this id is pre-defined by Android)
				//android.R.id.list,
				//R.id.plats_list,
				cursor,
				new String[] { HorecaContract.Ouverture.DEBUT, HorecaContract.Ouverture.FIN },
				new int[] { android.R.id.text1 }); // the list of objects to be adapted
		// to remove deprecation warning, I need to add ", 0" but it is only in API 11 and we need 2.3.3 which is API 10
		
		adapter.setViewBinder(this);
		
		ouvertures_list = (ListView) view.findViewById(R.id.ouvertures_list);
		ouvertures_list.setAdapter(adapter);
		
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
	
    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
    	Date debut = new Date(cursor.getLong(HorecaContract.Ouverture.DEBUT_INDEX));
    	((TextView) view).setText(debut.toString());
        return true;
    }
	
}