package com.expandablecardview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.expandablecardview.R
import com.expandablecardview.data.DataStore
import com.expandablecardview.data.Heroes
import kotlinx.android.synthetic.main.activity_add_data.*
import java.util.*

class AddHeroes : AppCompatActivity() {
    var id: Int? = null

    /**
     * Call this method to launch the activity.
     */
    companion object {
        val HEROES_ID = "HEROES_ID"

        fun launchActivity(context: Context) {
            val intent = Intent(context, AddHeroes::class.java)
            context.startActivity(intent)
        }

        fun launchActivity(context: Context, id: Int) {
            val intent = Intent(context, AddHeroes::class.java)
            intent.putExtra(HEROES_ID, id)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)
        id = intent.getIntExtra(HEROES_ID, -1)
        if (id!! > -1) {
            getHeroesUsingId(id!!)
        }
    }

    private fun getHeroesUsingId(id: Int) {
        DataStore.execute {
            val heroes = DataStore.heroes.loadAllByIds(id)
            Handler(Looper.getMainLooper()).post {
                val hero: List<Heroes> = heroes
                tv_title.setText(hero[id].text)
                tv_desc.setText(hero[id].description)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_accept, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_accept -> {
                save()
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        DataStore.execute(Runnable {
            val hero = updateHeroes()
            DataStore.heroes.insert(hero)
        })
        /*Toast.makeText(this, "Hero Saved", Toast.LENGTH_SHORT).show()*/
    }

    private fun updateHeroes(): Heroes {
        val hero = Heroes()
        hero.text = tv_title.text.toString()
        hero.description = tv_desc.text.toString()
        hero.updateAt = Date()
        return hero
    }
}