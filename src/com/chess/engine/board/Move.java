package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationTile;
    public static final Move NULL_MOVE = new NullMove();
    private Move(final Board board, final Piece movedPiece, final int destinationTile){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationTile = destinationTile;
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime*result + this.movedPiece.getPiecePosition();
        result = prime*result + this.movedPiece.hashCode();
        return result;
    }
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove  = (Move) other;
        return this.getDestinationTile() == otherMove.getDestinationTile() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }
    public int getDestinationTile(){
        return this.destinationTile;
    }
    public Piece getMovedPiece(){
        return this.movedPiece;
    }
    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }
    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

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

    public static class AttackingMove extends Move{
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

        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackingMove)){
                return false;
            }
            final AttackingMove otherAttackMove = (AttackingMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }
    public static final class MajorMove extends Move{
        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationTile){
            super(board,movedPiece, destinationTile);
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(Board board, Piece movedPiece, int destinationTile) {
            super(board, movedPiece, destinationTile);
        }
    }

    public static class PawnAttackMove extends AttackingMove {
        public PawnAttackMove(Board board, Piece movedPiece, int destinationTile, final Pawn attackedPawn) {
            super(board, movedPiece, destinationTile, attackedPawn);
        }
    }

    public static  final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationTile, final Pawn attackedPawn) {
            super(board, movedPiece, destinationTile, attackedPawn);
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(Board board, Piece movedPiece, int destinationTile) {
            super(board, movedPiece, destinationTile);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getActivePlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.getActivePlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnpassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getActivePlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move{
        public CastleMove(final Board board,
                         final Piece movedPiece,
                         final int destinationTile){
            super(board,movedPiece, destinationTile);
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(Board board, Piece movedPiece, int destinationTile) {
            super(board, movedPiece, destinationTile);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationTile) {
            super(board, movedPiece, destinationTile);
        }
    }
    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Null Move");
        }
    }

    public static class MoveFactory {
        private MoveFactory(){
            throw new RuntimeException("Not instantiable!");
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){
            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate &&
                move.getDestinationTile() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}