package com.example.notes.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDbManager(context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase // открытие дб
    }

    suspend fun insertToDb(title: String, content: String, uri: String, time: String) = withContext(Dispatchers.IO){ // записать в бд
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI, uri)
            put(MyDbNameClass.COLUMN_NAME_TIME, time)
        }

        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

    suspend fun updateItem(title: String, content: String, uri: String, id: Int, time: String) = withContext(Dispatchers.IO){
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI,uri)
            put(MyDbNameClass.COLUMN_NAME_TIME,time)
        }

        val selection = BaseColumns._ID + "=$id"
        db?.update(MyDbNameClass.TABLE_NAME, values, selection, null)
    }

    suspend fun readDbData(searchText : String): ArrayList<ListItem> = withContext(Dispatchers.IO){ // считывание с бд
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDbNameClass.COLUMN_NAME_TITLE} like ?" //запрос в бд
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, selection, arrayOf("%$searchText%"), //% - поиск по символу а не по целому слову
            null, null, null)

        with(cursor){
            while(cursor?.moveToNext()!!){
                val dataTitle = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_TITLE))
                val dataContent = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_CONTENT))
                val dataUri = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_IMAGE_URI))
                val dataId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val dataTime = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_TIME))
                val item = ListItem()
                item.title = dataTitle
                item.desc = dataContent
                item.uri = dataUri
                item.id = dataId
                item.time = dataTime
                dataList.add(item)
            }
        }
        cursor?.close()

        return@withContext dataList
    }
    fun closeDb(){
        myDbHelper.close()
    }
    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }
}