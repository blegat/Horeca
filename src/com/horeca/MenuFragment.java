package com.horeca;

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
import android.widget.TextView;

public class MenuFragment extends Fragment {
	
	private ListView plats_list = null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
         Bundle savedInstanceState) {
		
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// Get the horeca from which to take the plats
		// specified by the HorecaListActivity calling this activity
		Bundle b = getActivity().getIntent().getExtras();
		long horeca_id = b.getLong("horeca_id");
		
		// Get the menu
		Cursor cursor = db.query(HorecaContract.Plat.TABLE_NAME,
				HorecaContract.Plat.COLUMN_NAMES,
				HorecaContract.Plat.HORECA_ID + " == ?",
				new String[]{((Long) horeca_id).toString()},
				null, null, null);
		
		View view = inflater.inflate(R.layout.menu_view, container, false);
		
		// Display the menu in a list
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), //this context
				android.R.layout.simple_list_item_1, //id of the item layout used by default for the individual rows (this id is pre-defined by Android)
				//android.R.id.list,
				//R.id.plats_list,
				cursor,
				new String[] { HorecaContract.Plat.NAME },
				new int[] { android.R.id.text1 }); // the list of objects to be adapted
		// to remove deprecation warning, I need to add ", 0" but it is only in API 11 and we need 2.3.3 which is API 10
		
		plats_list = (ListView) view.findViewById(R.id.plats_list);
		plats_list.setAdapter(adapter);
		
		db.close(); // the adapter uses it so we can't do it earlier
		
		plats_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
        		Intent i = new Intent(getActivity(), PlatActivity.class);
        		// Tell the PlatActivity which plat to display
        		// by giving its id
        		i.putExtra("_id", id);
        		startActivity(i);
			}
		});
		//return getListView();
        return view;
	}
	
}
