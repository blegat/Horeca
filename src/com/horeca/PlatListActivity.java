package com.horeca;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

public class PlatListActivity extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// Get the horeca from which to take the plats
		// specified by the HorecaListActivity calling this activity
		Bundle b = getIntent().getExtras();
		long horeca_id = b.getLong("horeca_id");
		
		// Get the menu
		Cursor cursor = db.query(HorecaContract.Plat.TABLE_NAME,
				HorecaContract.Plat.COLUMN_NAMES,
				HorecaContract.Plat.HORECA_ID + " == ?",
				new String[]{((Long) horeca_id).toString()},
				null, null, null);
		
		// Display the menu in a list
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, //this context
				android.R.layout.simple_list_item_1, //id of the item layout used by default for the individual rows (this id is pre-defined by Android)
				cursor,
				new String[] { HorecaContract.Plat.NAME },
				new int[] { android.R.id.text1 }); // the list of objects to be adapted
		// to remove deprecation warning, I need to add ", 0" but it is only in API 11 and we need 2.3.3 which is API 10
		setListAdapter(adapter);
		
		db.close(); // the adapter uses it so we can't do it earlier
		
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
        		Intent i = new Intent(PlatListActivity.this, PlatActivity.class);
        		// Tell the PlatActivity which plat to display
        		// by giving its id
        		i.putExtra("_id", id);
        		startActivity(i);
			}
		});
	}
	
}
