package com.horeca;

import android.provider.BaseColumns;

public class HorecaContract {
	// Prevents the FeedReaderContract class from being instantiated.
	private HorecaContract() {}
	/*
	 * The `*_INDEX` are there for optimization.
	 * If a query is done with COLOMN_NAMES as a selection,
	 * to avoid calling getColumnIndex call or hardcoding the index,
	 * the index in COLUMN_NAMES is in `*_INDEX`
	 */
	public static abstract class Horeca implements BaseColumns {
		public static final String TABLE_NAME = "horecas";
		public static final int _ID_INDEX = 0;
		public static final String NAME = "nom";
		public static final int NAME_INDEX = 1;
		public static final String[] COLUMN_NAMES = new String[]{_ID, NAME};
	}
	public static abstract class Plat implements BaseColumns {
		public static final String TABLE_NAME = "plats";
		public static final int _ID_INDEX = 0;
		public static final String HORECA_ID = "horeca_id";
		public static final int HORECA_ID_INDEX = 1;
		public static final String NAME = "nom";
		public static final int NAME_INDEX = 2;
		public static final String PRICE = "prix";
		public static final int PRICE_INDEX = 3;
		public static final String DESCRIPTION = "description";
		public static final int DESCRIPTION_INDEX = 4;
		public static final String STOCK = "stock";
		public static final int STOCK_INDEX = 5;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			HORECA_ID, NAME, PRICE,
			DESCRIPTION, STOCK};
	}
}
