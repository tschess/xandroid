package io.tschess.tschess.model

import io.tschess.tschess.piece.Piece
import io.tschess.tschess.piece.chess.bishop.Bishop
import io.tschess.tschess.piece.chess.bishop.BishopBlack
import io.tschess.tschess.piece.chess.bishop.BishopWhite
import io.tschess.tschess.piece.chess.king.King
import io.tschess.tschess.piece.chess.king.KingBlack
import io.tschess.tschess.piece.chess.king.KingWhite
import io.tschess.tschess.piece.chess.knight.Knight
import io.tschess.tschess.piece.chess.knight.KnightBlack
import io.tschess.tschess.piece.chess.knight.KnightWhite
import io.tschess.tschess.piece.chess.pawn.Pawn
import io.tschess.tschess.piece.chess.pawn.PawnBlack
import io.tschess.tschess.piece.chess.pawn.PawnWhite
import io.tschess.tschess.piece.chess.queen.Queen
import io.tschess.tschess.piece.chess.queen.QueenBlack
import io.tschess.tschess.piece.chess.queen.QueenWhite
import io.tschess.tschess.piece.chess.rook.Rook
import io.tschess.tschess.piece.chess.rook.RookBlack
import io.tschess.tschess.piece.chess.rook.RookWhite
import io.tschess.tschess.piece.fairy.amazon.Amazon
import io.tschess.tschess.piece.fairy.amazon.AmazonBlack
import io.tschess.tschess.piece.fairy.amazon.AmazonWhite
import io.tschess.tschess.piece.fairy.hunter.Hunter
import io.tschess.tschess.piece.fairy.hunter.HunterBlack
import io.tschess.tschess.piece.fairy.hunter.HunterWhite
import io.tschess.tschess.piece.fairy.poison.*

class Render(val white: Boolean) {

    fun getPiece(name: String): Piece? {
        if(name.contains("Knight")){
            if(name.contains("White")){
                return KnightWhite()
            }
            return KnightBlack()
        }
        if(name.contains("Bishop")){
            if(name.contains("White")){
                return BishopWhite()
            }
            return BishopBlack()
        }
        if(name.contains("Queen")){
            if(name.contains("White")){
                return QueenWhite()
            }
            return QueenBlack()
        }
        if(name.contains("Rook")){
            val rook: Rook = if(name.contains("White")){
                RookWhite()
            } else {
                RookBlack()
            }
            if(!name.contains("_x")){
                rook.touched = true
            }
            return rook
        }
        if(name.contains("Pawn")){
            val pawn: Pawn = if(name.contains("White")){
                PawnWhite()
            } else {
                PawnBlack()
            }
            if(!name.contains("_x")){
                pawn.touched = true
            }
            return pawn
        }
        if(name.contains("King")){
            val king: King = if(name.contains("White")){
                KingWhite()
            } else {
                KingBlack()
            }
            if(!name.contains("_x")){
                king.touched = true
            }
            return king
        }
        if(name.contains("Poison")){
            val poison: Poison = if(name.contains("White")){
                PoisonWhite(white) //poison = PoisonWhite(white: self.white)
            } else {
                PoisonBlack(white) //poison = PoisonBlack(white: self.white)
            }
            if(!name.contains("_x")){
                poison.touched = true
            }
            return poison
        }
        if(name.contains("Hunter")){
            if(name.contains("White")){
                return HunterWhite()
            }
            return HunterBlack()
        }
        if(name.contains("Amazon")){
            if(name.contains("White")){
                return AmazonWhite()
            }
            return AmazonBlack()
        }
        if(name.contains("Reveal")){
            if(name.contains("White")){
                return RevealWhite()
            }
            return RevealBlack()
        }
        return null
    }

    fun getDefault(name: String): Piece? {
        if(name.toLowerCase().contains("knight")){
            return Knight()
        }
        if(name.toLowerCase().contains("bishop")){

            return Bishop()
        }
        if(name.toLowerCase().contains("queen")){

            return Queen()
        }
        if(name.toLowerCase().contains("rook")){

            return Rook()
        }
        if(name.toLowerCase().contains("pawn")){

            return Pawn()
        }
        if(name.toLowerCase().contains("king")){

            return King()
        }
        if(name.toLowerCase().contains("poison")){

            return Poison()
        }
        if(name.toLowerCase().contains("hunter")){

            return Hunter()
        }
        if(name.toLowerCase().contains("amazon")){

            return Amazon()
        }
        return null
    }
}