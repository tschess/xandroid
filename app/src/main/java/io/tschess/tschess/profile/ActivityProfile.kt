package io.tschess.tschess.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.iid.FirebaseInstanceId
import io.tschess.tschess.R
import io.tschess.tschess.header.HeaderSelf

import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import io.tschess.tschess.start.ActivityStart

import org.json.JSONObject
import java.io.ByteArrayOutputStream


class ActivityProfile : AppCompatActivity() {

    private val REQUEST_CODE: Int = 666
    private val parsePlayer: ParsePlayer = ParsePlayer()
    private lateinit var progressBar: ProgressBar
    private lateinit var playerSelf: EntityPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        extras.clear()

        val headerSelf: HeaderSelf = findViewById(R.id.header)
        headerSelf.initialize(this.playerSelf)

        val listView: ListView = findViewById(R.id.list_view)
        val itemsAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_list_item_1,
            listOf("update photo", "notifications", "information", "sign out")
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: View = super.getView(position, convertView, parent)
                val text: TextView = view.findViewById<View>(android.R.id.text1) as TextView
                text.setTextColor(Color.BLACK)
                return view
            }
        }
        listView.adapter = itemsAdapter
        listView.setOnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }
            if (position == 1) {
                notifications()
            }
            if (position == 2) {
                //extras.putExtra("player_self", playerSelf)
                //val intent = Intent(this, ActivityInfo::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                //startActivity(intent)
                //finish()
            }
            if (position == 3) {
                renderMenuLogout()
            }
        }
        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                onBackPressed()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val uri: Uri = data.data!!
        val url = "${ServerAddress().IP}:8080/player/avatar"
        val defaultAvatar: ImageView = findViewById(R.id.avatar)
        val glide: RequestManager = Glide.with(applicationContext)
        progressBar.visibility = View.VISIBLE
        val params: MutableMap<String, String> = mutableMapOf()
        params["id"] = playerSelf.id
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            glide
                .asBitmap()
                .load(uri)
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
                        resource.compress(Bitmap.CompressFormat.JPEG, 9, byteArrayOutputStream)
                        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                        val avatar: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        params["avatar"] = avatar
                        val jsonObjectRequest: JsonObjectRequest = JsonObjectRequest(
                            Request.Method.POST, url, JSONObject(params as Map<*, *>),
                            { response: JSONObject ->
                                progressBar.visibility = View.INVISIBLE
                                val player: EntityPlayer = parsePlayer.execute(response)
                                playerSelf = player

                                // this.imageView = findViewById(R.id.image)
                                //        glide.load(attributes.getDrawable(R.styleable.CardHome_home_image))
                                //            .into(imageView)

                                glide.load(player.avatar).apply(RequestOptions.circleCropTransform()).into(object : CustomTarget<Drawable>() {
                                    override fun onResourceReady(
                                        resource: Drawable,
                                        transition: Transition<in Drawable>?
                                    ) {
                                        defaultAvatar.setImageDrawable(resource)
                                        playerSelf.drawable = resource
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {}
                                })
                            },
                            { progressBar.visibility = View.INVISIBLE })
                        VolleySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
                    }
                })
        }
    }

    @SuppressLint("HardwareIds")
    fun renderMenuLogout() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle("\uD83D\uDEAA tschess \uD83C\uDFC3\u200D♂️")
        dialogBuilder.setMessage("\uD83E\uDD14 Sure you want to sign out?")
            .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, id ->
                this.progressBar.visibility = View.VISIBLE
                val device: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                val url = "${ServerAddress().IP}:8080/player/clear/${device}"
                val request = JsonObjectRequest(
                    Request.Method.POST, url, null,
                    {
                        this.out()
                    },
                    {
                        this.out()
                    }
                )
                request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
                VolleySingleton.getInstance(this).addToRequestQueue(request)
            })
            .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
    }

    private fun notifications() {

        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle("\uD83D\uDDE3️ Notification settings \uD83D\uDC42")
        dialogBuilder.setMessage("☎️ Receive new move notifications?")
            .setPositiveButton("yes \uD83D\uDC4C", DialogInterface.OnClickListener { dialog, id ->
                val url = "${ServerAddress().IP}:8080/player/push"
                this.progressBar.visibility = View.VISIBLE

                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        // Get new Instance ID token
                        val token: String? = task.result?.token
                        // Log and dialog_toast
                        //val msg = getString(R.string.msg_token_fmt, token)
                        var note_key: String = "NULL"
                        if (!token.isNullOrBlank()) {
                            note_key = token
                        }
                        //Log.d("TAG", "ANDROID_${note_key}")
                        //Toast.makeText(baseContext, note_key, Toast.LENGTH_LONG).show()

                        val params = HashMap<String, String>()
                        params["id"] = playerSelf.id
                        params["note_key"] = "ANDROID_${note_key}"

                        val jsonObject = JSONObject(params as Map<*, *>)
                        Log.d("jsonObject", "${jsonObject}")

                        val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                            { response: JSONObject ->

                                Log.d("response", "${response}")
                                //response

                                this.progressBar.visibility = View.INVISIBLE

                                //TODO: ???
                            },
                            {
                                this.progressBar.visibility = View.INVISIBLE
                                //TODO: remove...
                            }
                        )
                        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
                        VolleySingleton.getInstance(this).addToRequestQueue(request)
                    })
            })
            .setNegativeButton("no", DialogInterface.OnClickListener { dialog, _ ->
                //TODO: FUCK
            })
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
    }

    private fun out() {
        this.progressBar.visibility = View.INVISIBLE
        val intent = Intent(applicationContext, ActivityStart::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        applicationContext.startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", playerSelf)

        finish()
    }

}
