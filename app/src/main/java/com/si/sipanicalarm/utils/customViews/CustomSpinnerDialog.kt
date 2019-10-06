package com.si.sipanicalarm.utils.customViews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.si.sipanicalarm.R
import com.si.sipanicalarm.ui.DeviceList


class CustomSpinnerDialog : DialogFragment() {

    var listOptions: ArrayList<Int> = arrayListOf()

    var listStates : ArrayList<String> = arrayListOf()

    private lateinit var listener: CustomSpinnerWardAdapter.onCustomSpinnerItemSelected
    private var selectedId: Int = -1
    var from: Int = 0

    enum class FOR{
        WARD,STATE
    }

    fun setListener(listener: CustomSpinnerWardAdapter.onCustomSpinnerItemSelected) {
        this.listener = listener
    }

    var titleText = ""

    companion object {
        const val OPTIONS = "options"
        const val PROPERTIES = "property"
        const val SELECTED_ID = "selected_id"
        const val FOR = "for"
        const val WARD = 1
        const val STATE = 2
        const val TITLE = "title"
        fun getInstance(title: String = "", listOptions: ArrayList<String>, selectedId: Int?): CustomSpinnerDialog {
            val dialog = CustomSpinnerDialog()
            val arguments = Bundle()
            arguments.putSerializable(OPTIONS, listOptions)
            arguments.putInt(FOR, STATE)
            if (selectedId == null)
                arguments.putInt(SELECTED_ID, -1)
            else
                arguments.putInt(SELECTED_ID, selectedId)
            arguments.putString(TITLE, title)
            dialog.arguments = arguments
            return dialog
        }

        fun getInstanceForWard(title: String = "", listOptions: ArrayList<Int>, selectedId: Int?): CustomSpinnerDialog {
            val dialog = CustomSpinnerDialog()
            val arguments = Bundle()
            arguments.putSerializable(OPTIONS, listOptions)
            arguments.putInt(FOR, WARD)
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

        from = arguments!!.getInt(FOR)
        if(from == WARD){
            listOptions = arguments?.getSerializable(OPTIONS) as ArrayList<Int>
        }else{
            listStates = arguments?.getSerializable(OPTIONS) as ArrayList<String>
        }
        selectedId = arguments!!.getInt(SELECTED_ID, -1)
        titleText = arguments!!.getString(TITLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_dialog_spinner, container)
        val rvCustomSpinner = view.findViewById<RecyclerView>(R.id.rvCustomSpinner)
        val pageTitle = view.findViewById<TextView>(R.id.pageTitle)
        pageTitle.text = titleText

        if(from == WARD){
            with(rvCustomSpinner) {
                layoutManager = LinearLayoutManager(activity!!) as RecyclerView.LayoutManager
                adapter = CustomSpinnerWardAdapter(
                    listOptions,
                    selectedId,
                    object : CustomSpinnerWardAdapter.onCustomSpinnerItemSelected {
                        override fun onItemSelected(selectedId: Int, position: Int) {
                            listener.onItemSelected(selectedId, position)
                            dismiss()
                        }

                    })
            }

        }else{
            with(rvCustomSpinner) {
                layoutManager = LinearLayoutManager(activity!!) as RecyclerView.LayoutManager
                adapter = CustomSpinnerStateAdapter(
                    listStates,
                    selectedId,
                    object : CustomSpinnerStateAdapter.onCustomSpinnerItemSelected {
                        override fun onItemSelected(selectedId: Int, position: Int) {
                            listener.onItemSelected(selectedId, position)
                            dismiss()
                        }

                    })
            }

        }
        return view

    }

    override fun onStart() {
        super.onStart()
        //dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }
}