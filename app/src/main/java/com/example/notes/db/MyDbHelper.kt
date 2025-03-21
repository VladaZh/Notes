package com.example.notes.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, MyDbNameClass.DATABASE_NAME,
    null, MyDbNameClass.DATABASE_VERSION){ // создание дб
    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(MyDbNameClass.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        db?.execSQL(MyDbNameClass.SQL_DELETE_TABLE)
        onCreate(db)
    }

}