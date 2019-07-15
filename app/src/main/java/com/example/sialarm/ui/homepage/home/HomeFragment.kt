package com.example.sialarm.ui.homepage.home

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sialarm.R
import kotlinx.android.synthetic.main.fragment_alert.*
import androidx.databinding.adapters.SeekBarBindingAdapter.setProgress
import kotlinx.android.synthetic.main.activity_pre_setup.*


class HomeFragment:Fragment() {

    var up:Long = 0
    var down : Long = 0
    var handler:Handler = Handler()
    var runnable:Runnable?=null
    var updateProgress = 0
    var counDownTimer : CountDownTimer?=null
    var time:Long  = 1500

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
        }
            btnUrgent.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(counDownTimer==null){
                            progress.visibility = View.VISIBLE
                            counDownTimer =object: CountDownTimer(time,10){
                                override fun onFinish() {
                                    counDownTimer=null
                                    btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
                                    progress.visibility = View.GONE
                                }

                                override fun onTick(millisUntilFinished: Long) {
                                    val finishedSeconds = time - millisUntilFinished
                                    val total = (finishedSeconds.toFloat() / time.toFloat()  * 100.0).toInt()
                                    progress.progress = total
                                    Log.d("time",total.toString())
                                }
                            }.start()
                        }
                        btnUrgent.setBackgroundResource(R.drawable.button_pressed)
                        progress.visibility = View.VISIBLE
                        down = System.currentTimeMillis()

                    }
                    MotionEvent.ACTION_UP -> {
                        progress.visibility = View.GONE
                        btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
                        progress.progress = 0
                        counDownTimer?.cancel()
                        counDownTimer = null
                        progress.visibility = View.GONE
                        return@OnTouchListener true
                    }
                }
                false
            })
    }
}