package io.tschess.tschess.tschess.controller

import android.util.Log
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.pawn.Pawn
import io.tschess.tschess.piece.fairy.hunter.Hunter
import io.tschess.tschess.piece.fairy.poison.Poison
import java.util.*

class Checker {

    fun other(coordinate: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        val king: Piece = state[coordinate[0]][coordinate[1]]!!
        val affiliationKing: String = king.affiliation.toLowerCase(Locale.ENGLISH)
        //Log.e(">>>>>>> affiliationKing", "${affiliationKing}")
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                val candidate: Piece = state[row][column] ?: continue
                if(candidate.name == "PieceAnte") {
                    continue
                }
                val affiliationCandidate: String = candidate.affiliation.toLowerCase(Locale.ENGLISH)
                val friendly: Boolean = affiliationCandidate == affiliationKing
                if (friendly) {
                    continue
                }
                //Log.e("----- candidate", "${candidate.name}")
                //Log.e("----- validate ", "${candidate.validate(arrayOf(row,column), coordinate, state)}")
                if (candidate.validate(arrayOf(row,column), coordinate, state)) {
                    return true
                }
            }
        }
        return false
    }

    fun coordinateKing(affiliationKing: String, state: Array<Array<Piece?>>): Array<Int> {
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                val candidate: Piece = state[row][column] ?: continue
                if (!candidate.name.contains("King")) {
                    continue
                }
                val affiliationCandidate: String = candidate.affiliation.toLowerCase(Locale.ENGLISH)
                val match: Boolean = affiliationCandidate == affiliationKing.toLowerCase(Locale.ENGLISH)
                if (match) {
                    return arrayOf(row, column)
                }
            }
        }
        return emptyArray()
    }

    //TODO: almost equivalent of 'listOpponent'
    private fun listCompatriot(king: Array<Int>, state: Array<Array<Piece?>>): Array<Array<Int>> {
        val arrayList: MutableList<Array<Int>> = mutableListOf()
        val pieceKing: Piece = state[king[0]][king[1]]!!
        val affiliationKing: String = pieceKing.affiliation
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                val coord: Array<Int> = arrayOf(row, column)
                if (coord.contentEquals(king)) {
                    continue
                }
                val candidate: Piece = state[row][column] ?: continue
                val match: Boolean = candidate.affiliation == affiliationKing
                if (!match) {
                    continue
                }
                arrayList.add(coord)
            }
        }
        return arrayList.toTypedArray()
    }

    //TODO: almost equivalent of 'listCompatriot'
    private fun listOpponent(king: Array<Int>, state: Array<Array<Piece?>>): Array<Array<Int>> {
        val arrayList: MutableList<Array<Int>> = mutableListOf()
        val pieceKing: Piece = state[king[0]][king[1]]!!
        val affiliationKing: String = pieceKing.affiliation
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                val coord: Array<Int> = arrayOf(row, column)
                if (coord.contentEquals(king)) {
                    continue
                }
                val candidate: Piece = state[row][column] ?: continue
                val match: Boolean = candidate.affiliation == affiliationKing
                if (match) {
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

    private fun thwart(king: Array<Int>, state0: Array<Array<Piece?>>): Boolean {
        //  val state1: Array<Array<Piece?>> = state0
        val state1: Array<Array<Piece?>> = state0.clone()

        val listAttacker: Array<Array<Int>> = this.listAttacker(king, state1)
        if (listAttacker.isEmpty()) {
            return true
        }

        val coordinateAttacker: Array<Int> = listAttacker[0]
        val pieceAttacker: Piece = state1[coordinateAttacker[0]][coordinateAttacker[1]]!!

        val listCompatriot: Array<Array<Int>> = this.listCompatriot(king, state1)
        listCompatriot.forEach { coordinateCompatriot ->

            val pieceCompatriot: Piece = state1[coordinateCompatriot[0]][coordinateCompatriot[1]]!!

            for (row: Int in 0..7) {
                for (column: Int in 0..7) {

                    if (pieceCompatriot.validate(coordinateCompatriot, arrayOf(row, column), state1)) {

                        val hold: Piece? = state1[row][column]

                        state1[row][column] = pieceCompatriot
                        state1[coordinateCompatriot[0]][coordinateCompatriot[1]] = null

                        if (!pieceAttacker.validate(coordinateAttacker, king, state1)) {
                            if (this.other(king, state1)) {
                                return false
                            }
                            return true
                        }
                        state1[row][column] = hold
                        state1[coordinateCompatriot[0]][coordinateCompatriot[1]] = pieceCompatriot
                    }
                }
            }
        }
        return false
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
                    val czech: Boolean = this.self(coord01, state)
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
        //listValidateKing.filter { it contentEquals coord00 }

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

}