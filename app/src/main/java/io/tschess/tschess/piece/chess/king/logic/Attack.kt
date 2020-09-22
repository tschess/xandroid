package io.tschess.tschess.piece.chess.king.logic

import io.tschess.tschess.piece.Piece

class Attack() {

    //private val logicPawn: LogicPawn = LogicPawn()
    private val evaluation: Evaluation = Evaluation()

    fun rowStraightDown(rowStraightDown: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(rowStraightDown == proposed){
            return evaluation.rowStraightDown(
                present = present,
                proposed = proposed,
                affiliation = affiliation,
                state = state,
                threat = threat)
        }
        return false
    }

    fun rowStraightDown_lookBehind(rowStraightDown_lookBehind: Array<Int>, rowStraightDown: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(rowStraightDown_lookBehind == proposed){
            if (rowStraightDown_lookBehind[0] >= 0) {
                if (evaluation.rowStraightDown(
                        present = present,
                        proposed = rowStraightDown,
                        affiliation = affiliation,
                        state = state,
                        threat = threat)) {
                    return true
                }
                return evaluation.evaluateAttackVector(
                    coordinate = rowStraightDown,
                    affiliation = affiliation,
                    vector = "HorizontalVertical",
                    state = state)
            }
        }
        return false
    }

    fun rowStraightUp(rowStraightUp: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(rowStraightUp == proposed){
            return evaluation.rowStraightUp(
                present = present,
                proposed = proposed,
                affiliation = affiliation,
                state = state,
                threat = threat)
        }
        return false
    }

    fun rowStraightUp_lookBehind(rowStraightUp_lookBehind: Array<Int>, rowStraightUp: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(rowStraightUp_lookBehind == proposed){
            if (rowStraightUp_lookBehind[0] <= 7) {
                if (evaluation.rowStraightUp(
                        present = present,
                        proposed = rowStraightUp,
                        affiliation = affiliation,
                        state = state,
                        threat = threat)) {
                    return true
                }
                return evaluation.evaluateAttackVector(
                    coordinate = rowStraightUp,
                    affiliation = affiliation,
                    vector = "HorizontalVertical",
                    state = state)
            }
        }
        return false
    }

    fun columnLeft(columnLeft: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(columnLeft == proposed){
            return evaluation.columnLeft(
                present = present,
                proposed = columnLeft,
                affiliation = affiliation,
                state = state,
                threat = threat)
        }
        return false
    }

    fun columnLeft_lookBehind(columnLeft_lookBehind: Array<Int>, columnLeft: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(columnLeft_lookBehind == proposed){
            if (columnLeft_lookBehind[1] <= 7) {
                if (evaluation.columnLeft(
                        present = present,
                        proposed = columnLeft,
                        affiliation = affiliation,
                        state = state,
                        threat = threat)) {
                    return true
                }
                return evaluation.evaluateAttackVector(
                    coordinate = columnLeft,
                    affiliation = affiliation,
                    vector = "HorizontalVertical",
                    state = state)
            }
        }
        return false
    }

    fun columnRight(columnRight: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(columnRight == proposed){
            return evaluation.columnRight(
                present = present,
                proposed = columnRight,
                affiliation = affiliation,
                state = state,
                threat = threat)
        }
        return false
    }

    fun columnRight_lookBehind(columnRight_lookBehind: Array<Int>, columnRight: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(columnRight_lookBehind == proposed){
            if (columnRight_lookBehind[1] >= 0) {
                if (evaluation.columnRight(
                        present = present,
                        proposed = columnRight,
                        affiliation = affiliation,
                        state = state,
                        threat = threat)) {
                    return true
                }
                return evaluation.evaluateAttackVector(
                    coordinate = columnRight,
                    affiliation = affiliation,
                    vector = "HorizontalVertical",
                    state = state)
            }
        }
        return false
    }

