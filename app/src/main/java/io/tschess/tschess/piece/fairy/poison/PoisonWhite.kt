package io.tschess.tschess.piece.fairy.poison

import io.tschess.tschess.R

class PoisonWhite(self_white: Boolean) : Poison(
    name = "PoisonWhite",
    imageDefault = if (self_white) R.drawable.white_landmine as Int else R.drawable.white_pawn as Int,
    imageTarget = if (self_white) R.drawable.target_white_landmine as Int? else R.drawable.target_white_pawn as Int?,
    imageSelect = if (self_white) R.drawable.selection_white_landmine as Int? else R.drawable.selection_white_pawn as Int?
)