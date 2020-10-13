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
    lateinit var textContentStrength: TextView
    lateinit var textContentWeakness: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fairy)
        this.textTitle = findViewById<TextView>(R.id.text_title)
        this.imageFairy = findViewById<ImageView>(R.id.image_fairy)
        this.textContentStrength = findViewById<TextView>(R.id.content_strength)
        this.textContentWeakness = findViewById<TextView>(R.id.content_weakness)
        if(name.toLowerCase() == "poison"){
            this.setPoison()
        }
        if(name.toLowerCase() == "hunter"){
            this.setHunter()
        }
        if(name.toLowerCase() == "amazon"){
            this.setAmazon()
        }
    }

    private fun setPoison() {
        this.textTitle.text = "Poison Pawn"
        this.imageFairy.setImageResource(R.drawable.red_landmine)
        val contentStrength: String =
            "• behaviour identical to a standard pawn with one caveat...\n• when captured, the opponent piece is also destroyed, i.e. eliminated from the game.\n• if king attempts to capture poison pawn the result is instant checkmate."
        this.textContentStrength.text = contentStrength
        val contentWeakness: String = "• appears to your opponent throughout the game as a standard pawn."
        this.textContentWeakness.text = contentWeakness
    }

    private fun setHunter() {
        this.textTitle.text = "Hunter"
        this.imageFairy.setImageResource(R.drawable.red_hunter)
        val contentStrength: String =
            "• behaviour identical to a standard pawn with one caveat...\n• when captured, the opponent piece is also destroyed, i.e. eliminated from the game.\n• if king attempts to capture poison pawn the result is instant checkmate."
        this.textContentStrength.text = contentStrength
        val contentWeakness: String = "• appears to your opponent throughout the game as a standard pawn."
        this.textContentWeakness.text = contentWeakness
    }

    private fun setAmazon() {
        this.textTitle.text = "Amazon"
        this.imageFairy.setImageResource(R.drawable.red_amazon)
        val contentStrength: String =
            "• behaviour identical to a standard pawn with one caveat...\n• when captured, the opponent piece is also destroyed, i.e. eliminated from the game.\n• if king attempts to capture poison pawn the result is instant checkmate."
        this.textContentStrength.text = contentStrength
        val contentWeakness: String = "• appears to your opponent throughout the game as a standard pawn."
        this.textContentWeakness.text = contentWeakness
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
