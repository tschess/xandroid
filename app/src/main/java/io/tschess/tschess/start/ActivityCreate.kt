package io.tschess.tschess.start

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import io.tschess.tschess.R

class ActivityCreate : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    private lateinit var inputUsername: EditText
    private lateinit var inputPassword: EditText

    private lateinit var buttonGo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        this.inputUsername = findViewById<EditText>(R.id.et_user_name)
        this.inputPassword = findViewById<EditText>(R.id.et_password)

        this.buttonGo = findViewById<Button>(R.id.btn_create)
        this.buttonGo.setOnClickListener {
            val username: String = inputUsername.text.toString()
            val password: String = inputPassword.text.toString()
            val device: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            UtilityStart(this, progressBar).evaluateCreate(username, password, device)
        }
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