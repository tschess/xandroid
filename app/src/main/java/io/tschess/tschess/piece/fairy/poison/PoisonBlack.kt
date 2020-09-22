package io.tschess.tschess.piece.fairy.poison

import io.tschess.tschess.R

class PoisonBlack(self_white: Boolean) : Poison(
    name = "PoisonBlack",
    imageDefault = if (self_white) R.drawable.black_pawn else R.drawable.black_landmine,
    imageTarget = if (self_white) R.drawable.target_black_pawn else R.drawable.target_black_landmine,
    imageSelect = if (self_white) R.drawable.selection_black_pawn else R.drawable.selection_black_landmine
)