package io.tschess.tschess.tschess

import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.widget.Chronometer
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.dialog.DialogChallenge
import io.tschess.tschess.dialog.DialogPush
import io.tschess.tschess.dialog.tschess.DialogDraw
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.ParseGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.tschess.component.Castle
import io.tschess.tschess.tschess.component.Explode
import io.tschess.tschess.tschess.component.Passant
import io.tschess.tschess.dialog.tschess.DialogPromo
import io.tschess.tschess.tschess.component.PromoLogic
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import org.json.JSONObject
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class ActivityTschess : AppCompatActivity(), Listener, Flasher {

    var white: Boolean

    private val polling: Timer
    private val parseGame: ParseGame
    private var highlight: List<Array<Int>>

    private val czecher: Czecher
    private val castle: Castle
    private val passant: Passant
    private val explode: Explode
    private val promoLogic: PromoLogic

    private val brooklyn: ZoneId
    private val formatter: DateTimeFormatter

    init {
        this.polling = Timer()
        this.parseGame = ParseGame()
        this.highlight = listOf(arrayOf(9, 9), arrayOf(9, 9))
        this.white = true

        this.czecher = Czecher()
        this.castle = Castle(activityTschess = this@ActivityTschess)
        this.passant = Passant(activityTschess = this@ActivityTschess)
        this.explode = Explode(activityTschess = this@ActivityTschess)
        this.promoLogic = PromoLogic(activityTschess = this@ActivityTschess)

        this.brooklyn = ZoneId.of("America/New_York")
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    }

    lateinit var dialogDraw: DialogDraw
    lateinit var progressBar: ProgressBar
    lateinit var notificationManager:  NotificationManager

    override fun onResume() {
        super.onResume()
        this.notificationManager.cancelAll()

        this.dialogDraw = DialogDraw(this, this.playerSelf, this.game, this.progressBar)

        Log.e("condition -->","setLabelNotification - C")

        this.polling.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                Log.e("condition -->","setLabelNotification - B")

                getUpdate()
            }
        }, 2000, TimeUnit.SECONDS.toMillis(1))
    }

    private lateinit var chronometer: Chronometer
    private lateinit var textViewTitle: TextView
    private lateinit var textViewTurnary: TextView
    private lateinit var textViewNotification: TextView

    lateinit var validator: Validator
    lateinit var game: EntityGame
    lateinit var matrix: Array<Array<Piece?>>

    private lateinit var boardView: BoardView
    private lateinit var playerSelf: EntityPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tschess)

        this.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        this.notificationManager.cancelAll()

        this.progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        this.progressBar.visibility = View.INVISIBLE

        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        this.playerSelf = extras.getExtra("player_self") as EntityPlayer
        this.game = extras.getExtra("game") as EntityGame
        extras.clear()
        val playerOther: EntityPlayer = this.game.getPlayerOther(this.playerSelf.username)

        val tabLayout: TabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                this.onTabSelected(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                if (game.status == "RESOLVED") {
                    flash()
                    return
                }
                renderOptionMenu()
            }
        })
        this.chronometer = findViewById(R.id.chronometer)
        this.setCountdown(game.updated)

        this.matrix = game.getMatrix(playerSelf.username!!)
        this.white = game.getWhite(playerSelf.username!!)

        val headerOther: HeaderSelf = findViewById(R.id.header)
        headerOther.initialize(playerOther)

        this.textViewTitle = findViewById(R.id.title)
        this.textViewTurnary = findViewById(R.id.turnary)
        this.textViewNotification = findViewById(R.id.notification)
        this.textViewNotification.text = ""

        this.boardView = findViewById(R.id.board_view)
        this.validator = Validator(white, this)
        this.boardView.setListener(this)

        this.setHighlightCoords()
        this.boardView.populateBoard(this.matrix, this.highlight, game.turn)
        this.setTurn()
        this.setCheckLabel()


    }



    override fun onBackPressed() {
        this.polling.cancel()
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", playerSelf)
        finish()
    }

    private fun setLabelNotification() {

        Log.e("condition -->","setLabelNotification")
        Log.e("condition -->","${this.game.condition}")

        if (this.game.status == "RESOLVED") {
            return
        }
        if (this.game.condition == "TBD") {
            this.textViewNotification.visibility = View.INVISIBLE
            return
        }
        if (this.game.condition == "PENDING") {
            this.drawProposal()
        }
    }

    private fun drawProposal() {
        if (this.game.status == "RESOLVED") {
            this.textViewNotification.visibility = View.INVISIBLE
            return
        }
        this.textViewNotification.visibility = View.VISIBLE
        runOnUiThread {
            this.textViewNotification.text = "proposal pending"
        }
        val username: String = this.game.getTurnUsername()
        runOnUiThread {
            this.textViewTurnary.text = "${username} to respond"
        }

        val turn: Boolean = this.game.getTurn(this.playerSelf.username)
        if (turn) {
            this.dialogDraw.render()
        }
    }

    private fun setCountdown(updated: String) {
        if (this.game.status == "RESOLVED") {
            return
        }
        val u01: ZonedDateTime = LocalDateTime.now().atZone(this.brooklyn)
        val u02: ZonedDateTime = LocalDateTime.parse(updated, this.formatter).atZone(this.brooklyn)
        val durationE1: Duration = Duration.between(u02, u01)
        val period24: Long = 60 * 60 * 24 * 1000.toLong()
        val periodXX: Long = period24 - durationE1.toMillis()
        this.chronometer.base = SystemClock.elapsedRealtime() + periodXX
        this.chronometer.isCountDown = true
        this.chronometer.start()
        this.chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
            val remainder: Long = it.base - SystemClock.elapsedRealtime()
            if (remainder <= 0) {
                val playerTurn: EntityPlayer = this.game.getTurnPlayer()
                val username: String = playerTurn.username!!
                val params = HashMap<String, Any>()
                params["id_game"] = this.game.id
                params["id_self"] = playerTurn.id!!
                params["id_oppo"] = this.game.getPlayerOther(username).id!!
                params["white"] = this.game.getWhite(username)
                val jsonObject = JSONObject(params as Map<*, *>)
                this.deliver(jsonObject, "resign")
            }
        }
    }

    private fun setEndgame() {
        if (this.game.status != "RESOLVED") {
            return
        }
        this.polling.cancel()
        this.textViewTitle.text = "game over"
        this.textViewTurnary.visibility = View.INVISIBLE
        this.chronometer.stop()
        this.chronometer.visibility = View.INVISIBLE
        this.textViewNotification.visibility = View.VISIBLE
        if (this.game.condition == "DRAW") {
            this.textViewNotification.text = "draw"
            return
        }
        val username: String = this.playerSelf.username
        if (this.game.getWinnerUsername() == username) {
            this.textViewNotification.text = "winner"
            return
        }
        this.textViewNotification.text = "you lose"
    }

    private fun setTurn() {
        if (this.game.status == "RESOLVED") {
            return
        }
        if (this.game.turn.toLowerCase() == "white") {
            this.textViewTurnary.text = "${this.game.white.username} to move"
            return
        }
        this.textViewTurnary.text = "${this.game.black.username} to move"
    }

    private fun setHighlightCoords() {
        if (this.game.highlight == "TBD") {
            this.highlight = listOf(arrayOf(9, 9), arrayOf(9, 9))
            return
        }
        val highlight00: MutableList<Int> = mutableListOf()
        this.game.highlight.forEach { char ->
            highlight00.add(char.toString().toInt())
        }
        if (this.game.getWhite(this.playerSelf.username!!)) {
            this.highlight =
                listOf(arrayOf(highlight00[0], highlight00[1]), arrayOf(highlight00[2], highlight00[3]))
        } else {
            this.highlight = listOf(
                arrayOf(7 - highlight00[0], 7 - highlight00[1]),
                arrayOf(7 - highlight00[2], 7 - highlight00[3])
            )
        }
    }

    override fun flash() {
        this.boardView.visibility = View.INVISIBLE
        window.decorView.rootView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        Timer().schedule(11) {
            runOnUiThread {
                boardView.visibility = View.VISIBLE
            }
        }
    }

    fun deliver(jsonObject: JSONObject, route: String = "update") {
        this.progressBar.visibility = View.VISIBLE
        val url = "${ServerAddress().IP}:8080/game/${route}"
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                Log.e("RESPONSE", "${response}")
                this.validator.clear()
            },
            { Log.e("error in volley request", "${it.message}") })
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 2, 1f)
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    lateinit var alertDialog: AlertDialog

    fun showDialogPromo(coord: Array<Int>) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogPromo =
            DialogPromo(
                coord,
                this@ActivityTschess,
                this
            )
        dialogPromo.coord = coord
        builder.setView(dialogPromo)
        builder.setCancelable(false)
        this.alertDialog = builder.create()
        this.alertDialog.show()
    }

    fun killDialogPromo() {
        this.alertDialog.dismiss()
    }

    fun renderOptionMenu() {
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialog)
        dialogBuilder.setTitle("⚡ tschess ⚡")
        dialogBuilder.setMessage("select game menu option below:")
        dialogBuilder.setPositiveButton("resign position \uD83E\uDD26") { dialog, _ ->
            this.progressBar.visibility = View.VISIBLE
            val url = "${ServerAddress().IP}:8080/game/resign"
            val params = HashMap<String, Any>()
            params["id_game"] = this.game.id
            params["id_self"] = this.playerSelf.id
            params["id_oppo"] = this.game.getPlayerOther(this.playerSelf.username).id
            params["white"] = this.game.getWhite(this.playerSelf.username)
            val jsonObject = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { },
                { }
            )
            VolleySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
            dialog.cancel()
        }
        if (this.game.getTurn(this.playerSelf.username)) {
            dialogBuilder.setNegativeButton("propose draw \uD83E\uDD1D") { dialog, _ ->
                this.progressBar.visibility = View.VISIBLE
                val url = "${ServerAddress().IP}:8080/game/prop/${this.game.id}"
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    { },
                    { }
                )
                VolleySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
                dialog.cancel()
            }
        }
        val alert: AlertDialog = dialogBuilder.create()
        alert.show()
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        if (this.game.getTurn(this.playerSelf.username)) {
            alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
        }
    }

    private fun updateable(updated: String): Boolean {
        val updated01: ZonedDateTime = LocalDateTime.parse(updated, this.formatter).atZone(this.brooklyn)
        val updatedXX: String = this.game.updated
        val updatedXY: ZonedDateTime = LocalDateTime.parse(updatedXX, this.formatter).atZone(this.brooklyn)
        return updated01.isAfter(updatedXY)
    }

    override fun touch(tile: View) {
        if (!this.game.getTurn(this.playerSelf.username!!)) {
            this.flash()
            return
        }
        val index: Int = boardView.indexOfChild(tile)
        val row: Int = index / 8
        val col: Int = index % 8
        //Log.d("---", "---")
        //Log.e("row", row.toString())
        //Log.e("column", col.toString())
        //Log.d("---", "---")
        val coord01: Array<Int> = arrayOf(row, col)
        val coord00: Array<Int>? = this.validator.getCoord()
        if (coord00 != null) {
            if (explode.evaluate(coord00, coord01, this.matrix)) {
                return
            }
            if (castle.execute(coord00, coord01, this.matrix)) {
                return
            }
            if (promoLogic.evaluate(coord01)) {
                this.showDialogPromo(coord01)
                return
            }
            if (passant.evaluate(coord00, coord01, this.matrix)) {
                return
            }
            if (this.validator.valid(coord01, this.matrix)) {
                val matrix00: Array<Array<Piece?>> = this.validator.deselect(this.matrix)
                this.matrix = matrix00
                val matrixXX: Array<Array<Piece?>> = this.validator.execute(propose = coord01, matrix = this.matrix)
                val state: List<List<String>> = this.validator.render(matrix = matrixXX, white = this.white)
                val highlight: String = if (this.white) {
                    "${coord00[0]}${coord00[1]}${coord01[0]}${coord01[1]}"
                } else {
                    "${7 - coord00[0]}${7 - coord00[1]}${7 - coord01[0]}${7 - coord01[1]}"
                }
                val params = HashMap<String, Any>()
                params["id_game"] = this.game.id
                params["state"] = state
                params["highlight"] = highlight
                params["condition"] = "TBD"
                val jsonObject = JSONObject(params as Map<*, *>)
                this.deliver(jsonObject)
                /* * */
                if(this.playerSelf.promptPopup()){
                    DialogPush(applicationContext, progressBar).notifications(playerSelf)
                }
                /* * */
                return
            }
            val matrix00: Array<Array<Piece?>> = this.validator.deselect(this.matrix)
            this.matrix = matrix00
            this.boardView.populateBoard(this.matrix, this.highlight, game.turn)
            this.validator.clear()
            return
        }
        val matrix00: Array<Array<Piece?>> = this.validator.highlight(coord01, this.matrix)
        this.matrix = matrix00
        this.boardView.populateBoard(this.matrix, this.highlight, game.turn)
    }

    private fun getUpdate() {

        /* * */
        //TODO: try it here...
        this.setLabelNotification()
        //TODO: ^^^
        /* * */

        val url = "${ServerAddress().IP}:8080/game/request/${game.id}"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response: JSONObject ->
                val game: EntityGame = parseGame.execute(response)
                val updatable: Boolean = this.updateable(game.updated)
                if (!updatable) {
                    return@Listener
                }
                this.progressBar.visibility = View.INVISIBLE
                this.game = game
                this.setHighlightCoords()

                this.matrix = this.game.getMatrix(this.playerSelf.username)
                this.boardView.populateBoard(this.matrix, this.highlight, game.turn) //old turn??

                this.setCheckMate()
                this.setEndgame()
                this.setTurn()
                this.setCountdown(game.updated)

                Log.e("condition -->","setLabelNotification - A")

                this.setLabelNotification()
                this.setCheckLabel()

            }, {
                Log.e("error in volley request", "${it.message}")
            })
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    private fun setCheckLabel() {
        val czech: Boolean = this.game.on_check
        if(!czech){
            return
        }
        val textTurn: String = this.textViewTurnary.text.toString()
        this.textViewTurnary.text = "$textTurn (✔️)"
    }

    private fun setCheckMate() {
        val affiliation: String = this.game.getAffiliationOther(this.playerSelf.username)
        val king: Array<Int> = czecher.kingCoordinate(affiliation, this.matrix)

        val mate: Boolean = czecher.mate(king, this.matrix)
        if (mate) {
            val url = "${ServerAddress().IP}:8080/game/mate/${this.game.id}"
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { this.progressBar.visibility = View.INVISIBLE },
                { this.progressBar.visibility = View.INVISIBLE }
            )
            VolleySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
            return
        }
        val czech: Boolean = czecher.other(king, this.matrix)
        if (!czech) {
            return
        }
        this.progressBar.visibility = View.VISIBLE
        val url = "${ServerAddress().IP}:8080/game/check/${this.game.id}"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { this.progressBar.visibility = View.INVISIBLE },
            { this.progressBar.visibility = View.INVISIBLE }
        )
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)

    }




}



