package com.si.sipanicalarm.ui.tutorial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.si.sipanicalarm.BR
import com.si.sipanicalarm.R
import com.si.sipanicalarm.base.BaseActivity
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.databinding.ActivityTutorialBinding
import com.si.sipanicalarm.ui.homepage.HomeActivity
import com.si.sipanicalarm.ui.tutorial.firstTutorial.FirstStepFragment
import com.si.sipanicalarm.utils.extensions.getNumber
import com.si.sipanicalarm.utils.extensions.isNetworkConnected
import com.si.sipanicalarm.utils.extensions.showConfirmationDialog
import com.si.sipanicalarm.utils.extensions.showValidationDialog
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TutorialActivity:BaseActivity<TutorialViewModel,ActivityTutorialBinding>(),FragmentListener{

    private val tutorialViewModel: TutorialViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_tutorial

    override fun getViewModel(): TutorialViewModel = tutorialViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private val prefs:PrefsManager by inject()

    companion object {
        fun newInstance(activity: Activity){
            val intent = Intent(activity,TutorialActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openFirstTutorial()
    }

    override fun openFirstTutorial(){
        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
            .replace(R.id.container, FirstStepFragment.newInstance())
            .commit()
    }

    override fun openSecondTutorial(){

        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
            .replace(R.id.container,SecondStepFragment.newInstance())
            .commit()

    }

    override fun openThirdTutorial(){
        prefs.setFirstTutorial(true)
        supportFragmentManager
            .beginTransaction()
            .disallowAddToBackStack()
            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
            .replace(R.id.container,ThirdStepFragment.newInstance())
            .commit()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {

            val uriContact = data?.data
            if(uriContact!=null){
                var contactNumber = ""
                var contactID =  ""

                // getting contacts ID
                val cursorID = contentResolver.query(uriContact!!,
                    arrayOf(ContactsContract.Contacts._ID),null,null,null)

                if (cursorID!!.moveToFirst()) {

                    contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID))
                    Log.d("", "Contact ID: " + contactID)

                }

                cursorID.close()


                // Using the contact ID now we will get contact phone number
                val cursorPhone = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                    arrayOf(contactID),null)



                if (cursorPhone!!.moveToFirst()) {
                    contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                }

                cursorPhone.close()
                Log.d("ContactNumber", "Contact Phone Number: " + contactNumber);

                var contactName = ""

                // querying contact data store
                val cursor = getContentResolver().query(uriContact, null, null, null, null);

                if (cursor!!.moveToFirst()) {

                    // DISPLAY_NAME = The display name for the contact.
                    // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                }

                cursor.close()
                if(contactNumber.isEmpty()){
                    showValidationDialog("SI Alarm",getString(R.string.number_error))
                }else{
                    tutorialViewModel.contactName = contactName
                    tutorialViewModel.contactNumber = contactNumber.getNumber()
                    val list = tutorialViewModel.listContacts.filter { contact-> contact.number==tutorialViewModel.contactNumber }
                    if(list.isNotEmpty()){
                        showConfirmationDialog("SI Alarm", "Congratulation, you have added your SI friend",ok = {
                            openSecondTutorial()
                        })}else{
                        if(isNetworkConnected()){
                            tutorialViewModel.insertContactValid.value = true
                        }else{
                            showValidationDialog("SI Alarm","You need to connect to internet to perform this action.")
                        }
                    }
                }
                Log.d("Contactname", "Contact Name: " + contactName)
            }
        }else if(requestCode  == 2){
            HomeActivity.newInstance(this)
            finish()
        }
    }
}