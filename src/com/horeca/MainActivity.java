package com.horeca;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Button selectItemButton = null;
	private long selected_ville_id = -1;
	private Spinner ville_spinner = null;
	private long selected_horecatype_id = -1;
	private Spinner horecatype_spinner = null;
	private long selected_plattype_id = -1;
	private Spinner plattype_spinner = null;
	private EditText distance_max = null;
	public static String VILLE_ID_EXTRA = "ville_id";
	public static String HORECATYPE_ID_EXTRA = "horecatype_id";
	public static String PLATTYPE_ID_EXTRA = "plattype_id";
	public static String DISTANCE_MAX_EXTRA = "distance_max";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		// Open the db
		MySqliteHelper sqliteHelper = new MySqliteHelper(this);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// Log a user in
		User.signIn(db, "jean@dupont.com", "foobar");
		Log.i("current_user", User.getCurrentUser().getName());
        
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
        
        distance_max = (EditText) findViewById(R.id.distance_max);
        
        selectItemButton = (Button) findViewById(R.id.button_choose_horeca);
        selectItemButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		Intent i = new Intent(MainActivity.this, HorecaListActivity.class);
        		i.putExtra(VILLE_ID_EXTRA, selected_ville_id);
        		i.putExtra(HORECATYPE_ID_EXTRA, selected_horecatype_id);
        		i.putExtra(PLATTYPE_ID_EXTRA, selected_plattype_id);
        		String dm = distance_max.getText().toString();
        		if (!dm.equals("")) {
        			try {
        				i.putExtra(DISTANCE_MAX_EXTRA, Double.parseDouble(dm));
        			} catch (NumberFormatException e) {
        				Toast.makeText(MainActivity.this, R.string.invalid_distance_max_warning, Toast.LENGTH_SHORT).show();
        				return;
        			}
        		}
        		startActivity(i);
        	}
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
