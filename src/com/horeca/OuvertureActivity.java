package com.horeca;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OuvertureActivity extends MyActivity implements View.OnClickListener {

	private TextView ouverture_debut = null;
	private TextView ouverture_fin = null;
	private TextView ouverture_places = null;
	private View reservation_layout = null;
	private EditText reservation_places = null;
	private Button reservation_button = null;
	private View current_reservation_layout = null;
	private TextView current_reservation_places = null;
	private Button delete_reservation_button = null;
	private Ouverture ouverture = null;
	private Reservation reservation = null;

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
		
		reservation_layout = findViewById(R.id.reservation_layout);
		reservation_places = (EditText) findViewById(R.id.reservation_number);
		reservation_button = (Button) findViewById(R.id.reservation_button);
		
		current_reservation_layout = findViewById(R.id.current_reservation_layout);
		current_reservation_places = (TextView) findViewById(R.id.current_reservation_number);
		delete_reservation_button = (Button) findViewById(R.id.delete_reservation_button);
		refreshSigning();
	}
	
	@Override
	protected void notifySignedIn() {
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		reservation = Reservation.getReservationForOuverture(db, ouverture);
		
		// close the db, exerything has been loaded in the constructor of Plat
		db.close();
		
		reservation_layout.setVisibility(View.VISIBLE);
		current_reservation_layout.setVisibility(View.VISIBLE);
		
		reservation_button.setOnClickListener(this);
		delete_reservation_button.setOnClickListener(this);
		
		if (reservation == null) {
			updateButtonsForNoReservation();
		} else {
			updateButtonsForReservation();
		}
	}
	
	@Override
	protected void notifySignedOut() {
		reservation_layout.setVisibility(View.GONE);
		current_reservation_layout.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View view) {
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		
		boolean needReload = false;

		if (view == reservation_button) {
			long places = 0;
			try {
				places = Long.parseLong(reservation_places.getText().toString());
			} catch (NumberFormatException e) {
				places = 0;
			}
			if (reservation == null) {
				if (places <= 0 || (ouverture.hasPlaces() && places > ouverture.getPlaces())) {
					Toast.makeText(this, R.string.invalid_amount_warning, Toast.LENGTH_SHORT).show();
				} else {
					reservation = Reservation.createReservation(db, ouverture, places);
					updateButtonsForReservation();
					needReload = true;
				}
			} else {
				if (reservation.getPlaces() != places) {
					if (places <= 0 ||
							(ouverture.hasPlaces() && ouverture.getPlaces() < places - reservation.getPlaces())) {
						Toast.makeText(this, R.string.invalid_amount_warning, Toast.LENGTH_SHORT).show();
					} else {
						reservation.setPlaces(db, places);
						updateButtonsForReservation(); // for current_reservation_places
						needReload = true;
					}
				}
			}
		} else {
			reservation.destroy(db);
			reservation = null;
			updateButtonsForNoReservation();
			needReload = true;
		}
		
		if (needReload && ouverture.hasPlaces()) {
			ouverture.reloadPlaces(db);
			ouverture_places.setText(((Long) ouverture.getPlaces()).toString());
		}		
		
		db.close();
	}
	
	public void updateButtonsForReservation() {
		reservation_button.setText(R.string.reservation_button_update_label);
		current_reservation_places.setText(String.valueOf(reservation.getPlaces()));
		current_reservation_layout.setVisibility(View.VISIBLE);
	}
	public void updateButtonsForNoReservation() {
		reservation_button.setText(R.string.reservation_button_new_label);
		current_reservation_layout.setVisibility(View.GONE);
	}

}