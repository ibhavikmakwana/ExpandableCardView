package com.expandablecardview.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.expandablecardview.data.HeroesContract.HeroesTable.CREATED_AT
import com.expandablecardview.data.HeroesContract.HeroesTable.ID
import com.expandablecardview.data.HeroesContract.HeroesTable.TABLE_NAME
import org.jetbrains.anko.db.transaction
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Bhavik Makwana on 1/9/2018.
 */
class HeroesDatabase(context: Context) {
    private val helper: HeroesOpenHelper = HeroesOpenHelper(context)

    /**
     * get all the records from the database
     */
    fun getAll(): List<Heroes> {
        val cursor = helper.readableDatabase.query(TABLE_NAME, null, null, null, null, null,
                CREATED_AT)
        return cursor.use(this::allFromCursor)
    }

    /**
     * get individual record from the database
     */
    fun getById(): Heroes {
        val cursor = helper.readableDatabase.query(TABLE_NAME, null, null, null, null, null,
                CREATED_AT)
        return cursor.use(this::fromCursor)
    }

    private fun fromCursor(cursor: Cursor): Heroes {
        var col = 0
        return Heroes().apply {
            id = cursor.getInt(col++)
            text = cursor.getString(col++)
            description = cursor.getString(col++)
            createdAt = Date(cursor.getLong(col++))
            updateAt = Date(cursor.getLong(col))
        }
    }

    private fun allFromCursor(cursor: Cursor): List<Heroes> {
        val retVal = ArrayList<Heroes>()
        while (cursor.moveToNext()) {
            retVal.add(fromCursor(cursor))
        }
        return retVal
    }

    private fun fromHero(hero: Heroes): ContentValues {
        return ContentValues().apply {
            val heroId = hero.id
            if (heroId != -1) {
                put(ID, heroId)
            }
            put(CREATED_AT, hero.createdAt.time)
            put(HeroesContract.HeroesTable.UPDATED_AT, hero.updateAt!!.time)
            put(HeroesContract.HeroesTable.TEXT, hero.text)
            put(HeroesContract.HeroesTable.DESC, hero.description)
        }
    }

    private fun fromHeroes(heroes: Array<out Heroes>): List<ContentValues> {
        val values = ArrayList<ContentValues>()
        heroes.mapTo(values) { fromHero(it) }
        return values
    }

    fun insert(vararg heroes: Heroes) {
        val values = fromHeroes(heroes)
        val db = helper.writableDatabase
        db.transaction {
            for (value in values) {
                insert(TABLE_NAME, null, value)
            }
        }
    }

    fun update(hero: Heroes) {
        val values = fromHero(hero)
        helper.writableDatabase.update(TABLE_NAME,
                values,
                BaseColumns._ID + " = ?",
                arrayOf(Integer.toString(hero.id)))
    }

    fun delete(hero: Heroes) {
        helper.writableDatabase.delete(TABLE_NAME,
                BaseColumns._ID + " = ?",
                arrayOf(Integer.toString(hero.id)))
    }

    fun loadAllByIds(vararg ids: Int): List<Heroes> {
        val questionMarks = ids.map { "?" }.joinToString { ", " }
        val args = ids.map { it.toString() }
        val selection = """$ID + " = ?""""
        val cursor = helper.readableDatabase.query(TABLE_NAME, null,
                selection,
                args.toTypedArray(), null, null, null)
        return cursor.use(this::allFromCursor)
    }
}