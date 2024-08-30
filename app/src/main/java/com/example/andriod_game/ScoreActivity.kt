package com.example.andriod_game

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.andriod_game.utilities.Context
import com.google.android.material.textview.MaterialTextView

class ScoreActivity : AppCompatActivity() {
    private lateinit var score_LBL_result: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_score)
        findviews()
        initViews()
        }

    private fun findviews() {
        score_LBL_result = findViewById(R.id.score_LBL_result)    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras

        //save for part 2
        val score = bundle?.getInt(Context.SCORE_KEY, 0)
        val message = bundle?.getString(Context.STATUS_KEY)

        score_LBL_result.text ="$message \n "
    }
}