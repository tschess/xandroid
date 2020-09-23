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
import io.tschess.tschess.model.EntityPlayer

class HeaderSelf(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val glide = Glide.with(context)

    init {
        inflate(context, R.layout.header_self, this)
    }

    fun initialize(player: EntityPlayer, title: String = "tschess") {

        val textViewTitle: TextView = findViewById(R.id.title)
        textViewTitle.text = title

        val textViewName: TextView = findViewById(R.id.username)
        textViewName.text = player.username

        /***/
        val textViewScore: TextView = findViewById(R.id.rate_value)
        textViewScore.text = player.elo.toString()
        /***/
        val textViewRank: TextView = findViewById(R.id.rank_value)
        textViewRank.text = player.rank.toString()
        /***/
        val imageDisp: ImageView = findViewById(R.id.disp_image)
        val drawableDisp: Drawable? = player.getDispInd(context)
        imageDisp.setImageDrawable(drawableDisp)


        val imageView: ImageView = findViewById(R.id.avatar)
        imageView.visibility = View.INVISIBLE
        val drawable: Drawable? = player.drawable
        if (drawable != null) {
            imageView.visibility = View.VISIBLE
            imageView.setImageDrawable(drawable)
        }

        glide.load(player.avatar).apply(RequestOptions.circleCropTransform()).into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(resource)
                player.drawable = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }


}