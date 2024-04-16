package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackingMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class King extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9};
    King(int piecePos, Alliance pieceAlliance) {
        super(piecePos, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final  Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE){
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
                || isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceALliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance != pieceALliance){
                        legalMoves.add(new AttackingMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }

        }

        return Collections.unmodifiableList(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (
                   (candidateOffset == -9)
                || (candidateOffset == -1)
                || (candidateOffset == 7));
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (
                   (candidateOffset == -7)
                || (candidateOffset == 1)
                || (candidateOffset == 9));
    }
}
