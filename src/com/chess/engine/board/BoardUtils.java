package com.chess.engine.board;

import java.util.*;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] FIRST_ROW = initRow(1);
    public static final boolean[] SECOND_ROW = initRow(2);
    public static final boolean[] THIRD_ROW = initRow(3);
    public static final boolean[] FOURTH_ROW = initRow(4);
    public static final boolean[] FIFTH_ROW = initRow(5);
    public static final boolean[] SIXTH_ROW = initRow(6);
    public static final boolean[] SEVENTH_ROW = initRow(7);
    public static final boolean[] EIGHTH_ROW = initRow(8);

    public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = intializePositionToCoordinateMap();
    public static final int START_TILE_INDEX = 0;

    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_ROW=8;

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_ROW;
        }while(columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[] initRow(final int rowNumber){
        final boolean[] row = new boolean[NUM_TILES];
        for(int i = (rowNumber-1)*8; i < (rowNumber*8); i++){
            row[i] = true;
        }
        return row;
    }

    private BoardUtils(){
        throw new RuntimeException("You cant instantiate board-utils!");
    }

    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < 64;
    }

    public static int getCoordinateAtPosition(final String position){
        return POSITION_TO_COORDINATE.get(position);
    }
    public static String getPositionAtCoordinate(final int tileCoordinate){
        return ALGEBRAIC_NOTATION.get(tileCoordinate);
    }
    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }
    private static Map<String, Integer> intializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }
}
