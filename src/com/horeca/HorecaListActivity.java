package com.horeca;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

public class HorecaListActivity extends ListActivity {
	
	private Ville ville;
	private HorecaType horecaType = null;
	private PlatType platType = null;
	private Ingredient ingredient = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// Get the ville
		Bundle b = getIntent().getExtras();
		ville = new Ville(b.getLong(MainActivity.VILLE_ID_EXTRA), db);
		if (b.containsKey(MainActivity.HORECATYPE_ID_EXTRA)) {
			horecaType = new HorecaType(b.getLong(MainActivity.HORECATYPE_ID_EXTRA), db);
		}
		if (b.containsKey(MainActivity.PLATTYPE_ID_EXTRA)) {
			platType = new PlatType(b.getLong(MainActivity.PLATTYPE_ID_EXTRA), db);
		}
		if (b.containsKey(MainActivity.INGREDIENT_NAME_EXTRA)) {
			ingredient = new Ingredient(b.getString(MainActivity.INGREDIENT_NAME_EXTRA), db);
		}
		setTitle(String.format(getResources().getString(R.string.title_activity_horeca_list), ville.getName()));
		
		Filter filter = new Filter();
		filter.setVille(ville);
		if (horecaType != null) {
			filter.setHorecaType(horecaType);
		}
		if (platType != null) {
			filter.setPlatType(platType);
		}
		if (ingredient != null) {
			filter.setIngredient(ingredient);
		}
		if (b.containsKey(MainActivity.PRICE_MIN_EXTRA)) {
			filter.setMinPrice(b.getDouble(MainActivity.PRICE_MIN_EXTRA));
		}
		if (b.containsKey(MainActivity.PRICE_MAX_EXTRA)) {
			filter.setMaxPrice(b.getDouble(MainActivity.PRICE_MAX_EXTRA));
		}
		if (b.containsKey(MainActivity.DISTANCE_MAX_EXTRA)) {
			filter.setMaxDistance(b.getDouble(MainActivity.DISTANCE_MAX_EXTRA));
		}
		
		// Create the List of restaurants to choose from
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, //this context
				android.R.layout.simple_list_item_1, //id of the item layout used by default for the individual rows (this id is pre-defined by Android)
				filter.getMatchingHorecas(db, this),
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
