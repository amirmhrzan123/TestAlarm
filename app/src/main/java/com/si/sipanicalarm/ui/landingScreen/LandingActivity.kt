package com.si.sipanicalarm.ui.landingScreen

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.si.sipanicalarm.BR
import com.si.sipanicalarm.R
import com.si.sipanicalarm.base.BaseActivity
import com.si.sipanicalarm.data.prefs.PrefsManager
import com.si.sipanicalarm.ui.homepage.HomeActivity
import com.si.sipanicalarm.ui.lillipin.CustomPinActivity
import com.si.sipanicalarm.ui.myProfile.MyProfileActivity
import com.si.sipanicalarm.ui.termsPrivacyPolicy.TermsPrivacyPolicy
import com.si.sipanicalarm.ui.tutorial.TutorialActivity
import com.si.sipanicalarm.utils.LinkTouchMovementMethod
import com.si.sipanicalarm.utils.Status
import com.si.sipanicalarm.utils.TouchableSpan
import com.si.sipanicalarm.utils.extensions.getNumber
import com.si.sipanicalarm.utils.extensions.setupUI
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.facebook.accountkit.ui.SkinManager
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.google.firebase.iid.FirebaseInstanceId
import com.si.sipanicalarm.databinding.ActivityLandingBinding
import kotlinx.android.synthetic.main.activity_landing.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LandingActivity : BaseActivity<LandingScreenViewModel, ActivityLandingBinding>(){

    private val landingViewModel : LandingScreenViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_landing

    override fun getViewModel(): LandingScreenViewModel = landingViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private val prefs : PrefsManager by inject()

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
        val intent = Intent(this,CustomPinActivity::class.java)

        if(prefs.isFirstTime()){
            prefs.setFirstTime(false)
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK)
            startActivityForResult(intent, 11)
        }else{
            if(prefs.isLogin()) {
                if(prefs.finishTutorial()){
                    if(prefs.isProfileComplete()){
                        HomeActivity.newInstance(this)
                        finish()
                    }else{
                        MyProfileActivity.newInstance(this,true)
                        finish()
                    }
                }else{
                    TutorialActivity.newInstance(this)
                    finish()
                }
            }
        }
        setSpannableText()
       /*else{
            if(prefs.)
            prefs.setFirstTime(true)

        }*/

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

                    if(it.data!!.equals("2")){
                        prefs.setLoginStatus(true)
                        TutorialActivity.newInstance(this@LandingActivity)
                        finish()
                    }else if(it.data.equals("3")){
                        prefs.setLoginStatus(true)
                        prefs.setFirstTutorial(true)
                        MyProfileActivity.newInstance(this@LandingActivity)
                        finish()
                    }else if(it.data.equals("4")){
                        HomeActivity.newInstance(this@LandingActivity)
                        finish()
                        prefs.setLoginStatus(true)
                        prefs.setFirstTutorial(true)
                        prefs.setProfileComplete(true)
                    }
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
             /*   landingViewModel.userName = etUserName.text!!.trim().toString()
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
                startActivityForResult(intent, APP_REQUEST_CODE)

                /* val intent = Intent(this@LandingActivity,PhoneNumberActivity::class.java)
                  showLoading("")
                  startActivityForResult(intent, APP_REQUEST_CODE)*/
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
            }

         /*   val phoneNumber = data.getStringExtra("NUMBER")
            landingViewModel.number = phoneNumber
            landingViewModel.userName = etUserName.text.toString()*/
/*
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@LandingActivity) { instanceIdResult ->
                val newToken = instanceIdResult.token
                landingViewModel.token = newToken
                Log.d("newToken", newToken)
                landingViewModel.isValid.value = true
            }*/
        }
    }


    private fun setSpannableText() {

        val termsAndConditionsText = getString(R.string.txt_terms_and_conditions)
        val privacyPolicyText = getString(R.string.txt_privacy_policy)
        val contractInfo = getString(R.string.terms_conditions, termsAndConditionsText, privacyPolicyText)
        val ss = SpannableString(contractInfo)
        val termsAndConditionTouchableSpan = object : TouchableSpan(ContextCompat.getColor(this, android.R.color.black), ContextCompat.getColor(this, android.R.color.black)) {
            override fun onClick(widget: View) {
                TermsPrivacyPolicy.newInstance(this@LandingActivity,TermsPrivacyPolicy.TERMSCONDITION)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)


            }
        }
        val privacyPolicyTouchableSpan = object : TouchableSpan(ContextCompat.getColor(this, android.R.color.black), ContextCompat.getColor(this, android.R.color.black)) {
            override fun onClick(widget: View) {
                TermsPrivacyPolicy.newInstance(this@LandingActivity,TermsPrivacyPolicy.PRIVACYPOLICY)
              //  iLandingFragment?.onTermsPrivacyClicked(WebPage.PRIVACY_POLICY)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)

            }
        }
        addClickToSpan(ss, termsAndConditionsText, termsAndConditionTouchableSpan)
        addClickToSpan(ss, privacyPolicyText, privacyPolicyTouchableSpan)
        tv_terms.movementMethod = LinkTouchMovementMethod()
        tv_terms.text = ss
        tv_terms.highlightColor = Color.TRANSPARENT


    }


    private fun makeTextBold(ss: SpannableString, string: String) {
        val start = ss.indexOf(string)
        val end = start + string.length
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }

    private fun addClickToSpan(spannableString: SpannableString, spanText: String, touchableSpan: TouchableSpan) {
        val start = spannableString.indexOf(spanText)
        val end = start + spanText.length
        spannableString.setSpan(touchableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }

}