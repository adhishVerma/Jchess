package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

import java.util.*;

public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player activePlayer;
    private final Pawn enPassantPawn;
    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, blackStandardLegalMoves, whiteStandardLegalMoves);
        this.activePlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    public Tile getTile(final int tileCoordinate){
        return this.gameBoard.get(tileCoordinate);
    }
    private static List<Tile> createGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i< BoardUtils.NUM_TILES; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return List.of(tiles);
    }
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i=0; i < BoardUtils.NUM_TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i+1)% BoardUtils.NUM_TILES_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    private static Collection<Piece> calculateActivePieces(final List<Tile> board, final Alliance alliance){
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile: board){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(activePieces);
    }
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }
    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }
    public Collection<Piece> getAllPieces(){
        ArrayList<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(this.whitePieces);
        allPieces.addAll(this.blackPieces);
        return Collections.unmodifiableList(allPieces);
    }
    public Player getBlackPlayer(){
        return this.blackPlayer;
    }
    public Player getWhitePlayer(){
        return this.whitePlayer;
    }

    public Player getActivePlayer(){
        return this.activePlayer;
    }
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieceList){
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece:pieceList){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }
    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        //Black pieces
        builder.setPiece(new Rook(0,Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2,Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4,Alliance.BLACK));
        builder.setPiece(new Bishop(5,Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7,Alliance.BLACK));
        for (int i = 8; i<16; i++){
            builder.setPiece(new Pawn(i, Alliance.BLACK));
        }
        //White pieces
        for (int i = 48; i<56; i++){
            builder.setPiece(new Pawn(i, Alliance.WHITE));
        }
        builder.setPiece(new Rook(56,Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58,Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60,Alliance.WHITE));
        builder.setPiece(new Bishop(61,Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63,Alliance.WHITE));
        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        List<Move> moveList = new ArrayList<>();
        moveList.addAll(this.whitePlayer.getLegalMoves());
        moveList.addAll(this.blackPlayer.getLegalMoves());
        return Collections.unmodifiableList(moveList);
    }

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;
        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        public Board build(){
            return new Board(this);
        }

        public void setEnpassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
