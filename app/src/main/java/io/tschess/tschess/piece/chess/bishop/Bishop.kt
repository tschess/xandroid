package io.tschess.tschess.piece.chess.bishop

import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.logic.Diagonal

open class Bishop(
    name: String = "Bishop",
    imageDefault: Int = R.drawable.red_bishop,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Piece(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 3,
    attackList = listOf("Diagonal"),
    chess = true
) {

    override fun validate(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        val self: Piece = matrix[present[0]][present[1]]!!
        if (Diagonal().plusPlus(present = present, proposed = proposed, state = matrix)) {
            val candidate: Piece = matrix[proposed[0]][proposed[1]] ?: return true
            if (candidate.name == "PieceAnte") {
                return true
            }
            return !self.compatriot(candidate)
        }
        if (Diagonal().minusPlus(present = present, proposed = proposed, state = matrix)) {
            val candidate: Piece = matrix[proposed[0]][proposed[1]] ?: return true
            if (candidate.name == "PieceAnte") {
                return true
            }
            return !self.compatriot(candidate)
        }
        if (Diagonal().plusMinus(present = present, proposed = proposed, state = matrix)) {
            val candidate: Piece = matrix[proposed[0]][proposed[1]] ?: return true
            if (candidate.name == "PieceAnte") {
                return true
            }
            return !self.compatriot(candidate)
        }
        if (Diagonal().minusMinus(present = present, proposed = proposed, state = matrix)) {
            val candidate: Piece = matrix[proposed[0]][proposed[1]] ?: return true
            if (candidate.name == "PieceAnte") {
                return true
            }
            return !self.compatriot(candidate)
        }
        return false
    }

}