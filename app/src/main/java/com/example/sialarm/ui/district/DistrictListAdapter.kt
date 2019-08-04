package com.example.sialarm.ui.district

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.sialarm.R
import com.example.sialarm.utils.extensions.District
import java.util.*


class DistrictListAdapter(
    private val context: Context,
    list: MutableList<District>,
    private val onListItemClickListener: OnListItemClickListener
) :
    BaseAdapter(), Filterable {
    private val TAG = "DistrictListAdapter"
    private var originalList: MutableList<District>? = null
    private var filteredList: MutableList<District>? = null
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
        var tvCountryName: TextView? = null

        init {
            views = view.findViewById(R.id.list_item_country)
            tvCountryName = view.findViewById(R.id.tv_list_country_name)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var convertViews = convertView
        if (convertViews == null) {
            convertViews = LayoutInflater.from(parent.context).inflate(R.layout.item_district, parent, false)
            holder = ViewHolder(convertViews!!)
            convertViews.tag = holder
        } else {
            holder = convertView!!.tag as ViewHolder
        }
        val district = filteredList!![position]
        holder.tvCountryName!!.setText(district.name)
        // load image

        holder.views!!.setOnClickListener {

            onListItemClickListener.onItemClick(district)
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
            val nlist = ArrayList<District>(count)
            var filterableString: String

            for (i in 0 until count) {
                filterableString = list[i].name
                if (filterableString.toLowerCase().contains(filterString)) {
                    val district = list[i]
                    nlist.add(district)
                }
            }
            results.values = nlist
            results.count = nlist.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            filteredList = results.values as ArrayList<District>
            notifyDataSetChanged()
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(district: District)
    }
}
