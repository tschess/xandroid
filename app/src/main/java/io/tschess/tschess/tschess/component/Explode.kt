package io.tschess.tschess.tschess.component

import android.os.Build
import androidx.annotation.RequiresApi
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.fairy.poison.RevealBlack
import io.tschess.tschess.piece.fairy.poison.RevealWhite
import io.tschess.tschess.tschess.ActivityTschess
import org.json.JSONObject
import java.util.HashMap

class Explode(val activityTschess: ActivityTschess) {

    fun evaluate(coordinate: Array<Int>?, proposed: Array<Int>, state0: Array<Array<Piece?>>): Boolean {
        if(coordinate == null){
            return false
        }
        val elementAttacker: Piece? = state0[coordinate!![0]][coordinate!![1]]
        if(elementAttacker == null){
            return false
        }
        val elementAttacked: Piece? = state0[proposed[0]][proposed[1]]
        if(elementAttacked == null){
            return false
        }
        if(!(elementAttacked!!.name.contains("Poison") && elementAttacked!!.target)){
            return false
        }
        //they are attacking a poison pawn...
        val stateX: Array<Array<Piece?>> = this.activityTschess.validator.deselect(state0)
        if(elementAttacker!!.name.contains("King")){
            if(!this.activityTschess.white){
                stateX[proposed[0]][proposed[1]] = RevealWhite()
            } else {
                stateX[proposed[0]][proposed[1]] = RevealBlack()
            }
            val state: List<List<String>> = this.activityTschess.validator.render(matrix = stateX, white = this.activityTschess.white)
            val params = HashMap<String, Any>()
            params["id_game"] = this.activityTschess.game.id
            params["state"] = state
            val jsonObject = JSONObject(params as Map<*,*>)
            this.activityTschess.deliver(jsonObject, "mine")
            this.activityTschess.showSpecialAlert("poison pawn!")
            return true
        }
        stateX[proposed[0]][proposed[1]] = null
        stateX[coordinate!![0]][coordinate!![1]] = null
        val stateUpdate: List<List<String>> = this.activityTschess.validator.render(matrix = stateX, white = this.activityTschess.white)
        val highlight: String = if (this.activityTschess.white) {
            "${coordinate[0]}${coordinate[1]}${proposed[0]}${proposed[1]}"
        } else {
            "${7 - coordinate[0]}${7 - coordinate[1]}${7 - proposed[0]}${7 - proposed[1]}"
        }
        val params = HashMap<String, Any>()
        params["id_game"] = this.activityTschess.game.id
        params["state"] = stateUpdate
        params["highlight"] = highlight
        params["condition"] = "LANDMINE"
        val jsonObject = JSONObject(params as Map<*,*>)
        this.activityTschess.deliver(jsonObject)
        this.activityTschess.showSpecialAlert("poison pawn!")
        return true
    }
}