package com.horeca;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class PlatActivity extends Activity {

	private TextView horeca_name = null;
	private TextView plat_name = null;
	private TextView plat_price = null;
	private TextView plat_description = null;
	private TextView plat_stock = null;
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
		
		// Set TextView's content
		setContentView(R.layout.activity_plat);
		horeca_name = (TextView) findViewById(R.id.horeca_name);
		horeca_name.setText(plat.getHoreca().getName());
		
		plat_name = (TextView) findViewById(R.id.plat_name);
		plat_name.setText(plat.getName());
		
		plat_price = (TextView) findViewById(R.id.plat_price);
		plat_price.setText(((Double) plat.getPrice()).toString());
		
		plat_description = (TextView) findViewById(R.id.plat_description);
		if (plat.hasDescription()) {
			plat_description.setText(plat.getDescription());
		} else {
			findViewById(R.id.plat_description_label).setVisibility(View.GONE);
			plat_description.setVisibility(View.GONE);
		}
		
		plat_stock = (TextView) findViewById(R.id.plat_stock);
		if (plat.hasStock()) {
			plat_stock.setText(((Long) plat.getStock()).toString());
		} else {
			plat_stock.setText(R.string.plat_stock_unknown);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plat, menu);
		return true;
	}

}
