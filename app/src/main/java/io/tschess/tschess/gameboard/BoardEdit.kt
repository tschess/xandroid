package io.tschess.tschess.gameboard

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.gameboard.BoardCommon

class BoardEdit @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    GridLayout(context, attrs, defStyleAttr) {

    lateinit var listenerDrop: OnDragListener
    lateinit var listenerClick: OnLongClickListener

    fun populateBoard(matrix: Array<Array<Piece?>>) {
        removeAllViews()
        val size: Int = Resources.getSystem().displayMetrics.widthPixels / 8
        for (row: Int in 0..1) {
            for (column: Int in 0..7) {
                val frameLayout = FrameLayout(context)
                val params = FrameLayout.LayoutParams(size, size)
                val tile = ImageView(context)
                tile.layoutParams = params
                tile.background = ColorDrawable(getColor(context, BoardCommon()
                    .getTileColor(row, column)))
                tile.scaleType = ImageView.ScaleType.CENTER_INSIDE
                frameLayout.addView(tile)
                frameLayout.setOnDragListener { view, event -> listenerDrop.onDrag(view, event) }

                if (matrix[row][column] != null) {
                    val visible: Int = matrix[row][column]!!.imageVisible

                    val drawable: Drawable = context.getDrawable(visible)!!
                    val imageView = ImageView(context)
                    imageView.setImageDrawable(drawable)
                    imageView.layoutParams = params
                    imageView.setOnLongClickListener { view -> listenerClick.onLongClick(view) }
                    frameLayout.addView(imageView)
                }

                this.addView(frameLayout)
            }
        }
    }

    init {
        this.rowCount = 2
        this.columnCount = 8
    }
}