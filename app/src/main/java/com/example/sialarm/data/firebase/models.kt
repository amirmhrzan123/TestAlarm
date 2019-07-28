package com.example.sialarm.data.firebase

import androidx.core.content.ContextCompat
import androidx.room.Entity
import com.example.sialarm.R
import com.example.sialarm.utils.FriendStatus
import com.google.firebase.database.DataSnapshot

data class Users(
    val active: Boolean=false,
    val address:String="",
    val email:String="",
    val id:String="",
    val latitude:String="",
    val longitude:String="",
    val notification_token:String="",
    val phone_number:String="",
    val username:String="",
    val device:String="",
    val timeStamp:Long=0
)

data class PhoneNumber(
    val id:String,
    val number:String
)



data class Friends( val id:String="",
                    val number:String="",
                    val status:Int=0,
                    val message:Boolean=false,
                    val name:String="",
                    val notification:Boolean = false){



    fun getFriendStatusColor():Int{
        with(FriendStatus){
            return when(status){
                FRIEND->{
                    R.drawable.bg_friends
                }
                REQUEST_FOR_FRIEND->{
                    R.drawable.bg_pending
                }
                BEING_REQUESTED->{
                    R.drawable.bg_request
                }
                else -> {
                    R.drawable.bg_request
                }
            }
        }
    }

    fun getFriendsText():String{
        with(FriendStatus){
            return when(status){
                FRIEND->{
                    "Friend"
                }
                BEING_REQUESTED->{
                    "Request"
                }
                REQUEST_FOR_FRIEND->{
                    "Pending"
                }
                else->{
                    ""
                }
            }
        }

    }
}

