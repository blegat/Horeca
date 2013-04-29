package com.horeca;

import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class CommandesActivity extends Activity implements ViewBinder, AdapterView.OnItemClickListener {
	
	private SimpleCursorAdapter adapter = null;
	private ListView commandes_list = null;
	
	private Plat plat = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the id of the plat to display provided by PlatListActivity
		Bundle b = getIntent().getExtras();
		long id = b.getLong("_id");
		
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// Get the plat from the db
		plat = new Plat(id, db);
		
		
		// Set the title of the activity
		setTitle(plat.getHoreca().getName() + " - " + plat.getName() + " - Commandes");
		
		// Set TextView's content
		setContentView(R.layout.activity_commandes);
		
		commandes_list = (ListView) findViewById(R.id.commandes_list);

		adapter = new SimpleCursorAdapter(this, //this context
				android.R.layout.simple_list_item_1, //id of the item layout used by default for the individual rows (this id is pre-defined by Android)
				//android.R.id.list,
				//R.id.plats_list,
				Commande.getAllCommandeForPlat(db, plat),
				new String[] { HorecaContract.Commande.TEMPS },
				new int[] { android.R.id.text1 }); // the list of objects to be adapted
		// to remove deprecation warning, I need to add ", 0" but it is only in API 11 and we need 2.3.3 which is API 10
		
		adapter.setViewBinder(this);
		
		commandes_list.setAdapter(adapter);
		
		db.close();
		
		commandes_list.setOnItemClickListener(this);
	}
	
	private void refreshCommandesList() {
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		adapter.changeCursorAndColumns(Commande.getAllCommandeForPlat(db, plat),
					new String[] { HorecaContract.Commande.TEMPS },
					new int[] { android.R.id.text1 });
				
		db.close();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		Commande commande = new Commande(db, id);
		commande.destroy(db);
		db.close();
		refreshCommandesList();
	}
	
    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
    	Date temps = new Date(cursor.getLong(HorecaContract.Commande.TEMPS_INDEX));
    	((TextView) view).setText(temps.toString());
        return true;
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.commandes, menu);
		return true;
	}

}
