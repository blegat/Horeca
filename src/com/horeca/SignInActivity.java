package com.horeca;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;


public class SignInActivity extends Activity {
	private EditText mot_de_passe_txt = null;
	private EditText mail_txt = null;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_signin);
	        mot_de_passe_txt = (EditText) findViewById(R.id.mot_de_passe);
	        mail_txt = (EditText) findViewById(R.id.mail);
	 }
}
