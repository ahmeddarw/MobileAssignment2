package com.example.assignment2ahmedjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    // giving the database and table  a name
    public static final String DATABASE_NAME = "LocationFinder";
    public static final String TABLE_NAME = "Locations";

    // initializing columns and their names in the table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LONG = "longitude";
    //constructor
    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating the location table with the id,address,latitude, and longitude
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_LONG + " REAL, " +
                COLUMN_LAT + " REAL)";
        db.execSQL(createTable);
    }

    //method that allows insertion of data into the table "locations"
    public long insert_data(String address, float longitude, float latitude) {
        if (address.isEmpty()) return -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //put values into the column required
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LONG, longitude);
        values.put(COLUMN_LAT, latitude);

        return db.insert(TABLE_NAME, null, values);
    }
    //method that allows data to be updated
    public boolean update_data(int id, String address, float longitude, float latitude) {
        //check if address is empty
        if (address.isEmpty()) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //put values into the column required
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LONG, longitude);
        values.put(COLUMN_LAT, latitude);
    //update row with matching id
        int result = db.update(
                TABLE_NAME, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        return result > 0;
    }
    // method that allows data to be deleted
    public boolean delete_data(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete row with matching id
        int delete = db.delete(TABLE_NAME,
                    COLUMN_ID +
                    " = ?", new String[]{id});
        return delete > 0;
    }
    // method to get data using address
    public Cursor getData(String address) {
        SQLiteDatabase db = this.getReadableDatabase();
        //query the database where the address matches and return result
        return db.query(
                TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_LONG, COLUMN_LAT},
                COLUMN_ADDRESS + " LIKE ?",
                new String[]{ address }, null, null, null
        );

    }
    //check if id exits
    public Cursor get_Id(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " +
                TABLE_NAME + " WHERE "
                + COLUMN_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(ID)});
    }
    @Override
    //method to check if the database needs to be upgraded
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
