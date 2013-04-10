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
import android.widget.Spinner;

public class MainActivity extends Activity {
	
	private Button selectItemButton = null;
	private long selected_ville_id = -1;
	private Spinner ville_spinner = null;
	public static String VILLE_ID_EXTRA = "ville_id";
	
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
        
        selectItemButton = (Button) findViewById(R.id.button_choose_horeca);
        selectItemButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		Intent i = new Intent(MainActivity.this, HorecaListActivity.class);
        		Log.i("ville_id main", ((Long) selected_ville_id).toString());
        		i.putExtra(VILLE_ID_EXTRA , selected_ville_id);
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
