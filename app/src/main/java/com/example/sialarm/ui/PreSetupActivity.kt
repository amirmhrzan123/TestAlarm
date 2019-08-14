package com.example.sialarm.ui

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.sialarm.R
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.ui.homepage.HomeActivity
import com.example.sialarm.ui.landingScreen.LandingActivity
import com.example.sialarm.utils.extensions.loadDrawable
import com.example.sialarm.utils.extensions.loadImage
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.activity_pre_setup.*
import org.koin.android.ext.android.inject
import java.util.*


class PreSetupActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private var tvPrev: TextView? = null
    private var tvNext: TextView? = null
    private var tvDescTitle: TextView? = null
    private var tvDescBody: TextView? = null

    private var btnPermissions: Button? = null

    private var ibOnBoard: ImageButton? = null
    private var ibGroups: ImageButton? = null
    private var ibEvents: ImageButton? = null
    private var ibMessaging: ImageButton? = null
    private var ibPermissions: ImageButton? = null

    private var llDescription: ConstraintLayout? = null

    private var viewState: ViewState? = null

    private var clImages: ConstraintLayout? = null


    private var detector: GestureDetector? = null

    private val prefs:PrefsManager by inject()


    private val clickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.tv_previous -> {
                when (getViewState()) {
                    PreSetupActivity.ViewState.INTRODUCTION -> {
                    }
                    PreSetupActivity.ViewState.SIGNUP -> {
                        //Bottom layout
                        tvPrev!!.visibility = View.INVISIBLE
                        ibOnBoard!!.isSelected = true
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutRight)
                            .duration(ANIMATION_INTRODUCTION_DURATION_EXIT.toLong())
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.app_name)
                                tvDescBody!!.text = "First"
                                ivPicture.loadDrawable(R.drawable.signup)
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(ANIMATION_INTRODUCTION_DURATION_ENTER.toLong())
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        //Main layout
                        viewState = ViewState.INTRODUCTION


                    }
                    ViewState.ADDFRIENDS -> {
                        //degreeEnd = 0f
                        //animationSetPrev(degreeEnd)
                        tvPrev!!.visibility = View.VISIBLE
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = true
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutRight)
                            .duration(100)
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_register)
                                tvDescBody!!.text = "Test register"
                                ivPicture.visibility = View.VISIBLE
                                ivPicture.loadDrawable(R.drawable.phone)
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(300)
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        viewState = ViewState.SIGNUP
                    }
                    PreSetupActivity.ViewState.SENDALERT -> {
                        //degreeEnd = 120f
                        //animationSetPrev(degreeEnd)
                        tvPrev!!.visibility = View.VISIBLE
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = true
                        ibMessaging!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutRight)
                            .duration(100)
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_add_contacts)
                                tvDescBody!!.text = "Test Friends"
                                ivPicture.loadDrawable(R.drawable.add_friends)
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(300)
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        viewState = ViewState.ADDFRIENDS
                        tvNext!!.setText(R.string.tv_next)
                    }

                    ViewState.PERMiSSIONS -> {
                        //degreeEnd = 240f
                        //animationSetPrev(degreeEnd)
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = true
                        ibPermissions!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutRight)
                            .duration(ANIMATION_INTRODUCTION_DURATION_EXIT.toLong())
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_send_alert)
                                tvDescBody!!.text = "Test alert"
                                ivPicture.visibility = View.VISIBLE
                                ivPicture.loadDrawable(R.drawable.sent_alert)
                                btnPermissions!!.visibility = View.INVISIBLE
                                tvDescBody!!.visibility = View.VISIBLE

                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(ANIMATION_INTRODUCTION_DURATION_ENTER.toLong())
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)
                        viewState = ViewState.SENDALERT
                        tvNext!!.setText("Next")
                    }
                }//Not possible
            }
            R.id.tv_next -> {
                when (getViewState()) {
                    PreSetupActivity.ViewState.INTRODUCTION -> {
                        tvPrev!!.visibility = View.VISIBLE
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = true
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutLeft)
                            .duration(ANIMATION_INTRODUCTION_DURATION_EXIT.toLong())
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_register)
                                tvDescBody!!.text = "Test register"
                                ivPicture.visibility = View.VISIBLE
                                ivPicture.loadDrawable(R.drawable.phone)
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(ANIMATION_INTRODUCTION_DURATION_ENTER.toLong())
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)
                        viewState = ViewState.SIGNUP
                    }
                    ViewState.SIGNUP -> {
                        //degreeEnd = 120f
                        //animationSetNext(degreeEnd)
                        tvPrev!!.visibility = View.VISIBLE
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = true
                        ibMessaging!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutLeft)
                            .duration(100)
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_add_contacts)
                                tvDescBody!!.text = "Test contacts"
                                ivPicture.loadDrawable(R.drawable.add_friends)
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(300)
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        viewState = ViewState.ADDFRIENDS
                    }
                    ViewState.ADDFRIENDS -> {
                       // degreeEnd = 240f
                        //animationSetNext(degreeEnd)
                        tvPrev!!.visibility = View.VISIBLE
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = true
                        //Text change
                        YoYo.with(Techniques.SlideOutLeft)
                            .duration(100)
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_send_alert)
                                tvDescBody!!.text = "Test alert"
                                ivPicture.loadDrawable(R.drawable.sent_alert)

                                YoYo.with(Techniques.SlideInRight)
                                    .duration(300)
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)
                        viewState = ViewState.SENDALERT
                    }
                    ViewState.SENDALERT -> {
                        tvPrev!!.visibility = View.VISIBLE
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        ibPermissions!!.isSelected = true
                        //Text change
                        YoYo.with(Techniques.SlideOutLeft)
                            .duration(ANIMATION_INTRODUCTION_DURATION_EXIT.toLong())
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_permissions)
                                tvDescTitle!!.visibility = View.GONE
                                tvDescBody!!.text = "Test permissions"
                                ivPicture.loadDrawable(R.drawable.location)
                                tvDescBody!!.visibility = View.INVISIBLE
                                btnPermissions!!.visibility = View.VISIBLE
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(ANIMATION_INTRODUCTION_DURATION_ENTER.toLong())
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        viewState = ViewState.PERMiSSIONS
                        tvNext!!.setText(R.string.tv_done)

                        //Navigate to other activity
                    }

                    ViewState.PERMiSSIONS->{
                        KotlinPermissions.with(this) // where this is an FragmentActivity instance
                            .permissions(Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS)
                            .onAccepted { permissions ->
                                if(permissions.size==2){
                                    LandingActivity.newInstance(this)
                                    prefs.setPreSetupComplete(true)

                                }
                            }
                            .onDenied {
                                //List of denied permissions
                            }
                            .onForeverDenied { permissions ->
                                //List of forever denied permissions
                            }
                            .ask()

                    }

                }//startSpecificActivity(SignUpNameActivity.class);
            }
        }
    }





    private enum class ViewState {
        INTRODUCTION, SIGNUP, ADDFRIENDS, SENDALERT, PERMiSSIONS
    }

    private fun getViewState(): ViewState {
        if (viewState == null) {
            viewState = ViewState.INTRODUCTION
        }
        return viewState!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_pre_setup)

        if(prefs.isLogin()){
            HomeActivity.newInstance(this)
            finish()
        }
        else if(prefs.getPreSetupComplete()){
            LandingActivity.newInstance(this)
            finish()
        }
        detector = GestureDetector(this, this)

        tvPrev = findViewById(R.id.tv_previous)
        tvNext = findViewById(R.id.tv_next)
        ibOnBoard = findViewById(R.id.ib_on_board)
        ibGroups = findViewById(R.id.ib_groups)
        ibEvents = findViewById(R.id.ib_events)
        ibMessaging = findViewById(R.id.ib_messaging)
        ibPermissions = findViewById(R.id.ib_permissions)


        //Text part
        llDescription = findViewById(R.id.ll_description)
        tvDescTitle = findViewById(R.id.tv_desc_title)
        tvDescBody = findViewById(R.id.tv_desc_body)

        //Button
        btnPermissions = findViewById(R.id.btn_permission)


        tvPrev!!.setOnClickListener(clickListener)
        tvNext!!.setOnClickListener(clickListener)

        initViews()

        btnPermissions!!.setOnClickListener {
            Log.d("permiss","cllicke")
            KotlinPermissions.with(this)
                .permissions(Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS)
                .onAccepted { permissions ->
                    if(permissions.size==2){
                        LandingActivity.newInstance(this)
                    }
                }
                .onDenied {
                    //List of denied permissions
                }
                .onForeverDenied { permissions ->
                    //List of forever denied permissions
                }
                .ask()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detector!!.onTouchEvent(event)
    }


    private fun initViews() {
        viewState = ViewState.INTRODUCTION
        tvPrev!!.visibility = View.INVISIBLE

        tvDescTitle!!.text = getString(R.string.app_name)
        tvDescBody!!.text = getString(R.string.tv_on_boarding_desc)

        ibOnBoard!!.isSelected = true
        ibGroups!!.isSelected = false
        ibEvents!!.isSelected = false
        ibMessaging!!.isSelected = false
        ibPermissions!!.isSelected = false

    }

    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent) {

    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent, e2: MotionEvent, distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {

    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        try {
            if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH) {
                return false
            }
            // right to left swipe
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                clickListener.onClick(tvNext)

            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                clickListener.onClick(tvPrev)

            }// left to right swipe
        } catch (e: Exception) {

        }

        return false
    }

    companion object {

        private val ANIMATION_DURATION = 400

        private val ANIMATION_INTRODUCTION_DURATION_ENTER = 100

        private val ANIMATION_INTRODUCTION_DURATION_EXIT = 150

        private val SCALE_DOWN_RATIO = 1.0f

        private val SCALE_UP_RATIO = 2.0f

        private val SWIPE_MIN_DISTANCE = 120

        private val SWIPE_MAX_OFF_PATH = 250

        private val SWIPE_THRESHOLD_VELOCITY = 200
    }
}
