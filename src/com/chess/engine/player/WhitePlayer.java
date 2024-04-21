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

public class WhitePlayer extends Player{
    public WhitePlayer(final Board board, final Collection<Move> legalMoves, final Collection<Move> OpponentMoves) {
        super(board, legalMoves, OpponentMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }
    @Override
    public Alliance getAlliance(){
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //white king's king side castle
            if(!this.board.getTile(61).isTileOccupied() &&
               !this.board.getTile(62).isTileOccupied()){
                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isTileOccupied() &&
                   rookTile.getPiece().isFirstMove() &&
                   rookTile.getPiece().getPieceType().isRook()){
                    if(calculateAttacksOnTile(61, opponentLegalMoves).isEmpty() &&
                       calculateAttacksOnTile(62, opponentLegalMoves).isEmpty() ){
                        // TODO Add:king-side castle
                        kingCastles.add(null);
                    }
                }
            }
            //white king's queen side castle
            if(!this.board.getTile(57).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(59).isTileOccupied()){
                final Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() &&
                   rookTile.getPiece().isFirstMove()&&
                   rookTile.getPiece().getPieceType().isRook()){
                    if(calculateAttacksOnTile(57, opponentLegalMoves).isEmpty()&&
                       calculateAttacksOnTile(58, opponentLegalMoves).isEmpty()&&
                       calculateAttacksOnTile(59, opponentLegalMoves).isEmpty()){
                        // TODO Add:queen-side castle
                        kingCastles.add(null);
                    }
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }
}
