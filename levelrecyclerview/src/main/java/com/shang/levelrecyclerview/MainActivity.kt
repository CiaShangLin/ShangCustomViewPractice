package com.shang.levelrecyclerview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.pdf.PdfDocument.Page
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var rvLevel: LevelRecyclerView
    private lateinit var tvLevel: TextView
    private val mLevelAdapter by lazy {
        LevelAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLevel = findViewById(R.id.tvLevel)
        rvLevel = findViewById(R.id.rvLevel)
        rvLevel.adapter = mLevelAdapter
        rvLevel.setOnLevelChangeListener(object : LevelRecyclerView.OnLevelChangeListener {
            override fun onLevelChange(position: Int) {
                tvLevel.text = "等級:${position}"
            }
        })

    }
}