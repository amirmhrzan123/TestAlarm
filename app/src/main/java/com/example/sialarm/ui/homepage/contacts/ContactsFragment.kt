package com.example.sialarm.ui.homepage.contacts

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.data.firebase.Friends
import com.example.sialarm.databinding.FragmentContactsBinding
import com.example.sialarm.ui.homepage.HomeViewModel
import com.example.sialarm.utils.Status
import kotlinx.android.synthetic.main.fragment_contacts.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsFragment:BaseFragment<ContactsViewModel,FragmentContactsBinding>() {

    companion object {
        fun newInstance():ContactsFragment{
            return ContactsFragment()
        }
    }

    private val contactsAdapter : ContactsAdapter by lazy{
        ContactsAdapter()

    }

    private val contactsViewModel : ContactsViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_contacts

    override fun getViewModel(): ContactsViewModel = contactsViewModel

    private val mainViewMOdel : HomeViewModel by sharedViewModel()

    override fun getBindingVariable(): Int = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactsViewModel.getContactsValid.value = true
        mainViewMOdel.contactTrigger.observe(this, Observer {
            contactsViewModel.contactName = mainViewMOdel.contactName
            contactsViewModel.contactNumber = mainViewMOdel.contactNumber
            contactsViewModel.insertContactValid.value = true
        })

        contactsViewModel.insertContacts.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                }
            }
        })

        contactsViewModel.getContacts.observe(this,Observer{
            when(it.status){
                Status.ERROR->{
                    showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                    val layoutManager = LinearLayoutManager(context)
                    rv_friendsList.layoutManager = layoutManager
                    rv_friendsList.adapter = contactsAdapter
                    contactsAdapter.setFriendsList(it.data!!)
                    contactsAdapter.listener = object: ContactsAdapter.ContactClickListener{
                        override fun onAcceptDeniedClicked(contact: Friends, accept: Boolean) {
                            if(accept){

                            }else{

                            }
                        }

                        override fun onFriendsContact(contact: Friends) {
                            //view friend profile
                        }

                    }
                }
                else ->{

                }
            }
        })
    }
}