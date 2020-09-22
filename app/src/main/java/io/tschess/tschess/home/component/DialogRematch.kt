package io.tschess.tschess.home.component

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import io.tschess.tschess.R

class DialogRematch(context: Context) : AlertDialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_rematch)

    }
}