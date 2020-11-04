package io.tschess.tschess.dialog.tschess

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import io.tschess.tschess.R

class DialogToast {

    fun passant(context: Context, inflater: LayoutInflater) {
        this.showSpecialAlert("♟️ ️en passant! \uD83D\uDC80", context, inflater)
    }

    fun poison(context: Context, inflater: LayoutInflater){
        this.showSpecialAlert("\uD83D\uDCA3 poison pawn!", context, inflater)
    }

    private fun showSpecialAlert(text: String, context: Context, inflater: LayoutInflater) {
        //val context: Context = applicationContext
        //val inflater: LayoutInflater = layoutInflater
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val message: TextView = toastView.findViewById(R.id.message)
        message.text = text
        val toast = Toast(context)
        toast.view = toastView
        toast.setGravity(
            Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL,
            0, 0
        )
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }
}