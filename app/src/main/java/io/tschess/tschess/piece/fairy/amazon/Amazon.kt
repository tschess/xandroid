package io.tschess.tschess.piece.fairy.amazon

import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.bishop.Bishop
import io.tschess.tschess.piece.chess.knight.Knight
import io.tschess.tschess.piece.chess.rook.Rook
import io.tschess.tschess.piece.fairy.Fairy

open class Amazon(
    name: String = "Amazon",
    imageDefault: Int = R.drawable.red_amazon,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Fairy(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 9,
    attackList = listOf("Knight", "Diagonal", "HorizontalVertical"),
    describe = "• performs all moves available to standard queen.${System.lineSeparator()}" +
            "• performs all moves available to standard knight.${System.lineSeparator()}"
) {

    override fun validate(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        if (Knight().validate(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (Rook().validate(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        if (Bishop().validate(present = present, proposed = proposed, matrix = matrix)) {
            return true
        }
        return false
    }

}