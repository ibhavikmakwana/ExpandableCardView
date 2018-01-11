package com.expandablecardview.adapter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.expandablecardview.R
import com.expandablecardview.base.ExpandableLayout
import com.expandablecardview.data.DataStore
import com.expandablecardview.data.Heroes
import com.expandablecardview.utils.layoutInflator
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class Adapter(private val context: Context) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val mExpandedPositionSet = HashSet<Int>()
    private var heroes: List<Heroes> = ArrayList()
    private var isRefreshing = false

    init {
        setHasStableIds(true)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        refresh()
    }

    override fun getItemId(position: Int): Long {
        return heroes[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return heroes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = context.layoutInflator.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hero = heroes[position]
        holder.text.text = hero.text
        holder.desc.text = hero.description

        holder.updateItem(position)
    }

    fun refresh() {
        if (isRefreshing) return
        isRefreshing = true
        DataStore.execute {
            val heroes = DataStore.heroes.getAll()
            Handler(Looper.getMainLooper()).post {
                this@Adapter.heroes = heroes
                notifyDataSetChanged()
                isRefreshing = false
            }
        }
    }

    /*inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text = itemView.tv_title
        var desc = itemView.tv_desc
        fun updateItem(position: Int) {
            itemView.expandable_layout.setOnExpandListener(object : ExpandableLayout.OnExpandListener {
                override fun onExpand(expanded: Boolean) {
                    registerExpand(position, itemView)
                }
            })
            itemView.expandable_layout.setExpand(mExpandedPositionSet.contains(position))

        }
    }*/

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text = itemView.tv_title
        var desc = itemView.tv_desc

        fun updateItem(position: Int) {
            val hero = heroes[position]

            itemView.expandable_layout.setOnExpandListener(object : ExpandableLayout.OnExpandListener {
                override fun onExpand(expanded: Boolean) {
                    registerExpand(position, itemView)
                }
            })
            itemView.expandable_layout.setExpand(mExpandedPositionSet.contains(position))
            /*itemView.tv_title.setOnClickListener {
                AddHeroes.launchActivity(context, hero.id)
            }*/
        }
    }

    private fun registerExpand(position: Int, itemView: View) {
        if (mExpandedPositionSet.contains(position)) {
            removeExpand(position)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.image_nav.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_drop_down_black_24dp))
            }
        } else {
            addExpand(position)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.image_nav.setImageDrawable(context.getDrawable(R.drawable.ic_arrow_drop_up_black_24dp))
            }
        }
    }

    private fun removeExpand(position: Int) {
        mExpandedPositionSet.remove(position)
    }

    private fun addExpand(position: Int) {
        mExpandedPositionSet.add(position)
    }
}