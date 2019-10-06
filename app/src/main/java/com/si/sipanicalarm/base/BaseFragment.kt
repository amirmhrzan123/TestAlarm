package com.si.sipanicalarm.base
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.si.sipanicalarm.utils.ProgressDialogHelper

abstract class BaseFragment<M : BaseViewModel<*>, V : ViewDataBinding> : Fragment() {
    var mViewDataBinding: V? = null
    private var mViewModel: M? = null
    var progressDialog: Dialog? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mViewDataBinding!!.root
    }

    override fun onResume() {
        mViewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding!!.lifecycleOwner = this
        mViewDataBinding!!.executePendingBindings()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        mViewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding!!.lifecycleOwner = this
        mViewDataBinding!!.executePendingBindings()

    }
    fun hideLoading() {
        progressDialog?.let {
            it.dismiss()
        }
    }


    fun showLoading(message: String) {
        progressDialog = ProgressDialogHelper.progressDialog(activity!!, message)
        progressDialog!!.show()
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

}