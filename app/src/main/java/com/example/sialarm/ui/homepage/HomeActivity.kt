package com.example.sialarm.ui.homepage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.app.dwell.base.BaseActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class HomeActivity:BaseActivity<HomeViewModel,ActivityMainBinding>() {

    lateinit var adapter: HomePagerAdapter
    lateinit var navigationAdapter: AHBottomNavigationAdapter
    private val bottomNavigationItems = ArrayList<AHBottomNavigationItem>()
    private val useMenuResource = true
    lateinit var tabColors: IntArray
    private val handler = Handler()
    private var currentFragment: Fragment?= null

    // UI
    lateinit var viewPager: ViewPager
    lateinit var bottomNavigation: AHBottomNavigation
    lateinit var floatingActionButton: FloatingActionButton


    private val homeViewModel: HomeViewModel by viewModel()

    override fun getLayoutId(): Int  = R.layout.activity_main

    override fun getViewModel(): HomeViewModel = homeViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    companion object {
        fun newInstance(activity: Activity){
            val intent = Intent(activity,HomeActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val enabledTranslucentNavigation = getSharedPreferences("shared", Context.MODE_PRIVATE)
            .getBoolean("translucentNavigation", false)
        setTheme(if (enabledTranslucentNavigation) R.style.AppTheme_TranslucentNavigation else R.style.AppTheme)
        initUI()
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

        bottomNavigation = findViewById(R.id.bottom_navigation)
        viewPager = findViewById(R.id.view_pager)

        floating_action_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            intent.putExtra("finishActivityOnSaveCompleted", true)
            startActivityForResult(intent, 1)
        }

            tabColors = applicationContext.resources.getIntArray(R.array.tab_colors)
            navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation)
            navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors)

        bottomNavigation.isTranslucentNavigationEnabled = true

        viewPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                bottomNavigation.currentItem = position
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

            if(position==1){
                floating_action_button.visibility = View.VISIBLE
            }else{
                floating_action_button.visibility = View.GONE
            }

           // currentFragment.willBeDisplayed()



            true
        })

        /*
		bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
			@Override public void onPositionChange(int y) {
				Log.d("DemoActivity", "BottomNavigation Position: " + y);
			}
		});
		*/

        viewPager.offscreenPageLimit = 3
        adapter = HomePagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter


        //bottomNavigation.setDefaultBackgroundResource(R.drawable.bottom_navigation_background);
    }

    /**
     * Update the bottom navigation colored param
     */
    fun updateBottomNavigationColor(isColored: Boolean) {
        bottomNavigation.isColored = isColored
    }

    /**
     * Return if the bottom navigation is colored
     */
    fun isBottomNavigationColored(): Boolean {
        return bottomNavigation.isColored
    }

    /**
     * Add or remove items of the bottom navigation
     */
    fun updateBottomNavigationItems(addItems: Boolean) {

        if (useMenuResource) {
                navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation)
                navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors)
                bottomNavigation.setNotification("1", 3)


        } else {
            if (addItems) {
                val item4 = AHBottomNavigationItem(
                   "Menu",
                    ContextCompat.getDrawable(this, R.drawable.ic_setting),
                    ContextCompat.getColor(this, R.color.color_tab_4)
                )

                bottomNavigation.addItem(item4)
                bottomNavigation.setNotification("1", 3)
            } else {
                bottomNavigation.removeAllItems()
                bottomNavigation.addItems(bottomNavigationItems)
            }
        }
    }

    /**
     * Show or hide the bottom navigation with animation
     */
    fun showOrHideBottomNavigation(show: Boolean) {
        if (show) {
            bottomNavigation.restoreBottomNavigation(true)
        } else {
            bottomNavigation.hideBottomNavigation(true)
        }
    }

    /**
     * Show or hide selected item background
     */
    fun updateSelectedBackgroundVisibility(isVisible: Boolean) {
        bottomNavigation.setSelectedBackgroundVisible(isVisible)
    }

    /**
     * Set title state for bottomNavigation
     */
    fun setTitleState(titleState: AHBottomNavigation.TitleState) {
        bottomNavigation.titleState = titleState
    }

    /**
     * Reload activity
     */
    fun reload() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    /**
     * Return the number of items in the bottom navigation
     */
    fun getBottomNavigationNbItems(): Int {
        return bottomNavigation.itemsCount
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1) {
           val uriContact = data?.data
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
            homeViewModel.contactNumber = contactNumber
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
                homeViewModel.contactName = contactName
            }

            cursor.close()
            homeViewModel.contactTrigger.value = true

            Log.d("Contactname", "Contact Name: " + contactName)
        }
    }
}
