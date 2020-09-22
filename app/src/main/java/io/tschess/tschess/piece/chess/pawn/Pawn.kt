package io.tschess.tschess.piece.chess.pawn

import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece

open class Pawn(
    name: String = "Pawn",
    imageDefault: Int = R.drawable.red_pawn,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Piece(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 1,
    attackList = listOf("Pawn"),
    chess = true
) {

    override fun validate(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        if(attack(present = present, propose = propose, matrix = matrix)) {
            return true
        }
        if(advanceTwo(present = present, propose = propose, matrix = matrix)) {
            return true
        }
        if(advanceOne(present = present, propose = propose, matrix = matrix)) {
            return true
        }
        return false
    }

    private fun advanceOne(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        if((present[0] - 1) - propose[0] == 0 && (present[1] - propose[1] == 0)) {
            if(matrix[present[0] - 1][present[1]] != null) {
                if(matrix[present[0] - 1][present[1]]!!.name == "PieceAnte") {
                    return true
                }
                return false
            }
            return true
        }
        return false
    }

    fun advanceTwo(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        if(this.touched){
            return false
        }
        if((present[0] - 2) - propose[0] == 0 && (present[1] - propose[1] == 0)) {
            if(matrix[present[0] - 2][present[1]] != null) {
                if(matrix[present[0] - 2][present[1]]!!.name != "PieceAnte") {
                    return false
                }
            } //it IS nil (2) ...
            if(matrix[present[0] - 1][present[1]] != null) {
                if(matrix[present[0] - 1][present[1]]!!.name != "PieceAnte") {
                    return false
                }
            } //it IS nil (1)
            //either they're both legal move or they're both nil...
            return !matrix[present[0]][present[1]]!!.touched
        }
        return false
    }

    private fun attack(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        if((present[0] - 1) - propose[0] == 0 && ((present[1] + 1) - propose[1] == 0)) {
            if(matrix[present[0] - 1][present[1] + 1] != null) {
                if(matrix[present[0] - 1][present[1] + 1]!!.name != "PieceAnte") {
                    return  !matrix[present[0] - 1][present[1] + 1]!!.compatriot(matrix[present[0]][present[1]]!!)
                }
            }
        }
        if((present[0] - 1) - propose[0] == 0 && ((present[1] - 1) - propose[1] == 0)) {
            if(matrix[present[0] - 1][present[1] - 1] != null) {
                if(matrix[present[0] - 1][present[1] - 1]!!.name != "PieceAnte") {

                    return  !matrix[present[0] - 1][present[1] - 1]!!.compatriot(matrix[present[0]][present[1]]!!)
                }
            }
        }
        return false
    }

    fun check(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        if((present[0] + 1) - propose[0] == 0 && ((present[1] + 1) - propose[1] == 0)) {
            if(matrix[present[0] + 1][present[1] + 1] != null) {
                if(matrix[present[0] + 1][present[1] + 1]!!.name != "PieceAnte") {
                    return  !matrix[present[0] + 1][present[1] + 1]!!.compatriot(matrix[present[0]][present[1]]!!)
                }
            }
        }
        if((present[0] + 1) - propose[0] == 0 && ((present[1] - 1) - propose[1] == 0)) {
            if(matrix[present[0] + 1][present[1] - 1] != null) {
                if(matrix[present[0] + 1][present[1] - 1]!!.name != "PieceAnte") {

                    return  !matrix[present[0] + 1][present[1] - 1]!!.compatriot(matrix[present[0]][present[1]]!!)
                }
            }
        }
        return false
    }
}