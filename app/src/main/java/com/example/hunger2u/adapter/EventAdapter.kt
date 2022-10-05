package com.example.hunger2u.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hunger2u.R
import com.example.hunger2u.data.Event

class EventAdapter(private val eventL: ArrayList<Event>): RecyclerView.Adapter<EventAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_event, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int){
        val event: Event = eventL[position]
        holder.itemName.text = event.eventName + "(" + event.eventStartDate + " - " + event.eventEndDate + ")"
        holder.itemDesc.text = event.eventDesc
        holder.itemTarget.text = "RM" + event.eventTarget.toString()
    }

    override fun getItemCount(): Int {
        return eventL.size
    }

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemName: TextView =  itemView.findViewById(R.id.tvCEName)
        var itemDesc: TextView = itemView.findViewById(R.id.tvCEDesc)
        var itemTarget: TextView = itemView.findViewById(R.id.tvCETarget)
    }

}