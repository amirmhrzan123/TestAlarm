package com.example.sialarm.utils

class FireKey {
    companion object {
        val USERS = "users"
        val DEVICE = "device"
        val FRIENDS = "friends"
        val PHONENUMBER = "phone_number"
    }
}

class Navigation{
    companion object {
        val LANDING = 1
        val TUTORIAL = 2
        val PROFILE = 3
        val HOME = 4
    }
}

class FriendStatus{
    companion object {
        val FRIEND = 1
        val REQUEST_FOR_FRIEND = 2
        val BEING_REQUESTED = 3
        val DECLINE_REQUEST = 4
        val BEING_DECLINE = 5
        val BLOCK = 6
        val BEING_BLOCK = 7
        val UNFRIEND = 8
    }
}

class Extras{
    companion object {
        val FROMNOTIFICATION = "fromNotification"
    }
}