package io.tschess.tschess.tschess.logic

import io.tschess.tschess.piece.Piece
import io.tschess.tschess.tschess.ActivityTschess
import kotlin.math.abs

class PromoLogic(var activityTschess: ActivityTschess) {

    lateinit var coord: Array<Int>

    fun evaluate(coord: Array<Int>): Boolean {
        this.coord = coord
        val coordXX: Array<Int> = this.activityTschess.transitioner.getCoord()!!
        val piece: Piece = this.activityTschess.matrix[coordXX[0]][coordXX[1]] ?: return false
        if (!piece.name.contains("Pawn") && !piece.name.contains("Poison") ) {
            return false
        }
        val rank: Boolean = coord[0] == 0
        val move: Boolean = abs(coordXX[0] - coord[0]) == 1
        if (!rank || !move) {
            return false
        }
        return true
    }

}