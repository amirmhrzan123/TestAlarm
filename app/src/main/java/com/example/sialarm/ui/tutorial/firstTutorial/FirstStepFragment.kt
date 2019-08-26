package com.example.sialarm.ui.tutorial.firstTutorial

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.databinding.FragmentFirstTutorialBinding
import com.example.sialarm.ui.tutorial.FragmentListener
import com.example.sialarm.ui.tutorial.TutorialViewModel
import com.example.sialarm.utils.CommonUtils
import com.example.sialarm.utils.Status
import com.example.sialarm.utils.extensions.isNetworkConnected
import com.example.sialarm.utils.extensions.showConfirmationDialog
import com.example.sialarm.utils.extensions.showValidationDialog
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirstStepFragment: BaseFragment<FirstStepViewModel,FragmentFirstTutorialBinding>() {

    private val firstStepViewModel : FirstStepViewModel by viewModel()


    override fun getLayoutId(): Int = R.layout.fragment_first_tutorial

    override fun getViewModel(): FirstStepViewModel = firstStepViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    lateinit var listener: FragmentListener

    private val tutorialViewModel: TutorialViewModel by sharedViewModel()

    companion object {
        val TAG = FirstStepFragment::class.java.simpleName

        fun newInstance(): FirstStepFragment {
            return FirstStepFragment()
        }
    }


    override fun onAttach(context: Context) {
        if(context is FragmentListener){
            listener = context
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tutorialViewModel.getContactsValid.value = true
        tutorialViewModel.getContacts.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    tutorialViewModel.listContacts.clear()
                    tutorialViewModel.listContacts.addAll(it.data!!)
                }
            }
        })

        tutorialViewModel.insertContacts.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                    activity!!.showConfirmationDialog("SI Alarm", "You have successfully added your friend.", ok = {
                        listener.openSecondTutorial()
                    })
                }
                Status.ERROR->{
                    hideLoading()
                    activity!!.showValidationDialog("SI Alarm",it.message!!)
                }
            }
        })

        floating_action_button.setOnClickListener {
            KotlinPermissions.with(activity!!)
                    .permissions(Manifest.permission.READ_CONTACTS)
                    .onAccepted { permissions ->
                        CommonUtils.openChooserDialog(activity!!) {
                            when(it){
                                CommonUtils.OPTION.ADDFROMCONTACT->{
                                    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                                    intent.putExtra("finishActivityOnSaveCompleted", true)
                                    startActivityForResult(intent, 1)
                                }
                                CommonUtils.OPTION.ADDANOTHERCONTACT->{
                                    CommonUtils.getAddNumberDialog(activity!!,{ number, userName->
                                        tutorialViewModel.contactName = userName
                                        tutorialViewModel.contactNumber = number
                                        val list = tutorialViewModel.listContacts.filter { contact-> contact.number==tutorialViewModel.contactNumber }
                                        if(list.isNotEmpty()){
                                            activity!!.showValidationDialog("SI Alarm","You already have added this friend.")
                                        }else{
                                            if(context!!.isNetworkConnected()){
                                                tutorialViewModel.insertContactValid.value = true
                                            }else{
                                                activity!!.showValidationDialog("SI Alarm","You need to connect to internet to perform this action.")
                                            }
                                        }
                                    },{message->
                                    }).show()
                                }
                            }
                        }
                    }
                    .onDenied {
                        //List of denied permissions
                    }
                    .onForeverDenied { permissions ->
                        //List of forever denied permissions
                    }
                    .ask()


        }



    }
}