package io.tschess.tschess.piece

import java.util.*

open class Piece(
    val name: String,
    val strength: Int = 0,
    var imageDefault: Int,
    val imageTarget: Int? = null,
    val imageSelect: Int? = null,
    var attackList: List<String>? = null,
    val chess: Boolean = false
) {
    var affiliation: String
    var target: Boolean
    var touched: Boolean
    var imageVisible: Int

    init {
        this.target = false
        this.touched = false
        this.imageVisible = this.imageDefault
        this.affiliation = "white"
        if(!this.name.toLowerCase(Locale.ENGLISH).contains("white")){
            this.affiliation = "black"
        }
    }

    fun compatriot(piece: Piece): Boolean {
        if(this.getWhite() == piece.getWhite()){
            return true
        }
        return false
    }

    private fun getWhite(): Boolean {
        if(this.name.toLowerCase(Locale.ENGLISH).contains("white")){
            return true
        }
        return false
    }

    open fun validate(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        return false
    }
}


