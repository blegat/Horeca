package com.horeca;

import android.provider.BaseColumns;

public class HorecaContract {
	// Prevents the FeedReaderContract class from being instantiated.
	private HorecaContract() {}
	/*
	 * The `*_INDEX` are there for optimization.
	 * If a query is done with COLOMN_NAMES as a selection,
	 * to avoid calling getColumnIndex call or hard-coding the index,
	 * the index in COLUMN_NAMES is in `*_INDEX`
	 */
	public static abstract class Ville implements BaseColumns {
		public static final String TABLE_NAME = "villes";
		public static final int _ID_INDEX = 0;
		public static final String NAME = "nom";
		public static final int NAME_INDEX = 1;
		public static final String CODEPOSTAL = "codepostal";
		public static final int CODEPOSTAL_INDEX = 2;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			NAME, CODEPOSTAL};
	}
	public static abstract class Horeca implements BaseColumns {
		public static final String TABLE_NAME = "horecas";
		public static final int _ID_INDEX = 0;
		public static final String NAME = "nom";
		public static final int NAME_INDEX = 1;
		public static final String VILLE_ID = "ville_id";
		public static final int VILLE_ID_INDEX = 2;
		public static final String NUMTEL = "numtel";
		public static final int NUMTEL_INDEX = 3;
		public static final String DESCRIPTION = "description";
		public static final int DESCRIPTION_INDEX = 4;
		public static final String[] COLUMN_NAMES = new String[]{_ID, NAME, VILLE_ID, NUMTEL, DESCRIPTION};
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
	public static abstract class Ingredient implements BaseColumns {
		public static final String TABLE_NAME = "ingredients";
		public static final int _ID_INDEX = 0;
		public static final String NAME = "nom";
		public static final int NAME_INDEX = 1;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			NAME};
	}
	public static abstract class User implements BaseColumns {
		public static final String TABLE_NAME = "users";
		public static final int _ID_INDEX = 0;
		public static final String EMAIL = "email";
		public static final int EMAIL_INDEX = 1;
		public static final String NAME = "nom";
		public static final int NAME_INDEX = 2;
		public static final String PASSWORD = "password";
		public static final int PASSWORD_INDEX = 3;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			EMAIL, NAME, PASSWORD};
	}
	public static abstract class Contient implements BaseColumns {
		public static final String TABLE_NAME = "contients";
		public static final int _ID_INDEX = 0;
		public static final String PLAT_ID = "plat_id";
		public static final int PLAT_ID_INDEX = 1;
		public static final String INGREDIENT_ID = "ingredient_id";
		public static final int INGREDIENT_ID_INDEX = 2;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			PLAT_ID, INGREDIENT_ID};
	}
	public static abstract class Ouverture implements BaseColumns {
		public static final String TABLE_NAME = "ouvertures";
		public static final int _ID_INDEX = 0;
		public static final String HORECA_ID = "horeca_id";
		public static final int HORECA_ID_INDEX = 1;
		public static final String DEBUT = "debut";
		public static final int DEBUT_INDEX = 2;
		public static final String FIN = "fin";
		public static final int FIN_INDEX = 3;
		public static final String PLACES = "places";
		public static final int PLACES_INDEX = 4;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			HORECA_ID, DEBUT, FIN, PLACES};
	}
	public static abstract class Reservation implements BaseColumns {
		public static final String TABLE_NAME = "reservations";
		public static final int _ID_INDEX = 0;
		public static final String USER_ID = "user_id";
		public static final int USER_ID_INDEX = 1;
		public static final String OUVERTURE_ID = "ouverture_id";
		public static final int OUVERTURE_ID_INDEX = 2;
		public static final String PLACES = "places";
		public static final int PLACES_INDEX = 3;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			USER_ID, OUVERTURE_ID, PLACES};
	}
	public static abstract class Commande implements BaseColumns {
		public static final String TABLE_NAME = "commandes";
		public static final int _ID_INDEX = 0;
		public static final String USER_ID = "user_id";
		public static final int USER_ID_INDEX = 1;
		public static final String PLAT_ID = "plat_id";
		public static final int PLAT_ID_INDEX = 2;
		public static final String TEMPS = "temps";
		public static final int TEMPS_INDEX = 3;
		public static final String NOMBRE = "nombres";
		public static final int NOMBRE_INDEX = 4;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			USER_ID, PLAT_ID, TEMPS, NOMBRE};
	}
	public static abstract class HorecaPictures implements BaseColumns {
		public static final String TABLE_NAME = "horecapictures"; 
		public static final int _ID_INDEX = 0;
		public static final String HORECA_ID = "horeca_id";
		public static final int HOREC_ID_INDEX = 1;
		public static final String PATH_PICTURE = "path_picture";
		public static final int PATH_PICTURE_INDEX = 2;
		public static final String NAME_PICTURE = "name_picture";
		public static final int NAME_PICTURE_INDEX = 3;
		public static final String[] COLUMN_NAMES = new String[]{_ID,
			HORECA_ID, PATH_PICTURE, NAME_PICTURE};
	}
}
