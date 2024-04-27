package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationTile;
    protected final boolean isFirstMove;
    public static final Move NULL_MOVE = new NullMove();
    private Move(final Board board, final Piece movedPiece, final int destinationTile){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationTile = destinationTile;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    private Move(final Board board, final int destinationTile){
        this.board = board;
        this.destinationTile = destinationTile;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime*result + this.movedPiece.getPiecePosition();
        result = prime*result + this.movedPiece.hashCode();
        result = prime*result + this.movedPiece.getPiecePosition();
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
        return  getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationTile() == otherMove.getDestinationTile() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }
    public Board getBoard(){return  this.board;}
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

    public static class AttackMove extends Move{
        final Piece attackedPiece;
        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationTile,
                          final Piece attackedPiece){
            super(board,movedPiece, destinationTile);
            this.attackedPiece = attackedPiece;
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
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }
    public static class MajorAttackMove extends AttackMove {
        public MajorAttackMove(final Board board, final Piece movedPiece, final int destinationTile, final Piece attackedPiece){
            super(board, movedPiece, destinationTile, attackedPiece);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }
        @Override
        public String toString(){
            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationTile);
        }
    }
    public static final class MajorMove extends Move{
        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationTile){
            super(board,movedPiece, destinationTile);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString()+BoardUtils.getPositionAtCoordinate(this.getDestinationTile());
        }

    }

    public static final class PawnMove extends Move {
        public PawnMove(Board board, Piece movedPiece, int destinationTile) {
            super(board, movedPiece, destinationTile);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.getDestinationTile());
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(Board board, Piece movedPiece, int destinationTile, final Piece attackedPawn) {
            super(board, movedPiece, destinationTile, attackedPawn);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).charAt(0) + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationTile);
        }
    }
    public static final class PawnPromotion extends Move{
        final Move decoratedMove;
        final Pawn promotedPawn;
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationTile());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }
        @Override
        public Board execute(){
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Builder builder  = new Builder();
            for(final Piece piece : pawnMovedBoard.getActivePlayer().getActivePieces()){
                if(!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : pawnMovedBoard.getActivePlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.getActivePlayer().getAlliance());
            return builder.build();
        };
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.decoratedMove.getDestinationTile())+"="+"Q";
        };
        @Override
        public int hashCode(){
            return decoratedMove.hashCode() + (31*promotedPawn.hashCode());
        }
        @Override
        public boolean equals(final Object other){
            return  this == other || other instanceof PawnPromotion && super.equals(other);
        }
        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }
        @Override
        public Piece getAttackedPiece(){
            return this.decoratedMove.getAttackedPiece();
        }
    }
    public static  final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationTile, final Pawn attackedPawn) {
            super(board, movedPiece, destinationTile, attackedPawn);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
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
                if(!this.attackedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getActivePlayer().getOpponent().getAlliance());
            return  builder.build();
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(Board board, Piece movedPiece, int destinationTile) {
            super(board, movedPiece, destinationTile);
        }
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationTile);
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
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                         final Piece movedPiece,
                         final int destinationTile,
                         final Rook castleRook,
                         final int castleRookStart,
                         final int castleRookDestination){
            super(board,movedPiece, destinationTile);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        public Rook getCastleRook(){
            return this.castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }
        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime*result + this.castleRook.hashCode();
            result = prime*result + this.castleRookDestination;
            return result;
        }
        @Override
        public boolean equals(final Object other){
            if(this==other){
                return true;
            }
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece : this.board.getActivePlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !piece.equals(getCastleRook())){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.getActivePlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            //Move the king with move-piece method
            builder.setPiece(this.movedPiece.movePiece(this));
            //Create a new Rook on the board
            //TODO : first move change of the new rook.
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.getActivePlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(Board board,
                                  Piece movedPiece,
                                  int destinationTile,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinationTile, castleRook, castleRookStart, castleRookDestination);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }
        @Override
        public String toString(){
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(Board board,
                                   Piece movedPiece,
                                   int destinationTile,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinationTile, castleRook, castleRookStart, castleRookDestination);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }
        @Override
        public String toString(){
            return "0-0-0";
        }
    }
    public static final class NullMove extends Move {
        public NullMove() {
            super(null,-1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Null Move");
        }
        @Override
        public int getCurrentCoordinate(){
            return -1;
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