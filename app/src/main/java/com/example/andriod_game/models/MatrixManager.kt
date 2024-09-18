package com.example.andriod_game.models


class MatrixManager {
    companion object{
        private var matrix: Array<IntArray> = Array(6){
            intArrayOf(0,0,0,0,0)
        }
        fun modifyMatrix(matrix: Array<IntArray>) :Array<IntArray>
        {
            var matrixReturn: Array<IntArray> = matrix
            for (i in 5 downTo 1){
                for(j in 4 downTo 0)
                {
                    matrixReturn[i][j] = matrixReturn[i-1][j]
                }
            }
            matrixReturn[0] =  intArrayOf(0,0,0,0,0)
            matrixReturn[0][(0..4).random()] = 1
            //add coins
            matrixReturn[0][(0..4).random()] = 2
            return matrixReturn
        }
        fun getFirstMatrix():Array<IntArray>
        {
            val matrix: Array<IntArray> = this.matrix
            return matrix
        }

    }
}