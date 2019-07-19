package com.example.sialarm.data.api

data class AcceptDenyRequestModel(
    val sender_id:String,
    val receiver_id:String,
    val notification_type_id:String,
    val accept:Boolean,
    val senderUserName:String)


data class SendAlertMessages(
    val sender_id:String,
    val userName:String
)

data class SendFriendRequest(
    val sender_id:String,
    val receiver_id:String,
    val notification_type_id:String,
    val userName:String
)