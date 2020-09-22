package io.tschess.tschess.piece.chess.king.logic


import io.tschess.tschess.piece.Piece

class LogicKing {

    val king: String = "King"

    fun minusMinus(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] - 1 >= 0 && coordinate[1] - 1 >= 0) {
            return evaluateName(
                attacker = king,
                coordinate = arrayOf(coordinate[0] - 1, coordinate[1] - 1),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun minusPlus(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] - 1 >= 0 && coordinate[1] + 1 <= 7) {
            return evaluateName(
                attacker = king,
                coordinate = arrayOf(coordinate[0] - 1, coordinate[1] + 1),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun plusMinus(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] + 1 <= 7 && coordinate[1] - 1 >= 0) {
            return evaluateName(
                attacker = king,
                coordinate = arrayOf(coordinate[0] + 1, coordinate[1] - 1),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun plusPlus(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] + 1 <= 7 && coordinate[1] + 1 <= 7) {
            return evaluateName(
                attacker = king,
                coordinate = arrayOf(coordinate[0] + 1, coordinate[1] + 1),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun zeroMinus(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] - 1 >= 0) {
            return evaluateName(
                attacker = king,
                coordinate = arrayOf(coordinate[0] - 1, coordinate[1]),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun zeroPlus(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] + 1 <= 7) {
            return evaluateName(
                attacker = king,
                coordinate = arrayOf(coordinate[0] + 1, coordinate[1]),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun oneMinus(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[1] - 1 <= 7) {
            return evaluateName(
                attacker = king,
                coordinate = arrayOf(coordinate[0], coordinate[1] - 1),
                affiliation = affiliation,
                state = state
            )
        }
        return false
    }

    fun onePlus(coordinate: Array<Int>, affiliation: String, state: Array<Array<Piece?>>): Boolean {
        if (coordinate[1] + 1 <= 7) {
            return evaluateName(
                attacker = king,
                coordinate = arrayOf(coordinate[0], coordinate[1] + 1),
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

                if (affiliation.toLowerCase() != occupant.affiliation.toLowerCase()) {
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