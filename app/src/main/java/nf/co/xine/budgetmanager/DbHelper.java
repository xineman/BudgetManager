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
                + "CATEGORY_ID INTEGER, "
                + "DATE TEXT, "
                + "VALUE TEXT, "
                + "FOREIGN KEY(ACCOUNT_ID) REFERENCES ACCOUNTS(_id), "
                + "FOREIGN KEY(CATEGORY_ID) REFERENCES CATEGORIES(_id))");
        db.execSQL("create table CATEGORIES ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT)");
        db.execSQL("create table ACCOUNT_TYPES ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT)");
        db.execSQL("create table BUDGET_SETUPS ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CATEGORY_ID INTEGER, "
                + "VALUE TEXT, "
                + "PERIOD INTEGER, "
                + "FOREIGN KEY(CATEGORY_ID) REFERENCES CATEGORIES(_id))");
        insertCategory(db, "Other");
        insertCategory(db, "Car");
        insertCategory(db, "Clothing");
        insertCategory(db, "Eating out");
        insertCategory(db, "Education");
        insertCategory(db, "Entertainment");
        insertCategory(db, "Gifts");
        insertCategory(db, "Groceries");
        insertCategory(db, "Insurance");
        insertCategory(db, "Medical");
        insertCategory(db, "Transport");
        insertCategory(db, "Taxes");
        insertAccountType(db, "Credit Card");
        insertAccountType(db, "Debit Card");
        insertAccountType(db, "Deposit");
        insertAccountType(db, "Cash");
        insertAccountType(db, "Other");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static void insertAccount(SQLiteDatabase db, String name, int type, String value, String currency) {
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("TYPE", type);
        values.put("VALUE", value);
        values.put("CURRENCY", currency);
        db.insertWithOnConflict("ACCOUNTS", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private static void insertAccountType(SQLiteDatabase db, String name) {
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        db.insertWithOnConflict("ACCOUNT_TYPES", null, values, SQLiteDatabase.CONFLICT_REPLACE);
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

    static void insertBudget(SQLiteDatabase db, int categoryId, String value, int period) {
        ContentValues values = new ContentValues();
        values.put("CATEGORY_ID", categoryId);
        values.put("VALUE", value);
        values.put("PERIOD", period);
        db.insertWithOnConflict("BUDGET_SETUPS", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
