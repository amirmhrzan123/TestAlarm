package com.example.sialarm.utils.customViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sialarm.R
import com.example.sialarm.ui.DeviceList

class CustomSpinnerAdapter(private val listOptions: ArrayList<DeviceList>, private val selectedId: Int, private val listener: onCustomSpinnerItemSelected) : RecyclerView.Adapter<CustomSpinnerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_spinner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listOptions.size

    override fun onBindViewHolder(holder: CustomSpinnerAdapter.ViewHolder, position: Int) {
        holder.textView.text = listOptions[position].name

        if (position == listOptions.size - 1)
            holder.view.visibility = View.GONE

        holder.itemView.setOnClickListener {
            listener.onItemSelected(listOptions[position].id.toInt(), position)
        }

        if (listOptions[position].id.toInt() == selectedId) {
            holder.ivSelected.visibility = View.VISIBLE
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById<TextView>(R.id.tvTitle)!!
        var view = itemView.findViewById<View>(R.id.view)!!
        var ivSelected = itemView.findViewById<ImageView>(R.id.ivSelected)!!
//            val tvTitle = itemView.find
    }

    interface onCustomSpinnerItemSelected {
        fun onItemSelected(selectedId: Int, position: Int)
    }
}