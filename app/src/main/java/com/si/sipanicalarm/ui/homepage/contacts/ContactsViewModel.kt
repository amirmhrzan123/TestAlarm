package com.si.sipanicalarm.ui.homepage.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.si.sipanicalarm.base.BaseViewModel
import com.si.sipanicalarm.data.api.AcceptDenyRequestModel
import com.si.sipanicalarm.data.firebase.Friends
import com.si.sipanicalarm.utils.AbsentLiveData
import com.si.sipanicalarm.utils.Resource
import javax.xml.transform.TransformerFactory

class ContactsViewModel constructor(private val repository:ContactsRepository):BaseViewModel<IContactsInterface>() {

    var contactNumber =  ""
    var contactName = ""
    var insertContactValid = MutableLiveData<Boolean>()
    var getContactsValid = MutableLiveData<Boolean>()
    var acceptDenyValid = MutableLiveData<AcceptDenyRequestModel>()
    var listContacts :  MutableList<Friends> = mutableListOf()



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

    val acceptDenyInvitation : LiveData<Resource<String>> = Transformations
        .switchMap(acceptDenyValid){
            repository.acceptDenyInvitation(acceptDenyRequestModel = it)
        }


}