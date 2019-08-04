package com.example.sialarm.ui.homepage.settings

import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.data.api.SendAlertMessages
import com.example.sialarm.data.api.SendSafeAlertMessages
import com.example.sialarm.databinding.FragmentSettingBinding
import com.example.sialarm.ui.history.HistoryActivity
import com.example.sialarm.ui.homepage.MainViewModel
import com.example.sialarm.ui.homepage.home.HomeFragment
import com.example.sialarm.ui.myProfile.MyProfileActivity
import com.example.sialarm.utils.Status
import com.example.sialarm.utils.extensions.isConnectingToInternet
import com.example.sialarm.utils.extensions.showConfirmationDialog
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class SettingsFragment: BaseFragment<SettingViewModel,FragmentSettingBinding>(),ISettingNavigator {
    override fun onHistoryClicked() {
        HistoryActivity.newInstance(activity!!)
    }

    override fun onProfileClicked() {
        MyProfileActivity.newInstance(activity!!)
    }

    override fun onLogoutClicked() {
    }

    override fun onSendSafeAlertClicked() {
        activity!!.showConfirmationDialog("","Are you sure you want to send safe alert messages to everyone?","Yes",
            "No"
        ) {
            if(isConnectingToInternet(context!!)){
                settingViewModel.settingValid.value = SendSafeAlertMessages()
            }else{
                val smsManager = SmsManager.getDefault() as SmsManager
                smsManager.sendTextMessage("+9779808878368", null, "SISA1234|${HomeFragment.mCurrentLocation.latitude}|${HomeFragment.mCurrentLocation.longitude}*", null, null)
            }
        }
    }

    private val settingViewModel : SettingViewModel by viewModel()


    override fun getLayoutId(): Int = R.layout.fragment_setting

    override fun getViewModel(): SettingViewModel = settingViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingViewModel.setNavigator(this)

        settingViewModel.sendSafeAlert.observe(this,androidx.lifecycle.Observer {
           when(it.status){
               Status.LOADING->{
                   showLoading("")
               }
               Status.SUCCESS->{
                   hideLoading()
               }
               Status.ERROR->{
                   hideLoading()
               }
           }
       })

    }
}