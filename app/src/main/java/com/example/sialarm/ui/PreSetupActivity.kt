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
import com.kotlinpermissions.KotlinPermissions
import org.koin.android.ext.android.inject
import java.util.*


class PreSetupActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private var tvPrev: TextView? = null
    private var tvNext: TextView? = null
    private var tvDescTitle: TextView? = null
    private var tvDescBody: TextView? = null
    private var tvPermissions: TextView? = null

    private var btnPermissions: Button? = null

    private var ibOnBoard: ImageButton? = null
    private var ibGroups: ImageButton? = null
    private var ibEvents: ImageButton? = null
    private var ibMessaging: ImageButton? = null
    private var ibPermissions: ImageButton? = null

    private var llDescription: RelativeLayout? = null

    private var viewState: ViewState? = null

    private var clImages: ConstraintLayout? = null

    private var ivOnBoarding: ImageView? = null
    private var ivEvents: ImageView? = null
    private var ivCalendar: ImageView? = null
    private var ivGuestList: ImageView? = null
    private var ivMain: ImageView? = null

    private var degreeEnd = 120f

    private var detector: GestureDetector? = null

    private val prefs:PrefsManager by inject()


    private val clickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.tv_previous -> {
                when (getViewState()) {
                    PreSetupActivity.ViewState.ON_BOARD -> {
                    }
                    PreSetupActivity.ViewState.EVENTS -> {
                        //Bottom layout
                        tvPrev!!.visibility = View.INVISIBLE
                        ibOnBoard!!.isSelected = true
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutRight)
                            .duration(ANIMATION_ON_BOARD_DURATION_EXIT.toLong())
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.app_name)
                                tvDescBody!!.text = "First"
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(ANIMATION_ON_BOARD_DURATION_ENTER.toLong())
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        //Main layout
                        YoYo.with(Techniques.SlideOutRight)
                            .duration(ANIMATION_ON_BOARD_DURATION_EXIT.toLong())
                            .onEnd {
                                clImages!!.visibility = View.INVISIBLE
                                ivOnBoarding!!.visibility = View.VISIBLE
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(ANIMATION_ON_BOARD_DURATION_ENTER.toLong())
                                    .playOn(ivOnBoarding)
                            }
                            .playOn(clImages)
                        viewState = ViewState.ON_BOARD
                    }
                    ViewState.GUEST_LIST -> {
                        degreeEnd = 0f
                        animationSetPrev(degreeEnd)
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
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(300)
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)
                        viewState = ViewState.EVENTS
                    }
                    PreSetupActivity.ViewState.CALENDAR -> {
                        degreeEnd = 120f
                        animationSetPrev(degreeEnd)
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
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(300)
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)
                        viewState = ViewState.GUEST_LIST
                        tvNext!!.setText(R.string.tv_next)
                    }

                    ViewState.PERMiSSIONS -> {
                        degreeEnd = 240f
                        animationSetPrev(degreeEnd)
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = true
                        ibPermissions!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutRight)
                            .duration(ANIMATION_ON_BOARD_DURATION_EXIT.toLong())
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_send_alert)
                                tvDescBody!!.text = "Test alert"
                                btnPermissions!!.visibility = View.INVISIBLE
                                tvDescBody!!.visibility = View.VISIBLE
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(ANIMATION_ON_BOARD_DURATION_ENTER.toLong())
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        //Main view
                        YoYo.with(Techniques.SlideOutRight)
                            .duration(ANIMATION_ON_BOARD_DURATION_EXIT.toLong())
                            .onEnd {
                                clImages!!.visibility = View.VISIBLE
                                tvPermissions!!.visibility = View.INVISIBLE
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(ANIMATION_ON_BOARD_DURATION_ENTER.toLong())
                                    .playOn(clImages)
                            }
                            .playOn(tvPermissions)
                        viewState = ViewState.CALENDAR
                        tvNext!!.setText("Next")
                    }
                }//Not possible
            }
            R.id.tv_next -> {
                when (getViewState()) {
                    PreSetupActivity.ViewState.ON_BOARD -> {
                        tvPrev!!.visibility = View.VISIBLE
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = true
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        //Text change
                        YoYo.with(Techniques.SlideOutLeft)
                            .duration(ANIMATION_ON_BOARD_DURATION_EXIT.toLong())
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_register)
                                tvDescBody!!.text = "Test register"
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(ANIMATION_ON_BOARD_DURATION_ENTER.toLong())
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        //Main view
                        YoYo.with(Techniques.SlideOutLeft)
                            .duration(ANIMATION_ON_BOARD_DURATION_EXIT.toLong())
                            .onEnd {
                                ivOnBoarding!!.visibility = View.INVISIBLE
                                clImages!!.visibility = View.VISIBLE
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(ANIMATION_ON_BOARD_DURATION_ENTER.toLong())
                                    .playOn(clImages)
                            }
                            .playOn(ivOnBoarding)
                        viewState = ViewState.EVENTS
                    }
                    ViewState.EVENTS -> {
                        degreeEnd = 120f
                        animationSetNext(degreeEnd)
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
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(300)
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)
                        viewState = ViewState.GUEST_LIST
                    }
                    ViewState.GUEST_LIST -> {
                        degreeEnd = 240f
                        animationSetNext(degreeEnd)
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
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(300)
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)
                        viewState = ViewState.CALENDAR
                    }
                    ViewState.CALENDAR -> {
                        tvPrev!!.visibility = View.VISIBLE
                        ibOnBoard!!.isSelected = false
                        ibGroups!!.isSelected = false
                        ibEvents!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        ibMessaging!!.isSelected = false
                        ibPermissions!!.isSelected = true
                        //Text change
                        YoYo.with(Techniques.SlideOutLeft)
                            .duration(ANIMATION_ON_BOARD_DURATION_EXIT.toLong())
                            .onEnd {
                                tvDescTitle!!.text = getString(R.string.tv_permissions)
                                tvDescBody!!.text = "Test permissions"
                                tvPermissions!!.visibility = View.VISIBLE
                                tvDescBody!!.visibility = View.INVISIBLE
                                btnPermissions!!.visibility = View.VISIBLE
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(ANIMATION_ON_BOARD_DURATION_ENTER.toLong())
                                    .playOn(llDescription)
                            }
                            .playOn(llDescription)

                        YoYo.with(Techniques.SlideOutLeft)
                            .duration(ANIMATION_ON_BOARD_DURATION_EXIT.toLong())
                            .onEnd {
                                YoYo.with(Techniques.SlideInRight)
                                    .duration(ANIMATION_ON_BOARD_DURATION_ENTER.toLong())
                                    .playOn(tvPermissions)
                            }
                            .playOn(clImages)
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

    private val scaleAnimatorNextList: List<Animator>
        get() {
            val list = ArrayList<Animator>()
            when (getViewState()) {
                ViewState.EVENTS -> {
                    val backgroundReverse = ivEvents!!.background as TransitionDrawable
                    backgroundReverse.reverseTransition(ANIMATION_DURATION)
                    list.add(getScaleDownXAnimator(ivEvents!!))
                    list.add(getScaleDownYAnimator(ivEvents!!))

                    val background = ivGuestList!!.background as TransitionDrawable
                    background.startTransition(ANIMATION_DURATION)
                    list.add(getScaleUpXAnimator(ivGuestList!!))
                    list.add(getScaleUpYAnimator(ivGuestList!!))
                }
                ViewState.GUEST_LIST -> {
                    val backgroundReverse = ivGuestList!!.background as TransitionDrawable
                    backgroundReverse.reverseTransition(ANIMATION_DURATION)
                    list.add(getScaleDownXAnimator(ivGuestList!!))
                    list.add(getScaleDownYAnimator(ivGuestList!!))

                    val background = ivCalendar!!.background as TransitionDrawable
                    background.startTransition(ANIMATION_DURATION)
                    list.add(getScaleUpXAnimator(ivCalendar!!))
                    list.add(getScaleUpYAnimator(ivCalendar!!))
                }
            }
            return list
        }

    private val scaleAnimatorPreviousList: List<Animator>
        get() {
            val list = ArrayList<Animator>()
            when (getViewState()) {
                ViewState.GUEST_LIST -> {
                    val backgroundReverse = ivGuestList!!.background as TransitionDrawable
                    backgroundReverse.reverseTransition(ANIMATION_DURATION)
                    list.add(getScaleDownXAnimator(ivGuestList!!))
                    list.add(getScaleDownYAnimator(ivGuestList!!))

                    val background = ivEvents!!.background as TransitionDrawable
                    background.startTransition(ANIMATION_DURATION)
                    list.add(getScaleUpXAnimator(ivEvents!!))
                    list.add(getScaleUpYAnimator(ivEvents!!))
                }
                ViewState.CALENDAR -> {
                    val backgroundReverse = ivCalendar!!.background as TransitionDrawable
                    backgroundReverse.reverseTransition(ANIMATION_DURATION)
                    list.add(getScaleDownXAnimator(ivCalendar!!))
                    list.add(getScaleDownYAnimator(ivCalendar!!))

                    val background = ivGuestList!!.background as TransitionDrawable
                    background.startTransition(ANIMATION_DURATION)
                    list.add(getScaleUpXAnimator(ivGuestList!!))
                    list.add(getScaleUpYAnimator(ivGuestList!!))
                }
            }
            return list
        }

    private enum class ViewState {
        ON_BOARD, EVENTS, GUEST_LIST, CALENDAR, PERMiSSIONS
    }

    private fun getViewState(): ViewState {
        if (viewState == null) {
            viewState = ViewState.ON_BOARD
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

        ivOnBoarding = findViewById(R.id.iv_on_boarding)
        clImages = findViewById(R.id.rl_images)
        //Image
        ivEvents = findViewById(R.id.iv_events)
        ivGuestList = findViewById(R.id.iv_guest_list)
        ivCalendar = findViewById(R.id.iv_calendar)
        ivMain = findViewById(R.id.iv_mid_circle)

        //Text part
        llDescription = findViewById(R.id.ll_description)
        tvDescTitle = findViewById(R.id.tv_desc_title)
        tvDescBody = findViewById(R.id.tv_desc_body)
        tvPermissions = findViewById(R.id.tvPermissions)

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

    private fun animationSetNext(degreeEnd: Float) {
        val set = AnimatorSet()

        val list = ArrayList<Animator>()
        list.addAll(getRotationAnimatorNextList(degreeEnd))
        list.addAll(scaleAnimatorNextList)

        set.playTogether(list)
        set.start()
    }

    private fun animationSetPrev(degreeEnd: Float) {
        val set = AnimatorSet()

        val list = ArrayList<Animator>()
        list.addAll(getRotationAnimatorPreviousList(degreeEnd))
        list.addAll(scaleAnimatorPreviousList)

        set.playTogether(list)
        set.start()
    }

    private fun getRotationAnimatorNextList(degreeEnd: Float): List<Animator> {
        val listRotationAnimators = ArrayList<Animator>()

        val animatorAllView = ObjectAnimator.ofFloat(clImages, View.ROTATION, degreeEnd)
        animatorAllView.repeatCount = 0
        animatorAllView.duration = ANIMATION_DURATION.toLong()
        animatorAllView.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                clImages!!.rotation = degreeEnd
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        listRotationAnimators.add(animatorAllView)

        val rotateGroup = ObjectAnimator.ofFloat(ivEvents, View.ROTATION, -degreeEnd)
        rotateGroup.repeatCount = 0
        rotateGroup.duration = ANIMATION_DURATION.toLong()
        rotateGroup.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                ivEvents!!.rotation = -degreeEnd
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        listRotationAnimators.add(rotateGroup)

        val rotateEvent = ObjectAnimator.ofFloat(ivGuestList, View.ROTATION, -degreeEnd)
        rotateEvent.repeatCount = 0
        rotateEvent.duration = ANIMATION_DURATION.toLong()
        rotateEvent.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                ivGuestList!!.rotation = -degreeEnd
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        listRotationAnimators.add(rotateEvent)

        val rotateMessaging = ObjectAnimator.ofFloat(ivCalendar, View.ROTATION, -degreeEnd)
        rotateMessaging.repeatCount = 0
        rotateMessaging.duration = ANIMATION_DURATION.toLong()
        rotateMessaging.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                ivCalendar!!.rotation = -degreeEnd
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        listRotationAnimators.add(rotateMessaging)

        return listRotationAnimators
    }

    private fun getRotationAnimatorPreviousList(degreeEnd: Float): List<Animator> {
        val list = ArrayList<Animator>()
        val animatorAllView = ObjectAnimator.ofFloat(clImages, View.ROTATION, degreeEnd)
        animatorAllView.repeatCount = 0
        animatorAllView.duration = ANIMATION_DURATION.toLong()
        animatorAllView.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                clImages!!.rotation = degreeEnd
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        list.add(animatorAllView)

        val animatorGroup = ObjectAnimator.ofFloat(ivEvents, View.ROTATION, -degreeEnd)
        animatorGroup.repeatCount = 0
        animatorGroup.duration = ANIMATION_DURATION.toLong()
        animatorGroup.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                ivEvents!!.rotation = -degreeEnd
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        list.add(animatorGroup)

        val animatorEvent = ObjectAnimator.ofFloat(ivGuestList, View.ROTATION, -degreeEnd)
        animatorEvent.repeatCount = 0
        animatorEvent.duration = ANIMATION_DURATION.toLong()
        animatorEvent.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                ivGuestList!!.rotation = -degreeEnd
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        list.add(animatorEvent)

        val animatorMessaging = ObjectAnimator.ofFloat(ivCalendar, View.ROTATION, -degreeEnd)
        animatorMessaging.repeatCount = 0
        animatorMessaging.duration = ANIMATION_DURATION.toLong()
        animatorMessaging.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                ivCalendar!!.rotation = -degreeEnd
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        list.add(animatorMessaging)
        return list
    }

    private fun getScaleDownXAnimator(imageView: ImageView): ObjectAnimator {
        val scaleDownX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, SCALE_DOWN_RATIO)
        scaleDownX.repeatCount = 0
        scaleDownX.duration = ANIMATION_DURATION.toLong()
        scaleDownX.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                when (imageView.id) {
                    R.id.iv_events -> ivEvents!!.scaleX = SCALE_DOWN_RATIO
                    R.id.iv_guest_list -> ivGuestList!!.scaleX = SCALE_DOWN_RATIO
                    R.id.iv_calendar -> ivCalendar!!.scaleX = SCALE_DOWN_RATIO
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        return scaleDownX
    }

    private fun getScaleDownYAnimator(imageView: ImageView): ObjectAnimator {
        val scaleDownY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, SCALE_DOWN_RATIO)
        scaleDownY.repeatCount = 0
        scaleDownY.duration = ANIMATION_DURATION.toLong()
        scaleDownY.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                when (imageView.id) {
                    R.id.iv_events -> ivEvents!!.scaleY = SCALE_DOWN_RATIO
                    R.id.iv_guest_list -> ivGuestList!!.scaleY = SCALE_DOWN_RATIO
                    R.id.iv_calendar -> ivCalendar!!.scaleY = SCALE_DOWN_RATIO
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        return scaleDownY
    }

    private fun getScaleUpXAnimator(imageView: ImageView): ObjectAnimator {
        val background = imageView.background as TransitionDrawable
        background.reverseTransition(ANIMATION_DURATION)
        val scaleUpX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, SCALE_UP_RATIO)
        scaleUpX.repeatCount = 0
        scaleUpX.duration = ANIMATION_DURATION.toLong()
        scaleUpX.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                when (imageView.id) {
                    R.id.iv_events -> ivEvents!!.scaleX = SCALE_UP_RATIO
                    R.id.iv_guest_list -> ivGuestList!!.scaleX = SCALE_UP_RATIO
                    R.id.iv_calendar -> ivCalendar!!.scaleX = SCALE_UP_RATIO
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        return scaleUpX
    }

    private fun getScaleUpYAnimator(imageView: ImageView): ObjectAnimator {
        val scaleUpY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, SCALE_UP_RATIO)
        scaleUpY.repeatCount = 0
        scaleUpY.duration = ANIMATION_DURATION.toLong()
        scaleUpY.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                when (imageView.id) {
                    R.id.iv_events -> ivEvents!!.scaleY = SCALE_UP_RATIO
                    R.id.iv_guest_list -> ivGuestList!!.scaleY = SCALE_UP_RATIO
                    R.id.iv_calendar -> ivCalendar!!.scaleY = SCALE_UP_RATIO
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        return scaleUpY
    }

    private fun initViews() {
        viewState = ViewState.ON_BOARD
        tvPrev!!.visibility = View.INVISIBLE

        tvDescTitle!!.text = getString(R.string.app_name)
        tvDescBody!!.text = getString(R.string.tv_on_boarding_desc)

        ibOnBoard!!.isSelected = true
        ibGroups!!.isSelected = false
        ibEvents!!.isSelected = false
        ibMessaging!!.isSelected = false
        ibPermissions!!.isSelected = false

        ivEvents!!.scaleX = SCALE_UP_RATIO
        ivEvents!!.scaleY = SCALE_UP_RATIO

        ivGuestList!!.scaleX = SCALE_DOWN_RATIO
        ivGuestList!!.scaleY = SCALE_DOWN_RATIO

        ivCalendar!!.scaleX = SCALE_DOWN_RATIO
        ivCalendar!!.scaleY = SCALE_DOWN_RATIO

        val backgroundReverse1 = ivGuestList!!.background as TransitionDrawable
        backgroundReverse1.reverseTransition(100)

        val backgroundReverse2 = ivCalendar!!.background as TransitionDrawable
        backgroundReverse2.reverseTransition(100)
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

        private val ANIMATION_ON_BOARD_DURATION_ENTER = 100

        private val ANIMATION_ON_BOARD_DURATION_EXIT = 150

        private val SCALE_DOWN_RATIO = 0.60f

        private val SCALE_UP_RATIO = 1.0f

        private val SWIPE_MIN_DISTANCE = 120

        private val SWIPE_MAX_OFF_PATH = 250

        private val SWIPE_THRESHOLD_VELOCITY = 200
    }
}
