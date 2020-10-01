package io.tschess.tschess.start

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.dialog.DialogOk
import io.tschess.tschess.home.ActivityHome
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject

class UtilityStart(private val activity: AppCompatActivity, val progressBar: ProgressBar) {

    private val parsePlayer: ParsePlayer = ParsePlayer()
    val context: Context = activity.applicationContext
    val dialog: DialogOk = DialogOk(context)

    fun requestServer(username: String, password: String, device: String, url: String) {
        progressBar.visibility = View.VISIBLE

        val params = HashMap<String, String>()
        params["username"] = username
        params["password"] = password
        params["device"] = device
        val jsonObject = JSONObject(params as Map<*, *>)

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response: JSONObject ->


                Log.e("RESPINSE!!!", response.toString())


                this.progressBar.visibility = View.INVISIBLE
                if (dialog.fail(response)) {
                    return@Listener
                }
                val playerSelf: EntityPlayer = parsePlayer.execute(response)
                this.startActivityHome(playerSelf)
            },
            Response.ErrorListener {
                this.progressBar.visibility = View.INVISIBLE
                //this.dialogInvalid()
            }
        )
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(context).addToRequestQueue(request)
    }

    fun evaluateCreate(username: String, password: String, device: String) {
        if (startActivityCreate(username, password)) {
            return
        }
        if (!isLettersOrDigits(username)) {
            val title: String = "⚡ illegal input ⚡"
            val message: String =
                "\uD83E\uDD16 username must be alphanumeric.\nplease re-evaluate and try again. \uD83D\uDCF2"
            this.dialog.render(title, message)
            return
        }
        val url: String = "${ServerAddress().IP}:8080/player/create"
        this.requestServer(username, password, device, url)
    }

    private fun isLettersOrDigits(chars: String): Boolean {
        for (c in chars) {
            if (c !in 'A'..'Z' && c !in 'a'..'z' && c !in '0'..'9') {
                return false
            }
        }
        return true
    }

    private fun startActivityCreate(username: String, password: String): Boolean {
        if (username != "" || password != "") {
            return false
        }
        val intent = Intent(context, ActivityCreate::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        activity.finish()
        return true
    }

    private fun startActivityHome(player: EntityPlayer) {
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", player)
        val intent = Intent(context, ActivityHome::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        activity.finish()
    }
}