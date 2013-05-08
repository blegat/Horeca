package com.horeca;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNumberActivity extends MyActivity{
	private EditText number = null;
	private Button button = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_addnumber);
	    number = (EditText) findViewById(R.id.number);
	    button = (Button) findViewById(R.id.add_number2);
	    button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	if (User.isSignedIn()) {
            		MySqliteHelper sqliteHelper = new MySqliteHelper(AddNumberActivity.this);
            		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
            		User.getCurrentUser().setNumtel(db, number.getText().toString());
            		db.close();
            	}
            	finish();
            }
	    });
	}
}
