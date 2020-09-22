package io.tschess.tschess.start

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.FirebaseApp
import io.tschess.tschess.R
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.home.ActivityHome
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject

class ActivityStart : AppCompatActivity() {

    private val parsePlayer: ParsePlayer = ParsePlayer()

    lateinit var progressBar: ProgressBar

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText

    private lateinit var buttonLogin: Button
    private lateinit var buttonCreate: Button
    private lateinit var buttonRecover: Button

    @SuppressLint("HardwareIds", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        FirebaseApp.initializeApp(applicationContext)

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        // get reference to all views
        this.editTextUsername = findViewById<EditText>(R.id.et_user_name)
        this.editTextPassword = findViewById<EditText>(R.id.et_password)

        this.buttonLogin = findViewById<Button>(R.id.btn_login)
        this.buttonCreate = findViewById<Button>(R.id.btn_create)
        this.buttonRecover = findViewById<Button>(R.id.recover)
        this.buttonRecover.setOnClickListener {
            this.dialogRecover()
        }

        this.buttonCreate.setOnClickListener {
            this.progressBar.visibility = View.VISIBLE
            val url = "${ServerAddress().IP}:8080/player/create"

            val username: String = editTextUsername.text.toString()
            val usernameFilter: String = username.filter { it.isLetterOrDigit() }

            val password: String = editTextPassword.text.toString()
            editTextUsername.setText("")
            editTextPassword.setText("")
            val device: String = Secure.getString(contentResolver, Secure.ANDROID_ID)

            if (username == "" && password == "") {
                val intent = Intent(applicationContext, ActivityCreate::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                applicationContext.startActivity(intent)
                finish()
                return@setOnClickListener
            }
            val params = HashMap<String, String>()
            //params["username"] = username
            params["username"] = usernameFilter
            params["password"] = password
            params["device"] = device

            val jsonObject = JSONObject(params as Map<*, *>)

            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Response.Listener { response: JSONObject ->
                    this.progressBar.visibility = View.INVISIBLE

                    if (response.has("fail")) {
                        this.dialogInvalid()
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
                    this.progressBar.visibility = View.INVISIBLE
                    this.dialogInvalid()
                }
            )
            request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
            VolleySingleton.getInstance(this).addToRequestQueue(request)

        }

        this.buttonLogin.setOnClickListener {
            this.progressBar.visibility = View.VISIBLE

            val url = "${ServerAddress().IP}:8080/player/login"

            val username: String = editTextUsername.text.toString()
            val password: String = editTextPassword.text.toString()
            editTextUsername.setText("")
            editTextPassword.setText("")
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
                    if (response.has("fail")) {
                        this.dialogInvalid()
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
                    this.dialogInvalid()
                }
            )
            request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
            VolleySingleton.getInstance(this).addToRequestQueue(request)
        }

    }

    private fun dialogInvalid() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle("⚡ tschess ⚡")
        dialogBuilder.setMessage("\uD83E\uDD16 invalid input.\nplease re-evaluate and try again. \uD83D\uDCF2")
        dialogBuilder.setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
    }

    private fun dialogRecover() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle("tschess \uD83D\uDD10")
        dialogBuilder.setMessage("\u2709\uFE0F to recover account please email:\n\nhello@tschess.io")
        dialogBuilder.setPositiveButton("ok", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
    }
}


