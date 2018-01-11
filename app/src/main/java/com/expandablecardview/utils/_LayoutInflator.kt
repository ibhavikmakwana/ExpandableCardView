package com.expandablecardview.utils

import android.content.Context
import android.view.LayoutInflater

/**
 * Created by Bhavik Makwana on 1/4/2018.
 */
val Context.layoutInflator get() = LayoutInflater.from(this)