package com.horeca;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PlatActivity extends MyActivity implements OnClickListener {

	private TextView horeca_name = null;
	private TextView plat_name = null;
	private TextView plat_price = null;
	private TextView plat_description = null;
	private TextView plat_stock = null;
	private TextView plat_ingredients = null;
	
	private View commande_sep = null;
	private DatePicker commande_date = null;
	private TimePicker commande_time = null;
	private EditText commande_nombre = null;
	private TextView commande_nombre_label = null;
	private Button commande_button = null;
	private Button current_commandes_button = null;
	private ImageButton favorite = null;
	private TextView favorite_label = null;
	
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
		
		// close the db, everything has been loaded in the constructor of Plat
		db.close();
		
		// Set the title of the activity
		setTitle(plat.getHoreca().getName() + " - " + plat.getName());
		
		// Set TextView's content
		setContentView(R.layout.activity_plat);
		horeca_name = (TextView) findViewById(R.id.horeca_name);
		horeca_name.setText(plat.getHoreca().getName());
		
		plat_name = (TextView) findViewById(R.id.plat_name);
		plat_name.setText(plat.getName());
		
		plat_price = (TextView) findViewById(R.id.plat_price);
		plat_price.setText(Utils.priceToString(plat.getPrice(), this));
		
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
		
		plat_ingredients = (TextView) findViewById(R.id.plat_ingredients);
		Ingredient[] ingredients = plat.getIngredients();
		if (ingredients.length == 0) {
			plat_ingredients.setText(R.string.plat_ingredients_none);
		} else {
			StringBuilder text = new StringBuilder();
			for (int i = 0; i < ingredients.length; i++) {
				if (i != 0) {
					text.append(", ");
				}
				text.append(ingredients[i].getName());
			}
			plat_ingredients.setText(text.toString());
		}
		
		commande_sep = (View) findViewById(R.id.commande_sep);
		commande_date = (DatePicker) findViewById(R.id.commande_date);
		commande_time = (TimePicker) findViewById(R.id.commande_time);
		commande_nombre_label = (TextView) findViewById(R.id.commande_nombre_label);
		commande_nombre = (EditText) findViewById(R.id.commande_nombre);
		commande_button = (Button) findViewById(R.id.commande_button);
		current_commandes_button = (Button) findViewById(R.id.current_commandes_button);
		favorite_label = (TextView) findViewById(R.id.plat_is_favorite);
		favorite = (ImageButton) findViewById(R.id.favorite);
		refreshSigning();
	}
	
	@Override
	protected void notifySignedOut() {
		commande_sep.setVisibility(View.GONE);
		commande_date.setVisibility(View.GONE);
		commande_time.setVisibility(View.GONE);
		commande_nombre_label.setVisibility(View.GONE);
		commande_nombre.setVisibility(View.GONE);
		commande_button.setVisibility(View.GONE);
		current_commandes_button.setVisibility(View.GONE);
	}
	
	@Override
	protected void notifySignedIn() {
		commande_sep.setVisibility(View.VISIBLE);
		commande_date.setVisibility(View.VISIBLE);
		commande_time.setVisibility(View.VISIBLE);
		commande_nombre_label.setVisibility(View.VISIBLE);
		commande_nombre.setVisibility(View.VISIBLE);
		commande_button.setVisibility(View.VISIBLE);
		current_commandes_button.setVisibility(View.VISIBLE);
		
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		int minute=cal.get(Calendar.MINUTE);
		
		commande_date.updateDate(year, month, day);
		commande_time.setCurrentHour(hour);
		commande_time.setCurrentMinute(minute);
		commande_button.setOnClickListener(this);
		current_commandes_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(PlatActivity.this, CommandesActivity.class);
				i.putExtra("_id", plat.getId());
				startActivity(i);
			}
		});
	}
	
	private void refreshStock(SQLiteDatabase db) {
		if (plat.hasStock()) {
			plat.reloadStock(db);
			plat_stock.setText(String.valueOf(plat.getStock()));
		}
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		refreshStock(db);
		
		db.close();
	}
	public void onResume() {
    	super.onResume();
    	if (User.isSignedIn()) {
    		// FIXME only do it if necessary
			MySqliteHelper sqliteHelper = new MySqliteHelper(this);
			SQLiteDatabase db = sqliteHelper.getReadableDatabase();
			
			plat.refreshFavorite(db);
			
			db.close();
    		if (plat.isFavorite()) {
    			favorite.setImageResource(R.drawable.star_on);
    		} else {
    			favorite.setImageResource(R.drawable.star_off);
    		}
    		favorite.setOnClickListener(this);
    		favorite_label.setVisibility(View.VISIBLE);
    		favorite.setVisibility(View.VISIBLE);
    	} else {
    		favorite_label.setVisibility(View.GONE);
    		favorite.setVisibility(View.GONE);
    	}
    }

	@Override
	public void onClick(View view) {
		if (view == favorite) {
			if (plat.isFavorite()) {
				favorite.setImageResource(R.drawable.star_off);
				MySqliteHelper sqliteHelper = new MySqliteHelper(this);
				SQLiteDatabase db = sqliteHelper.getReadableDatabase();
				plat.removeFavorite(db);
				db.close();
			} else {
				MySqliteHelper sqliteHelper = new MySqliteHelper(this);
				SQLiteDatabase db = sqliteHelper.getReadableDatabase();
				favorite.setImageResource(R.drawable.star_on);
				plat.setFavorite(db);
				db.close();
			}
		} else {
			long nombre = 0;
			try {
				nombre = Long.parseLong(commande_nombre.getText().toString());
			} catch (NumberFormatException e) {
				nombre = 0;
			}
			if (nombre <= 0 || (plat.hasStock() && nombre > plat.getStock())) {
				Toast.makeText(this, R.string.invalid_amount_warning, Toast.LENGTH_SHORT).show();
			} else {
				MySqliteHelper sqliteHelper = new MySqliteHelper(this);
				SQLiteDatabase db = sqliteHelper.getWritableDatabase();
			
				Commande.createCommande(db, plat,
						new GregorianCalendar(commande_date.getYear(), commande_date.getMonth(),
								commande_date.getDayOfMonth(), commande_time.getCurrentHour(),
								commande_time.getCurrentMinute()).getTime(), nombre);
				
				refreshStock(db);
			
				db.close();
			}
		}
	}
}