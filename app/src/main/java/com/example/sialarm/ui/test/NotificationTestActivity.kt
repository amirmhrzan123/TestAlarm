package com.example.sialarm.ui.test

import android.app.Notification
import android.os.Bundle
import android.util.Log
import com.example.sialarm.R
import com.google.firebase.iid.FirebaseInstanceId
import com.app.dwell.base.BaseActivity
import com.example.sialarm.BR
import com.example.sialarm.BuildConfig
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.databinding.ActivityHomeBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotificationTestActivity:BaseActivity<NotificationTestViewModel,ActivityHomeBinding>() {

    private val notificationTestViewModel : NotificationTestViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun getViewModel(): NotificationTestViewModel = notificationTestViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        BuildConfig.DEBUG
        initFCM()
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            token = instanceIdResult.token
        }
        button.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Notification").child("-LjBLMhFSjsxJzajKNiJ")
                .child("address")
                .setValue("kupondole")

        }
        //sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d("Registration", "sendRegistrationToServer: sending token to server: " + token!!)
        val reference = FirebaseDatabase.getInstance().reference
        reference.child("user")
            .child("-LjBLMhFSjsxJzajKNiJ")
            .child("")
            .setValue(token)
    }


    private fun initFCM() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d("Init fcm", "initFCM: token: " + token!!)
        sendRegistrationToServer(token)

    }
}