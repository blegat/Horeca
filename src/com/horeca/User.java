package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class User {
	private static User current_user = null;
	public static boolean isSignedIn() {
		return current_user != null;
	}
	public static User getCurrentUser() {
		return current_user;
	}
	public static boolean signIn(SQLiteDatabase db, String email, String password) {
		current_user = new User(email, db);
		if (current_user.passwordEquals(password)) {
			return true;
		} else {
			current_user = null;
			return false;
		}
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.User.TABLE_NAME,
				HorecaContract.User.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	
	
	private long id;
	private String email;
	private String name;
	private String password; // TODO secure it
	public User (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db, HorecaContract.User._ID + " = ?", new String[]{String.valueOf(id)});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.User._ID_INDEX);
		this.email = cursor.getString(HorecaContract.User.EMAIL_INDEX);
		this.name = cursor.getString(HorecaContract.User.NAME_INDEX);
		this.password = cursor.getString(HorecaContract.User.PASSWORD_INDEX);
		cursor.close();
	}
	private User (String email, SQLiteDatabase db) {
		Cursor cursor = getCursor(db, HorecaContract.User.EMAIL + " = ?", new String[]{email});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.User._ID_INDEX);
		this.email = cursor.getString(HorecaContract.User.EMAIL_INDEX);
		this.name = cursor.getString(HorecaContract.User.NAME_INDEX);
		this.password = cursor.getString(HorecaContract.User.PASSWORD_INDEX);
		cursor.close();
	}
	
	public long getId () {
		return id;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	private boolean passwordEquals(String password) {
		// TODO secure it
		return this.password.equals(password);
	}
}