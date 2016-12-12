package nf.co.xine.budgetmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "settings";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ACCOUNTS ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "TYPE TEXT, "
                + "VALUE TEXT, "
                + "CURRENCY TEXT)");
        db.execSQL("CREATE TABLE TRANSACTIONS ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ACCOUNT_ID INTEGER, "
                + "NAME TEXT, "
                + "TYPE INTEGER, "
                + "CATEGORY_ID INTEGER, "
                + "DATE TEXT, "
                + "VALUE TEXT, "
                + "FOREIGN KEY(ACCOUNT_ID) REFERENCES ACCOUNTS(_id), "
                + "FOREIGN KEY(CATEGORY_ID) REFERENCES CATEGORIES(_id))");
        db.execSQL("create table CATEGORIES ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT)");
        insertCategory(db, "Zalupa");
        insertCategory(db, "Profit");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static void insertAccount(SQLiteDatabase db, String name, String type, String value, String currency) {
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("TYPE", type);
        values.put("VALUE", value);
        values.put("CURRENCY", currency);
        db.insertWithOnConflict("ACCOUNTS", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    static void insertTransaction(SQLiteDatabase db, int accountId, int categoryId, String name, String date, String value) {
        ContentValues values = new ContentValues();
        values.put("ACCOUNT_ID", accountId);
        values.put("CATEGORY_ID", categoryId);
        values.put("NAME", name);
        values.put("DATE", date);
        values.put("VALUE", value);
        db.insertWithOnConflict("TRANSACTIONS", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    static void insertCategory(SQLiteDatabase db, String name) {
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        db.insertWithOnConflict("CATEGORIES", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
