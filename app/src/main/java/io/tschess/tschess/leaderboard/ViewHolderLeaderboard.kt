package io.tschess.tschess.leaderboard

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chauthai.swipereveallayout.SwipeRevealLayout
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.ParseGame
import io.tschess.tschess.model.EntityPlayer
//import io.tschess.tschess.other.ActivityOther
//import io.tschess.tschess.quick.ActivityQuick
import io.tschess.tschess.snapshot.ActivitySnapshot
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.other.ActivityOther
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton

class ViewHolderLeaderboard(
    var layout_swipe: SwipeRevealLayout? = null,
    var layout_row: ConstraintLayout? = null,
    var avatar: ImageView? = null,
    var username: TextView? = null,
    var date: TextView? = null,
    var disp_ind: ImageView? = null,
    //var challenge_image: ImageView? = null,
    //var challenge_title: TextView? = null,
    var recent_image: ImageView? = null,
    var recent_title: TextView? = null,
    var layout_option_swipe: LinearLayout? = null,
    var layout_recent: FrameLayout? = null,
    //var layout_challenge: FrameLayout? = null,
    val rank_value: TextView? = null,
    val rank_ind: TextView? = null, //TODO: maybe don't need this...
    val context: Context,
    val playerOther: EntityPlayer,
    val playerSelf: EntityPlayer,

    var progressBar: ProgressBar,
    var dialogger: Dialogger,
    val activityLeaderboard: ActivityLeaderboard

){

    private val glide = Glide.with(context)
    private val parseGame: ParseGame = ParseGame()

    init {
        this.username!!.text = playerOther.username

        val drawable: Drawable? = playerOther.drawable
        //if (drawable != null) {
            //avatar!!.visibility = View.VISIBLE
            //avatar!!.setImageDrawable(drawable)
        //}
        glide.load(playerOther.avatar).apply(RequestOptions.circleCropTransform()).into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                avatar!!.visibility = View.VISIBLE
                avatar!!.setImageDrawable(resource)
                playerOther.drawable = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {}
        })

        this.date!!.text = playerOther.getDateText()

        this.rank_ind!!.text = "rank:"
        this.rank_value!!.text = playerOther.rank.toString()
        this.disp_ind!!.setImageDrawable(playerOther.getDispInd(context))

        this.layout_row!!.setOnClickListener {
            if (layout_swipe!!.isOpened) {
                layout_swipe!!.close(true)
                return@setOnClickListener
            }


            activityLeaderboard.dialogChallenge(playerSelf, playerOther)

            //val intent = Intent(context, ActivityOther::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            //extras.putExtra("player_self", playerSelf)
            //extras.putExtra("player_other", playerOther)
            //context.startActivity(intent)
        }

        this.recent_image!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_recent)!!)
        this.recent_title!!.text = "GAMES"
        this.layout_recent!!.setOnClickListener {

            layout_swipe!!.close(false)

            val intent = Intent(context, ActivityOther::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("player_other", playerOther)
            context.startActivity(intent)



            //this.progressBar.visibility = View.VISIBLE
            //val url = "${ServerAddress().IP}:8080/game/recent/${playerOther.id}"
            //val jsonObjectRequest = JsonObjectRequest(
            //    Request.Method.GET, url, null,
            //    Response.Listener { response ->
            //        this.progressBar.visibility = View.INVISIBLE
            //        if (response.has("fail")) {
            //            this.dialogger.render(playerOther.username)
            //            return@Listener
            //        }
            //        val game: EntityGame = parseGame.execute(response)
            //        val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            //        extras.putExtra("player_self", playerSelf)
            //        extras.putExtra("game", game)
            //
            //        val intent = Intent(context, ActivitySnapshot::class.java)
            //        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //
            //        context.startActivity(intent)
            //    },
            //    Response.ErrorListener {
            //        this.progressBar.visibility = View.INVISIBLE
            //        this.dialogger.render(playerOther.username)
            //    }
            //)
            //VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
            //layout_swipe!!.close(false)
        }

    }



}