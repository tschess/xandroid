package io.tschess.tschess.tschess

import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import android.widget.TextView
import io.tschess.tschess.dialog.tschess.DialogDraw
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime

class Labeler(
    var game: EntityGame,
    val playerSelf: EntityPlayer,
    val labelNotification: TextView,
    val activityTschess: ActivityTschess,
    val dialogDraw: DialogDraw
) {

    init {
        this.labelNotification.text = ""
    }

    fun setLabelNotification() {
        if (this.game.status == "RESOLVED") {
            return
        }
        if (this.game.condition == "TBD") {
            this.labelNotification.visibility = View.INVISIBLE
            return
        }
        if (this.game.condition == "PENDING") {
            this.setDrawProposal()
        }
    }

    private fun setDrawProposal() {
        if (this.game.status == "RESOLVED") {
            this.labelNotification.visibility = View.INVISIBLE
            return
        }
        this.labelNotification.visibility = View.VISIBLE
        val username: String = this.game.getTurnUsername()
        activityTschess.runOnUiThread {
            this.labelNotification.text = "proposal pending"
            this.labelNotification.text = "$username to respond"
        }
        val turn: Boolean = this.game.getTurn(this.playerSelf.username)
        if (!turn) {
            return
        }
        this.dialogDraw.render()
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
                val username: String = playerTurn.username
                this.networker.timeout(this.game.id, playerTurn.id, this.game.getPlayerOther(username).id, this.game.getWhite(username))
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

    private fun setCheckLabel() {
        val czech: Boolean = this.game.on_check
        if(!czech){
            return
        }
        val textTurn: String = this.textViewTurnary.text.toString()
        this.textViewTurnary.text = "$textTurn (✔️)"
    }

}