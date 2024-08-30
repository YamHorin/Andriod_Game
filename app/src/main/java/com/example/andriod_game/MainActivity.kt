package com.example.andriod_game

import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.andriod_game.logic.GameManager
import com.example.andriod_game.utilities.Context
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView

class MainActivity : AppCompatActivity() {
    private lateinit var gameManager: GameManager
    private lateinit var main_IMG_hearts: Array<ShapeableImageView>
    private lateinit var main_FAB_Left: ExtendedFloatingActionButton
    private lateinit var main_FAB_Right:ExtendedFloatingActionButton
    private lateinit var main_IMG_playe_positions :Array<AppCompatImageView>
    private lateinit var main_IMG_enemy_matrix_position: Array<Array<AppCompatImageView>>
    private  var currentPosition = 1
    private val linkBackgound:String  = "https://cdn.media.amplience.net/i/boconcept/15adafbb-ddea-45f5-a2ec-afd9008065f0?locale=*&w=3020&fmt=auto&upscale=false&sm=c&qlt=75&h=3020&%24auto-poi%24="
    private lateinit var main_IMG_background:AppCompatImageView

    val handler: Handler = Handler(Looper.getMainLooper())
    val runnable: Runnable = object : Runnable {
        override fun run() {
            //reschedule
            handler.postDelayed(this,1000L  )
            //update ui
            refresUI()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = GameManager()
        initViews()
       handler.postDelayed(runnable, 0)
        }

    private fun initViews() {
        main_FAB_Right.setOnClickListener { v -> moveRight() }
        main_FAB_Left.setOnClickListener { v ->moveLeft() }
        main_IMG_playe_positions[currentPosition-1].visibility = View.INVISIBLE
        main_IMG_playe_positions[currentPosition+1].visibility = View.INVISIBLE
        //picture background
        Glide
            .with(this)
            .load(linkBackgound)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(main_IMG_background);
        //put photos of the enemy and the player
        for (picture in main_IMG_playe_positions)
        {
            Glide
                .with(this)
                   .load(R.drawable.woman)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(picture);
        }
        for (array in main_IMG_enemy_matrix_position)
        {
            for (picture in array)
            {
                Glide
                    .with(this)
                    .load(R.drawable.cockroach)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(picture);
            }
        }

    }

    private fun moveLeft() {
        if (currentPosition>0)
        {
            currentPosition--;
            gameManager.playerMove(currentPosition)
            main_IMG_playe_positions[currentPosition].visibility = View.VISIBLE
            main_IMG_playe_positions[currentPosition+1].visibility = View.INVISIBLE
            Log.d("player Move left" , "***current position  = $currentPosition")
        }

    }

    private fun moveRight() {
        if (currentPosition<2)
        {
            currentPosition++;
            gameManager.playerMove(currentPosition)
            main_IMG_playe_positions[currentPosition].visibility = View.VISIBLE
            main_IMG_playe_positions[currentPosition-1].visibility = View.INVISIBLE
            Log.d("playerMove right" , "***current position  = $currentPosition")
        }
    }


    private fun findViews() {
        main_FAB_Left = findViewById(R.id.main_FAB_Left)
        main_FAB_Right = findViewById(R.id.main_FAB_Right)
        main_IMG_background = findViewById(R.id.main_IMG_background)
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_IMG_playe_positions = arrayOf(
            findViewById(R.id.main_ACIMGV_playPosition0),
            findViewById(R.id.main_ACIMGV_playPosition1),
            findViewById(R.id.main_ACIMGV_playPosition2)
        )
        //init map that connect image row col into id in R
        val imageMap = mapOf(
            "image00" to R.id.main_ACIMGV_enemyPosition_00,
            "image01" to R.id.main_ACIMGV_enemyPosition_01,
            "image02" to R.id.main_ACIMGV_enemyPosition_02,
            "image10" to R.id.main_ACIMGV_enemyPosition_10,
            "image11" to R.id.main_ACIMGV_enemyPosition_11,
            "image12" to R.id.main_ACIMGV_enemyPosition_12,
            "image20" to R.id.main_ACIMGV_enemyPosition_20,
            "image21" to R.id.main_ACIMGV_enemyPosition_21,
            "image22" to R.id.main_ACIMGV_enemyPosition_22,
            "image30" to R.id.main_ACIMGV_enemyPosition_30,
            "image31" to R.id.main_ACIMGV_enemyPosition_31,
            "image32" to R.id.main_ACIMGV_enemyPosition_32
        )
        main_IMG_enemy_matrix_position = Array(4) { row ->
            Array(3) { col ->
                val imageName = "image$row$col"
                val imageId = imageMap[imageName]
                Log.d("main_IMG_enemy_matrix_position" , "image$row$col has been found")
                findViewById<AppCompatImageView>(imageId!!)// Fetch the AppCompatImageView
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
    //TODO
    private fun refresUI()
    {
        if (gameManager.isGameLost) { // Lost:
            Log.d("Game Status", "Game Over! " + gameManager.score)
            changeActivity("Game Over! ", gameManager.score)
            handler.removeCallbacks(runnable)
        }
        else{
            //check crash
            val isItACrash:Boolean = gameManager.checkCrash()
            if (isItACrash)
            {
                toastAndVibrate("you lost a life!")
                main_IMG_hearts[main_IMG_hearts.size - gameManager.lostLifes].visibility =
                    View.INVISIBLE
            }

           //refres enemy ui
            val matrixEnemy: Array<IntArray> = gameManager.changesFrameEnemy()
            Log.d("matrix" , matrixEnemy.toString())
            for (i in 0..3)
            {
                for(j in 0..2)
                {
                    if (matrixEnemy[i][j]==1)
                        main_IMG_enemy_matrix_position[i][j].visibility = View.VISIBLE
                    else
                        main_IMG_enemy_matrix_position[i][j].visibility = View.INVISIBLE

                }
            }
            Log.d("Game Status", "Game is continues :) socore: " + gameManager.score)
        }

    }
    private fun vibrate() {
        val vibrator:Vibrator  =
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.S){
                val vibratorManager = this.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            }
            else{
                this.getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            val oneShotVibrate = VibrationEffect.createOneShot(500 , VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(oneShotVibrate)
        }
        else
            vibrator.vibrate(500)

    }
    private fun toastAndVibrate(text: String) {
        toast(text)
        vibrate()
    }
    //TODO
    private fun changeActivity(string: String, score: Int) {
        val intent = Intent(this, ScoreActivity::class.java);
        var bundle = Bundle()
        bundle.putInt(Context.SCORE_KEY, score)
        bundle.putString(Context.STATUS_KEY, string)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()

    }

}

