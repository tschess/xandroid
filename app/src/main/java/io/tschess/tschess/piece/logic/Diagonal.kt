package io.tschess.tschess.piece.logic

import io.tschess.tschess.piece.Piece
import kotlin.math.abs

class Diagonal {

    fun recurseInto(coordinate: Array<Int>, direction: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        val coord00: Array<Int> = arrayOf(coordinate[0], direction[0])
        val bound00: Boolean = bounded(coordinate = coord00)

        val coord01: Array<Int> = arrayOf(coordinate[1], direction[1])
        val bound01: Boolean = bounded(coordinate = coord01)

        if (bound00 && bound01) {

            val xx00: Boolean = abs(direction[0]) <= 1
            val xx01: Boolean = abs(direction[1]) <= 1

            if (xx00 && xx01) {
                val candidate: Piece? = state[coordinate[0] + direction[0]][coordinate[1] + direction[1]]
                if (candidate != null) {
                    if (candidate.name == "PieceAnte") {
                        return true
                    }
                    return false
                }
                return true
            }

            val mm00 = state[coordinate[0] + direction[0]][coordinate[1] + direction[1]] == null
            val mm01 =  state[coordinate[0] + direction[0]][coordinate[1] + direction[1]]?.name == "PieceAnte"

            if (mm00 || mm01) {
                var displacementRow = 0
                if (direction[0] != 0) {
                    //displacementRow = direction[0] < 0 ? direction[0] + 1 : direction[0] - 1
                    if (direction[0] < 0) {
                        displacementRow = direction[0] + 1
                    } else {
                        displacementRow = direction[0] - 1
                    }
                }
                var displacementColumn = 0
                if (direction[1] != 0) {
                    //displacementColumn = direction[1] < 0 ? direction[1] + 1 : direction[1] - 1
                    if (direction[1] < 0) {
                        displacementColumn = direction[1] + 1
                    } else {
                        displacementColumn = direction[1] - 1
                    }
                }
                return recurseInto(
                    coordinate = coordinate,
                    direction = arrayOf(displacementRow, displacementColumn),
                    state = state
                )
            }
        }
        return false
    }

    fun forSearchCriteria(present: Array<Int>, proposed: Array<Int>, directionA: Array<Int>, directionB: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        val equivalentMagnitude: Boolean = equivalentMagnitude(directionA = directionA, directionB = directionB)
        val operatorInRange: Boolean = operatorInRange(directionA = directionA, directionB = directionB)
        if (equivalentMagnitude && operatorInRange) {
            return recurseInto(coordinate = present, direction = directionB, state = state)
        }
        return false
    }

    fun equivalentMagnitude(directionA: Array<Int>, directionB: Array<Int>): Boolean {
        return directionA[0] * directionB[0] == directionA[1] * directionB[1]
    }

    fun operatorInRange(directionA: Array<Int>, directionB: Array<Int>): Boolean {
        return (directionA[0] * directionB[0]) >= 1 && (directionA[0] * directionB[0]) <= 6 &&
                (directionA[1] * directionB[1]) >= 1 && (directionA[1] * directionB[1]) <= 6
    }

    fun bounded(coordinate: Array<Int>): Boolean {
        return coordinate[0] + coordinate[1] in 0..7
    }

    fun operatorIsZero(coordinate: Array<Int>): Boolean {
        return coordinate[0] == 0 && coordinate[1] == 0
    }

    fun plusPlus(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        return validator(directionA = arrayOf(+1, +1), present = present, proposed = proposed, state = state)
    }

    fun minusPlus(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        return validator(directionA = arrayOf(-1, +1), present = present, proposed = proposed, state = state)
    }

    fun plusMinus(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        return validator(directionA = arrayOf(+1, -1), present = present, proposed = proposed, state = state)
    }

    fun minusMinus(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        return validator(directionA = arrayOf(-1, -1), present = present, proposed = proposed, state = state)
    }

    fun validator(directionA: Array<Int>, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        val displacementRow: Int = proposed[0] - present[0] - directionA[0]
        val displacementColumn: Int  = proposed[1] - present[1] - directionA[1]
        val directionB: Array<Int> = arrayOf(displacementRow, displacementColumn)
        val operatorIsZero: Boolean = operatorIsZero(coordinate = directionB)
        if (operatorIsZero) {
            return true
        }
        return forSearchCriteria(present = present, proposed = proposed, directionA = directionA, directionB = directionB, state = state)
    }

}