package com.horeca;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
	}
}
