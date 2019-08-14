package com.example.sialarm.ui.landingScreen

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import android.os.Parcelable
import android.util.Base64
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.databinding.ActivityLandingBinding
import com.example.sialarm.ui.homepage.HomeActivity
import com.example.sialarm.utils.Status
import com.example.sialarm.utils.extensions.getNumber
import com.example.sialarm.utils.extensions.setupUI
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.facebook.accountkit.ui.SkinManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_landing.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LandingActivity : BaseActivity<LandingScreenViewModel, ActivityLandingBinding>(){

    private val landingViewModel : LandingScreenViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_landing

    override fun getViewModel(): LandingScreenViewModel = landingViewModel

    override fun getBindingVariable(): Int = BR.viewModel



    var APP_REQUEST_CODE = 99

    companion object {
        fun newInstance(activity: Activity){
            activity.startActivity(Intent(activity,LandingActivity::class.java))
            activity.finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

        setupUI(main_layout,this)




        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@LandingActivity) { instanceIdResult ->
            val newToken = instanceIdResult.token
            landingViewModel.token = newToken
            Log.d("newToken", newToken)
        }
        AccountKit.initialize(
            applicationContext, AccountKit.InitializeCallback {
            }
        )
        landingViewModel.insertUsers.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    hideLoading()
                    HomeActivity.newInstance(this@LandingActivity)
                    finish()
                }
            }
        })
        printHashKey()
        btnLogin.setOnClickListener {
            if(etUserName.text!!.trim().toString().isEmpty()){
                Toast.makeText(this,"Please enter username.",Toast.LENGTH_SHORT).show()
            }else{
                showLoading("")
                /*landingViewModel.userName = etUserName.text!!.trim().toString()
                landingViewModel.number = "++9779849276763"
                landingViewModel.isValid.value = true*/

                 val intent = Intent(applicationContext, AccountKitActivity::class.java)
                 val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                     LoginType.PHONE,
                     AccountKitActivity.ResponseType.TOKEN
                 )

                 configurationBuilder.setUIManager(
                     SkinManager(
                         SkinManager.Skin.CONTEMPORARY,
                         resources.getColor(R.color.colorBackground),
                         R.drawable.bg,
                         SkinManager.Tint.BLACK,
                         0.10
                     )
                 )
                 intent.putExtra(
                     AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                     configurationBuilder.build()
                 )
                 showLoading("")
                 startActivityForResult(intent, APP_REQUEST_CODE)
            }
        }
    }

    private fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                applicationContext.packageName,
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request

            val loginResult = data!!.getParcelableExtra<Parcelable>(AccountKitLoginResult.RESULT_KEY) as AccountKitLoginResult
            val responseMessage: String
            if (loginResult.error != null) {
                responseMessage = loginResult.error!!.errorType.message
                hideLoading()
            } else if (loginResult.wasCancelled()) {
                responseMessage = "Login Cancelled"
                hideLoading()
            } else {
                if (loginResult.accessToken != null) {
                    responseMessage = "Success: " + loginResult.accessToken!!.accountId
                    AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
                        override fun onSuccess(account: Account) {
                            val accountKitId = account.id
                            val phoneNumber = account.phoneNumber
                            Log.d("phoneNumber",phoneNumber.toString().getNumber())
                            landingViewModel.number = phoneNumber.toString().getNumber()
                            landingViewModel.userName = etUserName.text.toString()
                            val phoneNumberString = phoneNumber.toString()

                            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@LandingActivity) { instanceIdResult ->
                                val newToken = instanceIdResult.token
                                landingViewModel.token = newToken
                                Log.d("newToken", newToken)
                                landingViewModel.isValid.value = true


                            }
                        }

                        override fun onError(error: AccountKitError) {
                            // Handle Error
                        }
                    })
                } else {
                    responseMessage = String.format(
                        "Success:%s...",
                        loginResult.authorizationCode!!.substring(0, 10)
                    )
                }
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.
                // Success! Start your next activity...
            }
        }
    }
}