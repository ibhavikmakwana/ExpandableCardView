package com.expandablecardview.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.expandablecardview.data.HeroesContract.SQL_CREATE_ENTRIES
import com.expandablecardview.data.HeroesContract.SQL_DELETE_ENTRIES

/**
 * Created by Bhavik Makwana on 1/9/2018.
 */
class HeroesOpenHelper(context: Context) : SQLiteOpenHelper(context, "heroes.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}