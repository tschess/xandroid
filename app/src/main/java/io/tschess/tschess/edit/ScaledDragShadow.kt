package io.tschess.tschess.edit

import android.graphics.Canvas
import android.graphics.Point
import android.view.View
import android.view.View.DragShadowBuilder

class ScaledDragShadow(view: View, private val scale: Float): DragShadowBuilder(view) {

    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        // calculate the scaled view's dimensions
        val scaledWidth = (view.width * scale).toInt()
        val scaledHeight = (view.height * scale).toInt()

        // set the scaled shadow size
        outShadowSize?.set(scaledWidth, scaledHeight)

        // re-center the out touch point using the scaled dimensions
        outShadowTouchPoint?.set(scaledWidth / 2, scaledHeight / 2)
    }

    override fun onDrawShadow(canvas: Canvas?) {
        // scale the canvas so it can fit the scaled view
        canvas?.scale(
            scale,
            scale
        )
        view.draw(canvas)
    }
}