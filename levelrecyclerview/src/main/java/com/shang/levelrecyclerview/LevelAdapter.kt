package com.shang.levelrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LevelAdapter : RecyclerView.Adapter<LevelViewHolder>() {

    private val mData = listOf(
        R.drawable.icon_vip_level_0,
        R.drawable.icon_vip_level_1,
        R.drawable.icon_vip_level_2,
        R.drawable.icon_vip_level_3,
        R.drawable.icon_vip_level_4,
        R.drawable.icon_vip_level_5,
        R.drawable.icon_vip_level_6,
        R.drawable.icon_vip_level_7,
        R.drawable.icon_vip_level_8,
        R.drawable.icon_vip_level_9,
        R.drawable.icon_vip_level_10,
    )

    override fun getItemCount(): Int = mData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(mData[position])
    }
}