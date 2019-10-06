package com.si.sipanicalarm.ui.history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.si.sipanicalarm.BR
import com.si.sipanicalarm.R
import com.si.sipanicalarm.base.BaseActivity
import com.si.sipanicalarm.databinding.ActivityHistoryBinding
import com.si.sipanicalarm.utils.Status
import kotlinx.android.synthetic.main.activity_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity: BaseActivity<HistoryViewModel, ActivityHistoryBinding>() {

    private val historyViewModel :HistoryViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_history

    private val historyAdapter: HistoryAdapter by lazy{
        HistoryAdapter()
    }

    override fun getViewModel(): HistoryViewModel = historyViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    companion object {
        fun newInstance(activity: Activity){
            val intent = Intent(activity,HistoryActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar_history)
        initActionBar()
        toolbar_history.title = "History"

        historyViewModel.historyValid.value = true
        historyViewModel.history.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    //  showLoading("")
                }
                Status.SUCCESS->{
                    hideLoading()
                    if(it.data!!.isNotEmpty()){
                    }else{
                    }
                    val linearLayoutManager = LinearLayoutManager(this)
                    rvHistory.layoutManager = linearLayoutManager
                    rvHistory.adapter = historyAdapter
                    historyAdapter.setHistoryList(it.data)

                }
                Status.ERROR->{
                    hideLoading()

                }
            }
        })

    }
}