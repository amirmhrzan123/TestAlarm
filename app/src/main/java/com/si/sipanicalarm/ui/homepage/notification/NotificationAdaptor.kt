package com.si.sipanicalarm.ui.homepage.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.si.sipanicalarm.BR.model
import com.si.sipanicalarm.databinding.ItemNotificationBinding


class NotificationAdaptor constructor(private val listNotification:MutableList<NotificationResponseModel> = mutableListOf(),
                                      private val click:(NotificationResponseModel)->Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         return Viewholder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false),click)
    }

    override fun getItemCount(): Int = listNotification.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Viewholder).onBind(position)
    }

    inner class Viewholder(private val itemNotificationBinding:ItemNotificationBinding,
    private val click:(NotificationResponseModel)->Unit):RecyclerView.ViewHolder(itemNotificationBinding.root){
        fun onBind(position:Int){
            with(itemNotificationBinding){
                model= listNotification[position]
                root.setOnClickListener {
                    click(listNotification[position])
                }
            }

        }
    }

    fun setNotificationList(list:List<NotificationResponseModel>){
        listNotification.clear()
        listNotification.addAll(list)
        notifyDataSetChanged()
    }
}
