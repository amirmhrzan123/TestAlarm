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
    fun setUserImage(image:String)
    fun getUserImage():String
    fun setLoginStatus(status: Boolean)
    fun isLogin(): Boolean
    fun setLoginAs(loginAs: Int)
    fun getLoginAs(): Int
    fun getPhoneNumber(): String
    fun setPhoneNumber(number: String)
    fun clear()
    fun isDeviceRegistered(): Boolean
    fun setIsDeviceRegistered(boolean: Boolean)
    fun setDeviceId(deviceId:String)
    fun getDeviceId():String
    fun setPreSetupComplete(boolean:Boolean)
    fun getPreSetupComplete():Boolean
    fun setTole(tole:String)
    fun getTole():String
    fun setState(state:String)
    fun getState():String
    fun setWardNo(ward: String)
    fun getWardNo():String
    fun setEmail(email:String)
    fun getEmail():String
    fun setDistrict(district:String)
    fun getDistrict():String

}