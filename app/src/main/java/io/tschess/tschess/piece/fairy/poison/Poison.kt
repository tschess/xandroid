package io.tschess.tschess.piece.fairy.poison

import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.pawn.Pawn
import io.tschess.tschess.piece.fairy.Fairy

open class Poison(
    name: String = "Poison",
    imageDefault: Int = R.drawable.red_landmine,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Fairy(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 2,
    attackList = listOf("PAWN"),
    describe =
    "• behaviour identical to a standard pawn with *one* caveat...${System.lineSeparator()}" +
            "• when captured, the opponent piece is also destroyed, i.e. eliminated from the game.${System.lineSeparator()}" +
            "• if king attempts to capture poison pawn the result is instant checkmate.${System.lineSeparator()}" +
            "• appears to your opponent throughout the game as a standard pawn.${System.lineSeparator()}"
) {

    override fun validate(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        return Pawn().validate(present = present, propose = propose, matrix = matrix)
    }

    fun check(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        return Pawn().check(present = present, propose = propose, matrix = matrix)
    }
}