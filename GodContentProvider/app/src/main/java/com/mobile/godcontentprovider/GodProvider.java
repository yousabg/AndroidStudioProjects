package com.mobile.godcontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


public class GodProvider extends ContentProvider {

    public final static String DBNAME = "NameDatabase";

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MAIN);
        }
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        }
    };

    public final static String TABLE_GODSTABLE = "God";
    public final static String COLUMN_GOD_NAME = "godname";
    public final static String COLUMN_ABILITYONE = "ablityone";
    public final static String COLUMN_ABILITYTWO = "ablitytwo";
    public final static String COLUMN_ABILITYTHREE = "ablitythree";
    public final static String COLUMN_ABILITYFOUR = "ablityfour";
    public final static String COLUMN_ULTIMATE = "ultimate";
    public final static String BASE_DMG = "basedmg";
    public final static String SPECIAL_DMG = "specialdmg";


    public static final String AUTHORITY = "com.mobile.godcontentprovider.provider";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY +"/" + TABLE_GODSTABLE);

    private static UriMatcher sUriMatcher;

    private MainDatabaseHelper dbHelper;

    private static final String SQL_CREATE_MAIN = "CREATE TABLE " +
            TABLE_GODSTABLE +  // Table's name
            "(" +               // The columns in the table
            " _ID INTEGER PRIMARY KEY, " +
            COLUMN_GOD_NAME +
            " TEXT," +
            COLUMN_ABILITYONE +
            " TEXT," +
            COLUMN_ABILITYTWO +
            " TEXT," +
            COLUMN_ABILITYTHREE +
            " TEXT," +
            COLUMN_ABILITYFOUR +
            " TEXT," +
            COLUMN_ULTIMATE +
            " TEXT," +
            BASE_DMG +
            " INTEGER, " +
            SPECIAL_DMG +
            " INTEGER)";




    public GodProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return dbHelper.getWritableDatabase().delete(TABLE_GODSTABLE, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String gname = values.getAsString(COLUMN_GOD_NAME).trim();
        String a1 = values.getAsString(COLUMN_ABILITYONE).trim();
        String a2 = values.getAsString(COLUMN_ABILITYTWO).trim();
        String a3 = values.getAsString(COLUMN_ABILITYTHREE).trim();
        String a4 = values.getAsString(COLUMN_ABILITYFOUR).trim();
        String ul = values.getAsString(COLUMN_ULTIMATE).trim();
        String baseDmg = values.getAsString(BASE_DMG).trim();
        String specialDmg = values.getAsString(SPECIAL_DMG).trim();


        if (gname.equals(""))
            return null;

        if (a1.equals(""))
            return null;

        if (a2.equals(""))
            return null;

        if (a3.equals(""))
            return null;

        if (a4.equals(""))
            return null;

        if (ul.equals(""))
            return null;

        if (baseDmg.equals(""))
            return null;

        if (specialDmg.equals(""))
            return null;

        long id = dbHelper.getWritableDatabase().insert(TABLE_GODSTABLE, null, values);

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return dbHelper.getReadableDatabase().query(TABLE_GODSTABLE, projection, selection, selectionArgs,
                null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return dbHelper.getWritableDatabase().update(TABLE_GODSTABLE, values, selection, selectionArgs);
    }
}