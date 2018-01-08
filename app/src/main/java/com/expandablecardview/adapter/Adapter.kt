package com.expandablecardview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.expandablecardview.R
import com.expandablecardview.base.ExpandableLayout
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class Adapter(context: Context) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mExpandedPositionSet = HashSet<Int>()
    private var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = mInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(item)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.updateItem(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun getItemCount(): Int {
        return 20
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun updateItem(position: Int) {
            itemView.expandable_layout.setOnExpandListener(object : ExpandableLayout.OnExpandListener {
                override fun onExpand(expanded: Boolean) {
                    registerExpand(position, itemView)
                }
            })
            itemView.expandable_layout.setExpand(mExpandedPositionSet.contains(position))

        }
    }

    private fun registerExpand(position: Int, itemView: View) {
        if (mExpandedPositionSet.contains(position)) {
            removeExpand(position)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.image_nav.setImageDrawable(mContext.getDrawable(R.drawable.ic_arrow_drop_down_black_24dp))
            }
        } else {
            addExpand(position)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.image_nav.setImageDrawable(mContext.getDrawable(R.drawable.ic_arrow_drop_up_black_24dp))
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