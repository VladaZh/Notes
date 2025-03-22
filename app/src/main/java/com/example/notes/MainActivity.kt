package com.example.notes

import android.annotation.SuppressLint
import android.content.Intent
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

//    val edTitle = findViewById<EditText>(edTitle)
//    val edContent = findViewById(R.id.edContent)
//    val tvTest = findViewById(R.id.tvTest)
    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    fun onClickNew(view: View){
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}