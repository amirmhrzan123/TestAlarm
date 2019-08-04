package com.example.sialarm.utils.customViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sialarm.R
import com.example.sialarm.ui.DeviceList

class CustomSpinnerDeviceAdapter(private val listOptions: ArrayList<DeviceList>, private val selectedId: Int, private val listener: onCustomSpinnerItemSelected) : RecyclerView.Adapter<CustomSpinnerDeviceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_spinner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listOptions.size

    override fun onBindViewHolder(holder: CustomSpinnerDeviceAdapter.ViewHolder, position: Int) {
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

class CustomSpinnerWardAdapter(private val listOptions: ArrayList<Int>, private val selectedId: Int, private val listener: onCustomSpinnerItemSelected) : RecyclerView.Adapter<CustomSpinnerWardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_spinner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listOptions.size

    override fun onBindViewHolder(holder: CustomSpinnerWardAdapter.ViewHolder, position: Int) {
        holder.textView.text = listOptions[position].toString()

        if (position == listOptions.size - 1)
            holder.view.visibility = View.GONE

        holder.itemView.setOnClickListener {
            listener.onItemSelected(listOptions[position], position)
        }

        if (listOptions[position] == selectedId) {
            holder.ivSelected.visibility = View.VISIBLE
        }else{
            holder.ivSelected.visibility = View.GONE
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


class CustomSpinnerStateAdapter(private val listOptions: ArrayList<String>, private val selectedId: Int, private val listener: onCustomSpinnerItemSelected) : RecyclerView.Adapter<CustomSpinnerStateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_spinner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listOptions.size

    override fun onBindViewHolder(holder: CustomSpinnerStateAdapter.ViewHolder, position: Int) {
        holder.textView.text = listOptions[position]
        if (position == listOptions.size - 1)
            holder.view.visibility = View.GONE

        holder.itemView.setOnClickListener {
            listener.onItemSelected(position, position)
        }

        if (position == selectedId) {
            holder.ivSelected.visibility = View.VISIBLE
        }else{
            holder.ivSelected.visibility = View.GONE
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