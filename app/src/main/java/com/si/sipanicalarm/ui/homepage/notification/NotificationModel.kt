package com.si.sipanicalarm.ui.homepage.notification

import com.si.sipanicalarm.utils.extensions.getMessageDateTime
import java.sql.Timestamp

data class NotificationResponseModel(
    val title: String= "",
    val message:String="",
    val notification_type_id:String="",
    val sender_id:String="",
    val timeStamp: Long=0,
    val latitude:String?="",
    val longitude:String?="",
    val link:String?=""
){
    fun getDateTime():String{
        println(timeStamp.toString())
        return timeStamp.getMessageDateTime()
    }
}