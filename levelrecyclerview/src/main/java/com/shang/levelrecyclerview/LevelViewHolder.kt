package com.shang.levelrecyclerview

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val layoutRoot: ConstraintLayout = itemView.findViewById(R.id.layoutRoot)
    private val ivLevel: ImageView = itemView.findViewById(R.id.ivLevel)

    init {
        itemView.setOnClickListener {
            (it.parent as RecyclerView).smoothScrollToPosition(layoutPosition)
        }
    }

    fun bind(drawable: Int) {
        ivLevel.setImageResource(drawable)
    }
}