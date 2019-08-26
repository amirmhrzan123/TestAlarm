package com.example.sialarm.ui.tutorial

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sialarm.R

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
    }
}