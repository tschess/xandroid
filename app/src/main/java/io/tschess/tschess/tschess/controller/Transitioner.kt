package io.tschess.tschess.tschess.controller

import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.PieceAnte
import io.tschess.tschess.tschess.Flasher
import kotlin.math.abs

class Transitioner(
private val white: Boolean,
private val flasher: Flasher? = null
) {

    fun clear() {
        this.coordinate = null
    }

    fun getCoord(): Array<Int>? {
        return coordinate
    }

    private val checker: Checker = Checker()

    private var coordinate: Array<Int>? = null

    fun execute(propose: Array<Int>, matrix: Array<Array<Piece?>>): Array<Array<Piece?>> {
        if (matrix[this.coordinate!![0]][this.coordinate!![1]] == null) {
            return matrix
        }
        val select: Piece = matrix[this.coordinate!![0]][this.coordinate!![1]]!!
        matrix[propose[0]][propose[1]] = select
        matrix[propose[0]][propose[1]]!!.touched = true
        matrix[this.coordinate!![0]][this.coordinate!![1]] = null
        return matrix
    }

    fun valid(coord: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        //TODO: check this here...
        //if(coord00 != null){}
        if (this.coordinate!!.contentEquals(coord)) {
            return false
        }
        val square: Piece? = matrix[coord[0]][coord[1]]
        if (this.available(square)) {
            return true
        }
        return false
    }

    private fun available(square: Piece?): Boolean {
        if (square == null) {
            return false
        }
        if (square.target) {
            return true
        }
        if (square.name == "PieceAnte") {
            return true
        }
        return false
    }


    private fun invalid(coord: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {
        if (this.coordinate == null) {
            val tschessElement: Piece? = matrix[coord[0]][coord[1]]
            if (tschessElement == null) {
                this.flasher!!.flash()
                return true
            }
            if (this.white) {
                if (tschessElement.affiliation.toLowerCase() != "white") {
                    this.flasher!!.flash()
                    return true
                }
            } else if (tschessElement.affiliation.toLowerCase() != "black") {
                this.flasher!!.flash()
                return true
            }
        }
        return false
    }

    private fun getName(piece: Piece?): String {
        if (piece == null) {
            return ""
        }
        if (!piece.touched) {
            return "${piece.name}_x"
        }
        return piece.name
    }

    private fun getName(input: Array<Piece?>, white: Boolean): List<String> {
        val output: Array<String> = arrayOf("", "", "", "", "", "", "", "")
        for ((index: Int, piece: Piece?) in input.withIndex()) {
            if (white) {
                output[index] = this.getName(piece)
            } else {
                output[abs(7 - index)] = this.getName(piece)
            }
        }
        return output.toList()
    }

    fun render(matrix: Array<Array<Piece?>>, white: Boolean): List<List<String>> {
        val rowA: List<String> = this.getName(input = matrix[0], white = white)
        val rowB: List<String> = this.getName(input = matrix[1], white = white)
        val rowC: List<String> = this.getName(input = matrix[2], white = white)
        val rowD: List<String> = this.getName(input = matrix[3], white = white)
        val rowE: List<String> = this.getName(input = matrix[4], white = white)
        val rowF: List<String> = this.getName(input = matrix[5], white = white)
        val rowG: List<String> = this.getName(input = matrix[6], white = white)
        val rowH: List<String> = this.getName(input = matrix[7], white = white)
        if (white) {
            return listOf(rowH, rowG, rowF, rowE, rowD, rowC, rowB, rowA)
        }
        return listOf(rowA, rowB, rowC, rowD, rowE, rowF, rowG, rowH)
    }

    fun deselect(matrix: Array<Array<Piece?>>): Array<Array<Piece?>> {
        for ((i: Int, row: Array<Piece?>) in matrix.withIndex()) {
            for ((j: Int, item: Piece?) in row.withIndex()) {
                val square: Piece = matrix[i][j] ?: continue
                if (square.name == "PieceAnte") {
                    matrix[i][j] = null
                    continue
                }
                if (square.target) {
                    val imageDefault: Int = square.imageDefault
                    matrix[i][j]!!.imageVisible = imageDefault
                    matrix[i][j]!!.target = false
                }
            }
        }
        if (matrix[this.coordinate!![0]][this.coordinate!![1]] != null) {
            val imageDefault: Int = matrix[this.coordinate!![0]][this.coordinate!![1]]!!.imageDefault
            matrix[this.coordinate!![0]][this.coordinate!![1]]!!.imageVisible = imageDefault
        }
        return matrix
    }

    fun highlight(coord: Array<Int>, matrix: Array<Array<Piece?>>): Array<Array<Piece?>> {
        if (this.invalid(coord = coord, matrix = matrix)) {
            return matrix
        }
        this.coordinate = coord
        for ((i: Int, row: Array<Piece?>) in matrix.withIndex()) {
            for ((j: Int, item: Piece?) in row.withIndex()) {
                val piece: Piece = matrix[this.coordinate!![0]][this.coordinate!![1]]!!
                if (piece.validate(present = coord, propose = arrayOf(i, j), matrix = matrix)) {
                    /* * */
                    val king: Array<Int> = checker.coordinateKing(piece.affiliation, matrix)
                    val hold: Piece? = matrix[i][j]
                    matrix[i][j] = piece
                    matrix[this.coordinate!![0]][this.coordinate!![1]] = null
                    val czech: Boolean = if (piece.name.contains("King")) {
                        checker.self(arrayOf(i, j), matrix)
                    } else {
                        checker.self(king, matrix)
                    }
                    matrix[i][j] = hold
                    matrix[this.coordinate!![0]][this.coordinate!![1]] = piece
                    if (!czech) {
                        val dest: Piece? = matrix[i][j]
                        if (dest == null) {
                            matrix[i][j] = PieceAnte()
                            continue
                        }
                        val imageTarget: Int = dest.imageTarget!!
                        matrix[i][j]!!.imageVisible = imageTarget
                        matrix[i][j]!!.target = true
                    }
                    /* * */

                }
            }
        }
        val select: Piece = matrix[this.coordinate!![0]][this.coordinate!![1]]!!
        val imageSelect: Int = select.imageSelect!!
        matrix[this.coordinate!![0]][this.coordinate!![1]]!!.imageVisible = imageSelect
        return matrix
    }
}