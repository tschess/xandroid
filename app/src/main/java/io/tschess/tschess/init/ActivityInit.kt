package io.tschess.tschess.init

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.R
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.home.ActivityHome
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.start.ActivityStart
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject

class ActivityInit : AppCompatActivity() {

    private val parsePlayer: ParsePlayer = ParsePlayer()

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        val device: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val url: String = "${ServerAddress().IP}:8080/player/device/${device}"
        val request = JsonObjectRequest(
            Request.Method.POST, url, null,
            Response.Listener { response: JSONObject ->
                if (response.has("fail")) {
                    this.bail()
                    return@Listener
                }
                val playerSelf: EntityPlayer = parsePlayer.execute(response)
                val intent = Intent(applicationContext, ActivityHome::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
                extras.putExtra("player_self", playerSelf)
                applicationContext.startActivity(intent)
                finish()
            },
            Response.ErrorListener {
                this.bail()
            }
        )
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    private fun bail() {
        Thread.sleep(1001.toLong())
        val thread: Thread = object : Thread() {
            override fun run() {
                val intent = Intent(applicationContext, ActivityStart::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                applicationContext.startActivity(intent)
                finish()
            }
        }
        thread.start()
    }
}