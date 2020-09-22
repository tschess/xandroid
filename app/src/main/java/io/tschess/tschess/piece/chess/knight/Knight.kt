package io.tschess.tschess.piece.chess.knight


import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece

open class Knight(
    name: String = "Knight",
    imageDefault: Int = R.drawable.red_knight,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Piece(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 3,
    attackList = listOf("Knight"),
    chess = true
) {

    override fun validate(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        if (minusTwo_minusOne(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (minusTwo_plusOne(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (plusTwo_minusOne(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (plusTwo_plusOne(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (minusOne_minusTwo(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (minusOne_plusTwo(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (plusOne_minusTwo(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (plusOne_plusTwo(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }

        return false
    }

    fun minusTwo_minusOne(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamematrix.getTschessElementMatrix()
        if ((present[0] - 2) - proposed[0] == 0 && (present[1] - 1) - proposed[1] == 0) {
            if (matrix[present[0] - 2][present[1] - 1] != null) {
                if (matrix[present[0] - 2][present[1] - 1]!!.name == "PieceAnte") {
                    return true
                }
                return matrix[present[0] - 2][present[1] - 1]!!.affiliation !=
                        matrix[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    fun minusTwo_plusOne(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamematrix.getTschessElementMatrix()
        if ((present[0] - 2) - proposed[0] == 0 && (present[1] + 1) - proposed[1] == 0) {
            if (matrix[present[0] - 2][present[1] + 1] != null) {
                if (matrix[present[0] - 2][present[1] + 1]!!.name == "PieceAnte") {
                    return true
                }
                return matrix[present[0] - 2][present[1] + 1]!!.affiliation !=
                        matrix[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    fun plusTwo_minusOne(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamematrix.getTschessElementMatrix()
        if ((present[0] + 2) - proposed[0] == 0 && (present[1] - 1) - proposed[1] == 0) {
            if (matrix[present[0] + 2][present[1] - 1] != null) {
                if (matrix[present[0] + 2][present[1] - 1]!!.name == "PieceAnte") {
                    return true
                }
                return matrix[present[0] + 2][present[1] - 1]!!.affiliation !=
                        matrix[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    fun plusTwo_plusOne(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamematrix.getTschessElementMatrix()
        if ((present[0] + 2) - proposed[0] == 0 && (present[1] + 1) - proposed[1] == 0) {
            if (matrix[present[0] + 2][present[1] + 1] != null) {
                if (matrix[present[0] + 2][present[1] + 1]!!.name == "PieceAnte") {
                    return true
                }
                return matrix[present[0] + 2][present[1] + 1]!!.affiliation !=
                        matrix[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }


    fun minusOne_minusTwo(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamematrix.getTschessElementMatrix()
        if ((present[0] - 1) - proposed[0] == 0 && (present[1] - 2) - proposed[1] == 0) {
            if (matrix[present[0] - 1][present[1] - 2] != null) {
                if (matrix[present[0] - 1][present[1] - 2]!!.name == "PieceAnte") {
                    return true
                }
                return matrix[present[0] - 1][present[1] - 2]!!.affiliation !=
                        matrix[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }


    fun minusOne_plusTwo(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamematrix.getTschessElementMatrix()
        if ((present[0] - 1) - proposed[0] == 0 && (present[1] + 2) - proposed[1] == 0) {
            if (matrix[present[0] - 1][present[1] + 2] != null) {
                if (matrix[present[0] - 1][present[1] + 2]!!.name == "PieceAnte") {
                    return true
                }
                return matrix[present[0] - 1][present[1] + 2]!!.affiliation !=
                        matrix[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    fun plusOne_minusTwo(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamematrix.getTschessElementMatrix()
        if ((present[0] + 1) - proposed[0] == 0 && (present[1] - 2) - proposed[1] == 0) {
            if (matrix[present[0] + 1][present[1] - 2] != null) {
                if (matrix[present[0] + 1][present[1] - 2]!!.name == "PieceAnte") {
                    return true
                }
                return matrix[present[0] + 1][present[1] - 2]!!.affiliation !=
                        matrix[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    fun plusOne_plusTwo(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamematrix.getTschessElementMatrix()
        if ((present[0] + 1) - proposed[0] == 0 && (present[1] + 2) - proposed[1] == 0) {
            if (matrix[present[0] + 1][present[1] + 2] != null) {
                if (matrix[present[0] + 1][present[1] + 2]!!.name == "PieceAnte") {
                    return true
                }
                return matrix[present[0] + 1][present[1] + 2]!!.affiliation !=
                        matrix[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

}