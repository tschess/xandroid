package io.tschess.tschess.piece.chess.king.logic

import io.tschess.tschess.piece.Piece

class Evaluation {

    val movementKing: MovementKing = MovementKing()

    fun rowStraightDown(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (proposed[0] <= 7) {
            if (state[proposed[0]][proposed[1]] != null) {
                return evaluateAttackVector(
                    present = present,
                    proposed = proposed,
                    affiliation = affiliation,
                    vector = "HorizontalVertical",
                    state = state,
                    threat = threat
                )
            }
            val coordinate0 = proposed[0] + 1
            val coordinate1 = proposed[1]
            return rowStraightDown(
                present = present,
                proposed = arrayOf(coordinate0, coordinate1),
                affiliation = affiliation,
                state = state,
                threat = threat
            )
        }
        return false
    }

    fun rowStraightUp(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (proposed[0] >= 0) {
            if (state[proposed[0]][proposed[1]] != null) {
                return evaluateAttackVector(
                    present = present,
                    proposed = proposed,
                    affiliation = affiliation,
                    vector = "HorizontalVertical",
                    state = state,
                    threat = threat
                )
            }
            val coordinate0 = proposed[0] - 1
            val coordinate1 = proposed[1]
            return rowStraightUp(
                present = present,
                proposed = arrayOf(coordinate0, coordinate1),
                affiliation = affiliation,
                state = state,
                threat = threat
            )
        }
        return false
    }

    fun diagonalDownRight(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (proposed[0] <= 7 && proposed[1] <= 7) {
            if (state[proposed[0]][proposed[1]] != null) {
                return evaluateAttackVector(
                    present = present,
                    proposed = proposed,
                    affiliation = affiliation,
                    vector = "Diagonal",
                    state = state,
                    threat = threat
                )
            }
            val coordinate0 = proposed[0] + 1
            val coordinate1 = proposed[1] + 1
            return diagonalDownRight(
                present = present,
                proposed = arrayOf(coordinate0, coordinate1),
                affiliation = affiliation,
                state = state,
                threat = threat
            )
        }
        return false
    }

    fun diagonalDownLeft(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (proposed[0] <= 7 && proposed[1] >= 0) {
            if (state[proposed[0]][proposed[1]] != null) {
                return evaluateAttackVector(
                    present = present,
                    proposed = proposed,
                    affiliation = affiliation,
                    vector = "Diagonal",
                    state = state,
                    threat = threat
                )
            }
            val coordinate0 = proposed[0] + 1
            val coordinate1 = proposed[1] - 1
            return diagonalDownLeft(
                present = present,
                proposed = arrayOf(coordinate0, coordinate1),
                affiliation = affiliation,
                state = state,
                threat = threat
            )
        }
        return false
    }

    fun diagonalUpLeft(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (proposed[0] >= 0 && proposed[1] >= 0) {
            if (state[proposed[0]][proposed[1]] != null) {
                return evaluateAttackVector(
                    present = present,
                    proposed = proposed,
                    affiliation = affiliation,
                    vector = "Diagonal",
                    state = state,
                    threat = threat
                )
            }
            val coordinate0 = proposed[0] - 1
            val coordinate1 = proposed[1] - 1
            return diagonalUpLeft(
                present = present,
                proposed = arrayOf(coordinate0, coordinate1),
                affiliation = affiliation,
                state = state,
                threat = threat
            )
        }
        return false
    }

    fun diagonalUpRight(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (proposed[0] >= 0 && proposed[1] <= 7) {
            if (state[proposed[0]][proposed[1]] != null) {
                return evaluateAttackVector(
                    present = present,
                    proposed = proposed,
                    affiliation = affiliation,
                    vector = "Diagonal",
                    state = state,
                    threat = threat
                )
            }
            val coordinate0 = proposed[0] - 1
            val coordinate1 = proposed[1] + 1
            return diagonalUpRight(
                present = present,
                proposed = arrayOf(coordinate0, coordinate1),
                affiliation = affiliation,
                state = state,
                threat = threat
            )
        }
        return false
    }

    fun columnLeft(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (proposed[1] >= 0) {
            if (state[proposed[0]][proposed[1]] != null) {
                return evaluateAttackVector(
                    present = present,
                    proposed = proposed,
                    affiliation = affiliation,
                    vector = "HorizontalVertical",
                    state = state,
                    threat = threat
                )
            }
            val coordinate0 = proposed[0]
            val coordinate1 = proposed[1] - 1
            return columnLeft(
                present = present,
                proposed = arrayOf(coordinate0, coordinate1),
                affiliation = affiliation,
                state = state,
                threat = threat
            )
        }
        return false
    }

    fun columnRight(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        if (proposed[1] <= 7) {
            if (state[proposed[0]][proposed[1]] != null) {
                return evaluateAttackVector(
                    present = present,
                    proposed = proposed,
                    affiliation = affiliation,
                    vector = "HorizontalVertical",
                    state = state,
                    threat = threat
                )
            }
            val coordinate0 = proposed[0]
            val coordinate1 = proposed[1] + 1
            return columnRight(
                present = present,
                proposed = arrayOf(coordinate0, coordinate1),
                affiliation = affiliation,
                state = state,
                threat = threat
            )
        }
        return false
    }

    fun evaluateAttackVector(
        present: Array<Int>,
        proposed: Array<Int>,
        affiliation: String,
        vector: String,
        state: Array<Array<Piece?>>,
        threat: Boolean
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()
        val occupant: Piece = state[proposed[0]][proposed[1]]!!
        if (affiliation != occupant.affiliation) {
            val attackVectorList: List<String>? = occupant.attackList
            if(attackVectorList == null){
                return false
            }
            if(attackVectorList.isEmpty()){
                return false
            }
            //for attackVector in
            attackVectorList.forEach {
                if (vector.toLowerCase().contains(it.toLowerCase())) {
                    if (threat) {
                        return true
                    } else if (!movementKing.movement(present = present, proposed = proposed)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun evaluateAttackVector(
        coordinate: Array<Int>,
        affiliation: String,
        vector: String,
        state: Array<Array<Piece?>>
    ): Boolean {
        //val tschessElementMatrix = gamestate.getTschessElementMatrix()

        if (coordinate[0] >= 0 && coordinate[0] <= 7 && coordinate[1] >= 0 && coordinate[1] <= 7) {

            if (state[coordinate[0]][coordinate[1]] != null) {

                val occupant: Piece = state[coordinate[0]][coordinate[1]]!!

                if (affiliation != occupant.affiliation) {
                    val attackVectorList = occupant.attackList!!

                    attackVectorList.forEach {
                        if (vector.toLowerCase().contains(it.toLowerCase())) {
                            return true
                        }
                    }
                }

                //

                //for attackVector in attackVectorList {
                //if vector.contains(attackVector) {
                //return true
                //}
                //}
                //}
            }
        }
        return false
    }
}