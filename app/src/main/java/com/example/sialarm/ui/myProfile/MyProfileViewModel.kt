package com.example.sialarm.ui.myProfile

import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.utils.FirebaseData
import com.google.firebase.database.FirebaseDatabase

class MyProfileViewModel constructor(private val repository:MyProfileRepository,
                                     private val firebaseData: FirebaseDatabase):BaseViewModel<IMyProfileNavigator>() {



    fun onImageClicked(){
        getNavigator().onImageClicked()
    }
}