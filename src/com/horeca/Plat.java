package com.horeca;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Plat {
	
	public static Cursor getAllPlatsInHoreca(SQLiteDatabase db, Horeca horeca) {
		return getCursor(db,
				HorecaContract.Plat.HORECA_ID + " = ?",
				new String[]{((Long) horeca.getId()).toString()});
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		String sort = null;
		if (User.isSignedIn()) {
			sort = "(SELECT COUNT(*) FROM " + HorecaContract.UserFavoritePlat.TABLE_NAME_Q 
				+ ", " + HorecaContract.Plat.TABLE_NAME_Q	
				+ " WHERE " + HorecaContract.UserFavoritePlat.PLAT_ID_Q + " = " 
				+ HorecaContract.Plat._ID_Q +" AND " + HorecaContract.UserFavoritePlat.USER_ID_Q 
				+ " = " + User.getCurrentUser().getId()  + ") DESC";
		}
		return db.query(HorecaContract.Plat.TABLE_NAME,
				HorecaContract.Plat.COLUMN_NAMES, selection, selectionArgs, null, null, sort, null);
	}
	private static Cursor getFavoriteCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.UserFavoritePlat.TABLE_NAME,
				HorecaContract.UserFavoritePlat.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	
	private long id;
	//private long horeca_id; // (1)
	private String name;
	private double price;
	private String description;
	private boolean hasStock;
	private long stock;
	private Horeca horeca;
	private Ingredient[] ingredients;
	//SQLiteDatabase db; // (1)
	private boolean isFavorite;
	
	public Plat (Cursor cursor) {
		this.id = cursor.getLong(HorecaContract.Plat._ID_INDEX);
		name = cursor.getString(HorecaContract.Plat.NAME_INDEX);
		price = ((double) cursor.getLong(HorecaContract.Plat.PRICE_INDEX)) / 100;
		description = cursor.getString(HorecaContract.Plat.DESCRIPTION_INDEX);
		hasStock = !cursor.isNull(HorecaContract.Plat.STOCK_INDEX);
		if (hasStock) {
			stock = cursor.getLong(HorecaContract.Plat.STOCK_INDEX);
		}
		// /!\ Missing ingredients and horeca
	}
	public Plat (long id, SQLiteDatabase db) {
		// this.db = db; // (1)
		Cursor cursor = getCursor(db,
				HorecaContract.Plat._ID + " == ?",
				new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.Plat._ID_INDEX);
		//horeca_id = cursor.getLong(HorecaContract.Plat.HORECA_ID_INDEX); // (1)
		horeca = new Horeca(cursor.getLong(HorecaContract.Plat.HORECA_ID_INDEX), db);
		// (1) Creation of horeca is done automatically here
		//     and not on getHoreca so it is only done if it is required
		//     because that way the db can be closed after this contructor
		//     A boolean could be passed to this constructor to tweak this
		//     behavior
		name = cursor.getString(HorecaContract.Plat.NAME_INDEX);
		price = ((double) cursor.getLong(HorecaContract.Plat.PRICE_INDEX)) / 100;
		description = cursor.getString(HorecaContract.Plat.DESCRIPTION_INDEX);
		hasStock = !cursor.isNull(HorecaContract.Plat.STOCK_INDEX);
		if (hasStock) {
			stock = cursor.getLong(HorecaContract.Plat.STOCK_INDEX);
		}
		cursor.close();
		cursor = db.query(HorecaContract.Contient.TABLE_NAME,
				new String[]{HorecaContract.Contient.INGREDIENT_ID},
				HorecaContract.Contient.PLAT_ID + " == ?",
				new String[]{((Long) this.id).toString()},
				null, null, null);
		ingredients = new Ingredient[cursor.getCount()];
		cursor.moveToFirst();
		for (int i = 0; i < ingredients.length; i++) {
			ingredients[i] = new Ingredient(cursor.getLong(0), db); // There is only 1 column we don't user COLUMN_NAMES
			cursor.moveToNext();
		}
		cursor.close();
		Cursor cursorF = getFavoriteCursor(db, HorecaContract.UserFavoritePlat.USER_ID + "= ? AND "+
				HorecaContract.UserFavoritePlat.PLAT_ID + "= ?",
				new String[]{String.valueOf(User.getCurrentUser().getId()),String.valueOf(this.getId())});
		cursorF.moveToFirst();
		isFavorite=(cursorF.getCount()==1);
		cursorF.close();
	}
	public void reloadStock(SQLiteDatabase db) {
		Cursor cursor = getCursor(db,
				HorecaContract.Plat._ID + " == ?",
				new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		hasStock = !cursor.isNull(HorecaContract.Plat.STOCK_INDEX);
		if (hasStock) {
			stock = cursor.getLong(HorecaContract.Plat.STOCK_INDEX);
		}
		cursor.close();
	}
	
	public long getId () {
		return id;
	}
	public Horeca getHoreca() {
		/*if (horeca == null) { // (1)
			// avoid querying the db twice
			horeca = new Horeca(horeca_id, db);
		}*/
		return horeca;
	}
	public String getName() {
		return name;
	}
	public double getPrice () {
		return price;
	}
	public boolean hasDescription() {
		return description != null;
	}
	public String getDescription() {
		return description;
	}
	public boolean hasStock() {
		return hasStock;
	}
	public long getStock() {
		return stock;
	}
	public void setStock(SQLiteDatabase db, long stock) {
		this.stock = stock;
	    ContentValues args = new ContentValues();
	    args.put(HorecaContract.Plat.STOCK, this.stock);
	    db.update(HorecaContract.Plat.TABLE_NAME, args, HorecaContract.Plat._ID + " = ?",
	    		new String[]{String.valueOf(this.id)});
	}
	public Ingredient[] getIngredients() {
		return ingredients;
	}
	public boolean isFavorite(){
		return isFavorite;
	}
	public void setFavorite(SQLiteDatabase db){
		ContentValues cv = new ContentValues();
		cv.put(HorecaContract.UserFavoritePlat.USER_ID, String.valueOf(User.getCurrentUser().getId()));
		cv.put(HorecaContract.UserFavoritePlat.PLAT_ID, String.valueOf(this.getId()));
		db.insert(HorecaContract.UserFavoriteHoreca.TABLE_NAME, null, cv);
		isFavorite=true;
	}
	public void removeFavorite(SQLiteDatabase db){
		String where = HorecaContract.UserFavoritePlat.USER_ID + " = ? AND " + HorecaContract.UserFavoritePlat.PLAT_ID + " = ?";
		String whereargs[] = {String.valueOf(User.getCurrentUser().getId()),String.valueOf(this.getId())};
		db.delete(HorecaContract.UserFavoriteHoreca.TABLE_NAME,where,whereargs);
		isFavorite=false;
	}
}