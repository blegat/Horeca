package com.horeca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends MyActivity {
	private TextView name = null;
	private TextView mail = null;
	private TextView address = null;
	private TextView ville = null;
	private Button button = null;
	private Button buttonnumber = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_profile);
	    name = (TextView) findViewById(R.id.show_name);
		name.setText(User.getCurrentUser().getName());
		mail = (TextView) findViewById(R.id.show_mail);
		mail.setText(User.getCurrentUser().getEmail());
		address = (TextView) findViewById(R.id.show_address);
		address.setText(User.getCurrentUser().getAddress());
		ville = (TextView) findViewById(R.id.show_ville);
		ville.setText(User.getCurrentUser().getVille().getName());
	    button = (Button) findViewById(R.id.Change_psw);
	    button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	startActivity(new Intent(ProfileActivity.this,ChangePasswordActivity.class));
            }
	    });
	    buttonnumber = (Button) findViewById(R.id.add_number);
	    buttonnumber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	startActivity(new Intent(ProfileActivity.this,AddNumberActivity.class));
            }
	    });
	}
}


