package io.tschess.tschess.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.model.ParsePlayer
import io.tschess.tschess.server.ServerAddress
import io.tschess.tschess.server.VolleySingleton
import kotlinx.android.synthetic.main.card_home.view.*
import org.json.JSONArray
import org.json.JSONObject

class UtilityRival(
    val rival0: CardHome,
    val rival1: CardHome,
    val rival2: CardHome,
    val playerSelf: EntityPlayer,
    val context: Context,
    val interfaceRival: Rival
) {

    val parsePlayer: ParsePlayer = ParsePlayer()
    val glide: RequestManager = Glide.with(context)

    init {
        this.rival0.visibility = View.INVISIBLE
        this.rival1.visibility = View.INVISIBLE
        this.rival2.visibility = View.INVISIBLE
        this.getRivals(context)
    }

    private fun setRival(json: JSONObject, cardHome: CardHome) {
        val playerRival: EntityPlayer = parsePlayer.execute(json)
        cardHome.playerRival = playerRival
        cardHome.name.text = playerRival.username
        glide.load(playerRival.avatar).apply(RequestOptions.circleCropTransform())
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    cardHome.imageView.setImageDrawable(resource)
                    cardHome.visibility = View.VISIBLE
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun getRivals(context: Context) {
        val url = "${ServerAddress().IP}:8080/player/rivals/${this.playerSelf.id}"
        val request = JsonArrayRequest(
            Request.Method.POST, url, null,
            { response: JSONArray ->
                this.setRival(response.getJSONObject(0), this.rival0)
                this.setRival(response.getJSONObject(1), this.rival1)
                this.setRival(response.getJSONObject(2), this.rival2)
            },
            {
            }
        )
        request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1F)
        VolleySingleton.getInstance(context).addToRequestQueue(request)
    }

    fun setRivalList(listMenu: List<EntityGame>) {
        val listActive: List<EntityGame> = listMenu.filter { it.status == "ONGOING" || it.status == "PROPOSED" }
        val listActiveUsername: MutableList<String> = arrayListOf()
        for (game: EntityGame in listActive) {
            val playerOther: EntityPlayer = game.getPlayerOther(playerSelf.username)
            val usernameOther: String = playerOther.username
            listActiveUsername.add(usernameOther)
        }
        val rival0: Boolean = listActiveUsername.contains(rival0.name.text.toString())
        this.determine(rival0, this.rival0)
        val rival1: Boolean = listActiveUsername.contains(rival1.name.text.toString())
        this.determine(rival1, this.rival1)
        val rival2: Boolean = listActiveUsername.contains(rival2.name.text.toString())
        this.determine(rival2, this.rival2)
    }

    private fun determine(active: Boolean, rival: CardHome) {
        if (active) {
            this.interfaceRival.shudder(rival)
            return
        }
        this.interfaceRival.challenge(rival)
    }

}