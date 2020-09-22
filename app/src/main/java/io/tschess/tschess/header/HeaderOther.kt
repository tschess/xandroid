package io.tschess.tschess.header

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityPlayer

class HeaderOther(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val glide = Glide.with(context)

    init {
        inflate(context, R.layout.header_other, this)
    }

    fun initialize(player: EntityPlayer, title: String = "tschess") {
        //val imageView: ImageView = findViewById(R.id.avatar)
        //imageView.visibility = View.INVISIBLE

        val textViewName: TextView = findViewById(R.id.username)
        textViewName.text = player.username

        val textViewTitle: TextView = findViewById(R.id.title)
        textViewTitle.text = title


        val textViewScore: TextView = findViewById(R.id.score)
        textViewScore.text = player.elo.toString()
        /***/
        val textViewRank: TextView = findViewById(R.id.rank)
        textViewRank.text = player.rank.toString()

        val textViewDate: TextView = findViewById(R.id.date)
        textViewDate.text = player.getDateText()


        val imageView: ImageView = findViewById(R.id.avatar)
        imageView.visibility = View.INVISIBLE
        val drawable: Drawable? = player.drawable
        if (drawable != null) {
            imageView.visibility = View.VISIBLE
            imageView.setImageDrawable(drawable)
        }
        glide.load(player.avatar).into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(resource)
                player.drawable = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
        //}
    }


}