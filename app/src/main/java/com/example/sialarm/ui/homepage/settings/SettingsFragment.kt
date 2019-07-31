package com.example.sialarm.ui.homepage.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.databinding.FragmentSettingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment: BaseFragment<SettingViewModel,FragmentSettingBinding>(),ISettingNavigator {
    override fun onProfileClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLogoutClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSendSafeAlertClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    }
}