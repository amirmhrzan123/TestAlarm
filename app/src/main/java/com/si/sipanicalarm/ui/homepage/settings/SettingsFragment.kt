package com.si.sipanicalarm.ui.homepage.settings

import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import com.si.sipanicalarm.BR
import com.si.sipanicalarm.R
import com.si.sipanicalarm.base.BaseFragment
import com.si.sipanicalarm.data.api.SendSafeAlertMessages
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.databinding.FragmentSettingBinding
import com.si.sipanicalarm.ui.device.AddDeviceActivity
import com.si.sipanicalarm.ui.history.HistoryActivity
import com.si.sipanicalarm.ui.landingScreen.LandingActivity
import com.si.sipanicalarm.ui.lillipin.CustomPinActivity
import com.si.sipanicalarm.ui.myProfile.MyProfileActivity
import com.si.sipanicalarm.ui.termsPrivacyPolicy.TermsPrivacyPolicy
import com.si.sipanicalarm.utils.Status
import com.si.sipanicalarm.utils.extensions.isConnectingToInternet
import com.si.sipanicalarm.utils.extensions.showConfirmationDialog
import com.si.sipanicalarm.utils.extensions.showValidationDialog
import com.facebook.accountkit.AccountKit
import com.github.omadahealth.lollipin.lib.managers.AppLock
import kotlinx.android.synthetic.main.fragment_setting.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment: BaseFragment<SettingViewModel,FragmentSettingBinding>(),ISettingNavigator {
    override fun onPrivacyClicked() {
        TermsPrivacyPolicy.newInstance(activity!!,TermsPrivacyPolicy.PRIVACYPOLICY)
    }

    override fun onTermsConditionClicked() {
        TermsPrivacyPolicy.newInstance(activity!!,TermsPrivacyPolicy.TERMSCONDITION)
    }

    private val prefs: PrefsManager by inject()


    override fun onChangePasswordClicked() {
        val intent = Intent(activity!!,CustomPinActivity::class.java)
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN)
        startActivity(intent)
    }

    override fun onEnablePasswordClicked() {
        if(!prefs.isPinCodeSet()){
            switches.isChecked = true
            prefs.setPinCode(true)
        }else{
            switches.isChecked = false
            prefs.setPinCode(false)
        }
    }

    override fun onAddDeviceClicked() {
        AddDeviceActivity.newInstance(activity!!)
    }



    override fun onHistoryClicked() {
        HistoryActivity.newInstance(activity!!)
    }

    override fun onProfileClicked() {
        MyProfileActivity.newInstance(activity!!)
    }

    override fun onLogoutClicked() {

        activity!!.showConfirmationDialog("Logout","Are you sure you want to logout?",ok = {
            if(isConnectingToInternet(activity!!)){
                AccountKit.logOut()
                settingViewModel.logoutValid.value = true
            }else{
                activity!!.showValidationDialog("Logout","Please check your internet connection to logout.")
            }
        })

    }

    override fun onSendSafeAlertClicked() {
        activity!!.showConfirmationDialog("","Are you sure you want to send safe alert messages to everyone?","Yes",
            "No"
        ) {
            if(isConnectingToInternet(context!!)){
                settingViewModel.settingValid.value = SendSafeAlertMessages()
            }else{
                val smsManager = SmsManager.getDefault() as SmsManager
                smsManager.sendTextMessage("+977${prefs.getDeviceId()}", null,
                    "SISA1234|27.9988776|85.1234567|00*", null, null)
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

        if(prefs.isAdmin()){
            rlAddDevice.visibility = View.VISIBLE
        }

        switches.isChecked = prefs.isPinCodeSet()



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

        settingViewModel.logout.observe(this@SettingsFragment, androidx.lifecycle.Observer {
            when(it.status){
                Status.SUCCESS->{
                    LandingActivity.newInstance(activity!!)
                    prefs.clear()
                    activity!!.finishAffinity()
                }
                Status.ERROR->{
                    activity!!.showValidationDialog(it.titleMessage!!,it.message!!)
                }
            }
        })

    }
}