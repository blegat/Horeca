package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Ingredient {
	private static int min (int a, int b) {
		if (a < b) {
			return a;
		} else {
			return b;
		}
	}
	private static int max (int a, int b) {
		return -min(-a, -b);
	}
	private static int dist (String a, String b, int lena, int lenb, int[][] tab) {
		if (-1 != tab[lena][lenb])
			return tab[lena][lenb];
		if (lena == 0)
			return lenb;
		if (lenb == 0)
			return lena;
		tab[lena][lenb] = min(
				min(dist(a, b, lena - 1, lenb - 1, tab) + (a.charAt(lena-1) == b.charAt(lenb-1) ? 0 : 1),
					dist(a, b, lena, lenb - 1, tab) + 1),
				dist(a, b, lena - 1, lenb, tab) + 1
				);
		return tab[lena][lenb];
	}
	private static int distance (String a, String b) {
		int[][] tab = new int[a.length()+1][b.length()+1];
		for (int i = 0; i <= a.length(); ++i)
			for (int j = 0; j <= b.length(); ++j)
				tab[i][j] = -1;
		return dist(a, b, a.length(), b.length(), tab);
	}
	private static int dist2 (String a, String b, int lena, int lenb, int curdiff) {
		if (curdiff < 0)
			return curdiff;
		if (lena == 0)
			return curdiff - lenb;
		if (lenb == 0)
			return curdiff - lena;
		return max(
				max(dist2(a, b, lena - 1, lenb - 1, curdiff - (a.charAt(lena-1) == b.charAt(lenb-1) ? 0 : 1)),
					dist2(a, b, lena, lenb - 1, curdiff - 1)),
				dist2(a, b, lena - 1, lenb, curdiff - 1)
				);
	}
	public static String getSuggestion (SQLiteDatabase db, String s, boolean dp) {
		String best = "";
		int bestDist = s.length();
		String scmp = s.toLowerCase();
		//int bestOcc = 0; // number of times the ingredient is used.
		Cursor all = getAllIngredients(db);
		all.moveToFirst();
		while (!all.isAfterLast()) {
			String cur = all.getString(HorecaContract.Ingredient.NAME_INDEX);
			String curcmp = cur.toLowerCase();
			int diff = (dp ? bestDist - distance(scmp, curcmp) :
				dist2(scmp, curcmp, s.length(), cur.length(), bestDist));
			if (0 < diff) {// || (0 == diff && dictio[i].getOccurences() > bestOcc)) {
				best = cur;
				bestDist = bestDist - diff;
				//bestOcc = dictio[i].getOccurences();
			}
			all.moveToNext();
		}
		return best;
	}
	
	private static Cursor getAllIngredients(SQLiteDatabase db) {
		return getCursor(db, null, null);
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.Ingredient.TABLE_NAME,
				HorecaContract.Ingredient.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	
	private long id;
	private String name;
	public Ingredient (String name, SQLiteDatabase db) {
		Log.i("ing", name);
		Cursor cursor = getCursor(db,
				HorecaContract.Ingredient.NAME + " == ?",
				new String[]{name});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.Ingredient._ID_INDEX);
		this.name = cursor.getString(HorecaContract.Ingredient.NAME_INDEX);
	}
	public Ingredient (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db,
				HorecaContract.Ingredient._ID + " == ?",
				new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.Ingredient._ID_INDEX);
		this.name = cursor.getString(HorecaContract.Ingredient.NAME_INDEX);
	}
	public long getId () {
		return id;
	}
	public String getName() {
		return name;
	}
}