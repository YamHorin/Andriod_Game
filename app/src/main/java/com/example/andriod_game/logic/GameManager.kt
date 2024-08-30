package com.example.andriod_game.logic

import android.util.Log
import com.example.andriod_game.models.MatrixManager

class GameManager (private val lifeCount: Int = 3){
    //save for part 2 of the project
    var score: Int = 0
        private set
    var currentIndex: Int = 1
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
        for (i in 0..2)
        {
            if (matrixEnemy[matrixEnemy.size-1][i]==1 && i == currentIndex)
            {
                Log.d("game notify" , "******* there is a crash in position $i ******")
                lostLifes++
                score-= POINTS
                bool = true
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