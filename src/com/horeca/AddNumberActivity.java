package com.horeca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNumberActivity extends MyActivity{
	private EditText number = null;
	private Button button = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_profile);
	    number = (EditText) findViewById(R.id.number);
	    button = (Button) findViewById(R.id.add_number2);
	    button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	
            }
	    });
	}
}
