package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject

class DialogSearch(context: Context, val playerSelf: EntityPlayer) : Dialog(context) {

    lateinit var progressBar: ProgressBar

    private lateinit var buttonSearch: Button

    private lateinit var inputUsername: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_search)

        this.progressBar = findViewById(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE //android:visibility="invisible"

        this.inputUsername = findViewById<EditText>(R.id.edit_username)

        this.buttonSearch = findViewById<Button>(R.id.btn_search)
        this.buttonSearch.setOnClickListener {


            val username: String = inputUsername.text.toString()
            Log.e("**USERNAME**", username)

            val url: String = "${ServerAddress().IP}:8080/player/search/${username}"
            val request = JsonObjectRequest(
                Request.Method.POST, url, null,
                Response.Listener { response: JSONObject ->

                    Log.e("-->>>", response.toString())


                    if (response.has("fail")) {
                        //this.bail()
                        return@Listener
                    }

                },
                Response.ErrorListener {
                    //this.bail()
                }
            )
            request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
            VolleySingleton.getInstance(context).addToRequestQueue(request)
        }


    }


}