package io.tschess.tschess.model

import android.util.Base64
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.bishop.Bishop
import io.tschess.tschess.piece.chess.king.King
import io.tschess.tschess.piece.chess.knight.Knight
import io.tschess.tschess.piece.chess.pawn.Pawn
import io.tschess.tschess.piece.chess.queen.Queen
import io.tschess.tschess.piece.chess.rook.Rook
import io.tschess.tschess.piece.fairy.amazon.Amazon
import io.tschess.tschess.piece.fairy.hunter.Hunter
import io.tschess.tschess.piece.fairy.poison.Poison
import org.json.JSONArray
import org.json.JSONObject

class ParsePlayer {

    fun execute(json: JSONObject): EntityPlayer {

        val id: String = json["id"] as String
        val username: String = json["username"] as String
        val avatar: String = json["avatar"].toString()
        val byteArray: ByteArray = Base64.decode(avatar, Base64.DEFAULT)
        val elo: Int = json["elo"].toString().toInt()
        val rank: Int = json["rank"].toString().toInt()
        val disp: Int = json["disp"].toString().toInt()
        val date: String = json["date"].toString()
        val note_key: String? = json["note_key"].toString()
        val config0: JSONArray = json["config0"] as JSONArray
        val list0: List<List<String>> = getConfigX(config0)
        val config1: JSONArray = json["config1"] as JSONArray
        val list1: List<List<String>> = getConfigX(config1)
        val config2: JSONArray = json["config2"] as JSONArray
        val list2: List<List<String>> = getConfigX(config2)
        val device: String = json["device"].toString()
        return EntityPlayer(
            id = id,
            username = username,
            avatar = byteArray,
            elo = elo,
            rank = rank,
            disp = disp,
            date = date,
            note_key = note_key,
            config0 = list0,
            config1 = list1,
            config2 = list2,
            device = device
        )
    }

    private fun getConfigX(json: JSONArray): List<List<String>> {
        val x0: JSONArray = json.get(0) as JSONArray
        val r0: List<String> = listOf(
            x0.get(0) as String,
            x0.get(1) as String,
            x0.get(2) as String,
            x0.get(3) as String,
            x0.get(4) as String,
            x0.get(5) as String,
            x0.get(6) as String,
            x0.get(7) as String
        )
        val x1: JSONArray = json.get(1) as JSONArray
        val r1: List<String> = listOf(
            x1.get(0) as String,
            x1.get(1) as String,
            x1.get(2) as String,
            x1.get(3) as String,
            x1.get(4) as String,
            x1.get(5) as String,
            x1.get(6) as String,
            x1.get(7) as String
        )
        return listOf(r0, r1)
    }

    fun getPiece(name: String): Piece? {
        if (name.contains("Knight")) {
            return Knight()
        }
        if (name.contains("Bishop")) {
            return Bishop()
        }
        if (name.contains("Queen")) {
            return Queen()
        }
        if (name.contains("Rook")) {
            return Rook()
        }
        if (name.contains("Pawn")) {
            return Pawn()
        }
        if (name.contains("King")) {
            return King()
        }
        if (name.contains("Poison")) {
            return Poison()
        }
        if (name.contains("Hunter")) {
            return Hunter()
        }
        if (name.contains("Amazon")) {
            return Amazon()
        }
        return null
    }
}
