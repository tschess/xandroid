package io.tschess.tschess.home.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.bishop.BishopBlack
import io.tschess.tschess.piece.chess.bishop.BishopWhite
import io.tschess.tschess.piece.chess.knight.KnightBlack
import io.tschess.tschess.piece.chess.knight.KnightWhite
import io.tschess.tschess.piece.chess.queen.QueenBlack
import io.tschess.tschess.piece.chess.queen.QueenWhite
import io.tschess.tschess.piece.chess.rook.RookBlack
import io.tschess.tschess.piece.chess.rook.RookWhite
import io.tschess.tschess.tschess.ActivityTschess
import org.json.JSONObject
import java.util.*

class DialogRematch(context: Context, attrs: AttributeSet? = null) : TableLayout(context, attrs) {


    init {
        inflate(context, R.layout.dialog_promo, this)
    }



}