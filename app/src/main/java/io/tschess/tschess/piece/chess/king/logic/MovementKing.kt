package io.tschess.tschess.piece.chess.king.logic

class MovementKing {

    fun movement(present: Array<Int>, proposed: Array<Int>): Boolean {
        if (zeroMinus(present = present, proposed = proposed)) {
            return true
        }
        if (zeroPlus(present = present, proposed = proposed)) {
            return true
        }
        if (oneMinus(present = present, proposed = proposed)) {
            return true
        }
        if (onePlus(present = present, proposed = proposed)) {
            return true
        }
        if (minusMinus(present = present, proposed = proposed)) {
            return true
        }
        if (minusPlus(present = present, proposed = proposed)) {
            return true
        }
        if (plusMinus(present = present, proposed = proposed)) {
            return true
        }
        if (plusPlus(present = present, proposed = proposed)) {
            return true
        }
        return false
    }

    fun zeroMinus(present: Array<Int>, proposed: Array<Int>): Boolean {
        return (present[0] - 1) - proposed[0] == 0 && present[1] - proposed[1] == 0
    }

    fun zeroPlus(present: Array<Int>, proposed: Array<Int>): Boolean {
        return (present[0] + 1) - proposed[0] == 0 && present[1] - proposed[1] == 0
    }

    fun oneMinus(present: Array<Int>, proposed: Array<Int>): Boolean {
        return present[0] - proposed[0] == 0 && (present[1] - 1) - proposed[1] == 0
    }

    fun onePlus(present: Array<Int>, proposed: Array<Int>): Boolean {
        return present[0] - proposed[0] == 0 && (present[1] + 1) - proposed[1] == 0
    }

    fun minusMinus(present: Array<Int>, proposed: Array<Int>): Boolean {
        return (present[0] - 1) - proposed[0] == 0 && (present[1] - 1) - proposed[1] == 0
    }

    fun minusPlus(present: Array<Int>, proposed: Array<Int>): Boolean {
        return (present[0] - 1) - proposed[0] == 0 && (present[1] + 1) - proposed[1] == 0
    }

    fun plusMinus(present: Array<Int>, proposed: Array<Int>): Boolean {
        return (present[0] + 1) - proposed[0] == 0 && (present[1] - 1) - proposed[1] == 0
    }

    fun plusPlus(present: Array<Int>, proposed: Array<Int>): Boolean {
        return (present[0] + 1) - proposed[0] == 0 && (present[1] + 1) - proposed[1] == 0
    }
}