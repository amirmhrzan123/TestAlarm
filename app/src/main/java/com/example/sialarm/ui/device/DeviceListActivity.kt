package com.example.sialarm.ui.device

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.databinding.ActivitySearchBinding
import com.example.sialarm.ui.homepage.MainViewModel
import com.example.sialarm.utils.extensions.Device
import com.example.sialarm.utils.extensions.setupUI
import com.example.sialarm.utils.extensions.showValidationDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject

class DeviceListActivity : BaseActivity<MainViewModel, ActivitySearchBinding>(), DeviceListAdapter.OnListItemClickListener{

    val gson =  Gson()

    override fun onItemClick(device: Device) {
        val output = Intent()
        output.putExtra("Extra",gson.toJson(device))
        setResult(RESULT_OK, output)
        finish()
    }

    companion object {
        fun newInstance(activity: Activity){
            val intent = Intent(activity, DeviceListActivity::class.java)
            activity.startActivity(intent);
        }
    }

    private val firebaseDatabase : FirebaseDatabase by inject()

    override fun getLayoutId(): Int = R.layout.activity_search

    private val mainViewModel : MainViewModel by inject()

    override fun getViewModel(): MainViewModel = mainViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private var listDevice = mutableListOf<Device>()

    private val adapter : DeviceListAdapter by lazy{
        DeviceListAdapter(this,  listDevice,onListItemClickListener =  this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupUI(activity_search,this)
        setSupportActionBar(toolbars)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        icBack.setOnClickListener {
            onBackPressed()
        }

        setDeviceListInAdapter()

        toolbars.title = "Search"
        search_view.setVoiceSearch(false)
        search_view.setHint(getString(R.string.search_hint_country))
        search_view.setEllipsize(true)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_district, menu)
        val item = menu.findItem(R.id.action_search)

        search_view.setMenuItem(item)
        return true
    }

    override fun onBackPressed() {
        if (search_view.isSearchOpen()) {
            search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    fun setDeviceListInAdapter(){
        firebaseDatabase.getReference("deviceList").addValueEventListener(object:ValueEventListener{
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
                    search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            adapter.filter.filter(newText.trim { it <= ' ' })
                            //Do some magic
                            return true
                        }
                    })

                    list_view.adapter = adapter
                    search_view.showSearch(false)
                    search_view.requestFocus()

                }else{
                    showValidationDialog("SI alarm","No device found");
                }
            }

        })
    }

}