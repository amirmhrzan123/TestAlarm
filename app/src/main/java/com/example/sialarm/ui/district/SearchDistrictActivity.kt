package com.example.sialarm.ui.district

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseActivity
import com.example.sialarm.databinding.ActivitySearchBinding
import com.example.sialarm.ui.homepage.MainViewModel
import com.example.sialarm.utils.extensions.District
import com.example.sialarm.utils.extensions.getDistrictsKeyValue
import com.example.sialarm.utils.extensions.setupUI
import com.google.gson.Gson
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject

class SearchDistrictActivity : BaseActivity<MainViewModel, ActivitySearchBinding>(),DistrictListAdapter.OnListItemClickListener{

    val gson =  Gson()

    override fun onItemClick(district: District) {
        val output = Intent()
        output.putExtra("Extra",gson.toJson(district))
        setResult(RESULT_OK, output)
        finish()
    }

    override fun getLayoutId(): Int = R.layout.activity_search

    private val mainViewModel : MainViewModel by inject()

    override fun getViewModel(): MainViewModel = mainViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private val adapter : DistrictListAdapter by lazy{
        DistrictListAdapter(this@SearchDistrictActivity, getDistrictsKeyValue(),this@SearchDistrictActivity)
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

        toolbars.title = "Search"
        search_view.setVoiceSearch(false)
        search_view.setHint(getString(R.string.search_hint_country))
        search_view.setEllipsize(true)
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

}