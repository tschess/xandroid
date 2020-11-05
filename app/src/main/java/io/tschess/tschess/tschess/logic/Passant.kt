package io.tschess.tschess.tschess.logic

import io.tschess.tschess.dialog.tschess.DialogToast
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.pawn.Pawn
import io.tschess.tschess.tschess.ActivityTschess
import org.json.JSONObject
import java.util.*

class Passant(
    val activityTschess: ActivityTschess
) {

    fun evaluate(coordinate: Array<Int>?, proposed: Array<Int>, state0: Array<Array<Piece?>>): Boolean {
        if (coordinate == null) {
            return false
        }
        var state1 = state0

        val tschessElement = state1[coordinate!![0]][coordinate!![1]]
        if (tschessElement == null) {
            return false
        }
        val affiliation = state1[coordinate!![0]][coordinate!![1]]!!.affiliation

        val pawn0: Boolean = state1[coordinate!![0]][coordinate!![1]]!!.name.contains("Pawn") ||
                state1[coordinate!![0]][coordinate!![1]]!!.name.contains("Poison")
        if (pawn0) {

            if (!Pawn()
                    .advanceTwo(present = coordinate, propose = proposed, matrix = state1)
            ) {
                return false
            }
            if (coordinate!![1] - 1 >= 0) {
                val examinee = state1[4][coordinate!![1] - 1]
                if (examinee != null) {

                    val pawn1: Boolean = examinee!!.name.contains("Pawn") ||
                            examinee!!.name.contains("Poison")

                    if (pawn1) {
                        if (examinee!!.affiliation != affiliation) {
                            state1[5][coordinate!![1]] = examinee!!
                            state1[4][coordinate!![1] - 1] = null
                            state1[coordinate!![0]][coordinate!![1]] = null

                            val matrix00: Array<Array<Piece?>> = this.activityTschess.transitioner.deselect(state1)
                            val state: List<List<String>> = this.activityTschess.transitioner.render(
                                matrix = matrix00,
                                white = this.activityTschess.white
                            )

                            val highlight: String = if (this.activityTschess.white) {
                                "${coordinate[0]}${coordinate[1]}${proposed[0]}${proposed[1]}"
                            } else {
                                "${7 - coordinate[0]}${7 - coordinate[1]}${7 - proposed[0]}${7 - proposed[1]}"
                            }
                            val params = HashMap<String, Any>()
                            params["id_game"] = this.activityTschess.game.id
                            params["state"] = state
                            params["highlight"] = highlight
                            params["condition"] = "TBD"
                            val jsonObject = JSONObject(params as Map<*, *>)
                            this.activityTschess.networker.deliver(jsonObject)
                            this.renderDialog()
                            return true
                        }
                    }
                }
            }
            if (coordinate!![1] + 1 <= 7) {
                val examinee = state1[4][coordinate!![1] + 1]
                if (examinee != null) {

                    val pawn1: Boolean = examinee!!.name.contains("Pawn") ||
                            examinee!!.name.contains("Poison")

                    if (pawn1) {
                        if (examinee!!.affiliation != affiliation) {
                            state1[5][coordinate!![1]] = examinee!!
                            state1[4][coordinate!![1] + 1] = null
                            state1[coordinate!![0]][coordinate!![1]] = null

                            val matrix00: Array<Array<Piece?>> = this.activityTschess.transitioner.deselect(state1)
                            val state: List<List<String>> = this.activityTschess.transitioner.render(
                                matrix = matrix00,
                                white = this.activityTschess.white
                            )

                            val highlight: String = if (this.activityTschess.white) {
                                "${coordinate[0]}${coordinate[1]}${proposed[0]}${proposed[1]}"
                            } else {
                                "${7 - coordinate[0]}${7 - coordinate[1]}${7 - proposed[0]}${7 - proposed[1]}"
                            }
                            val params = HashMap<String, Any>()
                            params["id_game"] = this.activityTschess.game.id
                            params["state"] = state
                            params["highlight"] = highlight
                            params["condition"] = "TBD"
                            val jsonObject = JSONObject(params as Map<*, *>)
                            this.activityTschess.networker.deliver(jsonObject)
                            this.renderDialog()
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    private fun renderDialog() {
        DialogToast().passant(this.activityTschess.applicationContext, this.activityTschess.layoutInflater)
    }


}
