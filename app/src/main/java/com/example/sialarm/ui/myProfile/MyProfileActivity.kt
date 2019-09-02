package com.example.sialarm.ui.myProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.data.firebase.Users
import com.example.sialarm.data.prefs.PrefsManager
import com.example.sialarm.databinding.ActivityMyProfileBinding
import com.example.sialarm.ui.device.DeviceListActivity
import com.example.sialarm.ui.device.DeviceListLowActivity
import com.example.sialarm.ui.district.DistrictListLowActivity
import com.example.sialarm.ui.district.SearchDistrictActivity
import com.example.sialarm.ui.homepage.HomeActivity
import com.example.sialarm.utils.Status
import com.example.sialarm.utils.customViews.CustomSpinnerDialog
import com.example.sialarm.utils.customViews.CustomSpinnerWardAdapter
import com.example.sialarm.utils.extensions.Device
import com.example.sialarm.utils.extensions.District
import com.example.sialarm.utils.extensions.loadImage
import com.example.sialarm.utils.extensions.showValidationDialog
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.activity_my_profile.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.ArrayList

class MyProfileActivity: BaseActivity<MyProfileViewModel, ActivityMyProfileBinding>(),IMyProfileNavigator{

    private val prefs: PrefsManager by inject()

    var stateList = arrayListOf<String>()

    private val myProfileViewModel : MyProfileViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_my_profile

    override fun getViewModel(): MyProfileViewModel = myProfileViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private val initial : Boolean by lazy{
        intent!!.getBooleanExtra("extra",false)
    }

    var deviceId = ""

    companion object {
        fun newInstance(activity: Activity, initial:Boolean=false){
            val intent = Intent(activity,MyProfileActivity::class.java)
            intent.putExtra("extra",initial)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar_profiler)
        myProfileViewModel.setNavigator(this)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        stateList = arrayListOf<String>("Province No. 1","Province No. 2",
            "Province No. 3","Gandaki Pradesh","Province No. 5","Karnali Pradesh","Sudurpashchim Pradesh")
        userName.setText(prefs.getUserName())
        email.setText(prefs.getEmail())
        etStreet.setText(prefs.getTole())
        etState.setText(prefs.getState())
        etWardNumber.setText(prefs.getWardNo())
        etDistrict.setText(prefs.getDistrict())
        etDevice.setText(prefs.getDeviceName())
        if(prefs.getWardNo().isNotEmpty()){
            myProfileViewModel.selectedWardId = prefs.getWardNo().toInt()
        }else{
            myProfileViewModel.selectedWardId = -1
        }
        if(prefs.getState().isNotEmpty()){
            myProfileViewModel.selectedStateId = stateList.indexOf(prefs.getState())
        }else{
            myProfileViewModel.selectedStateId = -1
        }


        circleImageView2.loadImage(prefs.getUserImage(),0)

        myProfileViewModel.updateProfile.observe(this, Observer{
            when(it.status){
                Status.LOADING->{
                    showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                    if(initial){
                        HomeActivity.newInstance(this)
                        finish()
                    }else{
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                }
                Status.ERROR->{
                    hideLoading()
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==3){
                val district = Gson().fromJson(data!!.getStringExtra("Extra"), District::class.java)
                etDistrict.setText(district.name)
            }else if(requestCode==4){
                val device = Gson().fromJson(data!!.getStringExtra("Extra"), Device::class.java)
                etDevice.setText(device.name)
                deviceId=device.id
            }else{
                HomeActivity.newInstance(this)
            }
        }
    }

    override fun onUpdateClicked() {

        if(userName.text.toString().isEmpty()){
            showValidationDialog("SI Alarm","Please write your username.")
        }else if(etState.text.toString().isEmpty()){
            showValidationDialog("SI Alarm","Please select your state.")
        }else if(etDistrict.text.toString().isEmpty()){
            showValidationDialog("SI Alarm", "Please select your district.")
        }else if(etWardNumber.text.toString().isEmpty()){
            showValidationDialog("SI Alarm","Please select your ward number.")
        }else if(etDevice.text.toString().isEmpty()){
            showValidationDialog("SI Alarm","Please select device of your locality.")
        }else{
            myProfileViewModel.userObserver.value = Users(email = email.text.toString(),
                username = userName.text.toString(),
                state = etState.text.toString(),
                district = etDistrict.text.toString(),
                ward = etWardNumber.text.toString().toInt(),
                tole = etStreet.text.toString(),
                device = deviceId,
                deviceName = etDevice.text.toString())
        }

    }

    override fun onWardClicked() {
        val wardList = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,29,30,31,32,33,34,35)

        val dialog = CustomSpinnerDialog.getInstanceForWard("Choose ward number", wardList as ArrayList<Int>, myProfileViewModel.selectedWardId)
        dialog.setListener(object : CustomSpinnerWardAdapter.onCustomSpinnerItemSelected {
            override fun onItemSelected(selectedId: Int, position: Int) {
                myProfileViewModel.selectedWardId = selectedId
                etWardNumber.setText(selectedId.toString())
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    override fun onDistrictClicked() {

        if(Build.VERSION.SDK_INT<21){
            startActivityForResult(Intent(this,DistrictListLowActivity::class.java),3)

        }else{
            startActivityForResult(Intent(this,DistrictListLowActivity::class.java),3)

          //  startActivityForResult(Intent(this,SearchDistrictActivity::class.java),3)

        }
    }

    override fun onDeviceClicked() {

         if (Build.VERSION.SDK_INT < 21){
             startActivityForResult(Intent(this,DeviceListLowActivity::class.java),4)

         }else{
             startActivityForResult(Intent(this,DeviceListLowActivity::class.java),4)

            // startActivityForResult(Intent(this,DeviceListActivity::class.java),4)

         }
    }


    override fun onStateClicked() {

        val dialog = CustomSpinnerDialog.getInstance("Choose State", stateList as ArrayList<String>, myProfileViewModel.selectedStateId)
        dialog.setListener(object : CustomSpinnerWardAdapter.onCustomSpinnerItemSelected {
            override fun onItemSelected(selectedId: Int, position: Int) {
                myProfileViewModel.selectedStateId = selectedId
                etState.setText(stateList[selectedId])
            }
        })
        dialog.show(supportFragmentManager, "")
    }



    override fun onImageClicked() {

        KotlinPermissions.with(this) // where this is an FragmentActivity instance
            .permissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onAccepted { permissions ->
                if(permissions.size == 3){
                    ImagePicker.with(this)
                        .crop(1f, 1f)               //Crop Square image(Optional)
                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .start { resultCode, data ->
                            if (resultCode == Activity.RESULT_OK) {
                                //Image Uri will not be null for RESULT_OK
                                val fileUri = data?.data
                                circleImageView2.setImageURI(fileUri)

                                //You can get File object from intent
                                val file: File = ImagePicker.getFile(data)!!

                                //You can also get File Path from intent
                                val filePath:String = ImagePicker.getFilePath(data)!!
                                val uri = Uri.fromFile(file)
                                myProfileViewModel.uploadImage(uri)
                            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
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