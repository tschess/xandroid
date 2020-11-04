package io.tschess.tschess.tschess.evaluation

import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.pawn.Pawn
import io.tschess.tschess.piece.fairy.hunter.Hunter
import io.tschess.tschess.piece.fairy.poison.Poison
import java.util.*

class Checker {

    private fun thwart(king: Array<Int>, state0: Array<Array<Piece?>>): Boolean {
        var state1: Array<Array<Piece?>> = state0
        val listAttacker: Array<Array<Int>> = this.listAttacker(king, state1)
        if (listAttacker.isEmpty()) {
            return true
        }
        val coordAttk: Array<Int> = listAttacker[0]
        val pieceAttk: Piece = state1[coordAttk[0]][coordAttk[1]]!!
        val listCompatriot: Array<Array<Int>> = this.listCompatriot(king, state1)
        listCompatriot.forEach { coordCmpt ->
            val compatriotElement: Piece = state1[coordCmpt[0]][coordCmpt[1]]!!
            for (row: Int in 0..7) {
                for (column: Int in 0..7) {
                    if (compatriotElement.validate(coordCmpt, arrayOf(row, column), state1)) {
                        val tschessElement: Piece? = state1[row][column]
                        state1[row][column] = compatriotElement
                        state1[coordCmpt[0]][coordCmpt[1]] = null
                        if (!pieceAttk.validate(coordAttk, king, state1)) {
                            if (this.other(king, state1)) {
                                return false
                            }
                            return true
                        }
                        state1[row][column] = tschessElement
                        state1[coordCmpt[0]][coordCmpt[1]] = compatriotElement
                    }
                }
            }
        }
        return false
    }

    fun mate(coord00: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        val king: Piece = state[coord00[0]][coord00[1]]!!
        val listValidateKing: MutableList<Array<Int>> = mutableListOf()
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                val coord01: Array<Int> = arrayOf(row,column)
                if (king.validate(coord00, coord01, state)) {
                    val hold: Piece? = state[coord01[0]][coord01[1]]
                    state[coord01[0]][coord01[1]] = king
                    state[coord00[0]][coord00[1]] = null
                    val czech: Boolean = this.other(coord01, state)
                    if(!czech){
                        listValidateKing.add(coord01)
                    }
                    state[coord00[0]][coord00[1]] = king
                    state[coord01[0]][coord01[1]] = hold
                }
            }
        }
        val listOpponent: Array<Array<Int>> = this.listOpponent(king = coord00, state = state)
        val listValidateKingOpponent: MutableList<Array<Int>> = mutableListOf()
        listValidateKing.forEach { move ->
            listOpponent.forEach { opponent ->
                val opponentElement: Piece = state[opponent[0]][opponent[1]]!!
                if (opponentElement.validate(opponent, move, state)) {
                    listValidateKingOpponent.add(move)
                }
            }
        }
        if (listValidateKing.isNotEmpty()) {
            return false
        }
        val listAttacker: Array<Array<Int>> = this.listAttacker(coord00, state)
        if (listAttacker.size > 1) {
            return true
        }
        if (!this.thwart(coord00, state)) {
            return true
        }
        return false
    }

    fun other(coord: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        val king: Piece = state[coord[0]][coord[1]]!!
        val affiliation: String = king.affiliation.toLowerCase(Locale.ENGLISH)
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                val candidate: Piece = state[row][column] ?: continue
                val friendly: Boolean = candidate.affiliation.toLowerCase(Locale.ENGLISH) == affiliation
                if (friendly) {
                    continue
                }
                if (candidate.validate(arrayOf(row,column), coord, state)) {
                    return true
                }
            }
        }
        return false
    }

    fun kingCoordinate(affiliation: String, state: Array<Array<Piece?>>): Array<Int> {
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                val tschessElement: Piece = state[row][column] ?: continue
                if (!tschessElement.name.contains("King")) {
                    continue
                }
                if (tschessElement.affiliation == affiliation) {
                    return arrayOf(row, column)
                }
            }
        }
        return emptyArray()
    }

    private fun listCompatriot(king: Array<Int>, state: Array<Array<Piece?>>): Array<Array<Int>> {
        val arrayList: MutableList<Array<Int>> = mutableListOf()
        val kingElement: Piece = state[king[0]][king[1]]!!
        val affiliation: String = kingElement.affiliation
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                if (arrayOf(row, column).contentEquals(king)) {
                    continue
                }
                val tschessElement: Piece = state[row][column] ?: continue
                if (tschessElement.affiliation != affiliation) {
                    continue
                }
                arrayList.add(arrayOf(row, column))
            }
        }
        return arrayList.toTypedArray()
    }

    private fun listOpponent(king: Array<Int>, state: Array<Array<Piece?>>): Array<Array<Int>> {
        val arrayList: MutableList<Array<Int>> = mutableListOf()
        val kingElement: Piece = state[king[0]][king[1]]!!
        val affiliation: String = kingElement.affiliation
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {

                val coord: Array<Int> = arrayOf(row, column)

                if (coord.contentEquals(king)) {
                    continue
                }
                val tschessElement: Piece = state[row][column] ?: continue
                if (tschessElement.affiliation == affiliation) {
                    continue
                }
                arrayList.add(coord)
            }
        }
        return arrayList.toTypedArray()
    }

    private fun listAttacker(king: Array<Int>, state: Array<Array<Piece?>>): Array<Array<Int>> {
        val arrayList: MutableList<Array<Int>> = mutableListOf()
        val listOpponent: Array<Array<Int>> = this.listOpponent(king = king, state = state)
        listOpponent.forEach { coord ->
            val opponentElement: Piece = state[coord[0]][coord[1]]!!
            if (opponentElement.validate(coord, king, state)) {
                arrayList.add(coord)
            }
        }
        return arrayList.toTypedArray()
    }

    fun self(coordinate: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        val king: Piece = state[coordinate[0]][coordinate[1]]!!
        val affiliation: String = king.affiliation.toLowerCase(Locale.ENGLISH)

        for (row: Int in 0..7) {
            for (column: Int in 0..7) {

                val candidate: Piece = state[row][column] ?: continue
                if (candidate.name == "PieceAnte") {
                    continue
                }
                val friendly: Boolean = candidate.affiliation.toLowerCase(Locale.ENGLISH) == affiliation
                if (friendly) {
                    continue
                }

                /* * */
                val pawn: Boolean = candidate.name.contains("Pawn")
                if (pawn) {
                    candidate as Pawn
                    if(candidate.check(arrayOf(row,column), coordinate, state)){
                        return true
                    }
                    else {
                        continue
                    }
                }
                val poison: Boolean = candidate.name.contains("Poison")
                if (poison) {
                    candidate as Poison
                    if(candidate.check(arrayOf(row,column), coordinate, state)){
                        return true
                    }
                    else {
                        continue
                    }
                }
                val hunter: Boolean = candidate.name.contains("Hunter")
                if (hunter) {
                    candidate as Hunter
                    if(candidate.check(arrayOf(row,column), coordinate, state)){
                        return true
                    }
                    else {
                        continue
                    }
                }
                /* * */

                if (candidate.validate(arrayOf(row,column), coordinate, state)) {
                    return true
                }
            }
        }
        return false
    }

}