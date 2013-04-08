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

public class HorecaListActivity extends ListActivity {

	public static final String CHOSEN_TEXT = "chosenText";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// Get the restaurants
		Cursor cursor = db.query(HorecaContract.Horeca.TABLE_NAME,
				HorecaContract.Horeca.COLUMN_NAMES, null, null, null, null, null);
		
		//db.close(); // too early, the adapter still uses it apparently
		
		// Create the List of restaurants to choose from
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, //this context
				android.R.layout.simple_list_item_1, //id of the item layout used by default for the individual rows (this id is pre-defined by Android)
				cursor,
				new String[] { HorecaContract.Horeca.NAME },
				new int[] { android.R.id.text1 }); // the list of objects to be adapted
		// to remove deprecation warning, I need to add ", 0" but it is only in API 11 and we need 2.3.3 which is API 10
		//db.close(); // too early, the adapter still uses it apparently
		setListAdapter(adapter);
		db.close();
		
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
        		Intent i = new Intent(HorecaListActivity.this, HorecaActivity.class);
        		// Tell PlatListActivity which horeca to take plats from
        		i.putExtra("horeca_id", id);
        		startActivity(i);
			}
		});
	}

}
