package com.example.andriod_game.models

import android.util.Log


class MatrixManager {
    companion object{
        private var matrix: Array<IntArray> = Array(4){
            intArrayOf(0,0,0)
        }
        fun modifyMatrix(matrix: Array<IntArray>) :Array<IntArray>
        {
            var matrixReturn: Array<IntArray> = matrix
            for (i in 3 downTo 1){
                for(j in 2 downTo 0)
                {
                    matrixReturn[i][j] = matrixReturn[i-1][j]
                }
            }
            matrixReturn[0] =  intArrayOf(0,0,0)
            matrixReturn[0][(0..2).random()] = 1
            Log.d("matrix in the game " , matrixReturn.toString())
            return matrixReturn
        }


        fun getFirstMatrix():Array<IntArray>
        {

            var matrix: Array<IntArray> = this.matrix
            Log.d(" first matrix in the game " , matrix.toString())

            return matrix
        }

    }
}