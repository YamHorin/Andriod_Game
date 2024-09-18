package com.example.andriod_game.logic

import android.util.Log
import com.example.andriod_game.Interfaces.CallBackSound
import com.example.andriod_game.models.MatrixManager

class GameManager (private val lifeCount: Int = 3){
    var callBackSound :CallBackSound? = null
    var score: Int = 0
        private set
    var currentIndex: Int = 2
        private set
    var lostLifes: Int = 0
        private set

    var matrixEnemy = MatrixManager.getFirstMatrix()

    val isGameLost: Boolean
        get() = lostLifes == lifeCount



    fun changesFrameEnemy() : Array<IntArray>
    {
        matrixEnemy = MatrixManager.modifyMatrix(matrixEnemy)
        return matrixEnemy
    }
    fun playerMove(index: Int)
    {
        currentIndex = index
    }
    fun checkCrash():Boolean
    {
        var bool:Boolean = false
        for (i in 0..4)
        {
            if (matrixEnemy[matrixEnemy.size-1][i]==1 && i == currentIndex)
            {
                Log.d("game notify" , "******* there is a crash in position $i ******")
                lostLifes++
                score-= POINTS
                bool = true
                callBackSound?.enemy()
            }
            if (matrixEnemy[matrixEnemy.size-1][i]==2 && i == currentIndex)
            {
                Log.d("game notify" , "******* you got coin at position $i ******")
                score+= 4*POINTS
                callBackSound?.coin()
            }
        }
        score+= POINTS
        Log.d("game notify" , "the score is  $score")
        return bool

    }
    companion object{
        private const val POINTS = 10
    }

}