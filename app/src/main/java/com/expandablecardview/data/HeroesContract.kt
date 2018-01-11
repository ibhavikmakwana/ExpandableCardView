package com.expandablecardview.data

import com.expandablecardview.data.HeroesContract.HeroesTable.CREATED_AT
import com.expandablecardview.data.HeroesContract.HeroesTable.DESC
import com.expandablecardview.data.HeroesContract.HeroesTable.ID
import com.expandablecardview.data.HeroesContract.HeroesTable.TABLE_NAME
import com.expandablecardview.data.HeroesContract.HeroesTable.TEXT
import com.expandablecardview.data.HeroesContract.HeroesTable.UPDATED_AT

/**
 * Created by Bhavik Makwana on 1/9/2018.
 */
object HeroesContract {
    object HeroesTable {
        val ID = "id"
        val TABLE_NAME = "heroes"
        val TEXT = "text"
        val DESC = "description"
        val CREATED_AT = "created_at"
        val UPDATED_AT = "updated_at"
    }

    val SQL_CREATE_ENTRIES = """CREATE TABLE" $TABLE_NAME (
        $ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        $TEXT TEXT,
        $DESC TEXT,
        $CREATED_AT INTEGER,
        $UPDATED_AT INTEGER
    )""".trimMargin()

    val SQL_DELETE_ENTRIES = """DROP TABLE IF EXISTS" $TABLE_NAME"""

    val SQL_QUERY_ALL =
            "SELECT * FROM $TABLE_NAME ORDER BY $CREATED_AT DESC"
}