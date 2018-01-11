package com.expandablecardview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.expandablecardview.R
import com.expandablecardview.adapter.Adapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        fab_add_data.setOnClickListener {
            AddHeroes.launchActivity(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    public override fun onDestroy() {
        super.onDestroy()
        rv_list_main!!.adapter = null
    }

    private fun setUpRecyclerView() {
        rv_list_main!!.layoutManager = LinearLayoutManager(this)
        rv_list_main!!.adapter = Adapter(this)
        //To add the spacing
        /*rv_list_main!!.addItemDecoration(SpaceItemDecoration(this, R.dimen.margin_small))*/
    }

    private fun refresh() {
        (rv_list_main!!.adapter as Adapter).refresh()
    }
}