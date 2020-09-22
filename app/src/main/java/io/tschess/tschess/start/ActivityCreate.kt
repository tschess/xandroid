package io.tschess.tschess.start

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import io.tschess.tschess.R
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.home.ActivityHome
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class ActivityCreate : AppCompatActivity() {

    private val parsePlayer: ParsePlayer = ParsePlayer()

    lateinit var progressBar: ProgressBar

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText

    private lateinit var buttonGo: Button

    @SuppressLint("HardwareIds", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        this.editTextUsername = findViewById<EditText>(R.id.et_user_name)
        this.editTextPassword = findViewById<EditText>(R.id.et_password)

        this.buttonGo = findViewById<Button>(R.id.btn_create)
        this.buttonGo.setOnClickListener {
            this.progressBar.visibility = View.VISIBLE

            val url = "${ServerAddress().IP}:8080/player/create"

            val username: String = editTextUsername.text.toString().toLowerCase(Locale.ENGLISH)
            val usernameFilter: String = username.filter { it.isLetterOrDigit() }

            val password: String = editTextPassword.text.toString()
            editTextUsername.setText("")
            editTextPassword.setText("")
            val device: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

            val params = HashMap<String, String>()
            //params["username"] = username
            params["username"] = usernameFilter
            params["password"] = password
            params["device"] = device

            val jsonObject = JSONObject(params as Map<*, *>)

            val request = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response: JSONObject ->
                    //this.progressBar.visibility = View.INVISIBLE

                    this.progressBar.visibility = View.INVISIBLE
                    Log.e("-->", "${response}")
                    if (response.has("fail")) {
                        this.dialogInvalid()
                        return@JsonObjectRequest
                    }
                    val playerSelf: EntityPlayer = parsePlayer.execute(response)

                    //val playerSelf: PlayerEntity = parsePlayer.execute(response)
                    val intent = Intent(applicationContext, ActivityHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
                    extras.putExtra("player_self", playerSelf)

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

    override fun onBackPressed() {
        val intent = Intent(applicationContext, ActivityStart::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        applicationContext.startActivity(intent)
        finish()
    }
}