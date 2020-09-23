package io.tschess.tschess.other

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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

        //if (row == null) {
        holder = ViewHolder()
        row = mInflater.inflate(R.layout.item_home, parent, false)

        val layout_row: ConstraintLayout = row.findViewById(R.id.layout_row) as ConstraintLayout
        layout_row.setOnClickListener {

            val intent = Intent(context, ActivitySnapshot::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            //playerSelf.avatar = null

            val extras: ExtendedDataHolder = ExtendedDataHolder().getInstance()
            extras.putExtra("player_self", playerSelf)
            extras.putExtra("game", game)

            //intent.putExtra("player_self", playerSelf)

            //game.white.avatar = null
            //game.black.avatar = null

            //intent.putExtra("game", game)
            context.startActivity(intent)

        }

        holder.layout_row = layout_row


        val playerOther: EntityPlayer = game.getPlayerOther(playerSelf.username)

        val avatar: ImageView = row.findViewById(R.id.avatar) as ImageView
        //val byteArray: ByteArray = Base64.decode(game.getAvatarOther(playerSelf.username!!), Base64.DEFAULT)
        //val bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        //avatar.setImageBitmap(bitmap)
        //glide.load(byteArray).into(avatar)
        val byteArray: ByteArray? = playerOther.avatar
        if(byteArray != null){
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            avatar.setImageBitmap(bitmap)
        } else {
            val byteArray: ByteArray = Base64.decode(playerOther.avatar, Base64.DEFAULT)
            glide.load(byteArray).into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    avatar.setImageDrawable(resource)
                    val bitmap: Bitmap = (resource as BitmapDrawable).bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val bitmapdata: ByteArray = stream.toByteArray()
                    //playerOther.byteArray = bitmapdata
                    //playerOther.avatar = null

                    //game.setByteArray(playerOther.username!!, bitmapdata)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        }

        val username: TextView = row.findViewById(R.id.username) as TextView
        username.text = game.getUsernameOther(playerSelf.username)
        holder.username = username


//        val date: TextView = row.findViewById(R.id.date) as TextView
//        date.text = game.getDate()
//        holder.date = date

//        val rank_ind: TextView = row.findViewById(R.id.rank_ind) as TextView
//        rank_ind.text = "odds: "
//        holder.rank_ind = rank_ind
//
//        val rank_cnt: TextView = row.findViewById(R.id.rank_cnt) as TextView
//        rank_cnt.text = game.getOdds(playerSelf.username)
//        holder.rank_cnt = rank_cnt

        //val disp_cnt: TextView = row.findViewById(R.id.disp_cnt) as TextView
        //disp_cnt.text = game.getDispCnt(playerSelf.username!!)
        //holder.disp_cnt = disp_cnt

        //val disp_ind: ImageView = row.findViewById(R.id.disp_ind) as ImageView
        //disp_ind.setImageDrawable(game.getDispInd(playerSelf.username!!, context))
        //holder.disp_ind = disp_ind

        row.tag = holder
        //}


        return row
    }

}