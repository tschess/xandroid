package io.tschess.tschess.tschess.component

import io.tschess.tschess.piece.Piece
import io.tschess.tschess.tschess.ActivityTschess
import org.json.JSONObject
import java.util.*

class Castle(var activityTschess: ActivityTschess? = null) {

    private fun opponentCoordinateList(kingCoordinate: Array<Int>, state: Array<Array<Piece?>>): List<Array<Int>> {
        val opponentCoordinateList: MutableList<Array<Int>> = mutableListOf()

        val kingAffiliation: String = state[kingCoordinate[0]][kingCoordinate[1]]!!.affiliation.toLowerCase()
        for (row: Int in 0..7) {
            for (column: Int in 0..7) {
                if (state[row][column] != null) {
                    if (state[row][column]!!.affiliation.toLowerCase() != kingAffiliation) {
                        val coord: Array<Int> = arrayOf(row, column)
                        opponentCoordinateList.add(coord)
                    }
                }
            }
        }
        return opponentCoordinateList
    }

    fun execute(coordinate: Array<Int>, proposed: Array<Int>, state0: Array<Array<Piece?>>): Boolean {
        if (coordinate[0] != 7 || proposed[0] != 7) {
            return false
        }
        val state1 = state0
        val tschessElement: Piece = state1[coordinate[0]][coordinate[1]] ?: return false
        if (!tschessElement.name.contains("King")) {
            return false
        }
        val tschessElementProposed: Piece = state1[proposed[0]][proposed[1]] ?: return false
        if (tschessElementProposed.name != "PieceAnte") {
            return false
        }
        val affiliation = tschessElement.affiliation
        if (affiliation.toLowerCase(Locale.getDefault()) == "white") {
            if (proposed.contentEquals(arrayOf(7, 6))) {
                val rook: Piece = state1[7][7] ?: return false
                if (!rook.name.contains("Rook")) {
                    return false
                }
                val imageDefault = tschessElement.imageDefault
                tschessElement!!.imageVisible = imageDefault
                state1[7][6] = tschessElement
                state1[7][6]!!.touched = true
                state1[coordinate[0]][coordinate[1]] = null
                state1[7][5] = rook
                state1[7][5]!!.touched = true
                state1[7][7] = null

                val matrix00: Array<Array<Piece?>> = this.activityTschess!!.validator.deselect(state1)
                val state: List<List<String>> =
                    this.activityTschess!!.validator.render(matrix00, this.activityTschess!!.white)
                val highlight: String = if (this.activityTschess!!.white) {
                    "${coordinate[0]}${coordinate[1]}${proposed[0]}${proposed[1]}"
                } else {
                    "${7 - coordinate[0]}${7 - coordinate[1]}${7 - proposed[0]}${7 - proposed[1]}"
                }
                val params = HashMap<String, Any>()
                params["id_game"] = this.activityTschess!!.game.id
                params["state"] = state
                params["highlight"] = highlight
                params["condition"] = "TBD"
                val jsonObject = JSONObject(params as Map<*, *>)
                this.activityTschess!!.networker.deliver(jsonObject)
                return true
            }
            if (proposed.contentEquals(arrayOf(7, 2))) {
                val rook = state1[7][0]
                if (rook == null) {
                    return false
                }
                if (!rook.name.contains("Rook")) {
                    return false
                }
                val imageDefault = tschessElement.imageDefault
                tschessElement.imageVisible = imageDefault
                state1[7][2] = tschessElement
                state1[7][2]!!.touched = true
                state1[coordinate[0]][coordinate[1]] = null
                state1[7][3] = rook
                state1[7][3]!!.touched = true
                state1[7][0] = null

                val matrix00: Array<Array<Piece?>> = this.activityTschess!!.validator.deselect(state1)
                val state: List<List<String>> =
                    this.activityTschess!!.validator.render(matrix00, this.activityTschess!!.white)
                val highlight: String = if (this.activityTschess!!.white) {
                    "${coordinate[0]}${coordinate[1]}${proposed[0]}${proposed[1]}"
                } else {
                    "${7 - coordinate[0]}${7 - coordinate[1]}${7 - proposed[0]}${7 - proposed[1]}"
                }
                val params = HashMap<String, Any>()
                params["id_game"] = this.activityTschess!!.game.id
                params["state"] = state
                params["highlight"] = highlight
                params["condition"] = "TBD"
                val jsonObject = JSONObject(params as Map<*, *>)
                this.activityTschess!!.networker.deliver(jsonObject)
                return true
            }
        }
        if (affiliation.toLowerCase() == "black") {
            if (proposed.contentEquals(arrayOf(7, 1))) {
                val rook = state1[7][0]
                if (rook == null) {
                    return false
                }
                if (!rook!!.name.contains("Rook")) {
                    return false
                }
                val imageDefault = tschessElement!!.imageDefault
                tschessElement!!.imageVisible = imageDefault
                state1[7][1] = tschessElement
                state1[7][1]!!.touched = true
                state1[coordinate[0]][coordinate[1]] = null
                state1[7][2] = rook
                state1[7][2]!!.touched = true
                state1[7][0] = null

                val matrix00: Array<Array<Piece?>> = this.activityTschess!!.validator.deselect(state1)
                val state: List<List<String>> =
                    this.activityTschess!!.validator.render(matrix00, this.activityTschess!!.white)
                val highlight: String = if (this.activityTschess!!.white) {
                    "${coordinate[0]}${coordinate[1]}${proposed[0]}${proposed[1]}"
                } else {
                    "${7 - coordinate[0]}${7 - coordinate[1]}${7 - proposed[0]}${7 - proposed[1]}"
                }
                val params = HashMap<String, Any>()
                params["id_game"] = this.activityTschess!!.game.id
                params["state"] = state
                params["highlight"] = highlight
                params["condition"] = "TBD"
                val jsonObject = JSONObject(params as Map<*, *>)
                this.activityTschess!!.networker.deliver(jsonObject)
                return true
            }
            if (proposed.contentEquals(arrayOf(7, 5))) {
                val rook: Piece? = state1[7][7]
                if (rook == null) {
                    return false
                }
                if (!rook.name.contains("Rook")) {
                    return false
                }
                val imageDefault: Int = tschessElement.imageDefault
                tschessElement.imageVisible = imageDefault
                state1[7][5] = tschessElement
                state1[7][5]!!.touched = true
                state1[coordinate[0]][coordinate[1]] = null
                state1[7][4] = rook
                state1[7][4]!!.touched = true
                state1[7][7] = null

                val matrix00: Array<Array<Piece?>> = this.activityTschess!!.validator.deselect(state1)
                val state: List<List<String>> =
                    this.activityTschess!!.validator.render(matrix00, this.activityTschess!!.white)
                val highlight: String = if (this.activityTschess!!.white) {
                    "${coordinate[0]}${coordinate[1]}${proposed[0]}${proposed[1]}"
                } else {
                    "${7 - coordinate[0]}${7 - coordinate[1]}${7 - proposed[0]}${7 - proposed[1]}"
                }
                val params = HashMap<String, Any>()
                params["id_game"] = this.activityTschess!!.game.id
                params["state"] = state
                params["highlight"] = highlight
                params["condition"] = "TBD"
                val jsonObject = JSONObject(params as Map<*, *>)
                this.activityTschess!!.networker.deliver(jsonObject)
                return true
            }
        }
        return false
    }

