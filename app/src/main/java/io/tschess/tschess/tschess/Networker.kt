package io.tschess.tschess.tschess

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject
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

}