package com.shang.levelprogress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var levelProgress: LevelProgress
    private lateinit var button: Button
    private var mLevel = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        levelProgress = findViewById(R.id.LevelProgress)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            mLevel++
            mLevel %= 7
            levelProgress.setLevel(mLevel)
        }
    }
}