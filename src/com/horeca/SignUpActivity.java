package com.horeca;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends MyActivity{
	private Button button = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_signup);
	    button = (Button) findViewById(R.id.sign_up_button);
	    button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
        		
        		}
        });
	}
}
