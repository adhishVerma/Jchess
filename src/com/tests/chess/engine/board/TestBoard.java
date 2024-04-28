package com.tests.chess.engine.board;

import com.chess.engine.board.Board;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestBoard {
    @Test
    public void initialBoard() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.getActivePlayer().getLegalMoves().size(), 20);
        assertEquals(board.getActivePlayer().getActivePieces().size(), 16);
        assertEquals(board.getActivePlayer().getOpponent().getLegalMoves().size(), 20);
        assertEquals(board.getActivePlayer().getOpponent().getActivePieces().size(), 16);
        assertFalse(board.getActivePlayer().isInCheck());
        assertFalse(board.getActivePlayer().isInCheckMate());
        assertFalse(board.getActivePlayer().isCastled());
//        assertTrue(board.getActivePlayer().isKingSideCastleCapable());
//        assertTrue(board.getActivePlayer().isQueenSideCastleCapable());
        assertEquals(board.getActivePlayer(), board.getWhitePlayer());
        assertEquals(board.getActivePlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getActivePlayer().getOpponent().isInCheck());
        assertFalse(board.getActivePlayer().getOpponent().isInCheckMate());
        assertFalse(board.getActivePlayer().getOpponent().isCastled());
//        assertTrue(board.getActivePlayer().getOpponent().isKingSideCastleCapable());
//        assertTrue(board.getActivePlayer().getOpponent().isQueenSideCastleCapable());
        assertTrue(board.getWhitePlayer().getAlliance().toString().equals("WHITE"));
        assertTrue(board.getBlackPlayer().getAlliance().toString().equals("BLACK"));

//        final Iterable<Piece> allPieces = board.getAllPieces();
//        final Iterable<Move> allMoves = Iterables.concat(board.getWhitePlayer().getLegalMoves(), board.getBlackPlayer().getLegalMoves());
//        for(final Move move : allMoves) {
//            assertFalse(move.isAttack());
//            assertFalse(move.isCastlingMove());
//            assertEquals(MoveUtils.exchangeScore(move), 1);
//        }

//        assertEquals(Iterables.size(allMoves), 40);
//        assertEquals(Iterables.size(allPieces), 32);
//        assertFalse(BoardUtils.isEndGame(board));
//        assertFalse(BoardUtils.isThreatenedBoardImmediate(board));
//        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
//        assertEquals(board.getPiece(35), null);
    }
}