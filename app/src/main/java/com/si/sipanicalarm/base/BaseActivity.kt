package com.si.sipanicalarm.base
import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.si.sipanicalarm.utils.ProgressDialogHelper


abstract class BaseActivity<M : BaseViewModel<*>, V : ViewDataBinding> : AppCompatActivity() {
    var mViewDataBinding: V? = null
    private var mViewModel: M? = null
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()

    }

    fun hideLoading() {
        progressDialog?.let {
            it.dismiss()
        }
    }


    /**
     * TODO show progressbar
     *
     * @param message the message that need to display during loading
     */
    fun showLoading(message: String) {
        progressDialog = ProgressDialogHelper.progressDialog(this, message)
        progressDialog!!.show()
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        mViewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding!!.setLifecycleOwner(this)
        mViewDataBinding!!.executePendingBindings()
    }


    /**
     * @return layout resource _id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): M

    /**
     * Override for set binding variable
     *
     * @return variable _id
     */
    abstract fun getBindingVariable(): Int

    override fun onBackPressed() {
        super.onBackPressed()

    }

    /**
     * TODO setup toolbar for activity
     *
     */
    fun initActionBar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return true
    }

    override fun onResume() {
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        super.onResume()
    }


}