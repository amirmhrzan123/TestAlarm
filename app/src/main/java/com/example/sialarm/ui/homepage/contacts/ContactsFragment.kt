package com.example.sialarm.ui.homepage.contacts

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.data.api.AcceptDenyRequestModel
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.databinding.FragmentContactsBinding
import com.example.sialarm.ui.homepage.MainViewModel
import com.example.sialarm.utils.Status
import com.example.sialarm.utils.extensions.isNetworkConnected
import com.example.sialarm.utils.extensions.showConfirmationDialog
import com.example.sialarm.utils.extensions.showValidationDialog
import kotlinx.android.synthetic.main.fragment_contacts.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsFragment:BaseFragment<ContactsViewModel,FragmentContactsBinding>(),
    ContactFriendsAdapter.FriendClickListener,SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        contactsViewModel.getContactsValid.value = true

    }

    override fun onFriendClick(clicked: ContactsItemViewModel.ListenerType) {
        when(clicked){
            is ContactsItemViewModel.ListenerType.AcceptFriendRequestListener->{
                activity!!.showConfirmationDialog("Friend request","Are you sure you want accept the friend request?",
                    "Yes","No",
                    ok = {
                        val acceptDenyRequestModel = AcceptDenyRequestModel(sender_id = prefs.getUserId(),
                            receiver_id = clicked.friend.number,
                            status = 1,
                            notification_type_id = "3",
                            senderUserName = prefs.getUserName())
                        contactsViewModel.acceptDenyValid.value = acceptDenyRequestModel

                    })
            }
            is ContactsItemViewModel.ListenerType.RejectFriendRequestListener->{
                activity!!.showConfirmationDialog("Friend request","Are you sure you want to reject the friend request?",
                    "Yes","No",
                    ok = {
                        val acceptDenyRequestModel = AcceptDenyRequestModel(sender_id = prefs.getUserId(),
                            receiver_id = clicked.friend.number,
                            status =  2,
                            notification_type_id = "3",
                            senderUserName = prefs.getUserName())
                        contactsViewModel.acceptDenyValid.value = acceptDenyRequestModel
                    })
            }
            is ContactsItemViewModel.ListenerType.UnfriendClickListener->{
                activity!!.showConfirmationDialog("Unfriend","Are you sure you want unfriend this friend?",
                    "Yes","No",
                    ok = {
                        val acceptDenyRequestModel = AcceptDenyRequestModel(sender_id = prefs.getUserId(),
                            receiver_id = clicked.friend.number,
                            status = 3,
                            notification_type_id = "3",
                            senderUserName = prefs.getUserName())
                        contactsViewModel.acceptDenyValid.value = acceptDenyRequestModel
                    })
            }
            is ContactsItemViewModel.ListenerType.DeleteClickListener->{
                activity!!.showConfirmationDialog("Cancel friend request","Are you sure you want to cancel the request?",
                    "Yes","No",
                    ok = {
                        val acceptDenyRequestModel = AcceptDenyRequestModel(sender_id = prefs.getUserId(),
                            receiver_id = clicked.friend.number,
                            status =  3,
                            notification_type_id = "3",
                            senderUserName = prefs.getUserName())
                        contactsViewModel.acceptDenyValid.value = acceptDenyRequestModel
                    })
            }
        }

    }

    companion object {
        fun newInstance():ContactsFragment{
            return ContactsFragment()
        }
    }

    private val contactsAdapter : ContactFriendsAdapter by lazy{
        ContactFriendsAdapter()

    }

    private val prefs:PrefsManager by inject()

    private val contactsViewModel : ContactsViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_contacts

    override fun getViewModel(): ContactsViewModel = contactsViewModel

    private val mainViewMOdel : MainViewModel by sharedViewModel()

    override fun getBindingVariable(): Int = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeContacts.setOnRefreshListener(this)
        contactsViewModel.getContactsValid.value = true
        mainViewMOdel.contactTrigger.observe(this, Observer {
            contactsViewModel.contactName = mainViewMOdel.contactName
            contactsViewModel.contactNumber = mainViewMOdel.contactNumber
            val list = contactsViewModel.listContacts.filter { contact-> contact.number==contactsViewModel.contactNumber }
            if(list.isNotEmpty()){
                activity!!.showValidationDialog("SI Alarm","You already have added this friend.")
            }else{
                if(context!!.isNetworkConnected()){
                    contactsViewModel.insertContactValid.value = true
                }else{
                    activity!!.showValidationDialog("SI Alarm","You need to connect to internet to perform this action.")
                }
            }
        })

        contactsViewModel.insertContacts.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                }
                Status.ERROR->{
                    hideLoading()
                    activity!!.showValidationDialog("SI Alarm",it.message!!)
                }
            }
        })

        contactsViewModel.acceptDenyInvitation.observe(this,Observer{
            when(it.status){
                Status.LOADING->{
                    showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()

                }
                Status.ERROR->{
                    hideLoading()
                    activity!!.showValidationDialog("SI Alarm",it.message!!)

                }
            }
        })

        contactsViewModel.getContacts.observe(this,Observer{
            when(it.status){
                Status.LOADING->{
                   // showLoading("")
                }
                Status.ERROR->{
                    hideLoading()
                    activity!!.showValidationDialog("SI Alarm",it.message!!)

                }
                Status.SUCCESS->{
                    hideLoading()
                    swipeContacts.isRefreshing = false
                    val layoutManager = LinearLayoutManager(context)
                    rv_friendsList.layoutManager = layoutManager
                    rv_friendsList.adapter = contactsAdapter
                    contactsAdapter.setListener(this@ContactsFragment)
                    contactsAdapter.setFriendsList(it.data!!)
                    contactsViewModel.listContacts.clear()
                    contactsViewModel.listContacts.addAll(it.data)

                    /*contactsAdapter.listener = object: ContactsAdapter.ContactClickListener{
                        override fun onAcceptDeniedClicked(contact: Friends, accept: Boolean) {
                            println("receiverNumber "+ contact.number)
                            if(accept){
                                val acceptDenyRequestModel = AcceptDenyRequestModel(sender_id = prefs.getUserId(),
                                    receiver_id = contact.number,
                                    accept =  true,
                                    notification_type_id = "3",
                                    senderUserName = prefs.getUserName())
                                contactsViewModel.acceptDenyValid.value = acceptDenyRequestModel
                            }else{
                                val acceptDenyRequestModel = AcceptDenyRequestModel(senderUserName = prefs.getUserName(),
                                    sender_id = prefs.getUserId(),
                                    receiver_id = contact.number,
                                    accept = false,
                                    notification_type_id = "3")
                                contactsViewModel.acceptDenyValid.value = acceptDenyRequestModel
                            }
                        }

                        override fun onFriendsContact(contact: Friends) {
                            //view friend profile
                        }

                    }*/
                }
                else ->{

                }
            }
        })
    }
}