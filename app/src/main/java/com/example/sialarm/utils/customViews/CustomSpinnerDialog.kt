package com.example.sialarm.utils.customViews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sialarm.R
import com.example.sialarm.ui.DeviceList


class CustomSpinnerDialog : DialogFragment() {

    var listOptions: ArrayList<DeviceList> = arrayListOf()


    private lateinit var listener: CustomSpinnerAdapter.onCustomSpinnerItemSelected
    private var selectedId: Int = -1

    fun setListener(listener: CustomSpinnerAdapter.onCustomSpinnerItemSelected) {
        this.listener = listener
    }

    var titleText = ""

    companion object {
        const val OPTIONS = "options"
        const val PROPERTIES = "property"
        const val SELECTED_ID = "selected_id"
        const val TITLE = "title"
        fun getInstance(title: String = "", listOptions: ArrayList<DeviceList>, selectedId: Int?): CustomSpinnerDialog {
            val dialog = CustomSpinnerDialog()
            val arguments = Bundle()
            arguments.putSerializable(OPTIONS, listOptions)
            if (selectedId == null)
                arguments.putInt(SELECTED_ID, -1)
            else
                arguments.putInt(SELECTED_ID, selectedId)
            arguments.putString(TITLE, title)
            dialog.arguments = arguments
            return dialog
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog)
        listOptions = arguments?.getSerializable(OPTIONS) as ArrayList<DeviceList>
        selectedId = arguments!!.getInt(SELECTED_ID, -1)
        titleText = arguments!!.getString(TITLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_dialog_spinner, container)
        val rvCustomSpinner = view.findViewById<RecyclerView>(R.id.rvCustomSpinner)
        val pageTitle = view.findViewById<TextView>(R.id.pageTitle)
        pageTitle.text = titleText

        with(rvCustomSpinner) {
            layoutManager = LinearLayoutManager(activity!!) as RecyclerView.LayoutManager
            adapter = CustomSpinnerAdapter(
                listOptions,
                selectedId,
                object : CustomSpinnerAdapter.onCustomSpinnerItemSelected {
                    override fun onItemSelected(selectedId: Int, position: Int) {
                        listener.onItemSelected(selectedId, position)
                        dismiss()
                    }

                })
        }
        return view

    }

    override fun onStart() {
        super.onStart()
        //dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }
}