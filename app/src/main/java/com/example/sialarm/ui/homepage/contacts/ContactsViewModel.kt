package com.example.sialarm.ui.homepage.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.sialarm.base.BaseViewModel
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.utils.AbsentLiveData
import com.example.sialarm.utils.Resource

class ContactsViewModel constructor(private val repository:ContactsRepository):BaseViewModel<IContactsInterface>() {

    var contactNumber =  ""
    var contactName = ""
    var insertContactValid = MutableLiveData<Boolean>()
    var getContactsValid = MutableLiveData<Boolean>()



    val insertContacts  : LiveData<Resource<List<Friends>>> = Transformations
        .switchMap(insertContactValid){insertContactValid->
            if(insertContactValid){
                repository.insertFriends(contactName,contactNumber)
            }else{
                AbsentLiveData.create()
            }

        }


    val getContacts : LiveData<Resource<List<Friends>>> = Transformations
        .switchMap(getContactsValid){ getContactsValid->
            if(getContactsValid){
                repository.getFriendsList()
            }else{
                AbsentLiveData.create()
            }

        }


}