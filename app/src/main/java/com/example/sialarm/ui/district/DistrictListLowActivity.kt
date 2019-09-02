package com.example.sialarm.ui.district

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.databinding.ActivityDeviceListLowBinding
import com.example.sialarm.ui.device.DeviceListViewModel
import com.example.sialarm.utils.extensions.District
import com.example.sialarm.utils.extensions.getDistrictsKeyValue
import com.example.sialarm.utils.extensions.setupUI
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_device_list_low.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DistrictListLowActivity: BaseActivity<DeviceListViewModel,ActivityDeviceListLowBinding>(),DistrictListAdapter.OnListItemClickListener {

    val gson =  Gson()

    override fun onItemClick(district: District) {
        val output = Intent()
        output.putExtra("Extra",gson.toJson(district))
        setResult(RESULT_OK, output)
        finish()
    }

    override fun getLayoutId(): Int = R.layout.activity_device_list_low

    private val mainViewModel : DeviceListViewModel by viewModel()

    override fun getViewModel(): DeviceListViewModel = mainViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private val adapter : DistrictListAdapter by lazy{
        DistrictListAdapter(this@DistrictListLowActivity, getDistrictsKeyValue(),this@DistrictListLowActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI(activity_device_search,this)
        ic_back.setOnClickListener {
            onBackPressed()
        }

        et_search.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

        })


        list_view.adapter = adapter

    }


    override fun onBackPressed() {

            super.onBackPressed()

    }

}