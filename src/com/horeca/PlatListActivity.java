package com.horeca;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class PlatListActivity extends ListActivity {
	
	List<String> someStrings = new ArrayList<String>();
	String chosenString;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		Cursor cursor = db.query(MySqliteHelper.TABLE_PLATS,
				MySqliteHelper.PLATS_COLUMNS, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			someStrings.add(cursor.getString(1));
			cursor.moveToNext();
		}
		
		db.close();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, //this context
				android.R.layout.simple_list_item_1, //id of the item layout used by default for the individual rows (this id is pre-defined by Android)
				someStrings); // the list of objects to be adapted
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				chosenString = someStrings.get(position);
				Toast.makeText(PlatListActivity.this, "You have chosen: " + chosenString, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
