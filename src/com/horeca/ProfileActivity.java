package com.horeca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends MyActivity {
	private Button button = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_profile);
	    button = (Button) findViewById(R.id.Change_psw);
	    button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	startActivity(new Intent(ProfileActivity.this,ChangePasswordActivity.class));
            }
	    });
	}
}


