package com.example.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.db.MyAdapter
import com.example.notes.db.MyDbManager
import com.example.notes.EditActivity
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    val myDbManager = MyDbManager(context = this)
    val myAdapter = MyAdapter(ArrayList(), this)
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initSearcView()
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
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.rcView)
        binding.rcView.adapter = myAdapter
    }
    fun fillAdapter(){

        val list = myDbManager.readDbData("")
        myAdapter.updateAdapter(list)
        if (list.size > 0){
            binding.tvNoElements.visibility = View.GONE
        } else {
            binding.tvNoElements.visibility = View.VISIBLE
        }
    }

    private fun initSearcView(){
        binding.Search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val list = myDbManager.readDbData(p0!!)
                myAdapter.updateAdapter(list)
                Log.d("SearchLog", "New Search : $p0")
                return true
            }
        })
    }

    private fun getSwapMg(): ItemTouchHelper{
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                p0: RecyclerView,
                p1: RecyclerView.ViewHolder,
                p2: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                myAdapter.removeItem(p0.adapterPosition, myDbManager)

            }
        })
    }

}