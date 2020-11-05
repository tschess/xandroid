package io.tschess.tschess.tschess

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Chronometer
import android.widget.TextView
import io.tschess.tschess.R
import io.tschess.tschess.dialog.tschess.DialogDraw
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Labeler(
    val playerSelf: EntityPlayer,
    val activityTschess: ActivityTschess,
    val networker: Networker,
    val brooklyn: ZoneId,
    val formatter: DateTimeFormatter,
    val polling: Timer,
    val dialogDraw: DialogDraw
) {

    private var labelTitle: TextView
    private var labelTurnary: TextView
    private var labelNotification: TextView
    private var labelChronometer: Chronometer

    init {
        this.labelChronometer = this.activityTschess.findViewById(R.id.chronometer)
        this.labelTitle = this.activityTschess.findViewById(R.id.title)
        this.labelTurnary = this.activityTschess.findViewById(R.id.turnary)
        this.labelNotification = this.activityTschess.findViewById(R.id.notification)
    }

    fun setLabel(game: EntityGame) {

        Log.e("setLabel","    0    ")

        this.setCountdown(game)
        this.setNotification(game)
        this.setTurn(game)

        this.setMate(game)
        this.setCheck(game)

    }

    private fun setCheck(game: EntityGame) {
        val check: Boolean = game.on_check
        if(!check){
            return
        }
        val textTurn: String = this.labelTurnary.text.toString()
        this.labelTurnary.text = "$textTurn (✔️)"
    }

    private fun setTurn(game: EntityGame) {
        if (game.turn.toLowerCase() == "white") {
            this.labelTurnary.text = "${game.white.username} to move"
            return
        }
        this.labelTurnary.text = "${game.black.username} to move"
    }

    private fun setMate(game: EntityGame) {
        if (game.status != "RESOLVED") {
            return
        }
        this.polling.cancel()
        this.labelTitle.text = "game over"
        this.labelTurnary.visibility = View.INVISIBLE
        this.labelChronometer.stop()
        this.labelChronometer.visibility = View.INVISIBLE
        this.labelNotification.visibility = View.VISIBLE
        if (game.condition == "DRAW") {
            this.labelNotification.text = "draw"
            return
        }
        val username: String = this.playerSelf.username
        if (game.getWinnerUsername() == username) {
            this.labelNotification.text = "winner"
            return
        }
        this.labelNotification.text = "you lose"
    }

    private fun setCountdown(game: EntityGame) {
        val u01: ZonedDateTime = LocalDateTime.now().atZone(this.brooklyn)
        val u02: ZonedDateTime = LocalDateTime.parse(game.updated, this.formatter).atZone(this.brooklyn)
        val durationE1: Duration = Duration.between(u02, u01)
        val period24: Long = 60 * 60 * 24 * 1000.toLong()
        val periodXX: Long = period24 - durationE1.toMillis()
        this.labelChronometer.base = SystemClock.elapsedRealtime() + periodXX
        this.labelChronometer.isCountDown = true
        this.labelChronometer.start()
        this.labelChronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
            val remainder: Long = it.base - SystemClock.elapsedRealtime()
            if (remainder <= 0) {
                val playerTurn: EntityPlayer = game.getTurnPlayer()
                val username: String = playerTurn.username
                this.networker.timeout(game.id, playerTurn.id, game.getPlayerOther(username).id, game.getWhite(username))
            }
        }
    }

    private fun setNotification(game: EntityGame) {
        if (game.condition == "PENDING") {
            this.labelNotification.visibility = View.VISIBLE
            val username: String = game.getTurnUsername()
            activityTschess.runOnUiThread {
                this.labelNotification.text = "proposal pending"
                this.labelTurnary.text = "$username to respond"
            }
            val turn: Boolean = game.getTurn(this.playerSelf.username)
            if (!turn) {
                return
            }
            this.dialogDraw.render()
            return
        }//(game.condition == "TBD")
        this.labelNotification.visibility = View.INVISIBLE
    }

}