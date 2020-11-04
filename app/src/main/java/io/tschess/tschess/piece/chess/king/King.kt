package io.tschess.tschess.piece.chess.king

import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.king.logic.Attack
import io.tschess.tschess.piece.chess.king.logic.MovementKing
import io.tschess.tschess.piece.chess.king.logic.Threat
import io.tschess.tschess.tschess.logic.Castle

open class King(
    name: String = "King",
    imageDefault: Int = R.drawable.red_king,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Piece(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 0,
    attackList = listOf("King"),
    chess = true
) {

    override fun validate(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        if (Castle().castle(kingCoordinate = present, proposed = proposed, state = matrix)) {
            return true
        }
        if (!MovementKing().movement(present = present, proposed = proposed)) {
            return false
        }
        if (Threat().evaluate(present = present, proposed = proposed, state = matrix)) {
            return false
        }
        if (Attack().evaluate(present = present, proposed = proposed, state = matrix)) {
            return false
        }
        val elementDest: Piece = matrix[proposed[0]][proposed[1]] ?: return true
        if (elementDest.name == "PieceAnte") {
            return true
        }
        val elementKing: Piece = matrix[present[0]][present[1]]!!
        return elementKing.affiliation != elementDest.affiliation
    }

}