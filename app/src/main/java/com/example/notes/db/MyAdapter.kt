package com.example.notes.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notes.R

class MyAdapter (listMain : ArrayList<String>): RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var listArray = listMain
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        fun setData(title:String){
            tvTitle.text = title
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder {
        val inflater = LayoutInflater.from(p0.context)
        return MyHolder(inflater.inflate(R.layout.rc_item, p0, false))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(p0: MyHolder, p1: Int) {
        p0.setData(listArray.get(p1))
    }
    fun updateAdapter (listItems : List<String>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

}