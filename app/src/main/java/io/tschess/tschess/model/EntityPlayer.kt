package io.tschess.tschess.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import androidx.core.content.ContextCompat
import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class EntityPlayer(
    val id: String,
    val username: String,
    var avatar: ByteArray,
    var elo: Int,
    var rank: Int,
    var disp: Int,
    var date: String,
    var note_value: Boolean,
    var note_key: String?,
    var config0: List<List<String>>,
    var config1: List<List<String>>,
    var config2: List<List<String>>,
    val device: String
) : Parcelable {

    var drawable: Drawable? = null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!, //id
        parcel.readString()!!, //username
        parcel.createByteArray()!!, //avatar
        parcel.readInt(), //elo
        parcel.readInt(), //rank
        parcel.readInt(), //disp
        parcel.readString()!!, //date
        parcel.readInt() != 0, //note_value
        parcel.readString(), //note_key
        parcel.createStringArrayList()!!.chunked(8),
        parcel.createStringArrayList()!!.chunked(8),
        parcel.createStringArrayList()!!.chunked(8),
        parcel.readString()!! //device
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.id)
        parcel.writeString(this.username)
        parcel.writeByteArray(this.avatar)
        parcel.writeInt(this.elo)
        parcel.writeInt(this.rank)
        parcel.writeInt(this.disp)
        parcel.writeString(this.date)
        parcel.writeInt(if (this.note_value) 1 else 0)
        parcel.writeString(this.note_key)
        parcel.writeStringList(this.config0.flatten())
        parcel.writeStringList(this.config1.flatten())
        parcel.writeStringList(this.config2.flatten())
        parcel.writeString(this.device)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntityPlayer> {

        override fun createFromParcel(parcel: Parcel): EntityPlayer {
            return EntityPlayer(parcel)
        }

        override fun newArray(size: Int): Array<EntityPlayer?> {
            return arrayOfNulls(size)
        }

    }

    fun getDateText(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val brooklyn: ZoneId = ZoneId.of("America/New_York")
        val zonedDateTime: ZonedDateTime = LocalDateTime.parse(this.date, formatter).atZone(brooklyn)
        return DateTimeFormatter.ofPattern("MM/dd/YYYY").format(zonedDateTime)
    }

    fun getDispInd(context: Context): Drawable? {
        if (this.disp == 0) {
            return null
        }
        if (this.disp >= 0) {
            return ContextCompat.getDrawable(context, R.drawable.up)!!
        }
        return ContextCompat.getDrawable(context, R.drawable.down)!!
    }

    fun setConfig(matrix: Array<Array<Piece?>>): List<List<String>> {
        val matrix00: MutableList<String> = mutableListOf("", "", "", "", "", "", "", "")
        val matrix01: MutableList<String> = mutableListOf("", "", "", "", "", "", "", "")
        for (index: Int in 0..7) {
            val occupant00: Piece? = matrix[0][index]
            if (occupant00 != null) {
                matrix00[index] = occupant00.name
            }
            val occupant01: Piece? = matrix[1][index]
            if (occupant01 != null) {
                matrix01[index] = occupant01.name
            }
        }
        return listOf(matrix01, matrix00)
    }

    fun getConfig(index: Int): Array<Array<Piece?>> {
        var config99: List<List<String>> = this.config0
        if (index == 1) {
            config99 = this.config1
        }
        if (index == 2) {
            config99 = this.config2
        }
        val configXX: Array<Array<Piece?>> = Array<Array<Piece?>>(2) {
            arrayOf(null, null, null, null, null, null, null, null)
        }
        for ((i: Int, row: List<String>) in config99.withIndex()) {
            val column: Array<Piece?> = arrayOfNulls(8)
            for ((j: Int, item: String) in row.withIndex()) {

                column[j] = ParsePlayer().getPiece(item)
            }
            configXX[i] = column
        }
        return configXX.reversedArray()
    }

    fun isPopup(): Boolean {
        if(this.note_key == null){
            return false
        }
        if(this.note_key == "POPUP"){
            return true
        }
        return false
    }

}