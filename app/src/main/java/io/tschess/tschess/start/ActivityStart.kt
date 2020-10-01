package io.tschess.tschess.start

import android.os.Bundle
import android.provider.Settings.Secure
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import io.tschess.tschess.R
import io.tschess.tschess.dialog.DialogOk
import io.tschess.tschess.server.ServerAddress

class ActivityStart : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    private lateinit var inputUsername: EditText
    private lateinit var inputPassword: EditText

    private lateinit var buttonLogin: Button
    private lateinit var buttonCreate: Button
    private lateinit var buttonRecover: Button

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
            DialogOk(applicationContext).render(title, message)
        }

        this.buttonCreate.setOnClickListener {
            val username: String = inputUsername.text.toString()
            val password: String = inputPassword.text.toString()
            val device: String = Secure.getString(contentResolver, Secure.ANDROID_ID)
            UtilityStart(this, progressBar).evaluateCreate(username, password, device)
        }

        this.buttonLogin.setOnClickListener {
            val url = "${ServerAddress().IP}:8080/player/login"
            val username: String = inputUsername.text.toString()
            val password: String = inputPassword.text.toString()
            val device: String = Secure.getString(contentResolver, Secure.ANDROID_ID)
            UtilityStart(this, progressBar).requestServer(username, password, device, url)
        }

    }


}


