package com.example.aman_negi.projectsql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    Context context;

    //schema
    public static final String DATABASE_NAME = "Member";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Users";
    //columns
    public static final String UID = "_id";
    public static final String IMAGE = "image";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    //query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IMAGE + " BLOB, " + NAME + " TEXT, " + USERNAME + " TEXT, " + PASSWORD + " TEXT, " + EMAIL + " TEXT, " + PHONE + " TEXT )";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
