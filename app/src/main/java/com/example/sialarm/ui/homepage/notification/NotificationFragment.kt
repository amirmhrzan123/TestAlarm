package com.example.sialarm.ui.homepage.notification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.databinding.FragmentNotificationBinding
import com.example.sialarm.utils.Status
import kotlinx.android.synthetic.main.fragment_notification.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.Intent
import android.net.Uri
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.utils.extensions.showValidationDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User


class NotificationFragment:BaseFragment<NotificationViewModel,FragmentNotificationBinding>(),SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        notificationViewModel.notificationValid.value = true
        swipeNotification.isRefreshing = false
    }

    companion object {
        fun newInstance():NotificationFragment{
            return NotificationFragment()
        }
    }

    private val notificationAdapter: NotificationAdaptor by lazy{
        NotificationAdaptor(click = {



            if(it.notification_type_id=="2"){

                FirebaseDatabase.getInstance().getReference("users").child(it.sender_id).addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()){
                            val user = p0.getValue(Users::class.java)
                            if(user!!.safeStatus!!){
                                activity!!.showValidationDialog("SI alarm","It seems your SI friend is already safe.")
                            }else{
                                val url ="http://maps.google.com/?q=${it.latitude},${it.longitude}&z=10"
                                val i = Intent(Intent.ACTION_VIEW)
                                i.data = Uri.parse(url)
                                startActivity(i)
                            }
                        }
                    }

                })


            }
        })
    }

    private val notificationViewModel: NotificationViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun getViewModel(): NotificationViewModel = notificationViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeNotification.setOnRefreshListener(this)
        notificationViewModel.notificationValid.value = true
        notificationViewModel.notification.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                  //  showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                    if(it.data!!.isNotEmpty()){
                        tv_noNotification.visibility = View.GONE
                    }else{
                        tv_noNotification.visibility = View.VISIBLE
                    }
                    val linearLayoutManager = LinearLayoutManager(activity!!)
                    rv_notification.layoutManager = linearLayoutManager
                    rv_notification.adapter = notificationAdapter
                    notificationAdapter.setNotificationList(it.data)

                }
                Status.ERROR->{
                    hideLoading()

                }
            }
        })
    }
}