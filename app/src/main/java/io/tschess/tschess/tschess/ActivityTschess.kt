package io.tschess.tschess.tschess

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.tabs.TabLayout
import io.tschess.tschess.R
import io.tschess.tschess.dialog.DialogPush
import io.tschess.tschess.dialog.tschess.DialogDraw
import io.tschess.tschess.dialog.tschess.DialogOption
import io.tschess.tschess.dialog.tschess.DialogPromo
import io.tschess.tschess.header.HeaderSelf
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParseGame
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import io.tschess.tschess.tschess.controller.Checker
import io.tschess.tschess.tschess.controller.Transitioner
import io.tschess.tschess.tschess.logic.Castle
import io.tschess.tschess.tschess.logic.Explode
import io.tschess.tschess.tschess.logic.Passant
import io.tschess.tschess.tschess.logic.PromoLogic
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class ActivityTschess : AppCompatActivity(), Listener, Flasher {

    var white: Boolean

    private val checker: Checker

    private val polling: Timer
    private val parseGame: ParseGame
    private var highlight: List<Array<Int>>

    private val castle: Castle
    private val passant: Passant
    private val explode: Explode
    private val promoLogic: PromoLogic

    private val brooklyn: ZoneId
    private val formatter: DateTimeFormatter

    init {
        this.checker = Checker()

        this.polling = Timer()
        this.parseGame = ParseGame()
        this.highlight = listOf(arrayOf(9, 9), arrayOf(9, 9))
        this.white = true

        this.castle = Castle(activityTschess = this@ActivityTschess)
        this.passant = Passant(activityTschess = this@ActivityTschess)
        this.explode = Explode(activityTschess = this@ActivityTschess)
        this.promoLogic = PromoLogic(activityTschess = this@ActivityTschess)

        this.brooklyn = ZoneId.of("America/New_York")
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    }

    lateinit var labeler: Labeler
    lateinit var networker: Networker
    lateinit var dialogDraw: DialogDraw
    lateinit var progressBar: ProgressBar
    lateinit var notificationManager: NotificationManager

    override fun onResume() {
        super.onResume()
        this.notificationManager.cancelAll()

        this.polling.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getUpdate()
            }
        }, 2000, TimeUnit.SECONDS.toMillis(1))


    }

    lateinit var transitioner: Transitioner
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
                DialogOption(applicationContext, playerSelf, game, progressBar).renderOptionMenu()
            }
        })

        this.matrix = game.getMatrix(playerSelf.username)
        this.white = game.getWhite(playerSelf.username)

        val headerOther: HeaderSelf = findViewById(R.id.header)
        headerOther.initialize(playerOther)

        this.dialogDraw = DialogDraw(this, this.playerSelf, this.game, this.progressBar)

        this.boardView = findViewById(R.id.board_view)



        this.transitioner = Transitioner(white, this)


        this.boardView.setListener(this)

        this.setHighlightCoords() //???
        this.boardView.populateBoard(this.matrix, this.highlight, game.turn)

        this.networker = Networker(this.progressBar, this.transitioner, applicationContext)

        this.labeler = Labeler(
            this.playerSelf,
            this@ActivityTschess,
            this.networker,
            this.brooklyn,
            this.formatter,
            this.polling,
            this.dialogDraw
        )
        this.labeler.setLabel(this.game)
    }

    override fun onBackPressed() {
        this.polling.cancel()
        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
        extras.putExtra("player_self", playerSelf)
        finish()
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
        if (this.game.getWhite(this.playerSelf.username)) {
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
        val coord00: Array<Int>? = this.transitioner.getCoord()
        if (coord00 != null) {
            if (explode.evaluate(coord00, coord01, this.matrix)) {
                return
            }
            if (castle.execute(coord00, coord01, this.matrix)) {
                return
            }
            if (promoLogic.evaluate(coord01)) {
                val dialogPromo = DialogPromo(coord01, this@ActivityTschess, this)
                dialogPromo.show()
                return
            }
            if (passant.evaluate(coord00, coord01, this.matrix)) {
                return
            }
            if (this.transitioner.valid(coord01, this.matrix)) {
                val matrix00: Array<Array<Piece?>> = this.transitioner.deselect(this.matrix)
                this.matrix = matrix00
                val matrixXX: Array<Array<Piece?>> = this.transitioner.execute(propose = coord01, matrix = this.matrix)
                val state: List<List<String>> = this.transitioner.render(matrix = matrixXX, white = this.white)
                val highlight: String = if (this.white) {
                    "${coord00[0]}${coord00[1]}${coord01[0]}${coord01[1]}"
                } else {
                    "${7 - coord00[0]}${7 - coord00[1]}${7 - coord01[0]}${7 - coord01[1]}"
                }
                this.networker.update(this.game.id, state, highlight)
                /* * */
                if (this.playerSelf.promptPopup()) {
                    DialogPush(applicationContext, progressBar).notifications(playerSelf)
                }
                /* * */
                return
            }
            val matrix00: Array<Array<Piece?>> = this.transitioner.deselect(this.matrix)
            this.matrix = matrix00
            this.boardView.populateBoard(this.matrix, this.highlight, game.turn)
            this.transitioner.clear()
            return
        }
        val matrix00: Array<Array<Piece?>> = this.transitioner.highlight(coord01, this.matrix)
        this.matrix = matrix00
        this.boardView.populateBoard(this.matrix, this.highlight, game.turn)
    }

    private fun getUpdate() {
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
                this.labeler.setLabel(this.game)


            }, {
                Log.e("error in volley request", "${it.message}")
            })
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    private fun setCheckMate() {
        val affiliation: String = this.game.getAffiliationOther(this.playerSelf.username)
        val king: Array<Int> = checker.coordinateKing(affiliation, this.game.getMatrix(this.playerSelf.username))
        Log.e("king", "$king")
        Log.e("affiliation", "$affiliation")

        val mate: Boolean = checker.mate(king, this.game.getMatrix(this.playerSelf.username))
        Log.e("MATE", "$mate")

        val other: Boolean = checker.other(king, this.game.getMatrix(this.playerSelf.username))
        Log.e("OTHER", "$other")

        val self: Boolean = checker.self(king, this.game.getMatrix(this.playerSelf.username))
        Log.e("SELF", "$self")


        if (mate) {
            this.networker.mate(this.game.id)
            return
        }
        if (!self) { //???
            return
        }
        this.progressBar.visibility = View.VISIBLE
        this.networker.check(this.game.id)
    }

    private fun updateable(updated: String): Boolean {
        val updated01: ZonedDateTime = LocalDateTime.parse(updated, this.formatter).atZone(this.brooklyn)
        val updatedXX: String = this.game.updated
        val updatedXY: ZonedDateTime = LocalDateTime.parse(updatedXX, this.formatter).atZone(this.brooklyn)
        return updated01.isAfter(updatedXY)
    }

}
