package com.horeca;

//import static android.provider.BaseColumns._ID;
import java.util.Calendar;

import android.content.ContentValues;
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
				HorecaContract.Horeca.MIN_PRICE + " INTEGER NOT NULL, " +
				HorecaContract.Horeca.MAX_PRICE + " INTEGER NOT NULL, " +
				HorecaContract.Horeca.LONGITUDE + " INTEGER NOT NULL, " +
				HorecaContract.Horeca.LATITUDE + " INTEGER NOT NULL, " +
				HorecaContract.Horeca.VILLE_ID + " INTEGER NOT NULL, " +
				HorecaContract.Horeca.NUMTEL + " TEXT NOT NULL, " +
				HorecaContract.Horeca.HORAIRE + " TEXT, " +
				HorecaContract.Horeca.DESCRIPTION + " TEXT);");
		db.execSQL("CREATE TABLE " + HorecaContract.UserFavoriteHoreca.TABLE_NAME + " (" +
				HorecaContract.UserFavoriteHoreca._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.UserFavoriteHoreca.USER_ID + " INTEGER NOT NULL, "+
				HorecaContract.UserFavoriteHoreca.HORECA_ID + " INTEGER NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.HorecaType.TABLE_NAME + "(" +
				HorecaContract.HorecaType._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.HorecaType.NAME + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.HorecaTypeJoin.TABLE_NAME + "(" +
				HorecaContract.HorecaTypeJoin._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.HorecaTypeJoin.HORECA_ID + " INTEGER NOT NULL, " +
				HorecaContract.HorecaTypeJoin.HORECATYPE_ID + " INTEGER NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.Plat.TABLE_NAME + "(" +
				HorecaContract.Plat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Plat.HORECA_ID + " INTEGER NOT NULL, " +
				HorecaContract.Plat.NAME + " TEXT NOT NULL, " +
				HorecaContract.Plat.PRICE + " INTEGER NOT NULL, " +
				HorecaContract.Plat.DESCRIPTION + " TEXT, " +
				HorecaContract.Plat.STOCK + " INTEGER);");
		db.execSQL("CREATE TABLE " + HorecaContract.UserFavoritePlat.TABLE_NAME + " (" +
				HorecaContract.UserFavoritePlat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.UserFavoritePlat.USER_ID + " INTEGER NOT NULL, "+
				HorecaContract.UserFavoritePlat.PLAT_ID + " INTEGER NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.PlatType.TABLE_NAME + "(" +
				HorecaContract.PlatType._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.PlatType.NAME + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.PlatTypeJoin.TABLE_NAME + "(" +
				HorecaContract.PlatTypeJoin._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.PlatTypeJoin.PLAT_ID + " INTEGER NOT NULL, " +
				HorecaContract.PlatTypeJoin.PLATTYPE_ID + " INTEGER NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.Ingredient.TABLE_NAME + "(" +
				HorecaContract.Ingredient._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Ingredient.NAME + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.User.TABLE_NAME + "(" +
				HorecaContract.User._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.User.EMAIL + " TEXT NOT NULL, " +
				HorecaContract.User.NAME + " TEXT NOT NULL, " +
				HorecaContract.User.PASSWORD + " TEXT NOT NULL, " +
				HorecaContract.User.VILLE_ID + " INTEGER NOT NULL REFERENCES " + HorecaContract.Ville.TABLE_NAME + "(" + HorecaContract.Ville._ID + "), " +
				HorecaContract.User.ADDRESS + " TEXT NOT NULL);");
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
		db.execSQL("CREATE TABLE " + HorecaContract.Picture.TABLE_NAME + "(" +
				HorecaContract.Picture._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Picture.HORECA_ID + " INTEGER NOT NULL, " +
				HorecaContract.Picture.PATH + " TEXT NOT NULL, " +
				HorecaContract.Picture.NAME + "  TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.Label.TABLE_NAME + "(" +
				HorecaContract.Label._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.Label.PATH + " TEXT NOT NULL, " +
				HorecaContract.Label.NAME + "  TEXT NOT NULL, " +
				HorecaContract.Label.DESCRIPTIONLABEL + "  TEXT NOT NULL);");
		db.execSQL("CREATE TABLE " + HorecaContract.LabelJoinHoreca.TABLE_NAME + "(" +
				HorecaContract.LabelJoinHoreca._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				HorecaContract.LabelJoinHoreca.HORECA_ID + " INTEGER NOT NULL, " +
				HorecaContract.LabelJoinHoreca.LABEL_ID + "  INTEGER NOT NULL);");
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
		long horeca_min_prices[] = {350, 3000, 230};
		long horeca_max_prices[] = {890, 8900, 380};
		double horeca_longitudes[] = {4613115, 4619547, 4616412};
		double horeca_latitudes[] = {50669579, 50664065, 50669155};
		long horeca_ville_ids[] = {1, 1, 1};
		String horeca_numtels[] = {"01045424242", "01045007007", "01045454545"};
		String horeca_horaires[] = {"24/24 7/7.", null, "Every day from 8h to 16h."};
		String horeca_descriptions[] = {"Nous, c est le goût !", "Cher mais bon !", null};
		int horeca_is_favorite[] = {0,1,0};
		for (int i = 0; i < horeca_names.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(HorecaContract.Horeca.NAME, horeca_names[i]);
			cv.put(HorecaContract.Horeca.MIN_PRICE, horeca_min_prices[i]);
			cv.put(HorecaContract.Horeca.MAX_PRICE, horeca_max_prices[i]);
			cv.put(HorecaContract.Horeca.LONGITUDE, horeca_longitudes[i]);
			cv.put(HorecaContract.Horeca.LATITUDE, horeca_latitudes[i]);
			cv.put(HorecaContract.Horeca.VILLE_ID, horeca_ville_ids[i]);
			cv.put(HorecaContract.Horeca.NUMTEL, horeca_numtels[i]);
			if (horeca_horaires[i] != null) {
				cv.put(HorecaContract.Horeca.HORAIRE, horeca_horaires[i]);
			}
			if (horeca_descriptions[i] != null) {
				cv.put(HorecaContract.Horeca.DESCRIPTION, horeca_descriptions[i]);
			}
			db.insert(HorecaContract.Horeca.TABLE_NAME, null, cv);
		}
		
		String picturesPath[] = {"http://www.reklampub.com/wp-content/uploads/2012/10/","http://www.autogrill.be/Backend/Data/FlashImages/"};
		String picturesName[] = {"quick.jpg","000054.jpg"};
		long horecaID_s[] = {1,1};
		for (int i = 0; i < picturesPath.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(HorecaContract.Picture.HORECA_ID, horecaID_s[i]);
			cv.put(HorecaContract.Picture.PATH, picturesPath[i]);
			cv.put(HorecaContract.Picture.NAME, picturesName[i]);
			db.insert(HorecaContract.Picture.TABLE_NAME, null, cv);
		}
		
		String labelPicPath[] = {"https://encrypted-tbn0.gstatic.com/images?q=tbn:","http://www.autogrill.be/Backend/Data/FlashImages/"};
		String labelPicName[] = {"ANd9GcS4J-bXI3jjTQZgtCUtMiX95nO5HC73ChWafb9BC0vB8kz5dh-9","000054.jpg"};
		String labelDescription[] = {"Une description","Une autre description"};
		for (int i = 0; i < labelPicPath.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(HorecaContract.Label.PATH, labelPicPath[i]);
			cv.put(HorecaContract.Label.NAME, labelPicName[i]);
			cv.put(HorecaContract.Label.DESCRIPTIONLABEL, labelDescription[i]);
			db.insert(HorecaContract.Label.TABLE_NAME, null, cv);
		}
		
		long labelId[] = {0, 1};
		long horecaId[] = {2, 2};
		for (int i = 0; i < labelId.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(HorecaContract.LabelJoinHoreca.HORECA_ID, horecaId[i]);
			cv.put(HorecaContract.LabelJoinHoreca.LABEL_ID, labelId[i]);
			db.insert(HorecaContract.LabelJoinHoreca.TABLE_NAME, null, cv);
		}
		
		
		String horecatype_names[] = {"Any type", "Fast Food", "Snack"};
		for (int i = 0; i < horecatype_names.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(HorecaContract.HorecaType.NAME, horecatype_names[i]);
			db.insert(HorecaContract.HorecaType.TABLE_NAME, null, cv);
		}
		
		long horecatypejoin_horeca_ids[] = {1, 1, 3};
		long horecatypejoin_horecatype_ids[] = {2, 3, 3};
		for (int i = 0; i < horecatypejoin_horeca_ids.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(HorecaContract.HorecaTypeJoin.HORECA_ID, horecatypejoin_horeca_ids[i]);
			cv.put(HorecaContract.HorecaTypeJoin.HORECATYPE_ID, horecatypejoin_horecatype_ids[i]);
			db.insert(HorecaContract.HorecaTypeJoin.TABLE_NAME, null, cv);
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
		
		String plattype_names[] = {"Any type", "Sandwicherie"};
		for (int i = 0; i < plattype_names.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(HorecaContract.PlatType.NAME, plattype_names[i]);
			db.insert(HorecaContract.PlatType.TABLE_NAME, null, cv);
		}
		
		long plattypejoin_plat_ids[] = {6, 7};
		long plattypejoin_plattype_ids[] = {2, 2};
		for (int i = 0; i < plattypejoin_plat_ids.length; i++) {
			ContentValues cv = new ContentValues();
			cv.put(HorecaContract.PlatTypeJoin.PLAT_ID, plattypejoin_plat_ids[i]);
			cv.put(HorecaContract.PlatTypeJoin.PLATTYPE_ID, plattypejoin_plattype_ids[i]);
			db.insert(HorecaContract.PlatTypeJoin.TABLE_NAME, null, cv);
		}
		
		String ingredient_names[] = {"Homard", "Crabe", "Raisin"};
		for(String ingredient_name : ingredient_names) {
			db.execSQL("INSERT INTO " + HorecaContract.Ingredient.TABLE_NAME + "(" +
					HorecaContract.Ingredient.NAME + ") VALUES('" + ingredient_name + "');");
		}
		
		String user_emails[] = {"jean@dupont.com"};
		String user_names[] = {"Jean Dupont"};
		String user_passwords[] = {"foobar"};
		long user_ville_ids[] = {1};
		String user_address[] = {"Rue de Bugdroid, 42"};
		for (int i = 0; i < user_emails.length; i++) {
			User.signUp(db, user_emails[i], user_names[i], user_passwords[i], user_passwords[i], user_ville_ids[i], user_address[i]);
			User.signOut();
			/*ContentValues cv = new ContentValues();
			cv.put(HorecaContract.User.EMAIL, user_emails[i]);
			cv.put(HorecaContract.User.NAME, user_names[i]);
			cv.put(HorecaContract.User.PASSWORD, user_passwords[i]);
			cv.put(HorecaContract.User.VILLE_ID, user_ville_ids[i]);
			cv.put(HorecaContract.User.ADDRESS, user_address[i]);
			db.insert(HorecaContract.User.TABLE_NAME, null, cv);*/
		}
		
		int user_id = 1;
		int horeca_id = 2;
			db.execSQL("INSERT INTO " + HorecaContract.UserFavoriteHoreca.TABLE_NAME + "(" +
		HorecaContract.UserFavoriteHoreca.USER_ID + "," + HorecaContract.UserFavoriteHoreca.HORECA_ID + ") VALUES ('" + 
					user_id + "', '" + horeca_id + "');");
		
		int plat_id=3;
		db.execSQL("INSERT INTO " + HorecaContract.UserFavoritePlat.TABLE_NAME + "(" +
				HorecaContract.UserFavoritePlat.USER_ID + "," + HorecaContract.UserFavoritePlat.PLAT_ID + ") VALUES ('" + 
							user_id + "', '" + plat_id + "');");

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
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.HorecaType.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.HorecaTypeJoin.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Plat.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.PlatType.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.PlatTypeJoin.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Ingredient.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.User.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Contient.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Ouverture.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Reservation.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Commande.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Picture.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.Label.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HorecaContract.LabelJoinHoreca.TABLE_NAME);
	}

}
