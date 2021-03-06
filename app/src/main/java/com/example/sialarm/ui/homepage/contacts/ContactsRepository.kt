package com.example.sialarm.ui.homepage.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sialarm.data.api.AcceptDenyRequestModel
import com.example.sialarm.data.api.ApiServices
import com.example.sialarm.data.api.SendFriendRequest
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.utils.FireKey
import com.example.sialarm.utils.Resource
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

                }

                override fun onDataChange(p0: DataSnapshot) {

                    val friendDatabase = firebaseDatabase.getReference(FireKey.FRIENDS)
                    if(p0.exists()){
                        val user = p0.getValue(Users::class.java)
                        val key = friendDatabase.child(user!!.id).push().key
                        receiverId = user.id
                        friendDatabase.child(user.id).child(key.toString())
                            .setValue(Friends(prefs.getUserId(),
                                prefs.getPhoneNumber(),
                                3,
                                false,
                                prefs.getUserName(),
                                true)).addOnCompleteListener {
                                //insert into the user who send request
                                val friendKey = firebaseDatabase.getReference(FireKey.FRIENDS).child(prefs.getUserId()).push().key
                                friendDatabase.child(prefs.getUserId()).child(friendKey.toString())
                                    .setValue(Friends(user.id,
                                        user.phone_number,
                                        2,
                                        false,
                                        user.username,
                                        false))
                                    .addOnCompleteListener {
                                        friendsResponse.postValue(Resource.success("","",null,null))
                                    }
                            }

                    }else{
                        val key = firebaseDatabase.getReference(FireKey.USERS).push().key.toString()
                        firebaseDatabase.getReference(FireKey.USERS).child(key)
                            .setValue(Users(id = key,active = false,
                                phone_number = number,username = userName)).addOnCompleteListener {
                                val friendKey = friendDatabase.child(key).push().key.toString()
                                        friendDatabase.child(key).child(friendKey)
                                    .setValue(Friends(prefs.getUserId(),
                                        prefs.getPhoneNumber(),
                                        3,
                                        false,
                                        prefs.getUserName(),
                                        false)).addOnCompleteListener {
                                        val nextKey = firebaseDatabase.getReference(FireKey.FRIENDS).child(prefs.getUserId()).push().key
                                        friendDatabase.child(prefs.getUserId()).child(nextKey.toString())
                                            .setValue(Friends(key,
                                                number,
                                                2,
                                                false,
                                                userName,
                                                true))
                                            .addOnCompleteListener {
                                                friendsResponse.postValue(Resource.success("","",null,null))
                                            }
                                    }

                            }
                        receiverId = key

                    }
                    viewModelScope.launch {
                        try{
                            friendsResponse.postValue(Resource.loading(null))
                            val response = apiServices.sendFriendRequest(sendFriendRequest = SendFriendRequest(prefs.getUserId(),receiverId,"3",prefs.getUserName()) )
                            friendsResponse.postValue(Resource.success("","",null))

                        }catch(e:Exception){
                            friendsResponse.postValue(Resource.error("","",null))
                        }
                    }
                }



            })

        return friendsResponse
    }

    fun getFriendsList():LiveData<Resource<List<Friends>>>{
        val friendsListResponse = MutableLiveData<Resource<List<Friends>>>()
        friendsListResponse.postValue(Resource.loading(null))
        firebaseDatabase.getReference(FireKey.FRIENDS).child(prefs.getUserId()).addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                listContacts.clear()
                if(p0.exists()){
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
        val taskMap = HashMap<String, Any>()
        if(acceptDenyRequestModel.accept){
            taskMap["status"]=1
            taskMap["notification"]=true
            taskMap["message"]=false
        }else{
            taskMap["status"]=4
            taskMap["notification"]=false
            taskMap["message"]=false
        }
        firebaseDatabase.getReference("friends").child(acceptDenyRequestModel.sender_id).child(acceptDenyRequestModel.receiver_id).updateChildren(taskMap)
        firebaseDatabase.getReference("friends").child(acceptDenyRequestModel.receiver_id).child(acceptDenyRequestModel.sender_id).updateChildren(taskMap)
        viewModelScope.launch {
            try{
                acceptDenyResponse.postValue(Resource.loading(null))
                val response = apiServices.acceptDenyInvitation(acceptDenyRequestModel).await()
                acceptDenyResponse.postValue(Resource.success("","",response))

            }catch(e:Exception){
                acceptDenyResponse.postValue(Resource.error("","",""))
            }
        }
        return acceptDenyResponse
    }

}