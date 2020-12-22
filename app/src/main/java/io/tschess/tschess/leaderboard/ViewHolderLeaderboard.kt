package io.tschess.tschess.leaderboard

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chauthai.swipereveallayout.SwipeRevealLayout
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ExtendedDataHolder
import io.tschess.tschess.model.ParseGame
import io.tschess.tschess.other.ActivityOther
import kotlinx.android.synthetic.main.card_home.view.*
import java.util.*
import kotlin.concurrent.schedule

class ViewHolderLeaderboard(
    var layout_swipe: SwipeRevealLayout? = null,
    var layout_row: ConstraintLayout? = null,
    var avatar: ImageView? = null,
    var username: TextView? = null,

    var disp_ind: ImageView? = null,

    var recent_image: ImageView? = null,
    var recent_title: TextView? = null,
    var layout_option_swipe: LinearLayout? = null,
    var layout_recent: FrameLayout? = null,

    val rank_value: TextView? = null,
    val rank_ind: TextView? = null, //TODO: maybe don't need this...
    val rating_value: TextView? = null,
    val rating_ind: TextView? = null,

    val more_vert: ImageView? = null,

    val context: Context,
    val playerOther: EntityPlayer,
    val playerSelf: EntityPlayer,

    var progressBar: ProgressBar,
    var dialogger: Dialogger,
    var shudder: Shudder,

    val activityLeaderboard: ActivityLeaderboard
) {

    private val glide = Glide.with(context)
    private val parseGame: ParseGame = ParseGame()

    init {
        this.username!!.text = playerOther.username

        glide.load(playerOther.avatar).apply(RequestOptions.circleCropTransform())
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    avatar!!.visibility = View.VISIBLE
                    avatar!!.setImageDrawable(resource)
                    playerOther.drawable = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        this.rank_ind!!.text = "rank:"
        this.rank_value!!.text = playerOther.rank.toString()
        this.disp_ind!!.setImageDrawable(playerOther.getDispInd(context))

        this.layout_row!!.setOnClickListener {
            if (layout_swipe!!.isOpened) {
                layout_swipe!!.close(true)
                return@setOnClickListener
            }
            val selfClick: Boolean =  this.username!!.text == playerSelf.username
            if(selfClick){
                //TODO: shudder or something...
                shudder.shake(this.avatar!!, this.username!!)
                return@setOnClickListener
            }
            activityLeaderboard.dialogChallenge(playerSelf = playerSelf, playerOther = playerOther, action = "INVITATION")
        }

        val drawableDisp: Drawable? = playerOther.getDispInd(context)
        disp_ind!!.setImageDrawable(drawableDisp)

        this.rating_ind!!.text = "rating:"
        this.rating_value!!.text = playerOther.elo.toString()

        val selfClick: Boolean =  this.username!!.text == playerSelf.username
        if(selfClick){
            this.layout_option_swipe!!.removeView(layout_recent)
            this.more_vert!!.visibility = View.INVISIBLE
        } else {
            this.more_vert!!.visibility = View.VISIBLE
        }

        this.recent_image!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_recent)!!)
        this.recent_title!!.text = "GAMES"
        this.layout_recent!!.setOnClickListener {

            layout_swipe!!.close(false)
            val intent = Intent(context, ActivityOther::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("billing_client", activityLeaderboard.billingClient)
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("player_other", playerOther)
            context.startActivity(intent)
        }

    }



}