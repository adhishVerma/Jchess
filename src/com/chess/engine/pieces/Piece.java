package com.chess.engine.pieces;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    protected  final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    Piece(final int piecePos, final Alliance pieceAlliance){
        this.piecePosition = piecePos;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public int getPiecePosition(){return this.piecePosition;}
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    public enum PieceType{
        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        ROOK("R"),
        QUEEN("Q"),
        KING("K");
        private final String pieceName;
        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }
    }
}
