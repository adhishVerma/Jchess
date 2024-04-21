package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
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

    public int getDestinationTile(){
        return this.destinationTile;
    }
    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public abstract Board execute();

    public static final class AttackingMove extends Move{
        final Piece attackedPiece;
        public AttackingMove(final Board board,
                             final Piece movedPiece,
                             final int destinationTile,
                             final Piece attackedPiece){
            super(board,movedPiece, destinationTile);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute(){
            return null;
        }
    }
    public static final class MajorMove extends Move{
        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationTile){
            super(board,movedPiece, destinationTile);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getActivePlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getActivePlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            //Moving the piece happens here
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getActivePlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
}