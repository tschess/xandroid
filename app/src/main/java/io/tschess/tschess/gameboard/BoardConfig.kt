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
import androidx.core.content.ContextCompat
import io.tschess.tschess.piece.Piece

class BoardConfig @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    GridLayout(context, attrs, defStyleAttr) {

    fun populateBoard(matrix: Array<Array<Piece?>>) {
        removeAllViews()
        val size: Int = Resources.getSystem().displayMetrics.widthPixels / 8
        for (row: Int in 0..1) {
            for (column: Int in 0..7) {
                val frameLayout = FrameLayout(context)
                val params = FrameLayout.LayoutParams(size, size)
                val tile = ImageView(context)
                tile.layoutParams = params
                tile.background =
                    ColorDrawable(ContextCompat.getColor(context, BoardCommon()
                        .getTileColor(row, column)))
                tile.scaleType = ImageView.ScaleType.CENTER_INSIDE
                if (matrix[row][column] != null) {
                    val visible: Int = matrix[row][column]!!.imageVisible
                    val foreground: Drawable = context.getDrawable(visible)!!
                    tile.foreground = foreground
                }
                frameLayout.addView(tile)
                this.addView(frameLayout)
            }
        }
    }

    init {
        this.rowCount = 2
        this.columnCount = 8
    }
}