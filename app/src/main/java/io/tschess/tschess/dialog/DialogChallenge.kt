package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityPlayer


class DialogChallenge(context: Context, val playerSelf: EntityPlayer, val playerOther: EntityPlayer, val accept: Boolean = false) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_challenge)


        val tViewTitle: TextView = findViewById(R.id.text_title)
        tViewTitle.text = "\uD83E\uDD1C ${playerSelf.username} vs. ${playerOther.username} \uD83E\uDD1B"


        val textViewTitle: TextView = findViewById(R.id.config_text)
        textViewTitle.text = "config:"

        val optionPicker: NumberPicker = findViewById<View>(R.id.number_picker) as NumberPicker

        val values: MutableList<String> = mutableListOf("random", "config. x", "config. y", "config. z", "   traditional (chess)   ")
        if(accept){
            values.add("mirror opponent")
            val text_send: TextView = findViewById(R.id.text_send)
            text_send.text = "let's play! \uD83C\uDF89"
        }

        optionPicker.minValue = 0
        optionPicker.maxValue = values.size - 1
        optionPicker.wrapSelectorWheel = true
        optionPicker.displayedValues = values.toTypedArray()

    }
}

