package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationTile;
    private Move(final Board board, final Piece movedPiece, final int destinationTile){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationTile = destinationTile;
    }

    public static final class AttackingMove extends Move{
        final Piece attackedPiece;
        public AttackingMove(final Board board,
                      final Piece movedPiece,
                      final int destinationTile,
                             final Piece attackedPiece){
            super(board,movedPiece, destinationTile);
            this.attackedPiece = attackedPiece;
        }
    }
    public static final class MajorMove extends Move{


        public MajorMove(final Board board,
                  final Piece movedPiece,
                  final int destinationTile
                  ){
            super(board,movedPiece, destinationTile);

        }
    }
}