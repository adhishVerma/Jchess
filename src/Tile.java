public abstract class Tile {

    int tileCoordinate;

    //Constructor
    Tile(int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile{
        EmptyTile(int coordinate){
            super(coordinate);
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
        Piece piece;

        OccupiedTile(int coordinate, Piece piece){
            super(coordinate);
            this.piece = piece;
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
