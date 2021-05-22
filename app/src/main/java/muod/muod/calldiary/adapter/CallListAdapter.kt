package com.muod.calldiary.adapter

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.call_list_item.view.*
import kotlinx.android.synthetic.main.task_item.view.*
import com.muod.calldiary.model.CallItem
import com.muod.calldiary.R.layout.call_list_item
import com.muod.calldiary.R.layout.task_item
import com.muod.calldiary.callback.DoneCallback
import com.muod.calldiary.model.CallObject
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG



class CallListAdapter(callBack:DoneCallback,items:ArrayList<CallObject>): RecyclerView.Adapter<CallListViewHolder>() {

    var items = items
    var callback = callBack
    var listenToCheck = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallListViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(task_item,parent,false)
        return CallListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: CallListViewHolder, position: Int) {
        var item=items[position]
        holder.title.text = item.title

        if(item.notes.isEmpty()){
            holder.notes.visibility = View.GONE
        }
        else{
            holder.notes.visibility = View.VISIBLE
            holder.notes.text = item.notes
        }

        holder.caller.text = callback.getContactName(item.number)+ "("+ item.number + ")" + "    " + item.date

        listenToCheck = false
        holder.checkBox.isChecked = item.isDone
        listenToCheck = true

        holder.checkBox.setOnCheckedChangeListener { view, isChecked ->
            if(listenToCheck) {
                if (isChecked) {
                    callback.doneItem(items[position])
                } else {
                    callback.unDoneItem(items[position])
                }
            }
        }

        holder.layout.setOnClickListener {
            callback.editItem(items[position].time)
        }

        if(item.isDone)
        holder.title.setPaintFlags(holder.title.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
       //
    }

}



class CallListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var layout = itemView.taskLayout
    val caller = itemView.callerTextView
    val title = itemView.titleTextView
    val notes = itemView.contentTextView
    val checkBox = itemView.checkBox
}
