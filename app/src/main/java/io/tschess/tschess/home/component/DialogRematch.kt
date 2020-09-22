package io.tschess.tschess.home.component

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import io.tschess.tschess.R

class DialogRematch(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_rematch)

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