package io.tschess.tschess.piece.chess.rook


import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.logic.HorizontalVertical

open class Rook(
    name: String = "Rook",
    imageDefault: Int = R.drawable.red_rook,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Piece(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 5,
    attackList = listOf("HorizontalVertical"),
    chess = true
) {

    override fun validate(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {

        if (HorizontalVertical().zeroPlus(present = present, proposed = proposed, matrix = matrix)) {
            if (matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if (matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation !=
                    matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if (HorizontalVertical().zeroMinus(present = present, proposed = proposed, matrix = matrix)) {
            if (matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if (matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation !=
                    matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if (HorizontalVertical().onePlus(present = present, proposed = proposed, matrix = matrix)) {
            if (matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if (matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation !=
                    matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if (HorizontalVertical().oneMinus(present = present, proposed = proposed, matrix = matrix)) {
            if (matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if (matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation !=
                    matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        return false
    }

}