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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.db.MyAdapter
import com.example.notes.db.MyDbManager
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    val myDbManager = MyDbManager(context = this)
    val myAdapter = MyAdapter(ArrayList())
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()


    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    fun onClickNew(view: View){
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    fun init(){
        binding.rcView.layoutManager = LinearLayoutManager(this)// элементы по вертикали
        binding.rcView.adapter = myAdapter
    }
    fun fillAdapter(){
        myAdapter.updateAdapter(myDbManager.readDbData())
    }

}