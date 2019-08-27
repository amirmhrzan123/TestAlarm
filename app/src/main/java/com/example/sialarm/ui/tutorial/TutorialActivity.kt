package com.example.sialarm.ui.tutorial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.databinding.ActivityTutorialBinding
import com.example.sialarm.ui.tutorial.firstTutorial.FirstStepFragment
import com.example.sialarm.utils.extensions.getNumber
import com.example.sialarm.utils.extensions.showValidationDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class TutorialActivity:BaseActivity<TutorialViewModel,ActivityTutorialBinding>(),FragmentListener{

    private val tutorialViewModel: TutorialViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_tutorial

    override fun getViewModel(): TutorialViewModel = tutorialViewModel

    override fun getBindingVariable(): Int = BR.viewModel

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
                    tutorialViewModel.contactNumber = contactNumber.getNumber()
                    tutorialViewModel.contactName = contactName
                    tutorialViewModel.contactTrigger.value = true
                }
                Log.d("Contactname", "Contact Name: " + contactName)
            }
        }
    }
}