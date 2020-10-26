package io.tschess.tschess.home

@FunctionalInterface
interface Refresher {
    fun refresh()
}

@FunctionalInterface
interface Rival {
    fun shudder(rival: CardHome)
    fun challenge(rival: CardHome)
}