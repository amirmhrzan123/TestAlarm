package com.example.sialarm.data.prefs

interface PrefsManager {
    fun getAccessToken(): String
    fun setAccessToken(token: String)
    fun setRefreshToken(refreshToken: String)
    fun getRefreshToken(): String
    fun setUserId(userId: String)
    fun getUserId(): String
    fun setUserName(userName: String)
    fun getUserName(): String
    fun setLoginStatus(status: Boolean)
    fun isLogin(): Boolean
    fun setLoginAs(loginAs: Int)
    fun getLoginAs(): Int
    fun getPhoneNumber(): String
    fun setPhoneNumber(number: String)
    fun setCompletedStepCount(pos: Int)
    fun getCompletedStepCount(): Int
    fun setCompletedSubStepCount(pos:Int)
    fun getCompletedSubStepCount():Int
    fun clear()
    fun isDeviceRegistered(): Boolean
    fun setIsDeviceRegistered(boolean: Boolean)
    fun setEnableSubscrition(enable:Boolean)
    fun getEnableSubscription():Boolean
    fun setDeviceId(deviceId:String)
    fun getDeviceId():String
    fun setPreSetupComplete(boolean:Boolean)
    fun getPreSetupComplete():Boolean
}