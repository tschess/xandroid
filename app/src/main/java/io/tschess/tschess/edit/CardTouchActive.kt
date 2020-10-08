package io.tschess.tschess.edit

import android.content.ClipData
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class CardTouchActive : View.OnTouchListener {
    lateinit var name: String
    lateinit var coord: String

    lateinit var labelInfo: TextView

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return if (motionEvent.action != MotionEvent.ACTION_DOWN) {
            false
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            val data: ClipData = ClipData.newPlainText("name", name)
            val item: ClipData.Item = ClipData.Item(coord)
            data.addItem(item)


            val poison: Boolean = name == "poison"
            val hunter: Boolean = name == "hunter"
            val amazon: Boolean = name == "amazon"

            if(poison || hunter || amazon){
                labelInfo.visibility = View.VISIBLE
            } else {
                labelInfo.visibility = View.INVISIBLE
            }

            view.startDrag(data, ScaledDragShadow(view, 2F), view, View.DRAG_FLAG_OPAQUE)
            true
        }
    }
}