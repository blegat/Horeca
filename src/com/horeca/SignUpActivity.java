package com.horeca;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends MyActivity {
	private Button button = null;
	private EditText email = null;
	private EditText name = null;
	private EditText password = null;
	private TextView password_err = null;
	private TextView mail_err = null;
	private EditText password_confirmation = null;
	private EditText address = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		email = (EditText) findViewById(R.id.mail);
		name = (EditText) findViewById(R.id.name);
		password = (EditText) findViewById(R.id.mot_de_passe);
		password_err = (TextView) findViewById(R.id.password_err);
		mail_err = (TextView) findViewById(R.id.mail_err);
		password_confirmation = (EditText) findViewById(R.id.password_confirmation);
		address = (EditText) findViewById(R.id.address);
				//setContentView(R.layout.activity_signup);
	    button = (Button) findViewById(R.id.sign_up_button);
	    button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	String new_name = name.getText().toString();
            	String new_email = email.getText().toString();
            	String new_psw = password.getText().toString();
            	String new_pswC = password_confirmation.getText().toString();
            	String new_address = address.getText().toString();
            	// On évite de garder la db ouverte trop longtemps
        		MySqliteHelper sqliteHelper = new MySqliteHelper(SignUpActivity.this);
        		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        		int err = User.signUp(db, new_email, new_name, new_psw, new_pswC, new_address);
        		db.close();
        		if (err == User.INVALID_EMAIL) {
        			password_err.setVisibility(View.GONE);
        			mail_err.setVisibility(View.VISIBLE);
        		}
        		else if (err == User.PASSWORDS_DONT_MATCH) {
        				mail_err.setVisibility(View.GONE);
        				password_err.setVisibility(View.VISIBLE);
        		}
        		else {
        			mail_err.setVisibility(View.GONE);
    				password_err.setVisibility(View.GONE);
    				finish();
        		}
        	}
        });
	}
}
