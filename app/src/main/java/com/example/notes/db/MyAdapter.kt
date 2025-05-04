package com.example.notes.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.EditActivity
import com.example.notes.R

class MyAdapter (listMain : ArrayList<ListItem>, contextM: Context): RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var listArray = listMain
    var context = contextM

    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
        val context = contextV

        fun setData(item:ListItem){
            tvTitle.text = item.title
            tvTime.text = item.time
            itemView.setOnClickListener{
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(MyIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.I_DESC_KEY, item.desc)
                    putExtra(MyIntentConstants.I_URI_KEY, item.uri)
                    putExtra(MyIntentConstants.I_ID_KEY, item.id)
                }
                context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder {
        val inflater = LayoutInflater.from(p0.context)
        return MyHolder(inflater.inflate(R.layout.rc_item, p0, false), context)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(p0: MyHolder, p1: Int) {
        p0.setData(listArray.get(p1))
    }

    fun updateAdapter (listItems : List<ListItem>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem (pos: Int, dbManager: MyDbManager){

        dbManager.removeItemFromDb(listArray[pos].id.toString())
        listArray.removeAt(pos)
        notifyItemChanged(0, listArray.size)
        notifyItemRemoved(pos)
    }

}