package io.tschess.tschess.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import io.tschess.tschess.R
import org.json.JSONObject

class DialogOk(val context: Context) {

    fun error(message: String) {
        val title: String = "❌ error ✋"
        this.render(title, message)
    }

    fun confirm(username: String) {
        val title: String = "✅ success \uD83D\uDC4C"
        val message: String = "challenge dispatched to ${username} ♟️"
        this.render(title, message)
    }

    fun render(title: String, message: String) {
        val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialog)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setPositiveButton("ok") { dialog, _ ->
            dialog.cancel()
        }
        val alert: AlertDialog = dialogBuilder.create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alert.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1)
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alert.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }
        alert.show()
    }

    fun fail(response: JSONObject): Boolean {
        if (response.has("unknown")) {
            val title: String = "⚡ unknown username ⚡"
            val message: String = "\uD83E\uDD16 no such player in database.\nplease re-evaluate and try again. \uD83D\uDCF2"
            this.render(title, message)
            return true
        }
        if (response.has("invalid")) {
            val title: String = "⚡ invalid password ⚡"
            val message: String = "\uD83E\uDD16 input password is incorrect.\nplease re-evaluate and try again. \uD83D\uDCF2"
            this.render(title, message)
            return true
        }
        if (response.has("reserved")) {
            val title: String = "⚡ reserved username ⚡"
            val message: String = "\uD83E\uDD16 input username is reserved.\nplease re-evaluate and try again. \uD83D\uDCF2"
            this.render(title, message)
            return true
        }
        //connection
        //illegal
        return false
    }

}