    fun diagonalUpRight(diagonalUpRight: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(diagonalUpRight == proposed){
            return evaluation.diagonalUpRight(
                present = present,
                proposed = diagonalUpRight,
                affiliation = affiliation,
                state = state,
                threat = threat)
        }
        return false
    }

    fun diagonalUpRight_lookBehind(diagonalUpRight_lookBehind: Array<Int>, diagonalUpRight: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(diagonalUpRight_lookBehind == proposed){
            if (diagonalUpRight_lookBehind[0] >= 0 && diagonalUpRight_lookBehind[1] <= 7) {
                if (evaluation.diagonalUpRight(
                        present = present,
                        proposed = diagonalUpRight,
                        affiliation = affiliation,
                        state = state,
                        threat = threat)) {
                    return true
                }
                return evaluation.evaluateAttackVector(
                    coordinate = diagonalUpRight,
                    affiliation = affiliation,
                    vector = "Diagonal",
                    state = state)
            }
        }
        return false
    }

    fun diagonalDownLeft(diagonalDownLeft: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(diagonalDownLeft == proposed){
            return evaluation.diagonalDownLeft(
                present = present,
                proposed = diagonalDownLeft,
                affiliation = affiliation,
                state = state,
                threat = threat)
        }
        return false
    }

    fun diagonalDownLeft_lookBehind(diagonalDownLeft_lookBehind: Array<Int>, diagonalDownLeft: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(diagonalDownLeft_lookBehind == proposed){
            if (diagonalDownLeft_lookBehind[0] <= 7 && diagonalDownLeft_lookBehind[1] >= 0) {
                if (evaluation.diagonalDownLeft(
                        present = present,
                        proposed = diagonalDownLeft,
                        affiliation = affiliation,
                        state = state,
                        threat = threat)) {
                    return true
                }
                return evaluation.evaluateAttackVector(
                    coordinate = diagonalDownLeft,
                    affiliation = affiliation,
                    vector = "Diagonal",
                    state = state)
            }
        }
        return false
    }

    fun diagonalUpLeft(diagonalUpLeft: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(diagonalUpLeft == proposed){
            return evaluation.diagonalUpLeft(
                present = present,
                proposed = diagonalUpLeft,
                affiliation = affiliation,
                state = state,
                threat = threat)
        }
        return false
    }

    fun diagonalUpLeft_lookBehind(diagonalUpLeft_lookBehind: Array<Int>, diagonalUpLeft: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(diagonalUpLeft_lookBehind == proposed){
            if (diagonalUpLeft_lookBehind[0] <= 7 && diagonalUpLeft_lookBehind[1] <= 7) {
                if (evaluation.diagonalUpLeft(
                        present = present,
                        proposed = diagonalUpLeft,
                        affiliation = affiliation,
                        state = state,
                        threat = threat)) {
                    return true
                }
                return evaluation.evaluateAttackVector(
                    coordinate = diagonalUpLeft,
                    affiliation = affiliation,
                    vector = "Diagonal",
                    state = state)
            }
        }
        return false
    }

    fun diagonalDownRight(diagonalDownRight: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(diagonalDownRight == proposed){
            return evaluation.diagonalDownRight(
                present = present,
                proposed = diagonalDownRight,
                affiliation = affiliation,
                state = state,
                threat = threat)
        }
        return false
    }

    fun diagonalDownRight_lookBehind(diagonalDownRight_lookBehind: Array<Int>, diagonalDownRight: Array<Int>, affiliation: String, present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>, threat: Boolean): Boolean {
        if(diagonalDownRight_lookBehind == proposed){
            if (diagonalDownRight_lookBehind[0] >= 0 && diagonalDownRight_lookBehind[1] >= 0) {
                if (evaluation.diagonalDownRight(
                        present = present,
                        proposed = diagonalDownRight,
                        affiliation = affiliation,
                        state = state,
                        threat = threat)) {
                    return true
                }
                return evaluation.evaluateAttackVector(
                    coordinate = diagonalDownRight,
                    affiliation = affiliation,
                    vector = "Diagonal",
                    state = state)
            }
        }
        return false
    }

