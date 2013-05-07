package com.horeca;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

public class HorecaActivity extends FragmentActivity {

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_horeca);
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // Presentation is the default tab which is mandatory
        // since it sets the title of the activity.
        // Setting the title here would require a access to the database
        // or sharing the Horeca object which would be complicating things.
        tabHost.addTab(tabHost.newTabSpec("pres").setIndicator("Présentation",
        		getResources().getDrawable(android.R.drawable.ic_menu_gallery)),
        		PresentationFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("menu").setIndicator("Menu",
        		getResources().getDrawable(android.R.drawable.ic_menu_agenda)),
        		MenuFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("horaire").setIndicator("Horaire",
        		getResources().getDrawable(android.R.drawable.ic_menu_today)),
        		HoraireFragment.class, null);
        // Affichage de photo d'horeca
        tabHost.addTab(tabHost.newTabSpec("photos").setIndicator("Photos",
        		getResources().getDrawable(android.R.drawable.ic_menu_gallery)),
        		ImageHorecaFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("label").setIndicator("Label",
        		getResources().getDrawable(android.R.drawable.ic_menu_gallery)),
        		LabelFragment.class, null);
     //   Button favorite=(Button)findViewById(R.id.favorite);
    //	favorite.setOnClickListener(new OnClickListener(){
    //	@Override
    	//	public void onClick(View v){
    		//int i;
    	//	}
    	//});
    }
}
