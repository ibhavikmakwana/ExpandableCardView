package com.expandablecardview

import android.app.Application
import com.expandablecardview.data.DataStore

/**
 * Created by Bhavik Makwana on 1/9/2018.
 */
class HeroesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DataStore.init(this)
    }
}