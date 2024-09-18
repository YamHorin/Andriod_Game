package com.example.andriod_game

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.andriod_game.Interfaces.CallBackSound
import com.example.andriod_game.Interfaces.MoveCallback
import com.example.andriod_game.logic.GameManager
import com.example.andriod_game.models.MoveDetector
import com.example.andriod_game.utilities.Context
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var main_MTV_score: MaterialTextView
    private lateinit var gameManager: GameManager
    private lateinit var main_IMG_hearts: Array<ShapeableImageView>
    private lateinit var main_FAB_Left: ExtendedFloatingActionButton
    private lateinit var main_FAB_Right: ExtendedFloatingActionButton
    private lateinit var main_IMG_player_positions: Array<AppCompatImageView>
    private lateinit var main_IMG_enemy_matrix_position: Array<Array<AppCompatImageView>>
    private var currentPosition = 2
    private val linkBackground: String =
        "https://cdn.media.amplience.net/i/boconcept/15adafbb-ddea-45f5-a2ec-afd9008065f0?locale=*&w=3020&fmt=auto&upscale=false&sm=c&qlt=75&h=3020&%24auto-poi%24="
    private lateinit var main_IMG_background: AppCompatImageView
    private var speedGameNormal: Long = 1000
    private var speedGameFast: Long = 500
    private var speedGame: Long = 1000
    private var isItSensors: Boolean = false
    private lateinit var moveDetector: MoveDetector

    val handler: Handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            //reschedule
            Log.d("speedGame", "speedGame = $speedGame")
            handler.postDelayed(this, speedGame)
            //update ui
            refreshUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val bundle: Bundle? = intent.extras
        val controller = bundle?.getString(Context.HOW_TO_PLAY_THE_GAME_KEY)
        if (controller == "Sensors")
            this.isItSensors = true
        val speed = bundle?.getString(Context.SPEED_KEY)
        if (speed == "Fast")
            this.speedGame = speedGameFast
        else
            this.speedGame = speedGameNormal
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = GameManager()
        gameManager.callBackSound = object : CallBackSound {
            override fun enemy() {
                playSound(R.raw.woman , 1.45f)
            }

            override fun coin() {
                playSound(R.raw.coin , 1.1f)
            }
        }
        initViews()
        handler.postDelayed(runnable, 0)
    }

    private fun initViews() {


        if (isItSensors) {
            main_FAB_Right.visibility = View.INVISIBLE
            main_FAB_Left.visibility = View.INVISIBLE
            moveDetector = MoveDetector(
                this,
                object : MoveCallback {
                    override fun moveZ(z: Float) {
                        if (z > 0)
                            moveLeft()
                        else
                            moveRight()
                    }

                    override fun moveX(x: Float) {
                        val num: Int = x.toInt()
                        Log.d("move call back", "x = $num")
                        speedGame -= (num * 100)
                    }

                }
            )
            moveDetector.start()
        } else {
            main_FAB_Right.setOnClickListener { moveRight() }
            main_FAB_Left.setOnClickListener { moveLeft() }
        }
        for (i in 0..<main_IMG_player_positions.size) {
            if (i != 2)
                main_IMG_player_positions[i].visibility = View.INVISIBLE
        }
        //picture background
        Glide
            .with(this)
            .load(linkBackground)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(main_IMG_background)
        //put photos of the enemy and the player
        for (picture in main_IMG_player_positions) {
            Glide
                .with(this)
                .load(R.drawable.woman)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(picture)
        }
        for (array in main_IMG_enemy_matrix_position) {
            for (picture in array) {
                Glide
                    .with(this)
                    .load(R.drawable.cockroach)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(picture)
            }
        }

    }


    private fun moveLeft() {
        if (currentPosition > 0) {
            currentPosition--
            gameManager.playerMove(currentPosition)
            main_IMG_player_positions[currentPosition].visibility = View.VISIBLE
            main_IMG_player_positions[currentPosition + 1].visibility = View.INVISIBLE
            Log.d("player Move left", "***current position  = $currentPosition")
        }

    }

    private fun moveRight() {
        if (currentPosition < 4) {
            currentPosition++
            gameManager.playerMove(currentPosition)
            main_IMG_player_positions[currentPosition].visibility = View.VISIBLE
            main_IMG_player_positions[currentPosition - 1].visibility = View.INVISIBLE
            Log.d("playerMove right", "***current position  = $currentPosition")
        }
    }


    private fun findViews() {
        main_MTV_score = findViewById(R.id.main_MTV_score)
        main_FAB_Left = findViewById(R.id.main_FAB_Left)
        main_FAB_Right = findViewById(R.id.main_FAB_Right)
        main_IMG_background = findViewById(R.id.main_IMG_background)
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_IMG_player_positions = arrayOf(
            findViewById(R.id.main_ACIMGV_playPosition0),
            findViewById(R.id.main_ACIMGV_playPosition1),
            findViewById(R.id.main_ACIMGV_playPosition2),
            findViewById(R.id.main_ACIMGV_playPosition3),
            findViewById(R.id.main_ACIMGV_playPosition4)
        )
        //init map that connect image row col into id in R
        val imageMap = mapOf(
            "image00" to R.id.main_ACIMGV_enemyPosition_00,
            "image01" to R.id.main_ACIMGV_enemyPosition_01,
            "image02" to R.id.main_ACIMGV_enemyPosition_02,
            "image03" to R.id.main_ACIMGV_enemyPosition_03,
            "image04" to R.id.main_ACIMGV_enemyPosition_04,
            "image10" to R.id.main_ACIMGV_enemyPosition_10,
            "image11" to R.id.main_ACIMGV_enemyPosition_11,
            "image12" to R.id.main_ACIMGV_enemyPosition_12,
            "image13" to R.id.main_ACIMGV_enemyPosition_13,
            "image14" to R.id.main_ACIMGV_enemyPosition_14,
            "image20" to R.id.main_ACIMGV_enemyPosition_20,
            "image21" to R.id.main_ACIMGV_enemyPosition_21,
            "image22" to R.id.main_ACIMGV_enemyPosition_22,
            "image23" to R.id.main_ACIMGV_enemyPosition_23,
            "image24" to R.id.main_ACIMGV_enemyPosition_24,
            "image30" to R.id.main_ACIMGV_enemyPosition_30,
            "image31" to R.id.main_ACIMGV_enemyPosition_31,
            "image32" to R.id.main_ACIMGV_enemyPosition_32,
            "image33" to R.id.main_ACIMGV_enemyPosition_33,
            "image34" to R.id.main_ACIMGV_enemyPosition_34,
            "image40" to R.id.main_ACIMGV_enemyPosition_40,
            "image41" to R.id.main_ACIMGV_enemyPosition_41,
            "image42" to R.id.main_ACIMGV_enemyPosition_42,
            "image43" to R.id.main_ACIMGV_enemyPosition_43,
            "image44" to R.id.main_ACIMGV_enemyPosition_44,
            "image50" to R.id.main_ACIMGV_enemyPosition_50,
            "image51" to R.id.main_ACIMGV_enemyPosition_51,
            "image52" to R.id.main_ACIMGV_enemyPosition_52,
            "image53" to R.id.main_ACIMGV_enemyPosition_53,
            "image54" to R.id.main_ACIMGV_enemyPosition_54,
        )
        main_IMG_enemy_matrix_position = Array(6) { row ->
            Array(5) { col ->
                val imageName = "image$row$col"
                val imageId = imageMap[imageName]
                Log.d("main_IMG_enemy_matrix_position", "image$row$col has been found")
                findViewById(imageId!!)// Fetch the AppCompatImageView
            }
        }

    }

    private fun toast(text: String) {
        Toast
            .makeText(
                this,
                text,
                Toast.LENGTH_LONG
            ).show()
    }

    private fun refreshUI() {
        main_MTV_score.text = gameManager.score.toString()
        if (gameManager.isGameLost) { // Lost:
            Log.d("Game Status", "Game Over! " + gameManager.score)
            changeActivity(gameManager.score)
            handler.removeCallbacks(runnable)
        } else {
            //check crash
            val isItACrash: Boolean = gameManager.checkCrash()
            if (isItACrash) {
                toastAndVibrate("you lost a life!")
                main_IMG_hearts[main_IMG_hearts.size - gameManager.lostLifes].visibility =
                    View.INVISIBLE
            }

            //refresh enemy ui
            val matrixEnemy: Array<IntArray> = gameManager.changesFrameEnemy()
            for (i in 0..5) {
                for (j in 0..4) {
                    when (matrixEnemy[i][j]) {
                        0 -> main_IMG_enemy_matrix_position[i][j].visibility = View.INVISIBLE
                        1 -> {
                            main_IMG_enemy_matrix_position[i][j].visibility = View.VISIBLE
                            Glide
                                .with(this)
                                .load(R.drawable.cockroach)
                                .centerCrop()
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(main_IMG_enemy_matrix_position[i][j])
                        }

                        2 -> {
                            main_IMG_enemy_matrix_position[i][j].visibility = View.VISIBLE
                            Glide
                                .with(this)
                                .load(R.drawable.spray_cleaner)
                                .centerCrop()
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(main_IMG_enemy_matrix_position[i][j])
                        }

                    }

                }
            }
            Log.d("Game Status", "Game is continues :) score: " + gameManager.score)
        }

    }

    private fun vibrate() {
        val vibrator: Vibrator =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    this.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                this.getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val oneShotVibrate =
                VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(oneShotVibrate)
        } else
            vibrator.vibrate(500)

    }

    private fun toastAndVibrate(text: String) {
        toast(text)
        vibrate()
    }

    private fun changeActivity( score: Int) {
        val intent = Intent(this, ScoreActivity::class.java)
        var bundle = Bundle()
        bundle.putInt(Context.SCORE_KEY, score)
        bundle.putString(Context.ACTIVITY_KEY, "main")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()

    }

    private fun playSound(resId: Int , volume:Float) {
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val mediaPlayer = MediaPlayer.create(this, resId)
            mediaPlayer.isLooping = false
            mediaPlayer.setVolume(volume, volume)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp: MediaPlayer? ->
                var mpl = mp
                mpl!!.stop()
                mpl.release()
                mpl = null
            }
        }


    }

}