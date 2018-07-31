package softfruit.solutions.calldiary.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.call_list_item.view.*
import softfruit.solutions.calldiary.model.CallItem
import softfruit.solutions.calldiary.R.layout.call_list_item
import softfruit.solutions.calldiary.model.CallObject

class CallListAdapter(items:ArrayList<CallObject>): RecyclerView.Adapter<CallListViewHolder>() {

    var items = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallListViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(call_list_item,parent,false)
        return CallListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: CallListViewHolder, position: Int) {
        var item=items[position]
        holder.callerName.text = item.number
        holder.notes.text = item.notes
        //

    }

}



class CallListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val callerName = itemView.callerNameTextView
    val time = itemView.timeTextView
    val duration = itemView.durationTextView
    val notes = itemView.notesTextView
}
