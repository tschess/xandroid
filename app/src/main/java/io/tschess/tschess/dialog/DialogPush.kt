package io.tschess.tschess.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject
import java.util.*

class DialogPush(private val context: Context, val progressBar: ProgressBar) {

    fun notifications(player: EntityPlayer) {
        val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialog)
        dialogBuilder.setTitle("\uD83D\uDC42 notification ☎")
        dialogBuilder.setMessage("\uD83E\uDDE0️ once opponent has moved\nwould you like to know?")
            .setPositiveButton("yes \uD83D\uDC4C", DialogInterface.OnClickListener { dialog, id ->
                val url = "${ServerAddress().IP}:8080/player/push"
                this.progressBar.visibility = View.VISIBLE

                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        val token: String? = task.result?.token
                        var note_key: String = "NULL"
                        if (!token.isNullOrBlank()) {
                            note_key = token
                        }
                        val params = HashMap<String, String>()
                        params["id"] = player.id
                        params["note_key"] = "ANDROID_${note_key}"
                        val jsonObject = JSONObject(params as Map<*, *>)
                        //Log.d("jsonObject", "${jsonObject}")
                        val request = JsonObjectRequest(
                            Request.Method.POST, url, jsonObject,
                            { response: JSONObject ->
                                //Log.d("response", "${response}")
                                //response
                                this.progressBar.visibility = View.INVISIBLE
                            },
                            {
                                this.progressBar.visibility = View.INVISIBLE
                            }
                        )
                        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
                        VolleySingleton.getInstance(context).addToRequestQueue(request)
                    })
            })
            .setNegativeButton("no", DialogInterface.OnClickListener { dialog, _ ->
                //TODO: FUCK

                val url = "${ServerAddress().IP}:8080/player/push"
                this.progressBar.visibility = View.VISIBLE

                //FirebaseInstanceId.getInstance().instanceId
                    //.addOnCompleteListener(OnCompleteListener { task ->
                        //val token: String? = task.result?.token
                        var note_key: String = "DECLINE"
                        //if (!token.isNullOrBlank()) {
                            //note_key = token
                        //}
                        val params = HashMap<String, String>()
                        params["id"] = player.id
                        params["note_key"] = "ANDROID_${note_key}"
                        val jsonObject = JSONObject(params as Map<*, *>)
                        //Log.d("jsonObject", "${jsonObject}")
                        val request = JsonObjectRequest(
                            Request.Method.POST, url, jsonObject,
                            { response: JSONObject ->
                                //Log.d("response", "${response}")
                                //response
                                this.progressBar.visibility = View.INVISIBLE
                            },
                            {
                                this.progressBar.visibility = View.INVISIBLE
                            }
                        )
                        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
                        VolleySingleton.getInstance(context).addToRequestQueue(request)
                    //})


            })
        val alert: AlertDialog = dialogBuilder.create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alert.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1)
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alert.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }
        alert.show()
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
    }
}