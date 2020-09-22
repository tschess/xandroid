package io.tschess.tschess.piece.chess.knight

import io.tschess.tschess.piece.Piece

class LogicKnight {

    val knight: String = "Knight"

    fun evaluate(coord: Array<Int>, affiliation: String, matrix: Array<Array<Piece?>>): Boolean {

        // minusTwo_minusOne
        if (coord[0] - 2 >= 0 && coord[1] - 1 >= 0) {
            if (evaluateName(attacker = knight, coord = arrayOf(coord[0] - 2, coord[1] - 1), affiliation = affiliation, matrix = matrix)){
                return true
            }
        }
        // minusOne_minusTwo
        if (coord[0] - 1 >= 0 && coord[1] - 2 >= 0) {
            if (evaluateName(attacker = knight, coord = arrayOf(coord[0] - 2, coord[1] - 1), affiliation = affiliation, matrix = matrix)){
                return true
            }
        }
        // minusTwo_plusOne
        if (coord[0] - 2 >= 0 && coord[1] + 1 >= 0) {
            if (evaluateName(attacker = knight, coord = arrayOf(coord[0] - 2, coord[1] - 1), affiliation = affiliation, matrix = matrix)){
                return true
            }
        }
        // minusOne_plusTwo
        if (coord[0] - 1 >= 0 && coord[1] + 2 >= 0) {
            if (evaluateName(attacker = knight, coord = arrayOf(coord[0] - 2, coord[1] - 1), affiliation = affiliation, matrix = matrix)){
                return true
            }
        }
        // plusTwo_minusOne
        if (coord[0] + 2 >= 0 && coord[1] - 1 >= 0) {
            if (evaluateName(attacker = knight, coord = arrayOf(coord[0] - 2, coord[1] - 1), affiliation = affiliation, matrix = matrix)){
                return true
            }
        }
        // plusTwo_plusOne
        if (coord[0] + 2 >= 0 && coord[1] + 1 >= 0) {
            if (evaluateName(attacker = knight, coord = arrayOf(coord[0] - 2, coord[1] - 1), affiliation = affiliation, matrix = matrix)){
                return true
            }
        }
        // plusOne_minusTwo
        if (coord[0] + 1 >= 0 && coord[1] - 2 >= 0) {
            if (evaluateName(attacker = knight, coord = arrayOf(coord[0] - 2, coord[1] - 1), affiliation = affiliation, matrix = matrix)){
                return true
            }
        }
        // plusOne_plusTwo
        if (coord[0] + 1 >= 0 && coord[1] + 2 >= 0) {
            if (evaluateName(attacker = knight, coord = arrayOf(coord[0] - 2, coord[1] - 1), affiliation = affiliation, matrix = matrix)){
                return true
            }
        }
        return false
    }

    private fun evaluateName(attacker: String, coord: Array<Int>, affiliation: String, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (coord[0] >= 0 && coord[0] <= 7 && coord[1] >= 0 && coord[1] <= 7) {
            if (matrix[coord[0]][coord[1]] != null) {
                val occupant: Piece? = matrix[coord[0]][coord[1]]
                if (affiliation != occupant!!.affiliation) {
                    val name: String = occupant.name
                    if (name.contains(attacker)) {
                        return true
                    }
                }
            }
        }
        return false
    }

}