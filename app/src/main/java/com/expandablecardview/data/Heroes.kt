package com.expandablecardview.data

import java.util.*

/**
 * Created by Bhavik Makwana on 1/9/2018.
 */
data class Heroes(
        var id: Int = -1,
        var text: String? = null,
        var description: String? = null,
        var createdAt: Date = Date(),
        var updateAt: Date? = null
)