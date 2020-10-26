package io.tschess.tschess.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer

class AdapterHome(val playerSelf: EntityPlayer, context: Context, objects: List<EntityGame>,
                  val activityHome: ActivityHome) :
    ArrayAdapter<EntityGame>(context, R.layout.item_home, objects) {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val binderHelper: ViewBinderHelper = ViewBinderHelper()
    lateinit var refresher: Refresher

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var row: View? = view
        row = mInflater.inflate(R.layout.item_home, parent, false)

        val game: EntityGame = getItem(position) as EntityGame
        val playerOther: EntityPlayer = game.getPlayerOther(playerSelf.username)

        val holder: ViewHolderHome = ViewHolderHome(
            row!!.findViewById(R.id.layout_swipe) as SwipeRevealLayout,
            row.findViewById(R.id.layout_row) as ConstraintLayout,
            row.findViewById(R.id.avatar) as ImageView,
            row.findViewById(R.id.username) as TextView,
            row.findViewById(R.id.action_title) as TextView,
            row.findViewById(R.id.action_image) as ImageView,
            row.findViewById(R.id.more_vert) as ImageView,
            row.findViewById(R.id.layout_option_swipe) as LinearLayout,
            row.findViewById(R.id.layout_accept) as FrameLayout,
            row.findViewById(R.id.accept_image) as ImageView,
            row.findViewById(R.id.accept_title) as TextView,
            row.findViewById(R.id.layout_reject) as FrameLayout,
            row.findViewById(R.id.reject_image) as ImageView,
            row.findViewById(R.id.reject_title) as TextView,
            row.findViewById(R.id.layout_rematch) as FrameLayout,
            row.findViewById(R.id.rematch_image) as ImageView,
            row.findViewById(R.id.rematch_title) as TextView,

            row.findViewById(R.id.emoji_indicator) as TextView,

            game,
            playerSelf,
            playerOther,
            context,
            refresher,
            position,
            activityHome
        )
        row.tag = holder
        binderHelper.bind(holder.layout_swipe, game.id)
        return row
    }

}

