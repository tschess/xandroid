package io.tschess.tschess.piece.fairy

import io.tschess.tschess.piece.Piece

open class Fairy(
    name: String,
    strength: Int,
    imageDefault: Int,
    imageTarget: Int?,
    imageSelect: Int?,
    attackList: List<String>,
    val describe: String
) : Piece(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = strength,
    attackList = attackList,
    chess = false
) {}