package com.example.sialarm.ui.tutorial

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.sialarm.R
import com.example.sialarm.utils.Status
import com.example.sialarm.utils.extensions.showConfirmationDialog
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.fragment_alert.btnStopTracking
import kotlinx.android.synthetic.main.fragment_alert.btnUrgent
import kotlinx.android.synthetic.main.fragment_alert.ll_alert
import kotlinx.android.synthetic.main.fragment_alert.progress
import kotlinx.android.synthetic.main.fragment_first_tutorial.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SecondStepFragment: Fragment() {

    lateinit var listener: FragmentListener

    var down : Long = 0
    var runnable:Runnable?=null
    var counDownTimer : CountDownTimer?=null
    var time:Long  = 1500
    var alertSend = false
    var buttonPressed = false

    private val secondViewModel : TutorialViewModel by sharedViewModel()



    companion object {
        val TAG = SecondStepFragment::class.java.simpleName

        fun newInstance():SecondStepFragment{
            return SecondStepFragment()
        }
    }

    override fun onAttach(context: Context) {
        if(context is FragmentListener){
            listener = context
        }
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_second_tutorial,container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnStopTracking.setOnClickListener {
            alertSend = false
            progress.progress = 0
            progress.visibility = View.INVISIBLE
            ll_alert.visibility = View.INVISIBLE
            btnUrgent.visibility = View.VISIBLE
        }

        runnable = Runnable{
            btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
        }

        next.setOnClickListener {
            listener.openThirdTutorial()
        }

        secondViewModel.sendAlertMessages.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    activity!!.showConfirmationDialog("","You have successfully sent test notification to your friends",ok = {
                        listener.openThirdTutorial()
                    })
                }
            }
        })

        btnUrgent.setOnTouchListener(View.OnTouchListener { view, motionEvent ->

            KotlinPermissions.with(activity!!)
                    .permissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS)
                    .onAccepted { permissions ->
                        when (motionEvent.action) {
                            MotionEvent.ACTION_DOWN -> {
                                if(counDownTimer==null){
                                    progress.visibility = View.VISIBLE
                                    counDownTimer =object: CountDownTimer(time,10){
                                        override fun onFinish() {
                                            counDownTimer=null
                                            btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
                                            btnUrgent.visibility = View.INVISIBLE
                                            ll_alert.visibility = View.VISIBLE
                                            alertSend = true
                                            progress.progress = 100
                                            buttonPressed = true
                                            secondViewModel.sendALertValid.value = true
                                            next.visibility = View.VISIBLE

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
                                btnUrgent.setBackgroundResource(R.drawable.button_unpressed)
                                counDownTimer?.cancel()
                                counDownTimer = null
                                if(!alertSend){
                                    progress.visibility = View.INVISIBLE
                                    progress.progress = 0
                                }
                            }
                        }
                    }
                    .onDenied {
                        //List of denied permissions
                    }
                    .onForeverDenied { permissions ->
                        //List of forever denied permissions
                    }
                    .ask()

            false
        })

    }
}