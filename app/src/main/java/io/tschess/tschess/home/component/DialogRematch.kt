package io.tschess.tschess.home.component

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import io.tschess.tschess.R


class DialogRematch(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_rematch)



        val textViewTitle: TextView = findViewById(R.id.config_text)
        textViewTitle.text = "config:"

        val myNumberPicker = findViewById<View>(R.id.number_picker) as NumberPicker

//Initializing a new string array with elements

//Initializing a new string array with elements
        val values = arrayOf("random", "config. x", "config. y", "config. z", "  traditional (chess)  ")

//Set min, max, wheel and populate.

//Set min, max, wheel and populate.
        myNumberPicker.minValue = 0
        myNumberPicker.maxValue = values.size - 1
        myNumberPicker.wrapSelectorWheel = true
        myNumberPicker.displayedValues = values

    }
}

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            // Get the layout inflater
//            val inflater = requireActivity().layoutInflater;
//
//            // Inflate and set the layout for the dialog
//            // Pass null as the parent view because its going in the dialog layout
//            builder.setView(inflater.inflate(R.layout.dialog_rematch, null))
//                // Add action buttons
//
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
//
//}