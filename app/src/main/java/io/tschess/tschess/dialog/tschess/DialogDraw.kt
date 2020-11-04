package io.tschess.tschess.dialog.tschess

import android.content.Context
import android.content.DialogInterface
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

class DialogDraw(
    val context: Context,
    val playerSelf: EntityPlayer,
    val game: EntityGame,
    var progressBar: ProgressBar
) {

    val idSelf: String
    val usernameSelf: String
    val playerOther: EntityPlayer
    val idOther: String
    val usernameOther: String

    val url: String
    val params: HashMap<String, Any>

    val title: String
    val message: String

    init {
        this.idSelf = this.playerSelf.id
        this.usernameSelf = this.playerSelf.username
        this.playerOther = this.game.getPlayerOther(this.usernameSelf)
        this.idOther = this.playerOther.id
        this.usernameOther = this.playerOther.username

        this.url = "${ServerAddress().IP}:8080/game/eval"
        this.params = HashMap<String, Any>()
        this.params["id_game"] = this.game.id
        this.params["id_self"] = this.idSelf
        this.params["id_other"] = this.idOther

        this.title = "⚡ tschess ⚡"
        this.message = "$usernameOther has proposed a draw️ \uD83D\uDE4F"
    }

    private fun execute(dialog: DialogInterface) {
        val jsonObject = JSONObject(this.params as Map<*, *>)
        val request =
            JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                {
                    this.progressBar.visibility = View.VISIBLE
                },
                { }
            )
        VolleySingleton.getInstance(context).addToRequestQueue(request)
        dialog.cancel()
    }

    fun render() {
        val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialog)
        dialogBuilder.setTitle(this.title)
        dialogBuilder.setMessage(this.message)
        dialogBuilder.setPositiveButton("accept") { dialog, _ ->
            this.params["accept"] = true
            this.execute(dialog)
        }
        dialogBuilder.setNegativeButton("reject") { dialog, _ ->
            this.params["accept"] = false
            this.execute(dialog)
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