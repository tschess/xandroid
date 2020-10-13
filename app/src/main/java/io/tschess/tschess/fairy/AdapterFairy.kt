package io.tschess.tschess.fairy

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.tschess.tschess.R
import io.tschess.tschess.piece.fairy.Fairy

class AdapterFairy(
    private val context: Context,
    private val dataSource: Array<Fairy>
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val fairy: Fairy = getItem(position) as Fairy

        val rowView: View = inflater.inflate(R.layout.item_fairy, parent, false)

        val textView: TextView = rowView.findViewById(R.id.text_fairy) as TextView
        textView.text = fairy.name

        val default: Int = fairy.imageDefault
        val drawable: Drawable = ContextCompat.getDrawable(context, default)!!
        val avatarImageView: ImageView = rowView.findViewById(R.id.avatar_fairy) as ImageView
        avatarImageView.setImageDrawable(drawable)

        return rowView
    }



}