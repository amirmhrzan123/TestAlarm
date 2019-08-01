package com.example.sialarm.data.api

data class AcceptDenyRequestModel(
    val sender_id:String,
    val receiver_id:String,
    val notification_type_id:String,
    val status:Int,
    val senderUserName:String)


data class SendAlertMessages(
    var sender_id:String="",
    var userName:String="",
    var latitude:String="",
    var longitude:String=""
)

data class SendFriendRequest(
    val sender_id:String,
    val receiver_id:String,
    val notification_type_id:String,
    val userName:String
)