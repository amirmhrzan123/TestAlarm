package com.si.sipanicalarm.ui.termsPrivacyPolicy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.si.sipanicalarm.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_terms_privacy.*
import org.koin.android.ext.android.inject
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


class TermsPrivacyPolicy : AppCompatActivity() {


    private val firebaseDatabase: FirebaseDatabase by inject()

    private val extras: Int by lazy {
        intent!!.getIntExtra(EXTRAS, 0)
    }

    companion object {
        const val TERMSCONDITION = 1
        const val PRIVACYPOLICY = 2
        const val EXTRAS = "extras"
        fun newInstance(activity: Activity, extras: Int) {
            val intent = Intent(activity, TermsPrivacyPolicy::class.java)
            intent.putExtra(EXTRAS, extras)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_privacy)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        if (extras == TERMSCONDITION) {
            toolbar.title = "Terms & conditions"
            firebaseDatabase.getReference("terms").child("text")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val text = p0.value.toString()
                            webView.isFocusable = true
                            webView.isFocusableInTouchMode = true
                            webView.settings.javaScriptEnabled = true
                            webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
                            webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                            webView.settings.domStorageEnabled = true
                            webView.settings.databaseEnabled = true
                            webView.settings.setAppCacheEnabled(true)
                            webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                            webView.loadData(text, "text/html", null)

                            println(text)
                        }
                    }

                })

        } else {
            firebaseDatabase.getReference("privacy").child("text")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val text = p0.value.toString()
                            webView.isFocusable = true
                            webView.isFocusableInTouchMode = true
                            webView.settings.javaScriptEnabled = true
                            webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
                            webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                            webView.settings.domStorageEnabled = true
                            webView.settings.databaseEnabled = true
                            webView.settings.setAppCacheEnabled(true)
                            webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                            webView.loadData(text, "text/html", null)

                            println(text)
                        }
                    }

                })


            /*webView.webViewClient = MyWebViewClient()
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.overScrollMode = WebView.OVER_SCROLL_NEVER
            webView.loadUrl("http://swodeshiinnovation.com.np/privacy-policy")*/

        }

    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)

            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            println("on finish")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId === android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }
}