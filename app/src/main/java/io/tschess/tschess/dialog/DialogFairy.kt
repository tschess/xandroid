package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import io.tschess.tschess.R
import io.tschess.tschess.model.ParseGame

class DialogFairy(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fairy)
    }



    companion object {
        fun candidate(name: String): Boolean {
            val poison: Boolean = name == "poison"
            val hunter: Boolean = name == "hunter"
            val amazon: Boolean = name == "amazon"

            return poison || hunter || amazon
        }
    }

}
