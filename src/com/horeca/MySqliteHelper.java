package com.horeca;

//import static android.provider.BaseColumns._ID;
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
		db.execSQL("CREATE TABLE " + HorecaContract.Horeca.TABLE_NAME + "(" +
				HorecaContract.Horeca._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Horeca.NAME + " TEXT NOT NULL);");
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
		db.execSQL("CREATE TABLE " + HorecaContract.Contient.TABLE_NAME + "(" +
				HorecaContract.Contient._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Contient.PLAT_ID + " INTEGER NOT NULL, " +
				HorecaContract.Contient.INGREDIENT_ID + " INTEGER NOT NULL);");
	}	
	
	public void populateDatabase(SQLiteDatabase db) {
		String horeca_names[] = {"Quick", "Longeatude", "Crousty"};
		for(String horeca_name : horeca_names) {
			db.execSQL("INSERT INTO " + HorecaContract.Horeca.TABLE_NAME + "(" +
					HorecaContract.Horeca.NAME + ") VALUES('" + horeca_name + "');");
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

		long contient_plat_ids[] = {4, 4, 5};
		long contient_ingredient_ids[] = {1, 2, 3};
		for (int i = 0; i < contient_plat_ids.length; i++) {
			db.execSQL("INSERT INTO " + HorecaContract.Contient.TABLE_NAME + "(" +
					HorecaContract.Contient.PLAT_ID + ", " + HorecaContract.Contient.INGREDIENT_ID +
					") VALUES('" + contient_plat_ids[i] + "', '" + contient_ingredient_ids[i] + "');");
		}
	}
	
	public void deleteDatabase(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Horeca.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Plat.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Ingredient.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Contient.TABLE_NAME);
	}

}
