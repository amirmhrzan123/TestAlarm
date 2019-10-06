package com.si.sipanicalarm.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.si.sipanicalarm.databinding.ItemHistoryBinding


class HistoryAdapter constructor(private val listHistory:MutableList<HistoryResponseModel> = mutableListOf())
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Viewholder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = listHistory.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Viewholder).onBind(position)
    }

    inner class Viewholder(private val itemHistoryBinding: ItemHistoryBinding): RecyclerView.ViewHolder(itemHistoryBinding.root){
        fun onBind(position:Int){
            with(itemHistoryBinding){
                model= listHistory[position]
            }
        }
    }

    fun setHistoryList(list:List<HistoryResponseModel>){
        listHistory.clear()
        listHistory.addAll(list)
        notifyDataSetChanged()
    }
}
