package com.example.sialarm.ui.homepage.notification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.databinding.FragmentNotificationBinding
import com.example.sialarm.utils.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment:BaseFragment<NotificationViewModel,FragmentNotificationBinding>() {

    companion object {
        fun newInstance():NotificationFragment{
            return NotificationFragment()
        }
    }

    private val notificationViewModel: NotificationViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun getViewModel(): NotificationViewModel = notificationViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationViewModel.notification.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                }
                Status.ERROR->{

                }
            }
        })
    }
}