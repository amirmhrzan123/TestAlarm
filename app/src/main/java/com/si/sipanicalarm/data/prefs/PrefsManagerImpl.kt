package com.si.sipanicalarm.data.prefs

import android.content.SharedPreferences

class PrefsManagerImpl(private val pref: SharedPreferences) : PrefsManager {
    override fun isProfileComplete(): Boolean = pref.profileComplete

    override fun setProfileComplete(profile: Boolean) {
        pref.profileComplete = profile
    }

    override fun finishTutorial(): Boolean = pref.tutorial

    override fun setFirstTutorial(tutorial: Boolean) {
        pref.tutorial = tutorial
    }

    override fun isFirstTime(): Boolean = pref.firstTime


    override fun setFirstTime(firstTime: Boolean) {
        pref.firstTime = firstTime
    }

    override fun setPinCode(pinCode: Boolean) {
        pref.pinCodeSet = pinCode
    }

    override fun isPinCodeSet(): Boolean = pref.pinCodeSet

    override fun setPinCodeNumber(number: String) {
        pref.pinCodeNumber = number
    }

    override fun getPinCodeNumber(): String = pref.pinCodeNumber

    override fun getDeviceName(): String {
        return pref.deviceName
    }

    override fun setDeviceName(device: String) {
        pref.deviceName = device
    }

    override fun isAdmin(): Boolean = pref.admin

    override fun setAdmin(admin: Boolean) {
        pref.admin = admin
    }

    override fun setInstruction(status: Boolean) {
        pref.instructionStatus = status
    }

    override fun getInstruction() = pref.instructionStatus

    override fun setDistrict(district: String) {
        pref.district = district
    }

    override fun getDistrict(): String =  pref.district

    override fun setTole(tole: String) {
        pref.tole = tole
    }

    override fun getTole(): String = pref.tole

    override fun setState(state: String) {
        pref.state = state
    }

    override fun getState(): String = pref.state

    override fun setWardNo(ward: String) {
        pref.wardNumber = ward
    }

    override fun getWardNo(): String = pref.wardNumber

    override fun setEmail(email: String) {
        pref.email = email
    }

    override fun getEmail(): String = pref.email

    override fun setUserImage(image: String) {
        pref.userImage = image
    }

    override fun getUserImage(): String {
        return pref.userImage
    }

    override fun setPreSetupComplete(boolean: Boolean) {
        pref.preSetupComplete = boolean
    }

    override fun getPreSetupComplete(): Boolean = pref.preSetupComplete

    override fun setDeviceId(deviceId: String) {
        pref.deviceId = deviceId
    }

    override fun getDeviceId(): String = pref.deviceId


    override fun clear() {
        pref.edit().clear().commit()
    }
    override fun getPhoneNumber(): String = pref.phoneNumber

    override fun setPhoneNumber(number: String) {
        pref.phoneNumber = number
    }



    override fun setLoginStatus(status: Boolean) {
        pref.isLogin = status
    }

    override fun isLogin(): Boolean = pref.isLogin

    companion object {
        const val TOLE = "tole"
        const val WARDNUMBER = "wardNumber"
        const val STATE = "state"
        const val EMAIL = "email"
        const val USERID = "userId"
        const val USERNAME = "userName"
        const val LOGINSTATUS = "loginStatus"
        const val PHONENUMBER = "phoneNumber"
        const val USERIMAGE = "userImage"
        const val DEVICE_ID = "device_id"
        const val PRE_SETUP_COMPLETE = "pre_setup_complete"
        const val DISTRICT = "district"
        const val INSTRUCTIONSTATUS = "instructionStatus"
        const val DEVICETYPE = "deviceType"
        const val ADMIN = "admin"
        const val DEVICENAME = "deviceName"
        const val PINCODESET = "pinCodeSet"
        const val PINCODENUMBER = "pinCodeNumber"
        const val FIRSTTIME = "firstTime"
        const val TUTORIALDONE = "tutorialDone"
        const val PROFILECOMPLETE = "profileComplete"
    }

