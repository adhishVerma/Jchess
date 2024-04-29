package com.tests.chess.engine.board;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
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

    @Test
    public void testFoolsMate(){
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getActivePlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),BoardUtils.getCoordinateAtPosition("f3")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getTransitionBoard().getActivePlayer().makeMove(
                Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e7"),BoardUtils.getCoordinateAtPosition("e5"))
        );
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getTransitionBoard().getActivePlayer().makeMove(
                Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("g2"),BoardUtils.getCoordinateAtPosition("g4"))
        );
        assertTrue(t3.getMoveStatus().isDone());
        final MoveStrategy moveStrategy = new MiniMax(4);
        final Move aiMove = moveStrategy.execute(t3.getTransitionBoard());
        final Move bestMove = Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d8"),BoardUtils.getCoordinateAtPosition("h4"));
        assertEquals(aiMove, bestMove);
    }
}