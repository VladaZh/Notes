package com.example.notes.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class MyDbManager(context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase // открытие дб
    }

    fun insertToDb(title: String, content: String, uri: String){ // записать в дб
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI,uri)
        }

        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

    fun readDbData(): ArrayList<ListItem>{ // считывание с дб
        val dataList = ArrayList<ListItem>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, null, null,
            null, null, null)

        with(cursor){
            while(cursor?.moveToNext()!!){
                val dataTitle = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_TITLE))
                val dataContent = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_CONTENT))
                val dataUri = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_IMAGE_URI))
                val dataId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val item = ListItem()
                item.title = dataTitle
                item.desc = dataContent
                item.uri = dataUri
                item.id = dataId
                dataList.add(item)
            }
        }
        cursor?.close()

        return dataList
    }
    fun closeDb(){
        myDbHelper.close()
    }
    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }
}