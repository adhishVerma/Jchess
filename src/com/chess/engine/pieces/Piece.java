package com.chess.engine.pieces;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    protected  final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;

    Piece(final int piecePos,
          final Alliance pieceAlliance,
          final PieceType pieceType){
        this.piecePosition = piecePos;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
        this.pieceType = pieceType;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public int getPiecePosition(){return this.piecePosition;}
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public  PieceType getPieceType(){
        return this.pieceType;
    };

    public abstract Piece movePiece(Move move);

    public enum PieceType{
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };
        private final String pieceName;
        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();
    }
}
