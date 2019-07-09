package com.example.sialarm.ui.homepage.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class ContactsRepository constructor(private val firebaseDatabase: FirebaseDatabase,
                                     private val viewModelScope:CoroutineScope,
                                     private val prefs:PrefsManager){

    var listContacts: MutableList<Friends> = mutableListOf()


    fun insertFriends(userName:String,number:String):LiveData<Resource<List<Friends>>>{
        val friendsResponse = MutableLiveData<Resource<List<Friends>>>()
        friendsResponse.postValue(Resource.loading(null))
        firebaseDatabase.getReference(FireKey.USERS).orderByChild("phone_number").equalTo(number)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val friendDatabase = firebaseDatabase.getReference(FireKey.FRIENDS)
                    if(p0.exists()){
                        val user = p0.getValue(Users::class.java)
                        val key = friendDatabase.child(user!!.id).push().key
                        friendDatabase.child(user.id).child(key.toString())
                            .setValue(Friends(prefs.getUserId(),prefs.getPhoneNumber(),3,false,prefs.getUserName())).addOnCompleteListener {
                                val friendKey = firebaseDatabase.getReference(FireKey.FRIENDS).child(prefs.getUserId()).push().key
                                friendDatabase.child(prefs.getUserId()).child(friendKey.toString())
                                    .setValue(Friends(user.id,user.phone_number,2,false,user.username))
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
                                    .setValue(Friends(prefs.getUserId(),prefs.getPhoneNumber(),3,false,prefs.getUserName())).addOnCompleteListener {
                                        val nextKey = firebaseDatabase.getReference(FireKey.FRIENDS).child(prefs.getUserId()).push().key
                                        friendDatabase.child(prefs.getUserId()).child(nextKey.toString())
                                            .setValue(Friends(key,number,2,false,userName))
                                            .addOnCompleteListener {
                                                friendsResponse.postValue(Resource.success("","",null,null))
                                            }
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

}