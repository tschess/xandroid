package io.tschess.tschess.edit

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import io.tschess.tschess.R

class CardEdit(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    val name: String
    val strength: Int

    val imageView: ImageView
    val textViewName: TextView
    val textViewStrength: TextView

    val listenerDefault: CardTouchActive = CardTouchActive()

    init {
        inflate(context, R.layout.card_edit, this)
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CardEdit)

        //this.listenerDefault


        this.textViewName = findViewById(R.id.name)
        this.name = attributes.getString(R.styleable.CardEdit_edit_name)!!
        textViewName.text = this.name

        /* * */
        this.imageView = findViewById(R.id.image)
        this.imageView.setImageDrawable(attributes.getDrawable(R.styleable.CardEdit_edit_image))

        this.listenerDefault.name = this.name
        this.listenerDefault.coord = "99"
        imageView.setOnTouchListener(this.listenerDefault)
        /* * */

        this.textViewStrength = findViewById(R.id.strength)
        val string: String = attributes.getString(R.styleable.CardEdit_edit_strength)!!
        textViewStrength.text = string
        this.strength = string.toInt()

        attributes.recycle()
    }

    fun setLabelInfo(labelInfo: TextView) {
        listenerDefault.labelInfo = labelInfo
    }


}