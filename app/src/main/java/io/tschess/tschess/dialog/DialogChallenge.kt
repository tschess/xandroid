package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityPlayer


class DialogChallenge(context: Context, val playerSelf: EntityPlayer, val playerOther: EntityPlayer) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_challenge)


        val tViewTitle: TextView = findViewById(R.id.text_title)
        tViewTitle.text = "\uD83E\uDD1C ${playerSelf.username} vs. ${playerOther.username} \uD83E\uDD1B"


        val textViewTitle: TextView = findViewById(R.id.config_text)
        textViewTitle.text = "config:"

        val myNumberPicker = findViewById<View>(R.id.number_picker) as NumberPicker


        val values = arrayOf("random", "config. x", "config. y", "config. z", "   traditional (chess)   ")

        myNumberPicker.minValue = 0
        myNumberPicker.maxValue = values.size - 1
        myNumberPicker.wrapSelectorWheel = true
        myNumberPicker.displayedValues = values

    }
}

