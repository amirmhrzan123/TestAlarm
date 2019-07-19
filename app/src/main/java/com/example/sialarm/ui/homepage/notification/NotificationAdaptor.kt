package com.example.sialarm.ui.homepage.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sialarm.BR.model
import com.example.sialarm.databinding.ItemNotificationBinding


class NotificationAdaptor constructor(private val listNotification:List<NotificationResponseModel> = mutableListOf())
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         return Viewholder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = listNotification.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Viewholder).onBind(position)
    }

    inner class Viewholder(private val itemNotificationBinding:ItemNotificationBinding):RecyclerView.ViewHolder(itemNotificationBinding.root){
        fun onBind(position:Int){
            with(itemNotificationBinding){
                model= listNotification[position]
            }
        }
    }
}
