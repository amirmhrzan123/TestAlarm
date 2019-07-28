package com.example.sialarm.ui.homepage.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sialarm.R
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.databinding.ItemContactsBinding
import com.example.sialarm.ui.homepage.notification.NotificationAdaptor
import com.example.sialarm.utils.FriendStatus

class ContactFriendsAdapter(private val listFriends:MutableList<Friends> = mutableListOf()): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var friendClickListener: FriendClickListener

    fun setListener(context: FriendClickListener){
        friendClickListener = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemContactsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = listFriends.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(position)
    }

    inner class ViewHolder(private val itemContactsBinding: ItemContactsBinding):
        RecyclerView.ViewHolder(itemContactsBinding.root),ContactsItemViewModel.FriendClickListener{
        override fun onFriendClick(clicked: ContactsItemViewModel.ListenerType) {
            friendClickListener.onFriendClick(clicked)
        }

        fun onBind(position:Int){
            with(itemContactsBinding){
                model = listFriends[position]
                viewModel = ContactsItemViewModel(model!!,this@ViewHolder)
                when(model?.status){
                    FriendStatus.FRIEND->{
                        tvBackground.background = ContextCompat.getDrawable(root.context, R.drawable.bg_friends)
                        view.background = ContextCompat.getDrawable(root.context,R.drawable.item_left_corner_friend)
                    }
                    FriendStatus.BEING_REQUESTED->{
                        tvBackground.background = ContextCompat.getDrawable(root.context,R.drawable.bg_request)
                        view.background = ContextCompat.getDrawable(root.context,R.drawable.item_left_corner_request)
                    }
                    else->{
                        tvBackground.background = ContextCompat.getDrawable(root.context,R.drawable.bg_pending)
                        view.background = ContextCompat.getDrawable(root.context,R.drawable.item_left_corner_pending)
                    }
                }
            }
        }
    }

    fun setFriendsList(list:List<Friends>){
        listFriends.clear()
        listFriends.addAll(list)
        notifyDataSetChanged()
    }


    interface FriendClickListener{
        fun onFriendClick(clicked: ContactsItemViewModel.ListenerType)
    }
}