package com.example.sialarm.ui.device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.sialarm.R
import com.example.sialarm.utils.extensions.Device
import java.util.ArrayList

class DeviceListAdapter(
    private val context: Context,
    private var list: MutableList<Device> = mutableListOf(),
    private val onListItemClickListener: OnListItemClickListener
    ) :
    BaseAdapter(), Filterable {
        private val TAG = "DeviceListAdapter"
        private var originalList: MutableList<Device>? = null
        private var filteredList: MutableList<Device>? = null
        private var mFilter = ItemFilter()

        init {
            this.originalList = list
            this.filteredList = list
        }

        override fun getCount(): Int {
            return filteredList!!.size
        }

        override fun getItem(position: Int): Any {
            return filteredList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        internal inner class ViewHolder(view: View) {
            var views: View? = null
            var tvDeviceName: TextView? = null
            var tvDevicePlace: TextView ? = null

            init {
                views = view.findViewById(R.id.list_item_device)
                tvDeviceName = view.findViewById(R.id.tv_device_name)
                tvDevicePlace = view.findViewById(R.id.place)
            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val holder: ViewHolder
            var convertViews = convertView
            if (convertViews == null) {
                convertViews = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
                holder = ViewHolder(convertViews!!)
                convertViews.tag = holder
            } else {
                holder = convertView!!.tag as ViewHolder
            }
            val device = filteredList!![position]
            holder.tvDeviceName!!.setText(device.name)
            holder.tvDevicePlace!!.setText(device.district+", "+device.ward)
            // load image

            holder.views!!.setOnClickListener {

                onListItemClickListener.onItemClick(device)
            }
            return convertViews
        }

        override fun getFilter(): Filter {
            return mFilter
        }




        private inner class ItemFilter : Filter() {
            override fun performFiltering(constraint: CharSequence): Filter.FilterResults {

                val filterString = constraint.toString().toLowerCase()
                val results = Filter.FilterResults()
                val list = originalList
                val count = list!!.size
                val nlist = ArrayList<Device>(count)
                var filterableString: String

                for (i in 0 until count) {
                    filterableString = list[i].name
                    if (filterableString.toLowerCase().contains(filterString)) {
                        val device = list[i]
                        nlist.add(device)
                    }
                }
                results.values = nlist
                results.count = nlist.size
                return results
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                filteredList = results.values as ArrayList<Device>
                notifyDataSetChanged()
            }
        }

        interface OnListItemClickListener {
            fun onItemClick(device: Device)
        }
    }
