package com.example.second

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

class MyAdapter(private val myDataset: ArrayList<NotiData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false) as TextView
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // for reverse list
        holder.textView.text = myDataset[myDataset.size - position - 1].toString()
    }

    override fun getItemCount() = myDataset.size
}