package io.tschess.tschess.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chauthai.swipereveallayout.SwipeRevealLayout
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import io.tschess.tschess.snapshot.ActivitySnapshot
import io.tschess.tschess.tschess.ActivityTschess
import org.json.JSONObject
import java.util.*

class ViewHolderHome(
    var layout_swipe: SwipeRevealLayout? = null,
    var layout_row: ConstraintLayout? = null,
    val avatar: ImageView? = null,
    var username: TextView? = null,
    var action_title: TextView? = null,
    var action_image: ImageView? = null,
    var more_vert: ImageView? = null,
    var layout_option_swipe: LinearLayout? = null,
    var layout_accept: FrameLayout? = null,
    var accept_image: ImageView? = null,
    var accept_title: TextView? = null,
    var layout_reject: FrameLayout? = null,
    var reject_image: ImageView? = null,
    var reject_title: TextView? = null,
    var layout_rematch: FrameLayout? = null,
    var rematch_image: ImageView? = null,
    var rematch_title: TextView? = null,
    val game: EntityGame,
    val playerSelf: EntityPlayer,
    val playerOther: EntityPlayer,
    val context: Context,
    var refresher: Refresher,
    val position: Int,
    val activityHome: ActivityHome
) {

    private val glide = Glide.with(context)

    init {
        this.username!!.text = game.getUsernameOther(playerSelf.username)

        glide.load(playerOther.avatar).apply(RequestOptions.circleCropTransform()).into(object :
            CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                avatar!!.visibility = View.VISIBLE
                avatar.setImageDrawable(resource)
                //playerOther.drawable = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
        if (game.status.contains("RESOLVED")) {
            this.setHisto()
        } else {
            this.setActive()
        }
        if (game.status == "PROPOSED") {
            this.setInvite()
            this.setInviteAccept()
            this.setInviteReject()
            this.layout_option_swipe!!.removeView(layout_rematch)
            if (game.getOutbound(playerSelf.username)) {
                this.layout_option_swipe!!.removeView(layout_accept)
            }
            if (game.getOutbound(playerSelf.username)) {
                this.action_title!!.text = "outbound"
                this.action_image!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_outbound)!!)
            } else {
                this.action_title!!.text = "inbound"
                this.action_image!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_inbound)!!)
            }
        }
        if (game.status == "ONGOING") {
            this.setTschess()
            this.layout_option_swipe!!.removeView(layout_accept)
            this.layout_option_swipe!!.removeView(layout_reject)
            this.layout_option_swipe!!.removeView(layout_rematch)
            if (game.getTurn(playerSelf.username)) {
                this.action_title!!.text = "action!"
                this.action_image!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_action)!!)
            } else {
                this.action_title!!.text = "pending"
                this.action_image!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_pending)!!)
            }
        }
    }

    private fun setInvite() {
        this.layout_row!!.setOnClickListener {
            if (this.layout_swipe!!.isClosed) {
                this.layout_swipe!!.open(true)
                return@setOnClickListener
            }
            if (this.layout_swipe!!.isOpened) {
                this.layout_swipe!!.close(true)
            }
        }
    }

    private fun setTschess() {
        this.layout_row!!.setOnClickListener {
            val intent = Intent(context, ActivityTschess::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("game", game)
            context.startActivity(intent)
        }
    }

    private fun setActive() {
        this.layout_row!!.setBackgroundColor(Color.WHITE)
        this.username!!.visibility = View.INVISIBLE
        this.action_title!!.setTextColor(Color.BLACK)
        this.action_title!!.visibility = View.VISIBLE
        this.action_image!!.visibility = View.VISIBLE
        this.more_vert!!.visibility = View.INVISIBLE
    }

    private fun setInviteAccept() {
        this.accept_image!!.setColorFilter(Color.parseColor("#aaaaaa"))
        this.accept_title!!.text = "accept"
        this.layout_accept!!.setOnClickListener {


            this.layout_swipe!!.close(true)
            activityHome.dialogRematch(playerSelf, playerOther, true)

            //dialogRematch(playerSelf: EntityPlayer, playerOther: EntityPlayer, accept: Boolean = false)

            //val intent = Intent(context, ActivityQuick::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            //extras.putExtra("player_self", playerSelf)
            //extras.putExtra("player_other", playerOther)
            //extras.putExtra("game_id", game.id)
            //val config: Int = (0..3).random()
            //extras.putExtra("config", config)
            //extras.putExtra("action", "ACCEPT")
            //context.startActivity(intent)
            //this.layout_swipe!!.close(false)
        }
    }

    private fun setInviteReject() {
        if (game.getOutbound(playerSelf.username)) {
            this.reject_image!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_rescind)!!)
            this.reject_image!!.setColorFilter(Color.parseColor("#aaaaaa"))
            this.reject_title!!.text = "rescind"
            this.layout_reject!!.setOnClickListener {
                val url = "${ServerAddress().IP}:8080/game/rescind"
                val params = HashMap<String, Any>()
                params["id_game"] = game.id
                params["id_self"] = playerSelf.id
                val jsonObject = JSONObject(params as Map<*, *>)
                val request =
                    JsonObjectRequest(
                        Request.Method.POST, url, jsonObject,
                        {
                            refresher.refresh(position)
                        },
                        {
                            Log.e("error in volley request", "${it.message}")
                        }
                    )
                VolleySingleton.getInstance(context).addToRequestQueue(request)
            }
            return
        }
        this.reject_image!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_reject)!!)
        this.reject_image!!.setColorFilter(Color.parseColor("#aaaaaa"))
        this.reject_title!!.text = "decline"
        this.layout_reject!!.setOnClickListener {
            val url = "${ServerAddress().IP}:8080/game/nack"
            val params = HashMap<String, Any>()
            params["id_game"] = game.id
            params["id_self"] = playerSelf.id
            val jsonObject = JSONObject(params as Map<*, *>)
            val request =
                JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    {
                        refresher.refresh(position)
                    },
                    {
                        Log.e("error in volley request", "${it.message}")
                    }
                )
            VolleySingleton.getInstance(context).addToRequestQueue(request)
        }
    }

    private fun setRematch() {
        this.rematch_title!!.text = "SNAPSHOT"
        this.layout_rematch!!.setOnClickListener {

            //if (this.layout_swipe!!.isOpened) {

            //} else {
            val intent = Intent(context, ActivitySnapshot::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("game", game)
            context.startActivity(intent)
            this.layout_swipe!!.close(false)
            //this.layout_swipe!!.close(false)
            //}

            //this.layout_swipe!!.close(true)
            //activityHome.dialogRematch()

            //val intent = Intent(context, ActivityQuick::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //val config: Int = (0..3).random()
            //val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            //extras.putExtra("player_self", playerSelf)
            //extras.putExtra("player_other", playerOther)
            //extras.putExtra("config", config)
            //extras.putExtra("game", game)
            //extras.putExtra("white", !game.getWhite(playerSelf.username))
            //extras.putExtra("action", "REMATCH")
            //context.startActivity(intent)
            //this.layout_swipe!!.close(false)
        }
    }

    private fun setSnapshot() {
        this.layout_row!!.setOnClickListener {

            if (this.layout_swipe!!.isOpened) {
                this.layout_swipe!!.close(true)
                return@setOnClickListener
            }

            this.layout_swipe!!.close(true)
            activityHome.dialogRematch(playerSelf, playerOther)
        }
    }

    private fun setHisto() {
        this.action_title!!.visibility = View.INVISIBLE
        this.action_image!!.visibility = View.INVISIBLE
        this.layout_option_swipe!!.removeView(layout_accept)
        this.layout_option_swipe!!.removeView(layout_reject)

        //TODO: maybe if 'confirm' remove rematch also...

        val white: Boolean = game.getWhite(playerSelf.username)

        //Log.e("STATUS", "${game.status}")
        //Log.e("white", "${white}")
        //Log.e("playerSelf.username", "${playerSelf.username}")

        if (game.status == "RESOLVED") {

            Log.e("-->", "A")

            this.setResolved()
            return
        }

        if (game.status.contains("WHITE")) {
            //RESOLVED_WHITE_BLACK
            //RESOLVED_WHITE
            if (white) {

                Log.e("-->", "B")

                this.updateConfirm(white)
                return
            }

            Log.e("-->", "C")

            this.setResolved()
            return
        }
        if (!white) {

            Log.e("-->", "D")

            //RESOLVED_WHITE_BLACK
            //RESOLVED_BLACK
            this.updateConfirm(white)
            return
        }

        Log.e("-->", "E")

        this.setResolved()
    }

    private fun setResolved() {
        this.setSnapshot()
        this.setRematch()
        this.layout_row!!.setBackgroundColor(Color.parseColor("#1F2123"))
        this.username!!.visibility = View.VISIBLE
        this.more_vert!!.visibility = View.VISIBLE
        val draw: Boolean = game.condition == "DRAW"
        if (draw) {
            this.more_vert!!.setColorFilter(Color.argb(255, 255, 255, 0))
            this.rematch_image!!.setColorFilter(Color.argb(255, 255, 255, 0))
            this.rematch_title!!.setTextColor(Color.argb(255, 255, 255, 0))
            return
        }
        val winner: Boolean = this.game.getWinnerUsername() == this.playerSelf.username
        if (winner) {
            this.more_vert!!.setColorFilter(Color.argb(255, 0, 255, 0))
            this.rematch_image!!.setColorFilter(Color.argb(255, 0, 255, 0))
            this.rematch_title!!.setTextColor(Color.argb(255, 0, 255, 0))
            return
        }
        this.more_vert!!.setColorFilter(Color.argb(255, 255, 0, 0))
        this.rematch_image!!.setColorFilter(Color.argb(255, 255, 0, 0))
        this.rematch_title!!.setTextColor(Color.argb(255, 255, 0, 0))
    }

    private fun updateConfirm(white: Boolean) {
        this.layout_row!!.setBackgroundColor(Color.WHITE)
        this.layout_option_swipe!!.removeView(layout_rematch)
        this.more_vert!!.visibility = View.INVISIBLE
        this.layout_row!!.setOnClickListener {

            val url = "${ServerAddress().IP}:8080/game/confirm"
            val params = HashMap<String, Any>()
            params["id_game"] = game.id
            params["white"] = white
            val jsonObject = JSONObject(params as Map<*, *>)
            val request =
                JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    {
                        refresher.refresh(position)
                    },
                    {
                        Log.e("error in volley request", "${it.message}")
                    }
                )
            VolleySingleton.getInstance(context).addToRequestQueue(request)

            val intent = Intent(context, ActivitySnapshot::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("game", game)
            context.startActivity(intent)
        }
    }
}

