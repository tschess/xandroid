package io.tschess.tschess.piece.fairy.hunter

import io.tschess.tschess.R
import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.fairy.Fairy
import io.tschess.tschess.piece.logic.Diagonal

open class Hunter(
    name: String = "Hunter",
    imageDefault: Int = R.drawable.red_hunter,
    imageTarget: Int? = null,
    imageSelect: Int? = null
) : Fairy(
    name = name,
    imageDefault = imageDefault,
    imageTarget = imageTarget,
    imageSelect = imageSelect,
    strength = 5,
    attackList = listOf("Hunter"),
    describe = "• moves forward as a bishop (diagonal).${System.lineSeparator()}" +
            "• moves backward as a knight (two by one).${System.lineSeparator()}"
) {

    fun check(present: Array<Int>, propose: Array<Int>, matrix: Array<Array<Piece?>>):  Boolean {
        if(Diagonal().plusPlus(present = present, proposed = propose, state = matrix)){
            if(matrix[propose[0]][propose[1]] == null){
                return true
            } else {
                return matrix[present[0]][present[1]]!!.affiliation !=
                        matrix[propose[0]][propose[1]]!!.affiliation
            }
        }
        if(Diagonal().plusMinus(present = present, proposed = propose, state = matrix)){
            if(matrix[propose[0]][propose[1]] == null){
                return true
            } else {
                return matrix[present[0]][present[1]]!!.affiliation !=
                        matrix[propose[0]][propose[1]]!!.affiliation
            }
        }
        // backwards knight moves...
        if(minusTwo_minusOne(present = present, proposed = propose, state = matrix)){
            return true
        }
        if(minusTwo_plusOne(present = present, proposed = propose, state = matrix)){
            return true
        }
        if(minusOne_minusTwo(present = present, proposed = propose, state = matrix)){
            return true
        }
        if(minusOne_plusTwo(present = present, proposed = propose, state = matrix)){
            return true
        }
        return false
    }

    override fun validate(present: Array<Int>, proposed: Array<Int>, matrix: Array<Array<Piece?>>): Boolean {

        // forward bishop moves...

        if(Diagonal().minusPlus(present = present, proposed = proposed, state = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null){
                return true
            } else {
                return matrix[present[0]][present[1]]!!.affiliation !=
                        matrix[proposed[0]][proposed[1]]!!.affiliation
            }
        }
        if(Diagonal().minusMinus(present = present, proposed = proposed, state = matrix)){
            if(matrix[proposed[0]][proposed[1]] == null){
                return true
            } else {
                return matrix[present[0]][present[1]]!!.affiliation !=
                        matrix[proposed[0]][proposed[1]]!!.affiliation
            }
        }
        // backwards knight moves...
        if(plusTwo_minusOne(present = present, proposed = proposed, state = matrix)){
            return true
        }
        if(plusTwo_plusOne(present = present, proposed = proposed, state = matrix)){
            return true
        }
        if(plusOne_minusTwo(present = present, proposed = proposed, state = matrix)){
            return true
        }
        if(plusOne_plusTwo(present = present, proposed = proposed, state = matrix)){
            return true
        }


        return false
    }

    fun plusTwo_minusOne(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if((present[0] + 2) - proposed[0] == 0 && (present[1] - 1) - proposed[1] == 0){
            if(state[present[0] + 2][present[1] - 1] != null) {
                return state[present[0] + 2][present[1] - 1]!!.affiliation !=
                        state[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    fun plusTwo_plusOne(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if((present[0] + 2) - proposed[0] == 0 && (present[1] + 1) - proposed[1] == 0){
            if(state[present[0] + 2][present[1] + 1] != null) {
                return state[present[0] + 2][present[1] + 1]!!.affiliation !=
                        state[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    fun plusOne_minusTwo(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if((present[0] + 1) - proposed[0] == 0 && (present[1] - 2) - proposed[1] == 0){
            if(state[present[0] + 1][present[1] - 2] != null) {
                return state[present[0] + 1][present[1] - 2]!!.affiliation !=
                        state[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }


    fun plusOne_plusTwo(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if((present[0] + 1) - proposed[0] == 0 && (present[1] + 2) - proposed[1] == 0){
            if(state[present[0] + 1][present[1] + 2] != null) {
                return state[present[0] + 1][present[1] + 2]!!.affiliation !=
                        state[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    //    // minusTwo_minusOne
    //    if (coordinate[0] - 2 >= 0 && coordinate[1] - 1 >= 0) {
    //        if(evaluateName(attacker: knight, coordinate: [coordinate[0] - 2, coordinate[1] - 1], affiliation: affiliation, gamestate: gamestate)){
    //            return true;
    //        }
    //    }
    fun minusTwo_minusOne(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if((present[0] - 2) - proposed[0] == 0 && (present[1] - 1) - proposed[1] == 0){
            if(state[present[0] - 2][present[1] - 1] != null) {
                return state[present[0] - 2][present[1] - 1]!!.affiliation !=
                        state[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    //    // minusTwo_plusOne
    //    if (coordinate[0] - 2 >= 0 && coordinate[1] + 1 >= 0) {
    //        if(evaluateName(attacker: knight, coordinate: [coordinate[0] - 2, coordinate[1] - 1], affiliation: affiliation, gamestate: gamestate)){
    //            return true;
    //        }
    //    }
    fun minusTwo_plusOne(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if((present[0] - 2) - proposed[0] == 0 && (present[1] + 1) - proposed[1] == 0){
            if(state[present[0] - 2][present[1] + 1] != null) {
                return state[present[0] - 2][present[1] + 1]!!.affiliation !=
                        state[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    //    // minusOne_minusTwo
    //    if (coordinate[0] - 1 >= 0 && coordinate[1] - 2 >= 0) {
    //        if(evaluateName(attacker: knight, coordinate: [coordinate[0] - 2, coordinate[1] - 1], affiliation: affiliation, gamestate: gamestate)){
    //            return true;
    //        }
    //    }
    fun minusOne_minusTwo(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if((present[0] - 1) - proposed[0] == 0 && (present[1] - 2) - proposed[1] == 0){
            if(state[present[0] - 1][present[1] - 2] != null) {
                return state[present[0] - 1][present[1] - 2]!!.affiliation !=
                        state[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

    fun minusOne_plusTwo(present: Array<Int>, proposed: Array<Int>, state: Array<Array<Piece?>>): Boolean {
        //let tschessElementMatrix = gamestate.getTschessElementMatrix()
        if((present[0] - 1) - proposed[0] == 0 && (present[1] + 2) - proposed[1] == 0){
            if(state[present[0] - 1][present[1] + 2] != null) {
                return state[present[0] - 1][present[1] + 2]!!.affiliation !=
                        state[present[0]][present[1]]!!.affiliation
            }
            return true
        }
        return false
    }

}