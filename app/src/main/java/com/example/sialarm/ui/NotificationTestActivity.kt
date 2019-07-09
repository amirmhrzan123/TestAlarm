package com.example.sialarm.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sialarm.R
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.auth.FirebaseAuth
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference



class NotificationTestActivity:AppCompatActivity() {

    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initFCM()
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            token = instanceIdResult.token
        }
        sendRegistrationToServer(token)
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