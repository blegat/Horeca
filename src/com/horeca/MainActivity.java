package com.horeca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends MyActivity {

	private Button selectItemButton = null;
	private long selected_ville_id = -1;
	private Spinner ville_spinner = null;
	private long selected_horecatype_id = -1;
	private Spinner horecatype_spinner = null;
	private long selected_plattype_id = -1;
	private Spinner plattype_spinner = null;
	private EditText ingredient = null;
	private TextView ingredient_error = null;
	private EditText price_min = null;
	private EditText price_max = null;
	private EditText distance_max = null;
	public static String VILLE_ID_EXTRA = "ville_id";
	public static String HORECATYPE_ID_EXTRA = "horecatype_id";
	public static String PLATTYPE_ID_EXTRA = "plattype_id";
	public static String INGREDIENT_NAME_EXTRA = "ingredient_name";
	public static String PRICE_MIN_EXTRA = "price_min";
	public static String PRICE_MAX_EXTRA = "price_max";
	public static String DISTANCE_MAX_EXTRA = "distance_max";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        ville_spinner = (Spinner) findViewById(R.id.ville_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
        		android.R.layout.simple_spinner_item, Ville.getAllVilles(db),
        		new String[]{HorecaContract.Ville.NAME}, new int[] {android.R.id.text1});
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ville_spinner.setAdapter(adapter);

        horecatype_spinner = (Spinner) findViewById(R.id.horecatype);

        SimpleCursorAdapter htadapter = new SimpleCursorAdapter(this,
        		android.R.layout.simple_spinner_item, HorecaType.getAllHorecaTypes(db),
        		new String[]{HorecaContract.HorecaType.NAME}, new int[] {android.R.id.text1});
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        htadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        horecatype_spinner.setAdapter(htadapter);

        plattype_spinner = (Spinner) findViewById(R.id.plattype);

        SimpleCursorAdapter ptadapter = new SimpleCursorAdapter(this,
        		android.R.layout.simple_spinner_item, PlatType.getAllPlatTypes(db),
        		new String[]{HorecaContract.PlatType.NAME}, new int[] {android.R.id.text1});
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        ptadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        plattype_spinner.setAdapter(ptadapter);

        db.close();

        ville_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
            	selected_ville_id = id;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selected_ville_id = -1;
            }
        });

        horecatype_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
            	selected_horecatype_id = id;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selected_horecatype_id = -1;
            }
        });
        plattype_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
            	selected_plattype_id = id;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selected_plattype_id = -1;
            }
        });

        ingredient = (EditText) findViewById(R.id.ingredient);
        ingredient_error = (TextView) findViewById(R.id.ingredient_error);
        price_min = (EditText) findViewById(R.id.price_min);
        price_max = (EditText) findViewById(R.id.price_max);
        distance_max = (EditText) findViewById(R.id.distance_max);

        selectItemButton = (Button) findViewById(R.id.button_choose_horeca);
        selectItemButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		Intent i = new Intent(MainActivity.this, HorecaListActivity.class);
        		String ingredient_name = ingredient.getText().toString();
        		if (!ingredient_name.equals("")) {
        			MySqliteHelper sqliteHelper = new MySqliteHelper(MainActivity.this);
        			SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        			String suggestion = Ingredient.getSuggestion(db, ingredient_name, false);
        			db.close();
        			if (!suggestion.toLowerCase().equals(ingredient_name.toLowerCase())) {
        				String error_message = "\"" + ingredient_name + "\" doesn't exists.";
        				if (suggestion != "") { // "" can be the closest match if it is very different from everything
        					error_message = error_message + " Did you mean \"" + suggestion + "\"?";
        				}
        				ingredient_error.setText(error_message);
        				Toast.makeText(MainActivity.this, suggestion, Toast.LENGTH_LONG).show();
        				return;
        			}
        			i.putExtra(INGREDIENT_NAME_EXTRA, suggestion);
        			// suggestion has the good case while ingredient_name might
        			// not have the good one
        		}
        		i.putExtra(VILLE_ID_EXTRA, selected_ville_id);
        		if (selected_horecatype_id != -1 && selected_horecatype_id != 1) {
        			i.putExtra(HORECATYPE_ID_EXTRA, selected_horecatype_id);
        		}
        		if (selected_plattype_id != -1 && selected_plattype_id != 1) {
        			i.putExtra(PLATTYPE_ID_EXTRA, selected_plattype_id);
        		}
        		include_number(price_min, PRICE_MIN_EXTRA, i, R.string.invalid_price_min_warning, MainActivity.this);
        		include_number(price_max, PRICE_MAX_EXTRA, i, R.string.invalid_price_max_warning, MainActivity.this);
        		include_number(distance_max, DISTANCE_MAX_EXTRA, i, R.string.invalid_distance_max_warning, MainActivity.this);
        		startActivity(i);
        	}
        });
    }

    private static void include_number(EditText et, String extra, Intent i, int warning, Context c) {
		String input = et.getText().toString();
		if (!input.equals("")) {
			Log.i(extra, input);
			try {
				i.putExtra(extra, Double.parseDouble(input));
			} catch (NumberFormatException e) {
				Toast.makeText(c, warning, Toast.LENGTH_SHORT).show();
				return;
			}
		}
    }
}
