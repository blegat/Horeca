package com.horeca;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class User {
	public static int SUCCESS = 0;
	public static int INVALID_EMAIL = 1;
	public static int INVALID_PASSWORD = 2;
	public static int PASSWORDS_DONT_MATCH = 3;
	private static User current_user = null;
	public static boolean isSignedIn() {
		return current_user != null;
	}
	public static User getCurrentUser() {
		return current_user;
	}
	public static int signUp(SQLiteDatabase db, String email, String name, String password, String passwordConfirmation, long ville_id, String address) {
		if (!password.equals(passwordConfirmation)) {
			return PASSWORDS_DONT_MATCH;
		}
		User user = new User(email, db);
		if (user.exists) {

			return INVALID_EMAIL;
		} else {
			user.name = name;
			user.setPassword(password);
			user.ville = new Ville(ville_id, db);
			user.address = address;
			user.save(db);
			current_user = user;
			return SUCCESS;
		}
	}
	public static int signIn(SQLiteDatabase db, String email, String password) {
		User user = new User(email, db);
		if (!user.exists) {
			return INVALID_EMAIL;
		}
		if (!user.passwordEquals(password)) {
			return INVALID_PASSWORD;
		}
		current_user = user;
		return SUCCESS;
	}
	public static void signOut() {
		current_user = null;
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.User.TABLE_NAME,
				HorecaContract.User.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}


	private long id;
	private boolean exists;
	private String email;
	private String name;
	private String password;
	private Ville ville;
	private String address;
	private String numtel;
	public User (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db, HorecaContract.User._ID + " = ?", new String[]{String.valueOf(id)});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.User._ID_INDEX);
		this.email = cursor.getString(HorecaContract.User.EMAIL_INDEX);
		this.name = cursor.getString(HorecaContract.User.NAME_INDEX);
		this.password = cursor.getString(HorecaContract.User.PASSWORD_INDEX);
		long ville_id = cursor.getLong(HorecaContract.User.VILLE_ID_INDEX);
		this.address = cursor.getString(HorecaContract.User.ADDRESS_INDEX);
		this.numtel = cursor.getString(HorecaContract.User.NUMTEL_INDEX);
		cursor.close();
		ville = new Ville(ville_id, db);
	}
	private User (String email, SQLiteDatabase db) {
		Cursor cursor = getCursor(db, HorecaContract.User.EMAIL + " = ?", new String[]{email});
		if (cursor.getCount() == 0) {
			exists = false;
			this.email = email;
		} else {
			exists = true;
			cursor.moveToFirst();
			this.id = cursor.getLong(HorecaContract.User._ID_INDEX);
			this.email = cursor.getString(HorecaContract.User.EMAIL_INDEX);
			this.name = cursor.getString(HorecaContract.User.NAME_INDEX);
			this.password = cursor.getString(HorecaContract.User.PASSWORD_INDEX);
			long ville_id = cursor.getLong(HorecaContract.User.VILLE_ID_INDEX);
			this.address = cursor.getString(HorecaContract.User.ADDRESS_INDEX);
			this.numtel = cursor.getString(HorecaContract.User.NUMTEL_INDEX);
			ville = new Ville(ville_id, db);
		}
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
	public Ville getVille() {
		return ville;
	}
	public String getAddress() {
		return address;
	}
	public boolean hasNumtel() {
		return numtel != null;
	}
	public String getNumtel() {
		return numtel;
	}
	public void setNumtel(SQLiteDatabase db, String numtel) {
		this.numtel = numtel;
	    ContentValues cv = new ContentValues();
	    cv.put(HorecaContract.User.NUMTEL, numtel);
	    db.update(HorecaContract.User.TABLE_NAME, cv, HorecaContract.User._ID + " = ?",
	    		new String[]{String.valueOf(this.id)});
	}
	private static String hashPassword(String password) {
		try {
			return AeSimpleSHA1.SHA1(password);
		} catch (NoSuchAlgorithmException e) {
			return password;
		} catch (UnsupportedEncodingException e) {
			return password;
		}
	}
	private void setPassword(String password) {
		this.password = hashPassword(password);
	}
	private boolean passwordEquals(String password) {
		Log.i("eq", this.password + "?" + password + "?");
		return this.password.equals(hashPassword(password));
	}
	private void save(SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(HorecaContract.User.EMAIL, email);
		cv.put(HorecaContract.User.NAME, name);
		cv.put(HorecaContract.User.PASSWORD, password);
		cv.put(HorecaContract.User.VILLE_ID, ville.getId());
		cv.put(HorecaContract.User.ADDRESS, address);
		this.id = db.insert(HorecaContract.User.TABLE_NAME, null, cv);
		this.exists = true;
	}
	public int updatePassword(SQLiteDatabase db, String currentPassword,
			String newPassword, String newPasswordConfirmation) {
		if (!passwordEquals(currentPassword)) {
			return INVALID_PASSWORD;
		} else if (!newPassword.equals(newPasswordConfirmation)) {
			return PASSWORDS_DONT_MATCH;
		} else {
		    ContentValues cv = new ContentValues();
		    cv.put(HorecaContract.User.PASSWORD, hashPassword(newPassword));
		    db.update(HorecaContract.User.TABLE_NAME, cv, HorecaContract.User._ID + " = ?",
		    		new String[]{String.valueOf(this.id)});
			return SUCCESS;
		}
	}
}
