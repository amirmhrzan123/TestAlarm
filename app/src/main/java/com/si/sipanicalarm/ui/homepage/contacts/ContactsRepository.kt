package com.si.sipanicalarm.ui.homepage.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.si.sipanicalarm.R
import com.si.sipanicalarm.data.api.AcceptDenyRequestModel
import com.si.sipanicalarm.data.api.ApiServices
import com.si.sipanicalarm.data.api.SendFriendRequest
import com.si.sipanicalarm.data.firebase.Friends
import com.si.sipanicalarm.data.firebase.Users
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.utils.FireKey
import com.si.sipanicalarm.utils.Resource
import com.si.sipanicalarm.utils.extensions.handleException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception



class ContactsRepository constructor(private val firebaseDatabase: FirebaseDatabase,
                                     private val viewModelScope:CoroutineScope,
                                     private val prefs:PrefsManager,
                                     private val apiServices: ApiServices){

    var listContacts: MutableList<Friends> = mutableListOf()

    fun insertFriends(userName:String,number:String):LiveData<Resource<List<Friends>>>{
        val friendsResponse = MutableLiveData<Resource<List<Friends>>>()
        friendsResponse.postValue(Resource.loading(null))
        var receiverId = ""
        firebaseDatabase.getReference(FireKey.USERS).orderByChild("phone_number").equalTo(number)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    friendsResponse.postValue(Resource.error("",p0.message,null))

                }
                override fun onDataChange(p0: DataSnapshot) {
                    val friendDatabase = firebaseDatabase.getReference(FireKey.FRIENDS)
                    if(p0.exists()){
                        var user:Users?=null
                        for(data in p0.children){
                            user = data.getValue(Users::class.java)
                        }
                        receiverId = user!!.phone_number
                        friendDatabase.child(user.phone_number).child(prefs.getUserId())
                            .setValue(Friends(prefs.getUserId(),
                                prefs.getPhoneNumber(),
                                3,
                                false,
                                prefs.getUserName(),
                                true)).addOnCompleteListener {
                                //insert into the user who send request
                                friendDatabase.child(prefs.getUserId()).child(number)
                                    .setValue(Friends(user.id,
                                        user.phone_number,
                                        2,
                                        false,
                                        user.username,
                                        false))
                                    .addOnCompleteListener {
                                        friendsResponse.postValue(Resource.success("","",null,null))
                                    }
                                    .addOnFailureListener {
                                        friendsResponse.postValue(Resource.error("","",null))
                                    }
                            }

                    }else{
                        firebaseDatabase.getReference(FireKey.USERS).child(number)
                            .setValue(Users(id = number,active = false,
                                phone_number = number,username = userName)).addOnCompleteListener {
                                        friendDatabase.child(number).child(prefs.getUserId())
                                    .setValue(Friends(prefs.getUserId(),
                                        prefs.getPhoneNumber(),
                                        3,
                                        false,
                                        prefs.getUserName(),
                                        false)).addOnCompleteListener {
                                        friendDatabase.child(prefs.getUserId()).child(number)
                                            .setValue(Friends(number,
                                                number,
                                                2,
                                                false,
                                                userName,
                                                true))
                                            .addOnCompleteListener {
                                                friendsResponse.postValue(Resource.success("","",null,null))
                                            }
                                            .addOnFailureListener {
                                                friendsResponse.postValue(Resource.error("","",null))
                                            }
                                    }

                            }
                        receiverId = number

                    }
                    viewModelScope.launch {
                        try{
                            friendsResponse.postValue(Resource.loading(null))
                            val response = apiServices.sendFriendRequest(sendFriendRequest = SendFriendRequest(prefs.getUserId(),receiverId,"3",prefs.getUserName()) )
                            friendsResponse.postValue(Resource.success("","",null))

                        }catch(e:Exception){
                            e.handleException(""){
                                friendsResponse.postValue(Resource.error("",it.message,null))

                            }
                        }
                    }
                }



            })

        return friendsResponse
    }

    fun getFriendsList():LiveData<Resource<List<Friends>>>{
        val friendsListResponse = MutableLiveData<Resource<List<Friends>>>()
        friendsListResponse.postValue(Resource.loading(null))
        println("number"+prefs.getUserId())
        firebaseDatabase.getReference(FireKey.FRIENDS).child(prefs.getUserId()).addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                friendsListResponse.postValue(Resource.error("",p0.message,null))

            }

            override fun onDataChange(p0: DataSnapshot) {
                listContacts.clear()
                if(p0.exists()){
                    println(p0.children.count())
                    for(data in p0.children){
                        val friend : Friends? = data.getValue(Friends::class.java)
                        listContacts.add(friend!!)
                    }
                    friendsListResponse.postValue(Resource.success("","",listContacts,null))
                }else{
                    friendsListResponse.postValue(Resource.success("","",listContacts,null))
                }
            }

        })

        return friendsListResponse
    }

    fun acceptDenyInvitation(acceptDenyRequestModel: AcceptDenyRequestModel):LiveData<Resource<String>>{
        val acceptDenyResponse = MutableLiveData<Resource<String>>()

        viewModelScope.launch {
            try{
                acceptDenyResponse.postValue(Resource.loading(null))
                val response = apiServices.acceptDenyInvitation(acceptDenyRequestModel).await()
                acceptDenyResponse.postValue(Resource.success("","",response))

            }catch(e:Exception){
                e.handleException(""){
                    acceptDenyResponse.postValue(Resource.error("",it.message,""))
                }
            }
        }
        return acceptDenyResponse
    }

}