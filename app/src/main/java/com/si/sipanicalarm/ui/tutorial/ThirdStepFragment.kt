package com.si.sipanicalarm.ui.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.si.sipanicalarm.R
import com.si.sipanicalarm.ui.myProfile.MyProfileActivity
import kotlinx.android.synthetic.main.fragment_third_tutorial.*

class ThirdStepFragment: Fragment() {

    lateinit var listener: FragmentListener

    companion object {
        val TAG = ThirdStepFragment::class.java.simpleName

        fun newInstance():ThirdStepFragment{
            return ThirdStepFragment()
        }
    }

    override fun onAttach(context: Context) {
        if(context is FragmentListener){
            listener = context
        }
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_third_tutorial,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnDone.setOnClickListener {

            val intent = Intent(activity!!,MyProfileActivity::class.java)
            activity!!.startActivityForResult(intent,2)
        }
    }

}