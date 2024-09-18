package com.example.andriod_game

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.andriod_game.utilities.Context
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textview.MaterialTextView

class StartMenuActivity : AppCompatActivity() {
    private lateinit var START_IMG_background : AppCompatImageView
    private lateinit var START_MTV_title :   MaterialTextView
    private lateinit var START_EFAB_play_game : ExtendedFloatingActionButton
    private lateinit var START_MTV_speedGame :MaterialTextView
    private lateinit var START_switch_speedSwitch :MaterialSwitch
    private lateinit var START_EFAB_scoreBoard : ExtendedFloatingActionButton
    private lateinit var START_MTV_HowToPlayGame :  MaterialTextView
    private lateinit var START_switch_HowToPlayGameSwitch :MaterialSwitch
    private lateinit var START_IMG_photoDown:AppCompatImageView

    private val linkBackgound:String  = "https://cdn.media.amplience.net/i/boconcept/15adafbb-ddea-45f5-a2ec-afd9008065f0?locale=*&w=3020&fmt=auto&upscale=false&sm=c&qlt=75&h=3020&%24auto-poi%24="
    private val linkPhotoDown:String = "https://cdn.pixabay.com/photo/2022/06/29/09/04/cockroach-7291324_1280.png"
    private var isItSensors :Boolean = true
    private var speedGameFast :Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_menu)
            findViews()
            initViews()
    }
    private fun findViews() {
        START_IMG_photoDown = findViewById(R.id.START_IMG_photoDown)
        START_IMG_background = findViewById(R.id.START_IMG_background)
        START_MTV_title = findViewById(R.id.START_MTV_title)
        START_EFAB_play_game = findViewById(R.id.START_EFAB_play_game)
        START_MTV_speedGame = findViewById(R.id.START_MTV_speedGame)
        START_switch_speedSwitch = findViewById(R.id.START_switch_speedSwitch)
        START_EFAB_scoreBoard = findViewById(R.id.START_EFAB_scoreBoard)
        START_MTV_HowToPlayGame = findViewById(R.id.START_MTV_HowToPlayGame)
        START_switch_HowToPlayGameSwitch = findViewById(R.id.START_switch_HowToPlayGameSwitch)
    }

    private fun initViews() {
        //backGround
        Glide
            .with(this)
            .load(linkBackgound)
            .centerCrop()
            .placeholder(R.drawable.unavailable_photo)
            .into(START_IMG_background);
        Glide
            .with(this)
            .load(linkPhotoDown)
            .centerCrop()
            .placeholder(R.drawable.unavailable_photo)
            .into(START_IMG_photoDown);



        //check switches
        START_switch_HowToPlayGameSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                START_MTV_HowToPlayGame.text ="Controller: Motion"
            else
                START_MTV_HowToPlayGame.text ="Controller: Buttons"
                isItSensors = isChecked
        }
        START_switch_speedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                START_MTV_speedGame.text ="Speed: Fast"
            else
                START_MTV_speedGame.text ="Speed: Slow"
            speedGameFast = isChecked
        }
        //buttons
        START_EFAB_play_game.setOnClickListener{v -> playGame()}
        START_EFAB_scoreBoard.setOnClickListener { v-> moveToScoreBoard() }
    }

    private fun moveToScoreBoard() {
        val intent = Intent(this, ScoreActivity::class.java);
        var bundle = Bundle()
        bundle.putString(Context.ACTIVITY_KEY, "start")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun playGame() {
        val intent = Intent(this, MainActivity::class.java);
        var bundle = Bundle()
        if (isItSensors)
            bundle.putString(Context.HOW_TO_PLAY_THE_GAME_KEY, "Sensors")
        else
            bundle.putString(Context.HOW_TO_PLAY_THE_GAME_KEY, "Buttons")
        if (speedGameFast)
            bundle.putString(Context.SPEED_KEY, "Fast")
        else
            bundle.putString(Context.SPEED_KEY, "Slow")
        bundle.putString(Context.ACTIVITY_KEY, "start")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

}