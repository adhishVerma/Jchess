package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board, final Collection<Move> legalMoves, final Collection<Move> OpponentMoves) {
        super(board,legalMoves, OpponentMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }
    @Override
    public Alliance getAlliance(){
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }
    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //Black king's king side castle
            if(!this.board.getTile(5).isTileOccupied() &&
                    !this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() &&
                        rookTile.getPiece().isFirstMove() &&
                        rookTile.getPiece().getPieceType().isRook()){
                    if(calculateAttacksOnTile(5, opponentLegalMoves).isEmpty() &&
                            calculateAttacksOnTile(6, opponentLegalMoves).isEmpty() ){
                        // TODO Add:king-side castle
                        kingCastles.add(null);
                    }
                }
            }
            //Black king's queen side castle
            if(!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()){
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() &&
                        rookTile.getPiece().isFirstMove() &&
                        rookTile.getPiece().getPieceType().isRook()){
                    if(calculateAttacksOnTile(1, opponentLegalMoves).isEmpty() &&
                            calculateAttacksOnTile(2, opponentLegalMoves).isEmpty() &&
                            calculateAttacksOnTile(3, opponentLegalMoves).isEmpty()){
                        // TODO Add:queen-side castle
                        kingCastles.add(null);
                    }
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }
}
