package com.example.sialarm.data.firebase

import com.example.sialarm.utils.FirebaseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


/**
 * Created by Amir on 10/13/2018.
 */
object SessionRepository {

    val userInfo: DatabaseReference by lazy {
        FirebaseDatabase.getInstance()
                .getReference("users")
    }

    val firebaseDataUserInfo =
        FirebaseData(userInfo)

    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    val uid: String?
        get() = currentUser?.uid

    val isLogin: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    fun isSelf(uid: String) = SessionRepository.uid == uid

    fun findUser(uid: String): Users? {
        return firebaseDataUserInfo.snapshots.find { it.key == uid }?.getValue(Users::class.java)
    }

}