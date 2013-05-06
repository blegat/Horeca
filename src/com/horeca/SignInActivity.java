package com.horeca;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity {
	private EditText mot_de_passe_txt = null;
	private EditText mail_txt = null;
	private Button button = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_signin);
	    mot_de_passe_txt = (EditText) findViewById(R.id.mot_de_passe);
	    mail_txt = (EditText) findViewById(R.id.mail);
	    button = (Button) findViewById(R.id.sign_in_button);
	    button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
        		// Open the db
        		MySqliteHelper sqliteHelper = new MySqliteHelper(SignInActivity.this);
        		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        		int err = User.signIn(db, mail_txt.getText().toString(), mot_de_passe_txt.getText().toString());
            }
        });
	}
}
