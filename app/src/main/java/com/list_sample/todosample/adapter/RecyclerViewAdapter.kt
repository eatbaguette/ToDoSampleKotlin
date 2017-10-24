package com.list_sample.todosample.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.list_sample.todosample.R
import com.list_sample.todosample.model.TodoModel
import io.realm.RealmResults

/**
 * Created by monkey on 2017/10/24.
 */

class RecyclerViewAdapter(private val dateList: RealmResults<TodoModel>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private lateinit var mitemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mitemClickListener = itemClickListener
    }

    // ViewHolder
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        var todoText: TextView = view.findViewById<TextView>(R.id.text_view_recycelr_view_cell)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mitemClickListener.onItemClick(itemView, adapterPosition)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recycler_view_cell, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = dateList[position]
        holder?.todoText?.text = item.todo
    }

    override fun getItemCount(): Int {
        return dateList.size
    }
}
