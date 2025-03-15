package com.example.notes.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class MyDbManager(context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase // открытие дб
    }

    fun insertToDb(title: String, content: String){ // записать в дб
        val values = ContentValues().apply {
            put(MyDbNameClass.COUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

    fun readDbData(): ArrayList<String>{ // считывание с дб
        val dataList = ArrayList<String>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, null, null,
            null, null, null)

        with(cursor){
            while(cursor?.moveToNext()!!){
                val dataText = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COUMN_NAME_TITLE))
                dataList.add(dataText.toString())
            }
        }
        cursor?.close()

        return dataList
    }
    fun closeDb(){
        myDbHelper.close()
    }
}