package com.horeca;

//import static android.provider.BaseColumns._ID;
import java.util.Calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "database.db";
	public static final int DATABASE_VERSION = 1;
	
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
		db.execSQL("CREATE TABLE " + HorecaContract.Ville.TABLE_NAME + "(" +
				HorecaContract.Ville._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Ville.NAME + " TEXT NOT NULL, " +
				HorecaContract.Ville.CODEPOSTAL + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.Horeca.TABLE_NAME + "(" +
				HorecaContract.Horeca._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Horeca.NAME + " TEXT NOT NULL, " +
				HorecaContract.Horeca.VILLE_ID + " INTEGER NOT NULL, " +
				HorecaContract.Horeca.NUMTEL + " TEXT NOT NULL, " +
				HorecaContract.Horeca.DESCRIPTION + " TEXT);");
		db.execSQL("CREATE TABLE " + HorecaContract.Plat.TABLE_NAME + "(" +
				HorecaContract.Plat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Plat.HORECA_ID + " INTEGER NOT NULL, " +
				HorecaContract.Plat.NAME + " TEXT NOT NULL, " +
				HorecaContract.Plat.PRICE + " INTEGER NOT NULL, " +
				HorecaContract.Plat.DESCRIPTION + " TEXT, " +
				HorecaContract.Plat.STOCK + " INTEGER);");
		db.execSQL("CREATE TABLE " + HorecaContract.Ingredient.TABLE_NAME + "(" +
				HorecaContract.Ingredient._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Ingredient.NAME + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.User.TABLE_NAME + "(" +
				HorecaContract.User._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.User.EMAIL + " TEXT NOT NULL, " +
				HorecaContract.User.NAME + " TEXT NOT NULL, " +
				HorecaContract.User.PASSWORD + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.Contient.TABLE_NAME + "(" +
				HorecaContract.Contient._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Contient.PLAT_ID + " INTEGER NOT NULL, " +
				HorecaContract.Contient.INGREDIENT_ID + " INTEGER NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.Ouverture.TABLE_NAME + "(" +
				HorecaContract.Ouverture._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Ouverture.HORECA_ID + " INTEGER NOT NULL, " +
				HorecaContract.Ouverture.DEBUT + " INTEGER NOT NULL, " +
				HorecaContract.Ouverture.FIN + " INTEGER NOT NULL, " +
				HorecaContract.Ouverture.PLACES + " INTEGER);");
		db.execSQL("CREATE TABLE " + HorecaContract.Reservation.TABLE_NAME + "(" +
				HorecaContract.Reservation._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Reservation.USER_ID + " INTEGER NOT NULL, " +
				HorecaContract.Reservation.OUVERTURE_ID + " INTEGER NOT NULL, " +
				HorecaContract.Reservation.PLACES + " INTEGER NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.Commande.TABLE_NAME + "(" +
				HorecaContract.Commande._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Commande.USER_ID + " INTEGER NOT NULL, " +
				HorecaContract.Commande.PLAT_ID + " INTEGER NOT NULL, " +
				HorecaContract.Commande.TEMPS + " INTEGER NOT NULL, " +
				HorecaContract.Commande.NOMBRE + " INTEGER NOT NULL);");
	}	
	
	public void populateDatabase(SQLiteDatabase db) {
		String ville_names[] = {"Louvain-la-Neuve", "Brussels"};
		long ville_codepostals[] = {1348, 1000};
		for (int i = 0; i < ville_names.length; i++) {
			db.execSQL("INSERT INTO " + HorecaContract.Ville.TABLE_NAME + "(" +
					HorecaContract.Ville.NAME + ", " + HorecaContract.Ville.CODEPOSTAL + ") VALUES('" +
					ville_names[i] + "', '" + ville_codepostals[i] + "');");
		}
		String horeca_names[] = {"Quick", "Longeatude", "Crousty"};
		long horeca_ville_ids[] = {1, 1, 1};
		String horeca_numtels[] = {"01045424242", "01045007007", "01045454545"};
		String horeca_descriptions[] = {"Nous, c est le goût !", "Cher mais bon !", null};
		for(int i = 0; i < horeca_names.length; i++) {
			String column_names = HorecaContract.Horeca.NAME + ", " +
					HorecaContract.Horeca.VILLE_ID + ", " + HorecaContract.Horeca.NUMTEL;
			String column_values = "'" + horeca_names[i] + "', '" + 
					horeca_ville_ids[i] + "', '" + horeca_numtels[i] + "'";
			if (horeca_descriptions[i] != null) {
				column_names = column_names + ", " + HorecaContract.Horeca.DESCRIPTION;
				column_values = column_values + ", '" + horeca_descriptions[i] + "'";
			}
			db.execSQL("INSERT INTO " + HorecaContract.Horeca.TABLE_NAME + "(" +
					column_names + ") VALUES(" + column_values + ");");
		}

		String plat_names[] = {"CheeseBurger", "Chicken Wigs Truck", "Cake méditerranéen",
					"Promenade de crustacés", "Gigondas", "Club", "Royal"};
		long plat_horeca_ids[] = {1, 1, 1,
				2, 2, 3, 3};
		long plat_prices[] = {300, 10000, 200,
				3000, 5500, 250, 365};
		String plat_descriptions[] = {
				null,
				"Huge amount of chicken",
				null,
				"Le meilleur plat de crustacé de Belgique",
				null,
				"Sandwich simple",
				"Sandwich magistral"};
		long plat_stocks[] = {-1, 4, 0,
				2, 10, -1, 10};
		for (int i = 0; i < plat_names.length; i++) {
			String column_names = HorecaContract.Plat.HORECA_ID + ", " +
					HorecaContract.Plat.NAME + ", " +
					HorecaContract.Plat.PRICE;
			String column_values = "'" + plat_horeca_ids[i] + "', '" +
					plat_names[i] + "', '" + plat_prices[i] + "'";
			if (plat_descriptions[i] != null) {
				column_names = column_names + ", " + HorecaContract.Plat.DESCRIPTION;
				column_values = column_values + ", '" + plat_descriptions[i] + "'";
			}
			if (plat_stocks[i] != -1) {
				column_names = column_names + ", " + HorecaContract.Plat.STOCK;
				column_values = column_values + ", '" + plat_stocks[i] + "'";
			}
			db.execSQL("INSERT INTO " + HorecaContract.Plat.TABLE_NAME + "(" +
					column_names + ") VALUES(" + column_values + ");");
		}

		String ingredient_names[] = {"Homard", "Crabe", "Raisin"};
		for(String ingredient_name : ingredient_names) {
			db.execSQL("INSERT INTO " + HorecaContract.Ingredient.TABLE_NAME + "(" +
					HorecaContract.Ingredient.NAME + ") VALUES('" + ingredient_name + "');");
		}
		
		String user_emails[] = {"jean@dupont.com"};
		String user_names[] = {"Jean Dupont"};
		String user_passwords[] = {"foobar"};
		for (int i = 0; i < user_emails.length; i++) {
			db.execSQL("INSERT INTO " + HorecaContract.User.TABLE_NAME + "(" +
					HorecaContract.User.EMAIL + ", " + HorecaContract.User.NAME + ", " +
					HorecaContract.User.PASSWORD + ") VALUES('" +
					user_emails[i] + "', '" + user_names[i] + "', '" +
					user_passwords[i] + "');");
		}

		long contient_plat_ids[] = {4, 4, 5};
		long contient_ingredient_ids[] = {1, 2, 3};
		for (int i = 0; i < contient_plat_ids.length; i++) {
			db.execSQL("INSERT INTO " + HorecaContract.Contient.TABLE_NAME + "(" +
					HorecaContract.Contient.PLAT_ID + ", " + HorecaContract.Contient.INGREDIENT_ID +
					") VALUES('" + contient_plat_ids[i] + "', '" + contient_ingredient_ids[i] + "');");
		}
		
		Calendar now_cal = Calendar.getInstance();
		long hour = 60 * 60 * 1000, now = now_cal.getTimeInMillis();
		long ouverture_horeca_ids[] = {1, 1, 1, 2, 2, 3, 3};
		long ouverture_debuts[] = {now, now + 3 * hour, now + 10 * hour,
				now + hour, now + 8 * hour,
				now + 6 * hour, now + hour / 2};
		long ouverture_fins[] = {now + hour, now + 5 * hour, now + 12 * hour,
				now + 3 * hour, now + 10 * hour,
				now + 9 * hour, now + 4 * hour};
		long ouverture_places[] = {4, 8, -1, 8, 7, 32, 8};
		for (int i = 0; i < ouverture_horeca_ids.length; i++) {
			String column_names = HorecaContract.Ouverture.HORECA_ID + ", " +
					HorecaContract.Ouverture.DEBUT + ", " + HorecaContract.Ouverture.FIN;
			String column_values = "'" + ouverture_horeca_ids[i] + "', '" +
					ouverture_debuts[i] + "', '" + ouverture_fins[i] + "'";
			if (ouverture_places[i] != -1) {
				column_names = column_names + ", " + HorecaContract.Ouverture.PLACES;
				column_values = column_values + ", '" + ouverture_places[i] + "'";
			}
			db.execSQL("INSERT INTO " + HorecaContract.Ouverture.TABLE_NAME + "(" +
					column_names + ") VALUES(" + column_values + ");");
		}
	}
	
	public void deleteDatabase(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Ville.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Horeca.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Plat.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Ingredient.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.User.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Contient.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Ouverture.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Reservation.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Commande.TABLE_NAME);
	}

}
