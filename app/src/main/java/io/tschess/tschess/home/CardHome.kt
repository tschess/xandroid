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

class CardHome(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val glide = Glide.with(context)

    val imageView: ImageView
    val textViewName: TextView

    init {
        inflate(context, R.layout.card_home, this)
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CardHome)

        //this.listenerDefault = ActivityHome.CardTouchActive()

        this.textViewName = findViewById(R.id.name)
        //this.name = attributes.getString(R.styleable.CardView_name)!!
        //textViewName.text = this.name

        /* * */
        this.imageView = findViewById(R.id.image)
        //this.imageView.setImageDrawable(attributes.getDrawable(R.styleable.CardRival_image))

        glide.load(attributes.getDrawable(R.styleable.CardHome_home_image)).apply(RequestOptions.circleCropTransform())
            .into(imageView)


        attributes.recycle()
    }

}