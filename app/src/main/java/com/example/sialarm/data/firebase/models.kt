package com.example.sialarm.data.firebase

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

data class Friends(
    val id:String="",
    val number:String="",
    val status:Int=0,
    val message:Boolean=false,
    val name:String="",
    val notification:Boolean = false
)

