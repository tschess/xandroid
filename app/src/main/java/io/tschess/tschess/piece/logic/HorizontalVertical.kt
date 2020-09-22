package io.tschess.tschess.piece.logic

import io.tschess.tschess.piece.Piece
import kotlin.math.abs

class HorizontalVertical {

    fun recurseInto(coordinate: Array<Int>, direction: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        if (bounded(coordinate = arrayOf(coordinate[0], direction[0])) && bounded(
                coordinate = arrayOf(
                    coordinate[1],
                    direction[1]
                )
            )
        ) {
            if (abs(direction[0]) <= 1 && abs(direction[1]) <= 1) {
                if (matrix[coordinate[0] + direction[0]][coordinate[1] + direction[1]] != null) {
                    if (matrix[coordinate[0] + direction[0]][coordinate[1] + direction[1]]!!.name == "PieceAnte") {
                        return true
                    }
                    return false
                }
                return true
            }
            if (matrix[coordinate[0] + direction[0]][coordinate[1] + direction[1]] == null ||
                matrix[coordinate[0] + direction[0]][coordinate[1] + direction[1]]!!.name == "PieceAnte"
            ) {
                var displacementRow = 0
                if (direction[0] != 0) {
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
                    matrix = matrix
                )
            }
        }
        return false
    }

    fun validator(
        directionA: Array<Int>,
        present: Array<Int>,
        proposed: Array<Int>,
        matrix: Array<Array<Piece?>>
    ): Boolean {
        val displacementRow = proposed[0] - present[0] - directionA[0]
        val displacementColumn = proposed[1] - present[1] - directionA[1]
        val directionB = arrayOf(displacementRow, displacementColumn)
        if (operatorIsZero(coordinate = directionB)) {
            return true
        }
        return forSearchCriteria(
            displacement = arrayOf(displacementRow, displacementColumn),
            present = present,
            proposed = proposed,
            directionA = directionA,
            directionB = directionB,
            matrix = matrix
        )
    }

    fun forSearchCriteria(
        displacement: Array<Int>,
        present: Array<Int>,
        proposed: Array<Int>,
        directionA: Array<Int>,
        directionB: Array<Int>,
        matrix: Array<Array<Piece?>>
    ): Boolean {
        if (displacement[0] == 0 && proposed[0] == present[0]) {
            return recurseInto(coordinate = present, direction = directionB, matrix = matrix)
        }
        if (displacement[1] == 0 && proposed[1] == present[1]) {
            return recurseInto(coordinate = present, direction = directionB, matrix = matrix)
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
        return coordinate[0] + coordinate[1] >= 0 && coordinate[0] + coordinate[1] <= 7
    }

    fun operatorIsZero(coordinate: Array<Int>): Boolean {
        return coordinate[0] == 0 && coordinate[1] == 0
    }

    fun zeroPlus(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        return validator(directionA = arrayOf(+1, 0), present = present, proposed = proposed, matrix = matrix)
    }

    fun zeroMinus(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        return validator(directionA = arrayOf(-1, 0), present = present, proposed = proposed, matrix = matrix)
    }

    fun onePlus(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        return validator(directionA = arrayOf(0, +1), present = present, proposed = proposed, matrix = matrix)
    }

    fun oneMinus(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        return validator(directionA = arrayOf(0, -1), present = present, proposed = proposed, matrix = matrix)
    }
}