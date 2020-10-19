package io.tschess.tschess.model


import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.model.Render
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

class EntityGame(
    val id: String,
    val state: List<List<String>>?,
    var status: String,
    var condition: String,
    var moves: Int,
    var white: EntityPlayer,
    var black: EntityPlayer,
    var challenger: String,
    var winner: String?,
    var turn: String,
    var on_check: Boolean,
    var highlight: String,
    var updated: String
) : Parcelable {

    @SuppressLint("NewApi")
    constructor(parcel: Parcel) : this(
        parcel.readString()!!, //id
        parcel.createStringArrayList()?.chunked(8), //state
        parcel.readString()!!, //status
        parcel.readString()!!, //condition
        parcel.readInt(), //moves
        parcel.readParcelable<EntityPlayer>(EntityPlayer::class.java.classLoader)!!, //white
        parcel.readParcelable<EntityPlayer>(EntityPlayer::class.java.classLoader)!!, //black
        parcel.readString()!!,//challenger
        parcel.readString(),//winner
        parcel.readString()!!,//turn
        parcel.readBoolean(), //on_check
        parcel.readString()!!, //highlight
        parcel.readString()!! //updated
    )

    @SuppressLint("NewApi")
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.id)
        parcel.writeStringList(this.state?.flatten())
        parcel.writeString(this.status)
        parcel.writeString(this.condition)
        parcel.writeInt(this.moves)
        parcel.writeParcelable(this.white, 0)
        parcel.writeParcelable(this.black, 0)
        parcel.writeString(this.challenger)
        parcel.writeString(this.winner)
        parcel.writeString(this.turn)
        parcel.writeBoolean(this.on_check)
        parcel.writeString(this.highlight)
        parcel.writeString(this.updated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntityGame> {
        override fun createFromParcel(parcel: Parcel): EntityGame {
            return EntityGame(parcel)
        }

        override fun newArray(size: Int): Array<EntityGame?> {
            return arrayOfNulls(size)
        }
    }

    fun getPlayerOther(username: String): EntityPlayer {
        if(this.white.username == username){
            return this.black
        }
        return this.white
    }

    fun getAffiliationOther(username: String): String {
        if(this.white.username == username){
            return "black"
        }
        return "white"
    }

    fun getDate(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val brooklyn: ZoneId = ZoneId.of("America/New_York")
        val zonedDateTime: ZonedDateTime = LocalDateTime.parse(this.updated, formatter).atZone(brooklyn)
        return DateTimeFormatter.ofPattern("MM/dd/YYYY").format(zonedDateTime)
    }

    fun getUsernameOther(username: String): String {
        if(this.white.username == username){
            return this.black.username
        }
        return this.white.username
    }

    fun getWhite(username: String): Boolean {
        if(this.white.username == username){
            return true
        }
        return false
    }

    fun getOutbound(username: String): Boolean {
        if(this.white.username == username){
            if(this.challenger.toLowerCase(Locale.ENGLISH) == "white"){
                return true
            }
            return false
        }
        if(this.challenger.toLowerCase(Locale.ENGLISH) == "white"){
            return false
        }
        return true
    }

    fun getWinnerUsername(): String {
        if(this.winner == null){
            return "Draw"
        }
        if(this.winner!!.toLowerCase(Locale.ENGLISH) == "white"){
            return this.white.username
        }
        return this.black.username
    }

    fun getWinnerAvatar(): ByteArray? {
        if(this.winner == null){
            return null
        }
        if(this.winner!!.toLowerCase(Locale.ENGLISH) == "white"){
            return this.white.avatar
        }
        return this.black.avatar
    }

    fun getTurn(username: String): Boolean {
        val white: Boolean = this.turn.toLowerCase(Locale.ENGLISH) == "white"
        if(this.white.username == username){
            if(white){
                return true
            }
            return false
        }
        if(white){
            return false
        }
        return true
    }

    fun getTurnUsername(): String {
        if(this.turn.toLowerCase(Locale.ENGLISH) == "white"){
            return this.white.username
        }
        return this.black.username
    }

    fun getTurnPlayer(): EntityPlayer {
        if(this.turn.toLowerCase(Locale.ENGLISH) == "white"){
            return this.white
        }
        return this.black
    }

    fun getMatrix(username: String): Array<Array<Piece?>> {
        val matrix: Array<Array<Piece?>> = Array<Array<Piece?>>(8) {
            arrayOf(null, null, null, null, null, null, null, null)
        }
        for ((i: Int, row: List<String>) in this.state!!.withIndex()) {
            val column: Array<Piece?> = arrayOfNulls(8)
            for ((j: Int, item: String) in row.withIndex()) {
                if(getWhite(username)){
                    column[j] = Render(this.getWhite(username)).getPiece(item)
                } else {
                    column[abs(7 - j)] = Render(this.getWhite(username)).getPiece(item)
                }
            }
            matrix[i] = column
        }
        if(getWhite(username)){
            return matrix.reversedArray()
        }
        return matrix
    }


}