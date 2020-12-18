package io.tschess.tschess.start

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
import io.tschess.tschess.home.ActivityHome
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject

class ActivityStart : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    private lateinit var inputUsername: EditText
    private lateinit var inputPassword: EditText

    private lateinit var buttonLogin: Button
    private lateinit var buttonCreate: Button
    private lateinit var buttonRecover: Button

    val parsePlayer: ParsePlayer = ParsePlayer()

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
            val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton("ok") { dialog, _ ->
                dialog.cancel()
            }
            val alert: AlertDialog = dialogBuilder.create()
            alert.show()
        }

        this.buttonCreate.setOnClickListener {
            val username: String = inputUsername.text.toString()
            val password: String = inputPassword.text.toString()
            val device: String = Secure.getString(contentResolver, Secure.ANDROID_ID)
            evaluateCreate(username, password, device)
        }

        this.buttonLogin.setOnClickListener {
            val url = "${ServerAddress().IP}:8080/player/login"
            val username: String = inputUsername.text.toString()
            val password: String = inputPassword.text.toString()
            val device: String = Secure.getString(contentResolver, Secure.ANDROID_ID)
            requestServer(username, password, device, url)
        }

    }

    fun requestServer(username: String, password: String, device: String, url: String) {
        progressBar.visibility = View.VISIBLE

        val params = HashMap<String, String>()
        params["username"] = username
        params["password"] = password
        params["device"] = device
        val jsonObject = JSONObject(params as Map<*, *>)

        Log.e("-request->", "${jsonObject}")

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response: JSONObject ->

                Log.e("-response->", "${response}")

                this.progressBar.visibility = View.INVISIBLE
                if (fail(response)) {
                    return@Listener
                }
                val playerSelf: EntityPlayer = parsePlayer.execute(response)
                this.startActivityHome(playerSelf)
            },
            {

                Log.e("-->", "${it}")

                this.progressBar.visibility = View.INVISIBLE
                val title: String = "⚡ server error ⚡"
                val message: String =
                    "\uD83D\uDD0C unable to reach server.\ncheck connection and try again. \uD83D\uDCF1"
                val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
                dialogBuilder.setTitle(title)
                dialogBuilder.setMessage(message)
                dialogBuilder.setPositiveButton("ok") { dialog, _ ->
                    dialog.cancel()
                }
                val alert: AlertDialog = dialogBuilder.create()
                alert.show()
            }
        )
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(request)
    }

    fun evaluateCreate(username: String, password: String, device: String) {
        if (startActivityCreate(username, password)) {
            return
        }
        if (!isLettersOrDigits(username)) {
            val title: String = "⚡ illegal input ⚡"
            val message: String =
                "\uD83E\uDD16 username must be alphanumeric.\nplease re-evaluate and try again. \uD83D\uDCF2"
            //this.dialog.render(title, message)
            val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton("ok") { dialog, _ ->
                dialog.cancel()
            }
            val alert: AlertDialog = dialogBuilder.create()
            alert.show()
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
        val intent = Intent(this, ActivityCreate::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        applicationContext.startActivity(intent)
        this.finish()
        return true
    }

    private fun startActivityHome(player: EntityPlayer) {
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", player)
        val intent = Intent(this, ActivityHome::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        applicationContext.startActivity(intent)
        this.finish()
    }

    fun fail(response: JSONObject): Boolean {
        if (response.has("unknown")) {
            val title: String = "⚡ unknown username ⚡"
            val message: String =
                "\uD83E\uDD16 no such player in database.\nplease re-evaluate and try again. \uD83D\uDCF2"
            this.render(title, message)
            return true
        }
        if (response.has("invalid")) {
            val title: String = "⚡ invalid password ⚡"
            val message: String =
                "\uD83E\uDD16 input password is incorrect.\nplease re-evaluate and try again. \uD83D\uDCF2"
            this.render(title, message)
            return true
        }
        if (response.has("reserved")) {
            val title: String = "⚡ reserved username ⚡"
            val message: String =
                "\uD83E\uDD16 input username is reserved.\nplease re-evaluate and try again. \uD83D\uDCF2"
            this.render(title, message)
            return true
        }
        //connection
        //illegal
        return false
    }

    fun render(title: String, message: String) {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setPositiveButton("ok") { dialog, _ ->
            dialog.cancel()
        }
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
    }
}


