package com.example.sialarm.ui.homepage.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.databinding.ItemAcceptedBinding
import com.example.sialarm.databinding.ItemDenyBinding
import com.example.sialarm.databinding.ItemPendingBinding
import com.example.sialarm.databinding.ItemRequestBinding


class ContactsAdapter constructor(
    private val contacts: MutableList<Friends> = mutableListOf<Friends>()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ACCEPTED = 1
        const val PENDING = 2
        const val REQUEST = 3
        const val DENY = 4

    }
    lateinit var listener : ContactClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ACCEPTED->{
                AcceptedViewHolder(ItemAcceptedBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            PENDING->{
                PendingViewHolder(ItemPendingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            REQUEST->{
                RequestViewHolder(ItemRequestBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            DENY->{
                DenyViewHolder(ItemDenyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
            else -> {
                DenyViewHolder(ItemDenyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
       return when(contacts[position].status){
            1-> ACCEPTED
            2-> PENDING
            3-> REQUEST
            4-> DENY
           else -> DENY
       }
    }

    override fun getItemCount(): Int {
       return contacts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
           is AcceptedViewHolder->holder.onBind(position)
           is PendingViewHolder->holder.onBind(position)
           is RequestViewHolder->holder.onBind(position)
           is DenyViewHolder->holder.onBind(position)
        }
    }

    inner class PendingViewHolder constructor(
        private val itemPendingBinding: ItemPendingBinding) :
        RecyclerView.ViewHolder(itemPendingBinding.root) {

        fun onBind(position: Int) {
            with(itemPendingBinding) {
                model = contacts[position]
            }
        }
    }

    inner class AcceptedViewHolder constructor(
        private val itemAcceptedBinding: ItemAcceptedBinding) :
        RecyclerView.ViewHolder(itemAcceptedBinding.root) {
        fun onBind(position: Int) {
            with(itemAcceptedBinding) {
                model = contacts[position]
            }
        }
    }

    inner class DenyViewHolder constructor(
        private val itemDenyBinding: ItemDenyBinding) :
        RecyclerView.ViewHolder(itemDenyBinding.root) {
        fun onBind(position: Int) {
            with(itemDenyBinding) {
                model = contacts[position]

            }
        }
    }

    inner class RequestViewHolder constructor(
        private val itemRequestBinding: ItemRequestBinding) :
        RecyclerView.ViewHolder(itemRequestBinding.root) {

        fun onBind(position: Int) {
            with(itemRequestBinding) {
                model = contacts[position]
            }
        }
    }

    fun setFriendsList(list:List<Friends>){
        contacts.clear()
        contacts.addAll(list)
        notifyDataSetChanged()
    }

    interface ContactClickListener{
        fun onAcceptDeniedClicked(contact:Friends,accept:Boolean)
        fun onFriendsContact(contact:Friends)
    }

}
