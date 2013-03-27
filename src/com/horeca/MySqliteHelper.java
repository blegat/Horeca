package com.horeca;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "database.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_HORECAS = "horecas";
	public static final String HORECA_NAME = "nom";
	public static final String[] HORECAS_COLUMNS = new String[]{_ID, HORECA_NAME};
	public static final String TABLE_PLATS = "plats";
	public static final String PLAT_NAME = "nom";
	public static final String[] PLATS_COLUMNS = new String[]{_ID, PLAT_NAME};
	
	public MySqliteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDatabase(db);
		populateDatabase(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		deleteDatabase(db);
		onCreate(db);
	}
	
	public void createDatabase(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_HORECAS + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HORECA_NAME + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + TABLE_PLATS + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLAT_NAME + " TEXT NOT NULL);");
	}
	
	public void populateDatabase(SQLiteDatabase db) {
		String horecas[] = {"Quick", "Longeatude", "Crousty"};
		for(String horeca : horecas) {
			db.execSQL("INSERT INTO " + TABLE_HORECAS + "(" + HORECA_NAME + ") VALUES('" + horeca + "');");
		}
		String plats[] = {"CheeseBurger", "Chicken Wigs Truck", "Cake méditerranéen", "Promenade de crustacés", "Gigondas", "Club", "Royal"};
		for (String plat : plats) {
			db.execSQL("INSERT INTO " + TABLE_PLATS + "(" + PLAT_NAME + ") VALUES('" + plat + "');");
		}
	}
	
	public void deleteDatabase(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HORECAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLATS);
	}

}
