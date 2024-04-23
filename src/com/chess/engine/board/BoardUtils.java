package com.chess.engine.board;

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
}
