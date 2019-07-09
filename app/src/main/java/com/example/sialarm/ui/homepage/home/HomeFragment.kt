package com.example.sialarm.ui.homepage.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sialarm.R
import kotlinx.android.synthetic.main.fragment_alert.*


class HomeFragment:Fragment() {

    var up:Long = 0
    var down : Long = 0
    var handler:Handler = Handler()
    var runnable:Runnable?=null
    var updateProgress = 0


    companion object {
        fun newInstance():HomeFragment{
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_alert,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runnable = Runnable{
            btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
            Toast.makeText(activity!!, "3 sec reached", Toast.LENGTH_SHORT).show()
        }
            btnUrgent.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        btnUrgent.setBackgroundResource(R.drawable.button_pressed)
                        progress.visibility = View.VISIBLE
                        tv_progress.visibility = View.VISIBLE
                        handler.postDelayed(runnable,3000)
                        Toast.makeText(activity!!, "Down", Toast.LENGTH_SHORT).show()
                        down = System.currentTimeMillis()

                    }
                    MotionEvent.ACTION_UP -> {
                        progress.visibility = View.GONE
                        tv_progress.visibility = View.GONE
                        btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
                        Toast.makeText(activity!!, "Up", Toast.LENGTH_SHORT).show()
                        up = System.currentTimeMillis()
                        handler.removeCallbacks(runnable)
                        return@OnTouchListener true
                    }
                }
                false
            })

    }
}