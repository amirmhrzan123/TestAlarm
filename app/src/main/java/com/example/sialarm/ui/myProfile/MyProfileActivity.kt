package com.example.sialarm.ui.myProfile

import android.os.Bundle
import com.app.dwell.base.BaseActivity
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.databinding.ActivityMyProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyProfileActivity: BaseActivity<MyProfileViewModel,ActivityMyProfileBinding>(){

    private val myProfileViewModel : MyProfileViewModel by viewModel()



    override fun getLayoutId(): Int = R.layout.activity_my_profile

    override fun getViewModel(): MyProfileViewModel = myProfileViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}