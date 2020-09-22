package io.tschess.tschess.model

import org.json.JSONArray
import org.json.JSONObject

class ParseGame {

    private val parsePlayer: ParsePlayer = ParsePlayer()

    fun execute(json: JSONObject): EntityGame {

        val id: String = json["id"] as String
        //Log.e("id", "${id}")

        val state: List<List<String>>? = getState(json["state"] as JSONArray)
        //Log.e("state", "${state.size}")

        // var status: String,
        val status: String = json["status"] as String
        //Log.e("status", "${status}")

        //    var condition: String,
        val condition: String = json["condition"] as String
        //Log.e("condition", "${condition}")

        //    var moves: Int,
        val moves: Int = json["moves"] as Int
        //Log.e("moves", "${moves}")

        //    var white: PlayerEntity,
        val white: EntityPlayer = parsePlayer.execute(json["white"] as JSONObject)
        //Log.e("white", "${white.username}")

        //    var white_elo: Int,
        //val white_elo: Int = json["white_elo"] as Int
        //Log.e("white_elo", "${white_elo}")

        //    var white_disp: Int,
        //val w0: String = json["white_disp"].toString()
        //var white_disp: Int? = null
        //if(w0 != "null"){
        //white_disp = w0.toInt()
        //}
        //

        //Log.e("white_disp", "${white_disp}")

        //    var black: PlayerEntity,
        //val black: String = json["black"] as String
        val black: EntityPlayer = parsePlayer.execute(json["black"] as JSONObject)
        //Log.e("black", "${black.username}")

        //    var black_elo: Int,
        //val black_elo: Int = json["black_elo"] as Int
        //Log.e("black_elo", "${black_elo}")

        //    var black_disp: Int,
        //val black_disp: Int = json["black_disp"] as Int
        //Log.e("black_disp", "${black_disp}")
        //val b0: String = json["white_disp"].toString()
        //var black_disp: Int? = null
        //if(b0 != "null"){
        //black_disp = b0.toInt()
        //}

        //    var challenger: String,
        val challenger: String = json["challenger"] as String
        //Log.e("challenger", "${challenger}")

        //    var winner: String?,
        //val winner: String? = json["winner"] as String?
        val winner: String? = optString(json, "winner")
        //Log.e("winner", "${winner}")

        //    var turn: String,
        val turn: String = json["turn"] as String
        //Log.e("turn", "${turn}")

        //    var on_check: Boolean,
        val on_check: Boolean = json["on_check"] as Boolean
        //Log.e("on_check", "${on_check}")

        //    var highlight: String,
        val highlight: String = json["highlight"] as String
        //Log.e("highlight", "${highlight}")

        //    var updated: String
        val updated: String = json["updated"] as String
        //Log.e("updated", "${updated}")

        return EntityGame(
            id = id,
            state = state,
            status = status,
            condition = condition,
            moves = moves,
            white = white,
            //white_elo = white_elo,
            //white_disp = white_disp,
            black = black,
            //black_elo = black_elo,
            //black_disp = black_disp,
            challenger = challenger,
            winner = winner,
            turn = turn,
            on_check = on_check,
            highlight = highlight,
            updated = updated
        )
    }

    private fun getState(json: JSONArray): List<List<String>>? {
        if(json.length() < 8){
            return null
        }
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
        val x2: JSONArray = json.get(2) as JSONArray
        val r2: List<String> = listOf(
            x2.get(0) as String,
            x2.get(1) as String,
            x2.get(2) as String,
            x2.get(3) as String,
            x2.get(4) as String,
            x2.get(5) as String,
            x2.get(6) as String,
            x2.get(7) as String
        )
        val x3: JSONArray = json.get(3) as JSONArray
        val r3: List<String> = listOf(
            x3.get(0) as String,
            x3.get(1) as String,
            x3.get(2) as String,
            x3.get(3) as String,
            x3.get(4) as String,
            x3.get(5) as String,
            x3.get(6) as String,
            x3.get(7) as String
        )
        val x4: JSONArray = json.get(4) as JSONArray
        val r4: List<String> = listOf(
            x4.get(0) as String,
            x4.get(1) as String,
            x4.get(2) as String,
            x4.get(3) as String,
            x4.get(4) as String,
            x4.get(5) as String,
            x4.get(6) as String,
            x4.get(7) as String
        )
        val x5: JSONArray = json.get(5) as JSONArray
        val r5: List<String> = listOf(
            x5.get(0) as String,
            x5.get(1) as String,
            x5.get(2) as String,
            x5.get(3) as String,
            x5.get(4) as String,
            x5.get(5) as String,
            x5.get(6) as String,
            x5.get(7) as String
        )
        val x6: JSONArray = json.get(6) as JSONArray
        val r6: List<String> = listOf(
            x6.get(0) as String,
            x6.get(1) as String,
            x6.get(2) as String,
            x6.get(3) as String,
            x6.get(4) as String,
            x6.get(5) as String,
            x6.get(6) as String,
            x6.get(7) as String
        )
        val x7: JSONArray = json.get(7) as JSONArray
        val r7: List<String> = listOf(
            x7.get(0) as String,
            x7.get(1) as String,
            x7.get(2) as String,
            x7.get(3) as String,
            x7.get(4) as String,
            x7.get(5) as String,
            x7.get(6) as String,
            x7.get(7) as String
        )
        return listOf(r0, r1, r2, r3, r4, r5, r6, r7)
    }

    private fun optString(json: JSONObject, key: String): String? {
        return if (json.isNull(key)) null else json.optString(key, "fuck")
    }

}