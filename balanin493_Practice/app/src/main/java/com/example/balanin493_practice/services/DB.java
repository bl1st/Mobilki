package com.example.balanin493_practice.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.balanin493_practice.models.UserInformation;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, login text not null unique, password text not null);";
        db.execSQL(sql);

    }

    public void RememberMe(String login, String password)
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "Insert into Users(login, password) VALUES('"+ login +"','"+password+"');";
        Cursor cur = db.rawQuery(sql,null);

    }


    public UserInformation GetRememberedUser()
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Users ORDER BY Id DESC LIMIT 1;";

        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst())
        {
            String login = cur.getString(1);
            String password = cur.getString(2);

            UserInformation uInf = new UserInformation(login, password);
            return uInf;
        }
        return null;

    }































    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
