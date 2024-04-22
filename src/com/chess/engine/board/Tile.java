package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTilesMap = new HashMap<>();
        for(int i=0; i < BoardUtils.NUM_TILES; i++){
            emptyTilesMap.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTilesMap);
    }

    //Constructor
    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    public int getTileCoordinate(){
        return this.tileCoordinate;
    }
    public static Tile createTile(final int tileCoordinate, Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile{
        private EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public String toString(){
            return "-";
        }
        @Override
        public boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getPiece(){
            return null;
        }
    }

    public  static final class OccupiedTile extends Tile{
        private final Piece piece;

        private OccupiedTile(final int coordinate, final Piece piece){
            super(coordinate);
            this.piece = piece;
        }
        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }
        @Override
        public boolean isTileOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return this.piece;
        }
    }
}
