package io.tschess.tschess.dialog.tschess

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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

class DialogPromo(var coord: Array<Int>, var activityTschess: ActivityTschess, context: Context) : Dialog(context) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.dialog_promo)

            val imageViewBiship: ImageView = findViewById(R.id.bishop_image)
            imageViewBiship.setOnClickListener(View.OnClickListener {
                val coord: Array<Int> = this.activityTschess.transitioner.getCoord()!!
                val matrix00: Array<Array<Piece?>> = this.activityTschess.transitioner.deselect(this.activityTschess.matrix)
                if (this.activityTschess.white) {
                    matrix00[coord[0]][coord[1]] = BishopWhite()
                } else {
                    matrix00[coord[0]][coord[1]] = BishopBlack()
                }
                this.activityTschess.matrix = matrix00
                this.dispatch(coord)
            })
            val imageViewQueen: ImageView = findViewById(R.id.queen_image)
            imageViewQueen.setOnClickListener(View.OnClickListener {
                val coord: Array<Int> = this.activityTschess.transitioner.getCoord()!!
                val matrix00: Array<Array<Piece?>> = this.activityTschess.transitioner.deselect(this.activityTschess.matrix)
                if (this.activityTschess.white) {
                    matrix00[coord[0]][coord[1]] = QueenWhite()
                } else {
                    matrix00[coord[0]][coord[1]] = QueenBlack()
                }
                this.activityTschess.matrix = matrix00
                this.dispatch(coord)
            })
            val imageViewRook: ImageView = findViewById(R.id.rook_image)
            imageViewRook.setOnClickListener(View.OnClickListener {
                val coord: Array<Int> = this.activityTschess.transitioner.getCoord()!!
                val matrix00: Array<Array<Piece?>> = this.activityTschess.transitioner.deselect(this.activityTschess.matrix)
                if (this.activityTschess.white) {
                    matrix00[coord[0]][coord[1]] = RookWhite()
                } else {
                    matrix00[coord[0]][coord[1]] = RookBlack()
                }
                this.activityTschess.matrix = matrix00
                this.dispatch(coord)
            })
            val imageViewKnight: ImageView = findViewById(R.id.knight_image)
            imageViewKnight.setOnClickListener(View.OnClickListener {
                val coord: Array<Int> = this.activityTschess.transitioner.getCoord()!!
                val matrix00: Array<Array<Piece?>> = this.activityTschess.transitioner.deselect(this.activityTschess.matrix)
                if (this.activityTschess.white) {
                    matrix00[coord[0]][coord[1]] = KnightWhite()
                } else {
                    matrix00[coord[0]][coord[1]] = KnightBlack()
                }
                this.activityTschess.matrix = matrix00
                this.dispatch(coord)
            })
        }

    private fun dispatch(coord: Array<Int>){
        val matrixXX: Array<Array<Piece?>> = this.activityTschess.transitioner.execute(propose = this.coord, matrix = this.activityTschess.matrix)
        val state: List<List<String>> = this.activityTschess.transitioner.render(matrix = matrixXX, white = this.activityTschess.white)
        val highlight: String = if (this.activityTschess.white) {
            "${coord[0]}${coord[1]}${this.coord[0]}${this.coord[1]}"
        } else {
            "${7 - coord[0]}${7 - coord[1]}${7 - this.coord[0]}${7 - this.coord[1]}"
        }
        val params = HashMap<String, Any>()
        params["id_game"] = this.activityTschess.game.id
        params["state"] = state
        params["highlight"] = highlight
        params["condition"] = "TBD"
        val jsonObject = JSONObject(params as Map<*,*>)
        this.activityTschess.networker.deliver(jsonObject)
        this.dismiss()
    }





}