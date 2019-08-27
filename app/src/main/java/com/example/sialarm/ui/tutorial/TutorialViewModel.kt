package com.example.sialarm.ui.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.data.api.AcceptDenyRequestModel
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.ui.homepage.contacts.ContactsRepository
import com.example.sialarm.ui.homepage.home.HomeRepository
import com.example.sialarm.utils.AbsentLiveData
import com.example.sialarm.utils.Resource
import com.example.sialarm.utils.SingleLiveEvent

class TutorialViewModel constructor(private val contactRepository: ContactsRepository,
                                    private val sendAlertRepository: HomeRepository):BaseViewModel<ITutorialNavigator>() {


    var contactNumber =  ""
    var contactName = ""
    var insertContactValid = MutableLiveData<Boolean>()
    var contactTrigger = SingleLiveEvent<Boolean>()

    var getContactsValid = MutableLiveData<Boolean>()
    var acceptDenyValid = MutableLiveData<AcceptDenyRequestModel>()
    var listContacts :  MutableList<Friends> = mutableListOf()
    var latitude = ""
    var longitude = ""
    val sendALertValid = MutableLiveData<Boolean>()

    val sendAlertMessages: LiveData<Resource<String>>  = Transformations
            .switchMap(sendALertValid){sendALertValid->
                if(sendALertValid){
                    sendAlertRepository.sendAlertToAll(latitude,longitude,true)
                }else{
                    AbsentLiveData.create()
                }
            }

    fun sendLocationUpdates( latitudes:String, longitudes:String){
        sendAlertRepository.sendUptoDateLatLong(latitudes,longitudes)
    }

    val getContacts : LiveData<Resource<List<Friends>>> = Transformations
            .switchMap(getContactsValid){ getContactsValid->
                if(getContactsValid){
                    contactRepository.getFriendsList()
                }else{
                    AbsentLiveData.create()
                }

            }



    val insertContacts  : LiveData<Resource<List<Friends>>> = Transformations
            .switchMap(insertContactValid){insertContactValid->
                if(insertContactValid){
                    contactRepository.insertFriends(contactName,contactNumber)
                }else{
                    AbsentLiveData.create()
                }

            }

}