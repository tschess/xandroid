package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import io.tschess.tschess.R

class DialogFairy(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fairy)
    }
    
    companion object {
        fun candidate(name: String): Boolean {
            val poison: Boolean = name.toLowerCase() == "poison"
            val hunter: Boolean = name.toLowerCase() == "hunter"
            val amazon: Boolean = name.toLowerCase() == "amazon"
            return poison || hunter || amazon
        }
    }

}
