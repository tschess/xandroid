package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import io.tschess.tschess.R

class DialogFairy(context: Context, val name: String) : Dialog(context) {

    lateinit var textTitle: TextView
    lateinit var imageFairy: ImageView
    lateinit var textContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fairy)
        this.textTitle = findViewById<TextView>(R.id.text_title)
        this.imageFairy = findViewById<ImageView>(R.id.image_fairy)
        this.textContent = findViewById<TextView>(R.id.content_text)
        this.setContent(name, textContent)
    }

    private fun setContent(name: String, contentText: TextView) {
        when (name.toLowerCase()) {
            "hunter" -> {
                this.textTitle.text = "Hunter"
                this.imageFairy.setImageResource(R.drawable.red_hunter)
                contentText.text = "• moves forward as standard bishop.\n" +
                        "• moves backwards as standard knight."
                return
            }
            "poison" -> {
                this.textTitle.text = "Poison Pawn"
                this.imageFairy.setImageResource(R.drawable.red_landmine)
                contentText.text =  "• appears to your opponent throughout the game as a standard pawn.\n" +
                        "• when captured, the opponent piece is also destroyed, i.e. eliminated from the game.\n" +
                        "• if king attempts to capture poison pawn the result is instant checkmate.\n" +
                        "• behaviour identical to a standard pawn.\n" +
                        "• cannot be promoted."
                return
            }
        } //"amazon"
        this.textTitle.text = "Amazon"
        this.imageFairy.setImageResource(R.drawable.red_amazon)
        contentText.text = "• performs all behavior of standard queen.\n" +
                "• performs all behavior of standard knight."
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
