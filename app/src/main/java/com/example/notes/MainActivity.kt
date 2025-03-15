package com.example.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notes.db.MyDbManager

class MainActivity : AppCompatActivity() {
    val myDbManager = MyDbManager(context = this)
    lateinit var edTitle: EditText
    lateinit var edContent: EditText
    lateinit var tvTest: TextView
//    val edTitle = findViewById<EditText>(edTitle)
//    val edContent = findViewById(R.id.edContent)
//    val tvTest = findViewById(R.id.tvTest)
    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        val dataList = myDbManager.readDbData()
        for (item in dataList){
            tvTest.append(item)
            tvTest.append("\n")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        edTitle = findViewById(R.id.edTitle)
        edContent = findViewById(R.id.edContent)
        tvTest = findViewById(R.id.tvTest)

    }

    fun onClickSave(view: View) {
        tvTest.text = ""
        myDbManager.insertToDb(edTitle.text.toString(), edContent.text.toString())
        val dataList = myDbManager.readDbData()
        for (item in dataList){
            tvTest.append(item)
            tvTest.append("\n")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}