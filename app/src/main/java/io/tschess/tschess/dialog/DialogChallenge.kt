package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParseGame
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import io.tschess.tschess.tschess.ActivityTschess
import org.json.JSONObject


class DialogChallenge(
    context: Context,
    val playerSelf: EntityPlayer,
    val playerOther: EntityPlayer,
    val game: EntityGame?,
    val action: String) : Dialog(context) {

    private val parseGame: ParseGame = ParseGame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_challenge)

        val textSend: TextView = findViewById(R.id.text_send)

        val textTitle: TextView = findViewById(R.id.text_title)
        textTitle.text = "\uD83E\uDD1C ${playerSelf.username} vs. ${playerOther.username} \uD83E\uDD1B"

        val textConfig: TextView = findViewById(R.id.config_text)
        textConfig.text = "config:"

        val listOption: MutableList<String> = mutableListOf("random", "config. x", "config. y", "config. z", "   traditional (chess)   ")
        if(action == "ACCEPT"){
            listOption.add("mirror opponent")
            textSend.text = "let's play! \uD83C\uDF89"
        }
        val picker: NumberPicker = findViewById<View>(R.id.number_picker) as NumberPicker
        picker.minValue = 0
        picker.maxValue = listOption.size - 1
        picker.wrapSelectorWheel = true
        picker.displayedValues = listOption.toTypedArray()

        textSend.setOnClickListener {
            //...
            if(action == "ACCEPT"){
                accept(picker.value, game!!.id)
                return@setOnClickListener
            }
            if(action == "REMATCH"){
                val white: Boolean = game!!.getWhite(playerSelf.username)
                rematch(picker.value, playerOther.id, white)
                return@setOnClickListener
            }
            //"INVITATION"
            invitation()
        }
    }

    fun rematch(config: Int, id_other: String, white: Boolean) {
        val url: String = "${ServerAddress().IP}:8080/game/rematch"

        val params: MutableMap<String, String> = mutableMapOf()
        params["id_other"] = id_other
        params["config"] = config.toString()
        params["white"] = white.toString()

        val listenerResponse: Response.Listener<JSONObject>? = Response.Listener {
            dismiss()
            //dialogConfirm()
        }
        val listenerError: Response.ErrorListener? = Response.ErrorListener {
            dismiss()
            //dialogError()
        }
        execute(url, listenerResponse, listenerError, params)
    }

    fun accept(config: Int, game_id: String) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["id_game"] = game_id
        params["config"] = config.toString()
        val listenerResponse: Response.Listener<JSONObject>? = Response.Listener { response ->
            val game: EntityGame = parseGame.execute(response)
            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("game", game)
            val intent = Intent(context, ActivityTschess::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            dismiss()
        }
        val listenerError: Response.ErrorListener? = Response.ErrorListener {
            dismiss()
            //dialogError()
        }
        val url: String = "${ServerAddress().IP}:8080/game/ack"
        execute(url, listenerResponse, listenerError, params)
    }

    fun invitation() {

    }

    fun execute(
        url: String?,
        listenerResponse: Response.Listener<JSONObject>?,
        listenerError: Response.ErrorListener?,
        params: MutableMap<String, String>
    ) {
        val jsonObject: JSONObject = JSONObject(params as Map<*, *>)
        val jsonObjectRequest: JsonObjectRequest =
            JsonObjectRequest(Request.Method.POST, url!!, jsonObject, listenerResponse!!, listenerError!!)
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }
}

