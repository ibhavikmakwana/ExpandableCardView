package com.expandablecardview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.expandablecardview.adapter.Adapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        rv_main.layoutManager = LinearLayoutManager(applicationContext)
        val summonerAdapter = Adapter(applicationContext)
        rv_main.adapter = summonerAdapter
    }
}