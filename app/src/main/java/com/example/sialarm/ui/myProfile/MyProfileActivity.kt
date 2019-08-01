package com.example.sialarm.ui.myProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.app.dwell.base.BaseActivity
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.databinding.ActivityMyProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.activity_my_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class MyProfileActivity: BaseActivity<MyProfileViewModel,ActivityMyProfileBinding>(),IMyProfileNavigator{
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

    private val myProfileViewModel : MyProfileViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_my_profile

    override fun getViewModel(): MyProfileViewModel = myProfileViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    companion object {
        fun newInstance(activity: Activity){
            val intent = Intent(activity,MyProfileActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar_profiler)
        myProfileViewModel.setNavigator(this)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }
}