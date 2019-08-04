package com.example.sialarm.ui.myProfile

import android.net.Uri
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.utils.FirebaseData
import com.example.sialarm.utils.Resource
import com.example.sialarm.utils.SingleLiveEvent
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MyProfileViewModel constructor(private val repository:MyProfileRepository,
                                     private val firebaseData: FirebaseDatabase,
                                     private val viewModelscope:CoroutineScope,
                                     private val prefs:PrefsManager):BaseViewModel<IMyProfileNavigator>() {


    val firebaseStorage:StorageReference by lazy{
        FirebaseStorage.getInstance()
            .reference
    }

    var selectedWardId = 0

    var selectedStateId = 0


    val progress = ObservableField<Int>()

    val visible = ObservableField<Boolean>()

    var imageurl = ObservableField<String>("")

    var userObserver = MutableLiveData<Users>()



    fun onImageClicked(){
        getNavigator().onImageClicked()
    }

    fun onStateClicked(){
        getNavigator().onStateClicked()
    }

    fun onWardClicked(){
        getNavigator().onWardClicked()
    }

    fun onDistrictClicked(){
        getNavigator().onDistrictClicked()
    }

    fun onUpdateClicked(){
        getNavigator().onUpdateClicked()
    }



     val updateProfile:LiveData<Resource<String>> = Transformations
        .switchMap(userObserver){user->
            repository.updateProfile(user)

        }


    fun uploadImage(uri: Uri?) {
        visible.set(true)
        viewModelscope.launch {
            try{
                firebaseStorage.child("image/${uri?.lastPathSegment}").putFile(uri!!)
                    .addOnProgressListener {
                        val progresses = 100.0 * it.bytesTransferred / it
                            .totalByteCount
                        progress.set(progresses.toInt())
                    }
                    .addOnCanceledListener {
                        visible.set(false)

                    }
                    .addOnSuccessListener{
                        firebaseStorage.child("image/${uri?.lastPathSegment}").downloadUrl
                            .addOnSuccessListener {
                                it.toString()
                                visible.set(false)
                                var url = it.toString()
                                if(prefs.getUserImage().isNotEmpty()){
                                    val storage = FirebaseStorage.getInstance().getReferenceFromUrl(prefs.getUserImage())
                                    storage.delete().addOnSuccessListener {
                                        println("SUCCESS")
                                        firebaseData.getReference("users").child(prefs.getUserId()).child("image").setValue(url)
                                        imageurl.set(url)

                                    }.addOnFailureListener {
                                        println("FAILURE"+it.message)
                                        imageurl.set(url)
                                        firebaseData.getReference("users").child(prefs.getUserId()).child("image").setValue(url)

                                    }
                                }else{
                                    imageurl.set(url)
                                    firebaseData.getReference("users").child(prefs.getUserId()).child("image").setValue(it.toString())
                                }
                            }
                    }
            }catch(e:Exception){

            }
        }

    }

}