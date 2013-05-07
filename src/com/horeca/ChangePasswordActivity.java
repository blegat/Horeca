package com.horeca;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePasswordActivity extends MyActivity {
	private Button button = null;
	EditText ancien_mdp = null;
	EditText nouveau_mdp = null;
	EditText confirmation_mdp = null;
	TextView password_error = null;
	private long id;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_changepassword);
	    ancien_mdp = (EditText) findViewById(R.id.mail);
		nouveau_mdp = (EditText) findViewById(R.id.name);
		confirmation_mdp = (EditText) findViewById(R.id.password_confirmation);
		password_error=(TextView) findViewById(R.id.password_error);
	    button = (Button) findViewById(R.id.change_pass_button);
	    button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	if(nouveau_mdp.getText().toString()==confirmation_mdp.getText().toString()){
            	MySqliteHelper sqliteHelper = new MySqliteHelper(ChangePasswordActivity.this);
        		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        		ContentValues cv = new ContentValues();
        		cv.put(HorecaContract.User.PASSWORD, nouveau_mdp.getText().toString());
        		db.insert(HorecaContract.User.TABLE_NAME, null, cv);
        		db.close();
        		finish();
            	}
            	else{
            		password_error.setVisibility(View.VISIBLE);
            	}
            }
	    });
	}
}

