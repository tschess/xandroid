package io.tschess.tschess.piece.chess.king.logic

import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.knight.LogicKnight
import io.tschess.tschess.piece.chess.pawn.LogicPawn

class Threat() {

    private val evaluation: Evaluation = Evaluation()
    private val pawnOffense: LogicPawn = LogicPawn()
    private val kingOffense: LogicKing = LogicKing()
    private val knightOffense: LogicKnight = LogicKnight()

    fun evaluate(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {

        val affiliation: String = state[present[0]][present[1]]!!.affiliation

        if (knightOffense.evaluate(coord = proposed, affiliation = affiliation, matrix = state)) {
            return true
        }

        if (kingOffense.minusMinus(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        if (kingOffense.minusPlus(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        if (kingOffense.plusMinus(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        if (kingOffense.plusPlus(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        if (kingOffense.zeroMinus(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        if (kingOffense.zeroPlus(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        if (kingOffense.oneMinus(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        if (kingOffense.onePlus(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        /* * */

        if (pawnOffense.c0_m1_c1_m1(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        if (pawnOffense.c0_m1_c1_p1(coordinate = proposed, affiliation = affiliation, state = state)) {
            return true
        }

        /* * */

        if (evaluation.rowStraightUp(
                present = present,
                proposed = arrayOf(proposed[0] - 1, proposed[1]),
                affiliation = affiliation,
                state = state,
                threat = true
            )
        ) {
            return true
        }
        if (evaluation.rowStraightDown(
                present = present,
                proposed = arrayOf(proposed[0] + 1, proposed[1]),
                affiliation = affiliation,
                state = state,
                threat = true
            )
        ) {
            return true
        }
        if (evaluation.columnLeft(
                present = present,
                proposed = arrayOf(proposed[0], proposed[1] - 1),
                affiliation = affiliation,
                state = state,
                threat = true
            )
        ) {
            return true
        }
        if (evaluation.columnRight(
                present = present,
                proposed = arrayOf(proposed[0], proposed[1] + 1),
                affiliation = affiliation,
                state = state,
                threat = true
            )
        ) {
            return true
        }
        if (evaluation.diagonalUpRight(
                present = present,
                proposed = arrayOf(proposed[0] - 1, proposed[1] + 1),
                affiliation = affiliation,
                state = state,
                threat = true
            )
        ) {
            return true
        }
        if (evaluation.diagonalDownLeft(
                present = present,
                proposed = arrayOf(proposed[0] + 1, proposed[1] - 1),
                affiliation = affiliation,
                state = state,
                threat = true
            )
        ) {
            return true
        }
        if (evaluation.diagonalUpLeft(
                present = present,
                proposed = arrayOf(proposed[0] - 1, proposed[1] - 1),
                affiliation = affiliation,
                state = state,
                threat = true
            )
        ) {
            return true
        }
        if (evaluation.diagonalDownRight(
                present = present,
                proposed = arrayOf(proposed[0] + 1, proposed[1] + 1),
                affiliation = affiliation,
                state = state,
                threat = true
            )
        ) {
            return true
        }
        return false
    }

}