    fun castle(kingCoordinate: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        if (kingCoordinate[0] != 7 || proposed[0] != 7) {
            return false
        }
        val king: Piece? = state[kingCoordinate[0]][kingCoordinate[1]]  //?: return false
        if (king == null) {
            return false
        }
        val affiliation: String = king.affiliation
        val searchSpace: Array<Array<Int>> = this.generateSearchSpace(proposed = proposed, affiliation = affiliation)
        if (!validateKingFirstTouch(kingCoordinate = kingCoordinate, state = state)) {
            return false
        }
        if (!validateRookFirstTouch(proposed = proposed, state = state, affiliation = affiliation)) {
            return false
        }
        val opponentCoordinateList: List<Array<Int>> = this.opponentCoordinateList(kingCoordinate, state)
        if (opponentCoordinateList.isEmpty()) {
            return false
        } //you'll always have at least one... the opponent king.
        opponentCoordinateList.forEach { coord ->
            val opponent: Piece? = state[coord[0]][coord[1]]
            searchSpace.forEach { space ->
                if (opponent != null) {
                    if (opponent.validate(present = coord, propose = space, matrix = state)) {
                        return false
                    }
                }
                if (!space.contentEquals(kingCoordinate)) {
                    val spaceOccupant: Piece? = state[space[0]][space[1]]
                    if (spaceOccupant != null) {
                        if (spaceOccupant.name != "PieceAnte") {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    private fun validateKingFirstTouch(kingCoordinate: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        val piece: Piece = state[kingCoordinate[0]][kingCoordinate[1]] ?: return false
        if (!piece.name.contains("King")) {
            return false
        }
        if (piece.touched) {
            return false
        }
        return true
    }

    private fun validateRookFirstTouch(
        proposed: Array<Int>,
        state: Array<Array<Piece?>>,
        affiliation: String
    ): Boolean {
        if (affiliation.toLowerCase() == "white") {
            if (proposed.contentEquals(arrayOf(7, 6))) {
                val candidateRook: Piece? = state[7][7]
                if (candidateRook != null) {
                    if (!candidateRook.name.contains("Rook")) {
                        return false
                    } else {
                        return !candidateRook.touched
                    }
                }
            }
            if (proposed.contentEquals(arrayOf(7, 2))) {
                val candidateRook: Piece? = state[7][0]
                if (candidateRook != null) {
                    if (!candidateRook.name.contains("Rook")) {
                        return false
                    } else {
                        return !candidateRook.touched
                    }
                }
            }
        }
        if (affiliation.toLowerCase() == "black") {
            if (proposed.contentEquals(arrayOf(7, 1))) {
                val candidateRook: Piece? = state[7][0]
                if (candidateRook != null) {
                    return if (!candidateRook.name.toLowerCase().contains("rook")) {
                        false
                    } else {
                        !candidateRook.touched
                    }
                }
            }
            if (proposed.contentEquals(arrayOf(7, 5))) {
                val candidateRook: Piece? = state[7][7]
                if (candidateRook != null) {
                    return if (!candidateRook.name.toLowerCase().contains("rook")) {
                        false
                    } else {
                        !candidateRook.touched
                    }
                }
            }
        }
        return false
    }

    private fun generateSearchSpace(proposed: Array<Int>, affiliation: String): Array<Array<Int>> {
        if (affiliation.toLowerCase() == "white") {
            if (proposed.contentEquals(arrayOf(7, 6))) {
                //Log.e("searchSpace", "kingSideWhite")
                return arrayOf(arrayOf(7, 4), arrayOf(7, 5), arrayOf(7, 6)) //kingSideWhite
            }
            if (proposed.contentEquals(arrayOf(7, 2))) {
                //Log.e("searchSpace", "queenSideWhite")
                return arrayOf(arrayOf(7, 4), arrayOf(7, 3), arrayOf(7, 2)) //queenSideWhite
            }
        }
        if (proposed.contentEquals(arrayOf(7, 1))) {
            //Log.e("searchSpace","kingSideBlack")
            return arrayOf(arrayOf(7, 3), arrayOf(7, 2), arrayOf(7, 1)) //kingSideBlack
        }
        //Log.e("searchSpace","queenSideBlack")
        return arrayOf(arrayOf(7, 3), arrayOf(7, 4), arrayOf(7, 5)) //queenSideBlack
    }

}