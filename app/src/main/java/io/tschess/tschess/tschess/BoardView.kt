package io.tschess.tschess.tschess

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import io.tschess.tschess.R
import io.tschess.tschess.gameboard.BoardCommon
import io.tschess.tschess.piece.Piece

class BoardView @JvmOverloads constructor(
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

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun populateBoard(
        matrix: Array<Array<Piece?>>,
        highlightList: List<Array<Int>>,
        turn: String
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
                tile.background = BoardCommon().getTileColorX(row, column)

                tile.scaleType = ImageView.ScaleType.CENTER_INSIDE
                if (highlightList != null) {
                    for (highlight in highlightList) {
                        if (arrayOf(row, column).contentEquals(highlight)) {
                            var indicator =
                                ContextCompat.getDrawable(context, R.drawable.highlight)
                            if (turn.toLowerCase() != "white") {
                                indicator = ContextCompat.getDrawable(context, R.drawable.highlight)
                            }
                            //indicator!!.alpha = 88
                            tile.setImageDrawable(indicator)
                        }
                    }
                }
                if (matrix[row][column] != null) {
                    val visible = matrix[row][column]!!.imageVisible
                    val foreground = context.getDrawable(visible)
                    tile.foreground = foreground
                }
                frameLayout.addView(tile)
                frameLayout.setOnClickListener { tile: View? ->
                    listener!!.touch(
                        tile!!
                    )
                }
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


@FunctionalInterface
interface Flasher {
    fun flash()
}

@FunctionalInterface
interface Listener {
    fun touch(tile: View)
}