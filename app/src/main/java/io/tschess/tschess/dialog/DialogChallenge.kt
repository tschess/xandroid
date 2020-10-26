package io.tschess.tschess.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io.tschess.tschess.R
import io.tschess.tschess.home.Refresher
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
    val game: EntityGame? = null,
    val action: String = "INVITATION",
    val refresher: Refresher? = null
) : Dialog(context) {

    lateinit var progressBar: ProgressBar
    private val parseGame: ParseGame = ParseGame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_challenge)

        //progress_bar

        this.progressBar = findViewById(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val textSend: TextView = findViewById(R.id.text_send)

        val textTitle: TextView = findViewById(R.id.text_title)
        textTitle.text = "\uD83E\uDD1C ${playerSelf.username} vs. ${playerOther.username} \uD83E\uDD1B"

        val textConfig: TextView = findViewById(R.id.config_text)
        textConfig.text = "config:"

        val listOption: MutableList<String> =
            mutableListOf("random", "config. 0", "config. 1", "config. 2", "   traditional (chess)   ")
        if (action == "ACCEPT") { //????????
            //????????
            //????????
            //????????
            listOption.add("mirror opponent")
            textSend.text = "let's play! \uD83C\uDF89"
        }
        val picker: NumberPicker = findViewById<View>(R.id.number_picker) as NumberPicker
        picker.minValue = 0
        picker.maxValue = listOption.size - 1
        picker.wrapSelectorWheel = true
        picker.displayedValues = listOption.toTypedArray()

        textSend.setOnClickListener {
            this.progressBar.visibility = View.VISIBLE

            if (action == "ACCEPT") {
                accept(picker.value, game!!.id)
                return@setOnClickListener
            }
            if (action == "REMATCH") {
                val white: Boolean = game!!.getWhite(playerSelf.username)
                rematch(picker.value, white)
                return@setOnClickListener
            }
            //"INVITATION"
            invitation(picker.value)
        }
    }

    fun rematch(config: Int, white: Boolean) {
        val url: String = "${ServerAddress().IP}:8080/game/rematch"

        val params: MutableMap<String, String> = mutableMapOf()
        params["id_self"] = playerSelf.id
        params["id_other"] = playerOther.id
        params["config"] = config.toString()
        params["white"] = white.toString()

        val listenerResponse: Response.Listener<JSONObject>? = Response.Listener {
            this.progressBar.visibility = View.INVISIBLE
            DialogOk(context).confirm(playerOther.username)
            refresher!!.refresh()
            dismiss()
        }
        val listenerError: Response.ErrorListener? = Response.ErrorListener {
            this.progressBar.visibility = View.INVISIBLE
            DialogOk(context).error("something went wrong! challenge wasn't delivered.")
            dismiss()
        }
        execute(url, listenerResponse, listenerError, params)
    }

    fun accept(config: Int, game_id: String) {
        val params: MutableMap<String, String> = mutableMapOf()
        params["id_game"] = game_id
        params["config"] = config.toString()
        val listenerResponse: Response.Listener<JSONObject>? = Response.Listener { response ->
            this.progressBar.visibility = View.INVISIBLE
            val game: EntityGame = parseGame.execute(response)
            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("game", game)
            val intent = Intent(context, ActivityTschess::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            dismiss()
        }
        val listenerError: Response.ErrorListener? = Response.ErrorListener {
            this.progressBar.visibility = View.INVISIBLE
            DialogOk(context).error("something went wrong! please try again later.")
            dismiss()
        }
        val url: String = "${ServerAddress().IP}:8080/game/ack"
        execute(url, listenerResponse, listenerError, params)
    }

    fun invitation(config: Int) {
        val url: String = "${ServerAddress().IP}:8080/game/challenge"

        val params: MutableMap<String, String> = mutableMapOf()
        params["id_self"] = playerSelf.id
        params["id_other"] = playerOther.id
        params["config"] = config.toString()

        val listenerResponse: Response.Listener<JSONObject>? = Response.Listener {
            this.progressBar.visibility = View.INVISIBLE
            DialogOk(context).confirm(playerOther.username)
            refresher!!.refresh()
            dismiss()
        }
        val listenerError: Response.ErrorListener? = Response.ErrorListener {
            this.progressBar.visibility = View.INVISIBLE
            DialogOk(context).error("something went wrong! challenge wasn't delivered.")
            dismiss()
        }
        execute(url, listenerResponse, listenerError, params)
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

