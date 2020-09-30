package io.tschess.tschess.start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.FirebaseApp
import io.tschess.tschess.R
import io.tschess.tschess.home.ActivityHome
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject

class ActivityStart : AppCompatActivity() {



    lateinit var progressBar: ProgressBar

    private lateinit var inputUsername: EditText
    private lateinit var inputPassword: EditText

    private lateinit var buttonLogin: Button
    private lateinit var buttonCreate: Button
    private lateinit var buttonRecover: Button



    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        FirebaseApp.initializeApp(applicationContext)

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        this.inputUsername = findViewById<EditText>(R.id.et_user_name)
        this.inputPassword = findViewById<EditText>(R.id.et_password)

        this.buttonLogin = findViewById<Button>(R.id.btn_login)
        this.buttonCreate = findViewById<Button>(R.id.btn_create)
        this.buttonRecover = findViewById<Button>(R.id.recover)
        this.buttonRecover.setOnClickListener {
            val title: String = "tschess \uD83D\uDD10"
            val message: String = "\u2709\uFE0F to recover account please email:\n\nhello@tschess.io"
            this.dialogOk(title, message)
        }

        this.buttonCreate.setOnClickListener {


        }

        this.buttonLogin.setOnClickListener {
            this.progressBar.visibility = View.VISIBLE

            val url = "${ServerAddress().IP}:8080/player/login"

            val username: String = inputUsername.text.toString()
            val password: String = inputPassword.text.toString()
            inputUsername.setText("")
            inputPassword.setText("")
            val device: String = Secure.getString(contentResolver, Secure.ANDROID_ID)

            val params = HashMap<String, String>()
            params["username"] = username
            params["password"] = password
            params["device"] = device

            if (username == "w") {
                params["username"] = "white"
                params["password"] = "password"
            }
            if (username == "b") {
                params["username"] = "black"
                params["password"] = "password"
            }

            val jsonObject = JSONObject(params as Map<*, *>)
            Log.e("-->", "${jsonObject}")

            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                { response: JSONObject ->

                    Log.e("-->", "${response}")

                    this.progressBar.visibility = View.INVISIBLE
                    //Log.e("-->", "${response}")
                    //if (response.has("fail")) {
                    if (this.fail(response)) {
                        //this.dialogInvalid()
                        return@JsonObjectRequest
                    }
                    val playerSelf: EntityPlayer = parsePlayer.execute(response)


                    Log.e("playerSelf", "${playerSelf.username}")
                    val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
                    extras.putExtra("player_self", playerSelf)
                    val intent = Intent(applicationContext, ActivityHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    applicationContext.startActivity(intent)
                    finish()
                },
                {
                    this.progressBar.visibility = View.INVISIBLE
                    //this.dialogInvalid()
                    //connect error mebe
                }
            )
            request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
            VolleySingleton.getInstance(this).addToRequestQueue(request)
        }

    }




}


