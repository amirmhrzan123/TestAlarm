package com.example.sialarm.ui.device

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.databinding.ActivityDeviceListLowBinding
import com.example.sialarm.utils.extensions.Device
import com.example.sialarm.utils.extensions.setupUI
import com.example.sialarm.utils.extensions.showValidationDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_device_list_low.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DeviceListLowActivity: BaseActivity<DeviceListViewModel,ActivityDeviceListLowBinding>(),DeviceListAdapter.OnListItemClickListener {

    val gson =  Gson()

    override fun onItemClick(device: Device) {
        val output = Intent()
        output.putExtra("Extra",gson.toJson(device))
        setResult(RESULT_OK, output)
        finish()
    }

    companion object {
        fun newInstance(activity: Activity){
            val intent = Intent(activity, DeviceListLowActivity::class.java)
            activity.startActivity(intent);
        }
    }

    private val firebaseDatabase : FirebaseDatabase by inject()

    override fun getLayoutId(): Int = R.layout.activity_device_list_low

    private val deviceListViewModel : DeviceListViewModel by viewModel()

    override fun getViewModel(): DeviceListViewModel = deviceListViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private var listDevice = mutableListOf<Device>()

    private val adapter : DeviceListAdapter by lazy{
        DeviceListAdapter(this,  listDevice,onListItemClickListener =  this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI(activity_device_search,this)

        ic_back.setOnClickListener {
            onBackPressed()
        }

        setDeviceListInAdapter()

    }


    override fun onBackPressed() {

            super.onBackPressed()

    }

    fun setDeviceListInAdapter(){
        firebaseDatabase.getReference("deviceList").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val deviceList = mutableListOf<Device>()
                    for(values in p0.children){
                        val device = values.getValue(Device::class.java)
                        deviceList.add(device!!)
                    }
                    listDevice = deviceList
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

                }else{
                    showValidationDialog("SI alarm","No device found");
                }
            }

        })
    }
}