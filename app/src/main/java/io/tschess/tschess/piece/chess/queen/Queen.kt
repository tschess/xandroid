package io.tschess.tschess.piece.chess.queen

import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.logic.Diagonal
import io.tschess.tschess.piece.logic.HorizontalVertical

open class Queen(
    name: String = "Queen",
    imageDefault: Int = R.drawable.red_queen,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Piece(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 9,
    attackList = listOf("Diagonal", "HorizontalVertical"),
    chess = true
) {

    override fun validate(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        if(Diagonal().plusPlus(present = present, proposed = proposed, state = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if(matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation != matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if(Diagonal().minusPlus(present = present, proposed = proposed, state = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if(matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation != matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if(Diagonal().plusMinus(present = present, proposed = proposed, state = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if(matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation != matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if(Diagonal().minusMinus(present = present, proposed = proposed, state = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if(matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation != matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        /* * */
        if(HorizontalVertical().zeroPlus(present = present, proposed = proposed, matrix = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if(matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation != matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if(HorizontalVertical().zeroMinus(present = present, proposed = proposed, matrix = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if(matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation != matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if(HorizontalVertical().onePlus(present = present, proposed = proposed, matrix = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if(matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation != matrix[proposed[0]][proposed[1]]!!.affiliation
        }
        if(HorizontalVertical().oneMinus(present = present, proposed = proposed, matrix = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null) {
                return true
            }
            if(matrix[proposed[0]][proposed[1]]!!.name == "PieceAnte") {
                return true
            }
            return matrix[present[0]][present[1]]!!.affiliation != matrix[proposed[0]][proposed[1]]!!.affiliation
        }

        return false
    }
}