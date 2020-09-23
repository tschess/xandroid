package io.tschess.tschess.gameboard

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import io.tschess.tschess.R

class CommonBoard {
    fun getTileColor(row: Int, column: Int): Int {
        if (row % 2 == 0) {
            return if (column % 2 == 0) {
                R.color.colorWhite
            } else R.color.colorBlack
        }
        return if (column % 2 == 0) {
            R.color.colorBlack
        } else R.color.colorWhite
    }

    fun getTileColorX(row: Int, column: Int): Drawable {
        if (row % 2 == 0) {
            return if (column % 2 == 0) {
                //Color.rgb(128, 0, 128).toDrawable()
                Color.WHITE.toDrawable()
            }
            //else Color.rgb(153, 102, 51).toDrawable()
            else Color.BLACK.toDrawable()
        }
        return if (column % 2 == 0) {
            //Color.rgb(153, 102, 51).toDrawable()
            Color.BLACK.toDrawable()
        }
        //else Color.rgb(128, 0, 128).toDrawable()
        else Color.WHITE.toDrawable()
    }

}