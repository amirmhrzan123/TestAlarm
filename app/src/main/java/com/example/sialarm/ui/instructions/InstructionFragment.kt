package com.example.sialarm.ui.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.sialarm.R
import com.example.sialarm.utils.extensions.loadDrawable
import com.example.sialarm.utils.extensions.loadImage


class InstructionFragment :DialogFragment(){

    private val tabPosition : Int by lazy{
        arguments!!.getInt(TAG)
    }


    companion object{
        val TAG = InstructionFragment::class.java!!.getSimpleName()


        fun newInstance(quoteRequestMode: Int): InstructionFragment {
            val fragment = InstructionFragment()
            val args = Bundle()
            args.putInt(TAG, quoteRequestMode)
            fragment.setArguments(args)
            return fragment
        }

     }



    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_slide_left_right, container, false)

        var imageview = v.findViewById<ImageView>(R.id.animation)
        var title = v.findViewById<TextView>(R.id.tvTitle)
        var descriptoin = v.findViewById<TextView>(R.id.tvDescription)
        if(tabPosition==0){
            title.setText("ALARM")
            imageview.loadDrawable(R.drawable.press)
        }else{
            title.setText("FRIENDS")
            imageview.loadDrawable(R.drawable.ic_swipe)
        }
        v.findViewById<Button>(R.id.button).setOnClickListener {
            dismiss()
        }
        return v    }
}