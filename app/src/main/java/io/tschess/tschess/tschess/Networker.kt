package io.tschess.tschess.tschess

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import io.tschess.tschess.tschess.evaluation.Validator
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.HashMap

class Networker(val progressBar: ProgressBar, val validator: Validator, val context: Context) {

    fun timeout(id_game: String, id_self: String, id_oppo: String, white: Boolean) {
        val params = HashMap<String, Any>()
        params["id_game"] = id_game
        params["id_self"] = id_self
        params["id_oppo"] = id_oppo
        params["white"] = white
        val jsonObject = JSONObject(params as Map<*, *>)
        this.deliver(jsonObject, "resign")
    }

    //setUpdate(...
    fun update(id_game: String, state: Any, highlight: Any) {
        val params = HashMap<String, Any>()
        params["id_game"] = id_game
        params["state"] = state
        params["highlight"] = highlight
        params["condition"] = "TBD"
        val jsonObject = JSONObject(params as Map<*, *>)
        this.deliver(jsonObject)
    }

    fun deliver(jsonObject: JSONObject, route: String = "update") {
        this.progressBar.visibility = View.VISIBLE
        val url = "${ServerAddress().IP}:8080/game/${route}"
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            {
                this.validator.clear()
            },
            { Log.e("error in volley request", "${it.message}") })
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 2, 1f)
        VolleySingleton.getInstance(context).addToRequestQueue(request)
    }

    private fun getUpdate() {
        val url = "${ServerAddress().IP}:8080/game/request/${game.id}"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response: JSONObject ->
                val game: EntityGame = parseGame.execute(response)
                val updatable: Boolean = this.updateable(game.updated)
                if (!updatable) {
                    return@Listener
                }
                this.progressBar.visibility = View.INVISIBLE
                this.game = game
                this.labeler.game = game

                this.setHighlightCoords()

                this.matrix = this.game.getMatrix(this.playerSelf.username)
                this.boardView.populateBoard(this.matrix, this.highlight, game.turn) //old turn??

                this.setCheckMate()
                this.setEndgame()
                this.setTurn()
                this.setCountdown(game.updated)

                this.labeler.setLabelNotification()
                this.setCheckLabel()

            }, {
                Log.e("error in volley request", "${it.message}")
            })
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    private fun setCheckMate() {
        val affiliation: String = this.game.getAffiliationOther(this.playerSelf.username)
        val king: Array<Int> = checker.kingCoordinate(affiliation, this.matrix)

        val mate: Boolean = checker.mate(king, this.matrix)
        if (mate) {
            val url = "${ServerAddress().IP}:8080/game/mate/${this.game.id}"
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { this.progressBar.visibility = View.INVISIBLE },
                { this.progressBar.visibility = View.INVISIBLE }
            )
            VolleySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
            return
        }
        val czech: Boolean = checker.other(king, this.matrix)
        if (!czech) {
            return
        }
        this.progressBar.visibility = View.VISIBLE
        val url = "${ServerAddress().IP}:8080/game/check/${this.game.id}"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { this.progressBar.visibility = View.INVISIBLE },
            { this.progressBar.visibility = View.INVISIBLE }
        )
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)

    }

    private fun updateable(updated: String): Boolean {
        val updated01: ZonedDateTime = LocalDateTime.parse(updated, this.formatter).atZone(this.brooklyn)
        val updatedXX: String = this.game.updated
        val updatedXY: ZonedDateTime = LocalDateTime.parse(updatedXX, this.formatter).atZone(this.brooklyn)
        return updated01.isAfter(updatedXY)
    }

}