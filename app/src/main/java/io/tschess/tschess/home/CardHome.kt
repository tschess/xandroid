package io.tschess.tschess.home

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityPlayer

class CardHome(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val glide = Glide.with(context)
    val imageView: ImageView
    val textViewName: TextView

    lateinit var playerRival: EntityPlayer

    init {
        inflate(context, R.layout.card_home, this)
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CardHome)
        this.textViewName = findViewById(R.id.name)
        this.imageView = findViewById(R.id.image)
        glide.load(attributes.getDrawable(R.styleable.CardHome_home_image)).apply(RequestOptions.circleCropTransform())
            .into(imageView)
        attributes.recycle()
    }

}

@FunctionalInterface
interface Rival {
    fun shudder(rival: CardHome)
    fun challenge(rival: CardHome)
}