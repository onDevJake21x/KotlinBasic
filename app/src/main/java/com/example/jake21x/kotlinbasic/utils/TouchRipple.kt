package com.example.jake21x.kotlinbasic.utils

import android.view.MotionEvent
import android.support.v7.widget.CardView
import android.view.View


/**
 * Created by Jake21x on 12/8/2017.
 */
abstract class TouchRipple : View.OnTouchListener {
    internal abstract var cardView: CardView

    fun setCardView(cardView: CardView): TouchRipple {
        this.cardView = cardView
        return this
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        // Convert to card view coordinates. Assumes the host view is
        // a direct child and the card view is not scrollable.
        val x = event.x + v.getLeft()
        val y = event.y + v.getTop()

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Simulate motion on the card view.
            cardView.drawableHotspotChanged(x, y)
        }

        // Simulate pressed state on the card view.
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> cardView.isPressed = true
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> cardView.isPressed = false
        }

        // Pass all events through to the host view.
        return false
    }
}