    override fun setUserId(userId: String) {
        pref.userId = userId
    }

    override fun getUserId(): String = pref.userId

    override fun setUserName(userName: String) {
        pref.userName = userName
    }

    override fun getUserName(): String = pref.userName

    private var SharedPreferences.pinCodeSet
        get() = getBoolean(PINCODESET, false)
        set(value) {
            putValue(PINCODESET, value)
        }


    private var SharedPreferences.pinCodeNumber
        get() = getString(PINCODENUMBER, "")
        set(value) {
            putValue(PINCODENUMBER, value)
        }

    private var SharedPreferences.firstTime
        get() = getBoolean(FIRSTTIME, true)
        set(value) {
            putValue(FIRSTTIME, value)
        }

    private var SharedPreferences.admin
        get() = getBoolean(ADMIN, false)
        set(value) {
            putValue(ADMIN, value)
        }


    private var SharedPreferences.deviceId
        get()= getString(DEVICE_ID,"")
        set(value){
            putValue(DEVICE_ID,value)
        }

    private var SharedPreferences.deviceName
        get() = getString(DEVICENAME,"")
        set(value){
            putValue(DEVICENAME,value)
        }


    private var SharedPreferences.userId
        get() = getString(USERID, "")
        set(value) {
            putValue(USERID, value)
        }

    private var SharedPreferences.userName
        get() = getString(USERNAME, "")
        set(value) {
            putValue(USERNAME, value)
        }
    private var SharedPreferences.isLogin
        get() = getBoolean(LOGINSTATUS, false)
        set(value) {
            putValue(LOGINSTATUS, value)
        }


    private var SharedPreferences.userImage
        get() = getString(USERIMAGE,"")
        set(value){
            putValue(USERIMAGE,value)
        }



    private var SharedPreferences.phoneNumber
        get() = getString(PHONENUMBER, "")
        set(value) {
            putValue(PHONENUMBER, value)
        }

    private var SharedPreferences.preSetupComplete
        get() = getBoolean(PRE_SETUP_COMPLETE,false)
        set(value){
            putValue(PRE_SETUP_COMPLETE,value)
        }

    private var SharedPreferences.email
        get() = getString(EMAIL,"")
        set(value){
            putValue(EMAIL,value)
        }

    private var SharedPreferences.tole
        get() = getString(TOLE,"")
        set(value){
            putValue(TOLE,value)
        }

    private var SharedPreferences.wardNumber
        get() = getString(WARDNUMBER,"")
        set(value){
            putValue(WARDNUMBER,value)
        }

    private var SharedPreferences.instructionStatus
        get() = getBoolean(INSTRUCTIONSTATUS,false)
        set(value){
            putValue(INSTRUCTIONSTATUS,value)
        }


    private var SharedPreferences.state
        get() = getString(STATE,"")
        set(value){
            putValue(STATE,value)
        }

    private var SharedPreferences.district
        get() = getString(DISTRICT,"")
        set(value){
            putValue(DISTRICT,value)
        }

    private var SharedPreferences.tutorial
        get() = getBoolean(TUTORIALDONE,false)
        set(value){
            putValue(TUTORIALDONE,value)
        }

    private var SharedPreferences.profileComplete
        get() = getBoolean(PROFILECOMPLETE,false)
        set(value){
            putValue(PROFILECOMPLETE,value)
        }


    private fun SharedPreferences.putValue(key: String, value: Any?) {
        when (value) {
            is String -> edit().putString(key, value).apply()
            is Boolean -> edit().putBoolean(key, value).apply()
            is Int -> edit().putInt(key, value).apply()
            is Float -> edit().putFloat(key, value).apply()
            else -> UnsupportedOperationException("Not yet implemented.")
        }
    }

}