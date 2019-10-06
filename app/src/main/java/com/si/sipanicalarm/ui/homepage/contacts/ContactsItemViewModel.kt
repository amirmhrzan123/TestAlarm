package com.si.sipanicalarm.ui.homepage.contacts

import android.content.res.Resources
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.si.sipanicalarm.data.firebase.Friends
import com.si.sipanicalarm.utils.FriendStatus

class ContactsItemViewModel constructor(private val friends:Friends, private val friendListener: FriendClickListener){

    lateinit var listenerTypes: FriendClickListener
    val friend = ObservableField<Friends>()
    val hasLeftMenu = ObservableBoolean()
    val hasRightMenu = ObservableBoolean()
    val acceptEnable = ObservableBoolean()
    val declineEnable = ObservableBoolean()
    val unfriendEnable = ObservableBoolean()
    val deleteEnable = ObservableBoolean()
    val messageEnable = ObservableBoolean()

    init {
        listenerTypes = friendListener
        hasRightMenu.set(true)
        with(FriendStatus){
            when(friends.status){
                FRIEND->{
                    unfriendEnable.set(true)
                    acceptEnable.set(false)
                    deleteEnable.set(false)
                    declineEnable.set(false)
                }
                REQUEST_FOR_FRIEND->{
                    unfriendEnable.set(false)
                    deleteEnable.set(true)
                    acceptEnable.set(false)
                    declineEnable.set(false)
                }
                BEING_REQUESTED->{
                    unfriendEnable.set(false)
                    deleteEnable.set(false)
                    acceptEnable.set(true)
                    declineEnable.set(true)
                }
                DECLINE_REQUEST, BEING_DECLINE, BEING_BLOCK, BLOCK, UNFRIEND->{
                    hasRightMenu.set(false)
                }
            }
        }

    }


    fun onAcceptClicked(){
        listenerTypes.onFriendClick(ListenerType.AcceptFriendRequestListener(friends))
    }

    fun onDeclineClicked(){
        listenerTypes.onFriendClick(ListenerType.RejectFriendRequestListener(friends))
    }

    fun onItemClick(){
        listenerTypes.onFriendClick(ListenerType.OnItemClickListener(friends))
    }

    fun onDeleteClick(){
        listenerTypes.onFriendClick(ListenerType.DeleteClickListener(friends))
    }

    fun onUnfriendClick(){
        listenerTypes.onFriendClick(ListenerType.UnfriendClickListener(friends))
    }

    sealed class ListenerType{
         class AcceptFriendRequestListener(val friend:Friends):ListenerType()
         class RejectFriendRequestListener(val friend:Friends):ListenerType()
         class OnItemClickListener(val friend:Friends):ListenerType()
         class UnfriendClickListener(val friend:Friends):ListenerType()
         class DeleteClickListener(val friend:Friends):ListenerType()
    }

    interface FriendClickListener{
        fun onFriendClick(clicked:ListenerType)
    }
}

