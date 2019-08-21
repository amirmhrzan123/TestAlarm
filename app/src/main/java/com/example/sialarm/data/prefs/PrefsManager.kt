package com.example.sialarm.data.prefs

interface PrefsManager {

    fun setUserId(userId: String)
    fun getUserId(): String
    fun setUserName(userName: String)
    fun getUserName(): String
    fun setUserImage(image:String)
    fun getUserImage():String
    fun setLoginStatus(status: Boolean)
    fun isLogin(): Boolean

    fun getPhoneNumber(): String
    fun setPhoneNumber(number: String)
    fun clear()

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
    fun setInstruction(status:Boolean)
    fun getInstruction():Boolean
    fun getDeviceName():String
    fun setDeviceName(device:String)
    fun isAdmin():Boolean
    fun setAdmin(admin:Boolean)
    fun setPinCode(pinCode:Boolean)
    fun isPinCodeSet():Boolean
    fun setPinCodeNumber(number:String)
    fun getPinCodeNumber():String
    fun isFirstTime():Boolean
    fun setFirstTime(firstTime:Boolean)

}