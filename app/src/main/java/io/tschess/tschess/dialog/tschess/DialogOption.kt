package io.tschess.tschess.dialog.tschess

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject
import java.util.*

class DialogOption( //TODO: same signature as _DialogDraw_ (consolidate) ...
    val context: Context,
    val playerSelf: EntityPlayer,
    val game: EntityGame,
    var progressBar: ProgressBar
) {

    fun renderOptionMenu() {
        val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialog)
        dialogBuilder.setTitle("⚡ tschess ⚡")
        dialogBuilder.setMessage("select game menu option below:")
        dialogBuilder.setPositiveButton("resign position \uD83E\uDD26") { dialog, _ ->
            this.progressBar.visibility = View.VISIBLE
            val url = "${ServerAddress().IP}:8080/game/resign"
            val params = HashMap<String, Any>()
            params["id_game"] = this.game.id
            params["id_self"] = this.playerSelf.id
            params["id_oppo"] = this.game.getPlayerOther(this.playerSelf.username).id
            params["white"] = this.game.getWhite(this.playerSelf.username)
            val jsonObject = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { },
                { }
            )
            VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
            dialog.cancel()
        }
        if (this.game.getTurn(this.playerSelf.username)) {
            dialogBuilder.setNegativeButton("propose draw \uD83E\uDD1D") { dialog, _ ->
                this.progressBar.visibility = View.VISIBLE
                val url = "${ServerAddress().IP}:8080/game/prop/${this.game.id}"
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    { },
                    { }
                )
                VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
                dialog.cancel()
            }
        }
        Handler(Looper.getMainLooper()).post {
            val alert: AlertDialog = dialogBuilder.create()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                alert.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alert.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
            }
            alert.show()
        }

    }
}