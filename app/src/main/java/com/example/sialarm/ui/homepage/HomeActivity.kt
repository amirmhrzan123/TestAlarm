package com.example.sialarm.ui.homepage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.os.Handler
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.aurelhubert.ahbottomnavigation.notification.AHNotification
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.databinding.ActivityMainBinding
import com.example.sialarm.ui.instructions.InstructionFragment
import com.example.sialarm.utils.CommonUtils
import com.example.sialarm.utils.extensions.convertDpToPixel
import com.example.sialarm.utils.extensions.getNumber
import com.example.sialarm.utils.extensions.showValidationDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class HomeActivity: BaseActivity<MainViewModel, ActivityMainBinding>() {

    lateinit var adapter: HomePagerAdapter
    lateinit var navigationAdapter: AHBottomNavigationAdapter
    private val bottomNavigationItems = ArrayList<AHBottomNavigationItem>()
    private val useMenuResource = true
    lateinit var tabColors: IntArray
    private val handler = Handler()
    private var currentFragment: Fragment?= null

    private var tabPosition = 0


    private val prefs: PrefsManager by inject()


    // UI
    lateinit var viewPager: ViewPager
    lateinit var bottomNavigation: AHBottomNavigation
    lateinit var floatingActionButton: FloatingActionButton


    private val mainViewModel: MainViewModel by viewModel()

    override fun getLayoutId(): Int  = R.layout.activity_main

    override fun getViewModel(): MainViewModel = mainViewModel

    override fun getBindingVariable(): Int = BR.viewModel



    companion object {
        fun newInstance(activity: Activity){
            val intent = Intent(activity,HomeActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        toolbar.title = "Alert"
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
        initUI()
        if(!prefs.getInstruction()){
            bl_menu.visibility = View.VISIBLE
            prefs.setInstruction(true)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * Init UI
     */
    @SuppressLint("RestrictedApi")
    private fun initUI() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
        mainViewModel.saveUsers()

        setPositionOfTooltip()

        bottomNavigation = findViewById(R.id.bottom_navigation)
        viewPager = findViewById(R.id.view_pager)
        bottomNavigation.accentColor = ContextCompat.getColor(this, R.color.color_blue_2590b8)
        bottomNavigation.inactiveColor = ContextCompat.getColor(this,R.color.white)
        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        iv_cross.setOnClickListener {
            bl_menu.visibility = View.GONE
        }

        iv_info.setOnClickListener {
            openInformationFragment()
        }



        floating_action_button.setOnClickListener {
                CommonUtils.openChooserDialog(this) {
                    when(it){
                        CommonUtils.OPTION.ADDFROMCONTACT->{
                            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                            intent.putExtra("finishActivityOnSaveCompleted", true)
                            startActivityForResult(intent, 1)
                        }
                        CommonUtils.OPTION.ADDANOTHERCONTACT->{
                            CommonUtils.getAddNumberDialog(this,{number,userName->
                                println("USername $userName")
                                mainViewModel.contactName = userName
                                mainViewModel.contactNumber = number
                                mainViewModel.contactTrigger.value = true

                            },{message->
                                showValidationDialog("SI Alsrm",message,"Ok")
                            }).show()
                        }
                    }
                }

        }

            tabColors = applicationContext.resources.getIntArray(R.array.tab_colors)
            navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation)
            navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors)

        mainViewModel.getNotificationCountValid.value = true

        mainViewModel.getNotificationResponse.observe(this,androidx.lifecycle.Observer {
            if(it.count==0){
                bottomNavigation.setNotification("",2)
            }else{
                bottomNavigation.setNotification(it.count.toString(),2)
            }
        })


        bottomNavigation.defaultBackgroundColor = ContextCompat.getColor(this, R.color.dark_green)
        viewPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                bottomNavigation.currentItem = position
                tabPosition = position

            }

        })

        bottomNavigation.setOnTabSelectedListener(AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
            if (currentFragment == null) {
                currentFragment = adapter.getCurrentFragment(position)
            }


            viewPager.setCurrentItem(position, true)


            if (currentFragment == null) {
                return@OnTabSelectedListener true
            }

            when(position){
                0->{
                    toolbar.title = "Alert"
                }
                1->{
                    toolbar.title = "My SI Friends"
                }
                2->{
                    toolbar.title = "Notifications"
                    mainViewModel.resetNotificationCount()
                }
                3->{
                    toolbar.title = "Settings"
                }
            }

            if(position==1){
                floating_action_button.visibility = View.VISIBLE
            }else{
                floating_action_button.visibility = View.GONE
            }

            if(tabPosition>1){
                iv_info.visibility = View.GONE

            }else{
                iv_info.visibility = View.VISIBLE
            }


            // currentFragment.willBeDisplayed()



            true
        })

        viewPager.offscreenPageLimit = 3
        adapter = HomePagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

    }

    fun openInformationFragment() {
        InstructionFragment.newInstance(tabPosition).show(supportFragmentManager,"")
        bl_menu.visibility = View.GONE

    }


    private fun setPositionOfTooltip() {
        val vto = iv_info.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                iv_info.getViewTreeObserver().removeGlobalOnLayoutListener(this)

                val posXY = IntArray(2)
                val x = iv_info.getLeft()
                val y = iv_info.getRight()
                val position = x  + (y-x)/2 - convertDpToPixel(16f)
                bl_menu.setArrowPosition(position.toFloat())

            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {

           val uriContact = data?.data
            if(uriContact!=null){
                var contactNumber = ""
                var contactID =  ""

                // getting contacts ID
                val cursorID = contentResolver.query(uriContact!!,
                    arrayOf(ContactsContract.Contacts._ID),null,null,null)

                if (cursorID!!.moveToFirst()) {

                    contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID))
                    Log.d("", "Contact ID: " + contactID)

                }

                cursorID.close()


                // Using the contact ID now we will get contact phone number
                val cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                    arrayOf(contactID),null)



                if (cursorPhone!!.moveToFirst()) {
                    contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                }

                cursorPhone.close()
                Log.d("ContactNumber", "Contact Phone Number: " + contactNumber);

                var contactName = ""

                // querying contact data store
                val cursor = getContentResolver().query(uriContact, null, null, null, null);

                if (cursor!!.moveToFirst()) {

                    // DISPLAY_NAME = The display name for the contact.
                    // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                }

                cursor.close()
                if(contactNumber.isEmpty()){
                    showValidationDialog("SI Alarm",getString(R.string.number_error))
                }else{
                    mainViewModel.contactNumber = contactNumber.getNumber()
                    mainViewModel.contactName = contactName
                    mainViewModel.contactTrigger.value = true
                }
                Log.d("Contactname", "Contact Name: " + contactName)
            }
            }
    }
}
