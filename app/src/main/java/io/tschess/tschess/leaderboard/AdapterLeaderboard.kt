package io.tschess.tschess.leaderboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.home.ViewHolderHome

class AdapterLeaderboard(
    val playerSelf: EntityPlayer,
    context: Context,
    objects: List<EntityPlayer>,
    var progressBar: ProgressBar,
    var dialogger: Dialogger,
    var shudder: Shudder,
    val activityLeaderboard: ActivityLeaderboard
) : ArrayAdapter<EntityPlayer>(context, R.layout.item_leaderboard, objects) {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val binderHelper: ViewBinderHelper = ViewBinderHelper()

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val row: View? = mInflater.inflate(R.layout.item_leaderboard, parent, false) //TODO: consolidate...

        val playerOther: EntityPlayer = getItem(position) as EntityPlayer

        val holder: ViewHolderLeaderboard = ViewHolderLeaderboard(
            row!!.findViewById(R.id.layout_swipe) as SwipeRevealLayout,
            row.findViewById(R.id.layout_row) as ConstraintLayout,
            row.findViewById(R.id.avatar) as ImageView,
            row.findViewById(R.id.username) as TextView,

            row.findViewById(R.id.disp_image) as ImageView,

            row.findViewById(R.id.recent_image) as ImageView,
            row.findViewById(R.id.recent_title) as TextView,
            row.findViewById(R.id.layout_option_swipe) as LinearLayout,
            row.findViewById(R.id.layout_recent) as FrameLayout,

            row.findViewById(R.id.rank_value) as TextView,
            row.findViewById(R.id.rank_indicator) as TextView,
            row.findViewById(R.id.rating_value) as TextView,
            row.findViewById(R.id.rating_indicator) as TextView,
            row.findViewById(R.id.more_vert) as ImageView,

            context,
            playerOther,
            this.playerSelf,
            progressBar,
            dialogger,
            shudder,
            activityLeaderboard
        )
        row.tag = holder
        binderHelper.bind(holder.layout_swipe, playerOther.id)
        return row
    }

}

