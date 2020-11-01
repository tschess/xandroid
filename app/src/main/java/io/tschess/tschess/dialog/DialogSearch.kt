package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.R
import io.tschess.tschess.home.ActivityHome
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import io.tschess.tschess.start.UtilityStart
import org.json.JSONObject

class DialogSearch(context: Context, val username: String) : Dialog(context) {

    lateinit var progressBar: ProgressBar

    private lateinit var buttonSearch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_search)

        this.progressBar = findViewById(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE //android:visibility="invisible"

        this.buttonSearch = findViewById<Button>(R.id.btn_search)
        this.buttonSearch.setOnClickListener {

            //   val title: String = "tschess \uD83D\uDD10"
            //            val message: String = "\u2709\uFE0F to recover account please email:\n\nhello@tschess.io"
            //            DialogOk(applicationContext).render(title, message)

            val url: String = "${ServerAddress().IP}:8080/player/search/${username}"
            val request = JsonObjectRequest(
                Request.Method.POST, url, null,
                Response.Listener { response: JSONObject ->



                    if (response.has("fail")) {
                        //this.bail()
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
                    //this.bail()
                }
            )
            request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
            VolleySingleton.getInstance(this).addToRequestQueue(request)
        }


    }


}