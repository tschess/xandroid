package io.tschess.tschess.snapshot

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import io.tschess.tschess.R
import io.tschess.tschess.board.CommonBoard
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.tschess.Listener

class ViewSnapshot @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    GridLayout(context, attrs, defStyleAttr) {
    private val whiteTile: Drawable?
    private val blackTile: Drawable?
    private var listener: Listener? = null
    public override fun onMeasure(width: Int, height: Int) {
        setMeasuredDimension(MeasureSpec.getSize(width), MeasureSpec.getSize(height))
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        )
    }

    fun populateBoard(
        matrix: Array<Array<Piece?>>
    ) {
        removeAllViews()
        val size = Resources.getSystem().displayMetrics.widthPixels / 8
        for (row in 0..7) {
            for (column in 0..7) {
                val imageId = determineTileColor(row, column)
                val frameLayout = FrameLayout(context)
                val params = FrameLayout.LayoutParams(size, size)
                val tile = ImageView(context)
                tile.layoutParams = params
                tile.background = CommonBoard().getTileColorX(row, column)
                tile.scaleType = ImageView.ScaleType.CENTER_INSIDE

                if (matrix[row][column] != null) {
                    val visible = matrix[row][column]!!.imageVisible
                    val foreground = context.getDrawable(visible)
                    tile.foreground = foreground
                }
                frameLayout.addView(tile)

                this.addView(frameLayout)
            }
        }
    }

    private fun determineTileColor(row: Int, column: Int): Drawable? {
        if (row % 2 == 0) {
            return if (column % 2 == 0) {
                whiteTile
            } else blackTile
        }
        return if (column % 2 == 0) {
            blackTile
        } else whiteTile
    }

    init {
        this.rowCount = 8
        this.columnCount = 8
        blackTile = ContextCompat.getDrawable(getContext(), R.drawable.tile_black)
        whiteTile = ContextCompat.getDrawable(getContext(), R.drawable.tile_white)
    }
}