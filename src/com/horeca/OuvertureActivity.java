package com.horeca;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OuvertureActivity extends Activity {

	private TextView ouverture_debut = null;
	private TextView ouverture_fin = null;
	private TextView ouverture_places = null;
	private Button reservation_button = null;
	private Ouverture ouverture = null;

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
		ouverture = new Ouverture(id, db);
		
		// close the db, exerything has been loaded in the constructor of Plat
		db.close();
		
		// Set TextView's content
		setContentView(R.layout.activity_ouverture);
		ouverture_debut = (TextView) findViewById(R.id.ouverture_debut);
		ouverture_debut.setText(ouverture.getDebut());
		
		ouverture_fin = (TextView) findViewById(R.id.ouverture_fin);
		ouverture_fin.setText(ouverture.getFin());
		
		ouverture_places = (TextView) findViewById(R.id.ouverture_places);
		if (ouverture.hasPlaces()) {
			ouverture_places.setText(((Long) ouverture.getPlaces()).toString());
		} else {
			findViewById(R.id.ouverture_places_label).setVisibility(View.GONE);
			ouverture_places.setVisibility(View.GONE);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plat, menu);
		return true;
	}

}