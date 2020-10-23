package io.tschess.tschess.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.tschess.tschess.dialog.DialogChallenge
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import kotlinx.android.synthetic.main.card_home.view.*
import org.json.JSONArray
import java.util.*
import kotlin.concurrent.schedule

class UtilityRival(
    val rival0: CardHome,
    val rival1: CardHome,
    val rival2: CardHome,
    val playerSelf: EntityPlayer,
    val glide: RequestManager
) {

    private val parsePlayer: ParsePlayer

    init {
        this.parsePlayer = ParsePlayer()
        this.rival0.visibility = View.INVISIBLE
        this.rival1.visibility = View.INVISIBLE
        this.rival2.visibility = View.INVISIBLE
    }

    fun getRivals(context: Context) {

        val url = "${ServerAddress().IP}:8080/player/rivals/${this.playerSelf.id}"
        val request = JsonArrayRequest(
            Request.Method.POST, url, null,
            { response: JSONArray ->
                val playerRival0: EntityPlayer = parsePlayer.execute(response.getJSONObject(0))
                rival0.playerRival = playerRival0
                rival0.name.text = playerRival0.username
                glide.load(playerRival0.avatar).apply(RequestOptions.circleCropTransform())
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            rival0.imageView.setImageDrawable(resource)
                            rival0.visibility = View.VISIBLE

                            //setRivalList()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
                val playerRival1: EntityPlayer = parsePlayer.execute(response.getJSONObject(1))
                rival1.playerRival = playerRival1
                rival1.name.text = playerRival1.username
                glide.load(playerRival1.avatar).apply(RequestOptions.circleCropTransform())
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            rival1.imageView.setImageDrawable(resource)
                            rival1.visibility = View.VISIBLE

                            //setRivalList()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
                val playerRival2: EntityPlayer = parsePlayer.execute(response.getJSONObject(2))
                rival2.playerRival = playerRival2
                rival2.name.text = playerRival2.username
                glide.load(playerRival2.avatar).apply(RequestOptions.circleCropTransform())
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            rival2.imageView.setImageDrawable(resource)
                            rival2.visibility = View.VISIBLE

                            //setRivalList()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })

            },
            {
            }
        )
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(context).addToRequestQueue(request)
    }

    fun setRival(usernameOther: String, rival0: CardHome) {
        val usernameRival0: String = rival0.name.text.toString()
        if (usernameOther == usernameRival0) {
            this.setShudder(rival0)
            return
        }
        this.setChallenge(rival0)
    }

    fun setRivalList(listMenu: List<EntityGame>) {
        val listActive: List<EntityGame> = listMenu.filter { it.status == "ONGOING" || it.status == "PROPOSED" }

        val listActiveUsername: MutableList<String> = arrayListOf()
        for (game: EntityGame in listActive) {
            val playerOther: EntityPlayer = game.getPlayerOther(playerSelf.username)
            val usernameOther: String = playerOther.username
            listActiveUsername.add(usernameOther)


        }





        if(listActiveUsername.contains(rival0.name.text.toString())){
            rival0.imageView.alpha = 0.5F
            rival0.name.alpha = 0.5F
        } else {
            rival0.imageView.alpha = 1F
            rival0.name.alpha = 1F
        }

        if(listActiveUsername.contains(rival1.name.text.toString())){
            rival1.imageView.alpha = 0.5F
            rival1.name.alpha = 0.5F
        } else {
            rival1.imageView.alpha = 1F
            rival1.name.alpha = 1F
        }

        if(listActiveUsername.contains(rival2.name.text.toString())){
            rival2.imageView.alpha = 0.5F
            rival2.name.alpha = 0.5F
        } else {
            rival2.imageView.alpha = 1F
            rival2.name.alpha = 1F
        }

        //for (game: EntityGame in listActive) {
            //val playerOther: EntityPlayer = game.getPlayerOther(playerSelf.username)
            //val usernameOther: String = playerOther.username

            //val usernameRival0: String = rival0.name.text.toString()
            //if(usernameOther == usernameRival0){
            //this.setShudder(rival0)
            //} else {
            //this.setChallenge(rival0)
            //}
            //val usernameRival1: String = rival1.name.text.toString()
            //if (usernameOther == usernameRival1) {
                //this.setShudder(rival1)
            //} else {
                //this.setChallenge(rival1)
            //}
            //val usernameRival2: String = rival2.name.text.toString()
            //if (usernameOther == usernameRival2) {
                //this.setShudder(rival2)
            //} else {
                //this.setChallenge(rival2)
            //}
        //}
    }

    private fun setShudder(rival: CardHome) {
//        rival.setOnClickListener {
//            rival.imageView.visibility = View.INVISIBLE
//            rival.name.visibility = View.INVISIBLE
//            window.decorView.rootView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
//            Timer().schedule(11) {
//                runOnUiThread {
//                    rival.imageView.visibility = View.VISIBLE
//                    rival.name.visibility = View.VISIBLE
//                }
//            }
//        }
//        rival.imageView.alpha = 0.5F
//        rival.name.alpha = 0.5F
    }

    private fun setChallenge(rival: CardHome) {
//        rival.imageView.alpha = 1F
//        rival.name.alpha = 1F
//        rival.setOnClickListener {
//            val dialogChallenge: DialogChallenge =
//                DialogChallenge(this, playerSelf, rival.playerRival, null, "INVITATION")
//            dialogChallenge.show()
//        }
    }
}