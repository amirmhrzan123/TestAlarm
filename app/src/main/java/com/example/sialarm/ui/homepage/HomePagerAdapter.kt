package com.example.sialarm.ui.homepage

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sialarm.ui.homepage.contacts.ContactsFragment
import com.example.sialarm.ui.homepage.home.HomeFragment
import com.example.sialarm.ui.homepage.notification.NotificationFragment
import com.example.sialarm.ui.homepage.settings.SettingsFragment
import java.util.ArrayList

class HomePagerAdapter  constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragmentList: ArrayList<Fragment>

    init {
        fragmentList = ArrayList(NUM_ITEMS)
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return HomeFragment.newInstance()
            1 -> return ContactsFragment.newInstance()
            2 -> return NotificationFragment.newInstance()
            3 -> return SettingsFragment.newInstance()

            else -> return null
        }


    }


    fun getCurrentFragment(position: Int): Fragment {
        return fragmentList[position]
    }


    override fun getPageTitle(position: Int): CharSequence? {
        // Generate title based on item position
        when (position) {
            0 -> return "Alert"
            1 -> return "Contacts"
            2 -> return "Notifications"
            3 -> return "More"
            else -> return null
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        if (fragmentList.size < NUM_ITEMS && !fragmentList.contains(fragment)) {
            fragmentList.add(fragment)
        }
        return fragment
    }


    override fun getCount(): Int {
        return NUM_ITEMS
    }

    companion object {
        private val NUM_ITEMS = 4
    }
}

