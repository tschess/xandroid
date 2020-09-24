package io.tschess.tschess.other

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import io.tschess.tschess.R
import io.tschess.tschess.model.EntityGame
import io.tschess.tschess.model.EntityPlayer
import io.tschess.tschess.snapshot.ActivitySnapshot
import io.tschess.tschess.model.ExtendedDataHolder
import java.io.ByteArrayOutputStream


class AdapterOther(
    val playerSelf: EntityPlayer,
    context: Context,
    objects: List<EntityGame>
) : ArrayAdapter<EntityGame>(context, R.layout.item_home, objects) {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val binderHelper: ViewBinderHelper = ViewBinderHelper()

    private val glide = Glide.with(context)

    private inner class ViewHolder {
        var layout_row: ConstraintLayout? = null
        var avatar: ImageView? = null
        var username: TextView? = null

        var date: TextView? = null
        var rank_ind: TextView? = null
        var rank_cnt: TextView? = null
        var disp_cnt: TextView? = null
        var disp_ind: ImageView? = null
    }


    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var row: View? = view
        val holder: ViewHolder

        val game: EntityGame = getItem(position) as EntityGame

        holder = ViewHolder()
        row = mInflater.inflate(R.layout.item_home, parent, false)

        val layout_row: ConstraintLayout = row.findViewById(R.id.layout_row) as ConstraintLayout
        layout_row.setOnClickListener {

            val intent = Intent(context, ActivitySnapshot::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("game", game)
            context.startActivity(intent)

        }

        holder.layout_row = layout_row
        layout_row.setBackgroundColor(Color.parseColor("#1F2123"))


        val action_title: TextView = row.findViewById(R.id.action_title) as TextView
        action_title.visibility = View.INVISIBLE

        val action_image: ImageView = row.findViewById(R.id.action_image) as ImageView
        action_image.visibility = View.INVISIBLE

        //more_vert
        val more_vert: ImageView = row.findViewById(R.id.more_vert) as ImageView
        more_vert.visibility = View.INVISIBLE


        val layout_rematch: FrameLayout = row.findViewById(R.id.layout_rematch) as FrameLayout
        val layout_reject: FrameLayout = row.findViewById(R.id.layout_reject) as FrameLayout
        val layout_accept: FrameLayout = row.findViewById(R.id.layout_accept) as FrameLayout

        val layout_option_swipe: LinearLayout = row.findViewById(R.id.layout_option_swipe)
        layout_option_swipe.removeView(layout_accept)
        layout_option_swipe.removeView(layout_reject)
        layout_option_swipe.removeView(layout_rematch)




        val playerOther: EntityPlayer = game.getPlayerOther(playerSelf.username)




        val imageView: ImageView = row.findViewById(R.id.avatar)
        imageView.visibility = View.INVISIBLE
        glide.load(playerOther.avatar).apply(RequestOptions.circleCropTransform()).into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(resource)
            }
            override fun onLoadCleared(placeholder: Drawable?) {}
        })

        val username: TextView = row.findViewById(R.id.username) as TextView
        username.text = game.getUsernameOther(playerSelf.username)
        holder.username = username

        row.tag = holder
        return row
    }

}