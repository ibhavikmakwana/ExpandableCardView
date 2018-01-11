package com.expandablecardview.data

import android.content.Context
import org.jetbrains.anko.doAsync

/**
 * Created by Bhavik Makwana on 1/9/2018.
 */
object DataStore {

    @JvmStatic
    lateinit var heroes: HeroesDatabase
        private set

    fun init(context: Context) {
        heroes = HeroesDatabase(context)
    }

    fun execute(runnable: Runnable) {
        execute { runnable.run() }
    }

    fun execute(fn: () -> Unit) {
        doAsync { fn() }
    }
}