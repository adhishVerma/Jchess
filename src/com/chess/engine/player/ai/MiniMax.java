package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy{
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    public MiniMax(final int searchDepth){
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }
    @Override
    public String toString(){
        return "Minimax";
    }
    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getActivePlayer() + "THINKING with depth ="+this.searchDepth);

        int numMoves = board.getActivePlayer().getLegalMoves().size();
        for(final Move move : board.getActivePlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.getActivePlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                //we use transition board so we use min max accordingly
                currentValue = board.getActivePlayer().getAlliance().isWhite() ? min(moveTransition.getTransitionBoard(), this.searchDepth-1) :
                                                                                 max(moveTransition.getTransitionBoard(),this.searchDepth-1);
                if(board.getActivePlayer().getAlliance().isWhite() && currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                    bestMove = move;
                }else if(board.getActivePlayer().getAlliance().isBlack()&& currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long executionTime = System.currentTimeMillis()-startTime;
        return bestMove;
    }
    //    min and max are co recursive functions
    public int min(final Board board,
                   final int depth){
        if(depth == 0 || isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move : board.getActivePlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.getActivePlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBoard(), depth-1);
                if(currentValue < lowestSeenValue){
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }
    public int max(final Board board,
                   final int depth){
        if(depth == 0 || isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.getActivePlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.getActivePlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getTransitionBoard(), depth-1);
                if(currentValue > highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

    private boolean isEndGameScenario(final Board board) {
        return board.getActivePlayer().isInCheckMate() ||
                board.getActivePlayer().isInStaleMate();
    }

}
