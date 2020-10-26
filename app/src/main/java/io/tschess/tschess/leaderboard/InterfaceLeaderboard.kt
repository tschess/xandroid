package io.tschess.tschess.leaderboard

import android.widget.ImageView
import android.widget.TextView

@FunctionalInterface
interface Dialogger {
    fun render(username: String)
}

@FunctionalInterface
interface Shudder {
    fun shake(avatar: ImageView, username: TextView)
}

@FunctionalInterface
interface Refresher {
    fun refresh()
}