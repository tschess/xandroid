package io.tschess.tschess.header

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer

class HeaderSnapshot(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val glide = Glide.with(context)

    init {
        inflate(context, R.layout.header_snapshot, this)
    }

    fun initialize(player: EntityPlayer, game: EntityGame, title: String = "endgame") {

        val textViewWinner: TextView = findViewById(R.id.winner)
        textViewWinner.text = game.getWinnerUsername()

        val textViewOutcome: TextView = findViewById(R.id.outcome)
        textViewOutcome.text = game.condition

        val textViewMoves: TextView = findViewById(R.id.moves)
        textViewMoves.text = game.moves.toString()

        val textViewTitle: TextView = findViewById(R.id.title)
        textViewTitle.text = title

        val imageView: ImageView = findViewById(R.id.avatar)
        val winner: String = game.getWinnerUsername()
        if(winner == player.username || winner == "Draw"){
            val drawable: Drawable? = player.drawable
            if (drawable != null) {
                //imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(drawable)
                return
            }
        }
        imageView.visibility = View.INVISIBLE
        val byteArray: ByteArray = game.getWinnerAvatar()!!
        glide.load(byteArray).apply(RequestOptions.circleCropTransform()).into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })

    }


}