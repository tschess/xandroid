package io.tschess.tschess.piece.chess.pawn

import io.tschess.tschess.piece.Piece

class LogicPawn {

    val pawn: String = "Pawn"

    fun c0_m1_c1_m1(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] - 1 >= 0 && coordinate[1] - 1 >= 0) {
            return evaluateName(
                attacker = pawn,
                coordinate = arrayOf(coordinate[0] - 1, coordinate!![1] - 1),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun c0_m1_c1_p1(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] - 1 >= 0 && coordinate[1] + 1 <= 7) {
            return evaluateName(
                attacker = pawn,
                coordinate = arrayOf(coordinate[0] - 1, coordinate!![1] + 1),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun evaluateName(
        attacker: String,
        coordinate: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>
    ): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (coordinate[0] >= 0 && coordinate[0] <= 7 && coordinate[1] >= 0 && coordinate[1] <= 7) {
            if (state[coordinate[0]][coordinate[1]] != null) {
                val occupant = state[coordinate[0]][coordinate[1]]!!
                if (affiliation != occupant.affiliation) {
                    val name = occupant.name
                    if (name.contains(attacker)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}