    /* * */

    fun evaluate(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = Array<Array<Piece?>>.getTschessElementMatrix()
        val affiliation = state[present[0]][present[1]]!!.affiliation

        val rowStraightDown: Array<Int> = arrayOf(present[0] + 1, present[1])
        if(this.rowStraightDown(
                rowStraightDown = rowStraightDown,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        val rowStraightDown_lookBehind = arrayOf(present[0] - 1, present[1])
        if(this.rowStraightDown_lookBehind(
                rowStraightDown_lookBehind = rowStraightDown_lookBehind,
                rowStraightDown = rowStraightDown,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        //
        val rowStraightUp = arrayOf(present[0] - 1, present[1])
        if(this.rowStraightUp(
                rowStraightUp = rowStraightUp,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        val rowStraightUp_lookBehind = arrayOf(present[0] + 1, present[1])
        if(this.rowStraightUp_lookBehind(
                rowStraightUp_lookBehind = rowStraightUp_lookBehind,
                rowStraightUp = rowStraightUp,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        //
        val columnLeft = arrayOf(present[0], present[1] - 1)
        if(this.columnLeft(
                columnLeft = columnLeft,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        val columnLeft_lookBehind = arrayOf(present[0], present[1] + 1)
        if(this.columnLeft_lookBehind(
                columnLeft_lookBehind = columnLeft_lookBehind,
                columnLeft = columnLeft,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        //
        val columnRight = arrayOf(present[0], present[1] + 1)
        if(this.columnRight(
                columnRight = columnRight,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        val columnRight_lookBehind = arrayOf(present[0], present[1] - 1)
        if(this.columnRight_lookBehind(
                columnRight_lookBehind = columnRight_lookBehind,
                columnRight = columnRight,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        //
        val diagonalUpRight = arrayOf(present[0] - 1, present[1] + 1)
        if(this.diagonalUpRight(
                diagonalUpRight = diagonalUpRight,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        val diagonalUpRight_lookBehind = arrayOf(present[0] + 1, present[1] - 1)
        if(this.diagonalUpRight_lookBehind(
                diagonalUpRight_lookBehind = diagonalUpRight_lookBehind,
                diagonalUpRight = diagonalUpRight,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        //
        val diagonalDownLeft = arrayOf(present[0] + 1, present[1] - 1)
        if(this.diagonalDownLeft(
                diagonalDownLeft = diagonalDownLeft,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        val diagonalDownLeft_lookBehind = arrayOf(present[0] - 1, present[1] + 1)
        if(this.diagonalDownLeft_lookBehind(
                diagonalDownLeft_lookBehind = diagonalDownLeft_lookBehind,
                diagonalDownLeft = diagonalDownLeft,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        //
        val diagonalUpLeft = arrayOf(present[0] - 1, present[1] - 1)
        if(this.diagonalUpLeft(
                diagonalUpLeft = diagonalUpLeft,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        val diagonalUpLeft_lookBehind = arrayOf(present[0] + 1, present[1] + 1)
        if(this.diagonalUpLeft_lookBehind(
                diagonalUpLeft_lookBehind = diagonalUpLeft_lookBehind,
                diagonalUpLeft = diagonalUpLeft,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        //
        val diagonalDownRight = arrayOf(present[0] + 1, present[1] + 1)
        if(this.diagonalDownRight(
                diagonalDownRight = diagonalDownRight,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }
        val diagonalDownRight_lookBehind = arrayOf(present[0] - 1, present[1] - 1)
        if(this.diagonalDownRight_lookBehind(
                diagonalDownRight_lookBehind = diagonalDownRight_lookBehind,
                diagonalDownRight = diagonalDownRight,
                affiliation = affiliation,
                present = present,
                proposed = proposed,
                state = state,
                threat = false)){
            return true
        }

        return false
    }

}