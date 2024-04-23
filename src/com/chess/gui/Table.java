package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveStatus;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;


public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,400);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    public Table(){
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }
    private JMenuBar populateMenuBar(){
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }
    private JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("open");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("open pgn");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i< BoardUtils.NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }

    private class TilePanel extends JPanel{
        private final int tileId;
        private final Color lightTileColor = new Color(227,193,144);
        private final Color darkTileColor = new Color(166,121,78);
        private static String pieceIconPath = "art/simple/";
        TilePanel (final BoardPanel boardPanel,
                   final int tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            validate();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent mouseEvent) {
                    if(isRightMouseButton(mouseEvent)){
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }else if(isLeftMouseButton(mouseEvent)){
                        if(sourceTile == null){
                            //first click
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null){
                                sourceTile = null;
                            }
                        }else{
                            //second click
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = null;
                            final MoveTransition moveTransition = chessBoard.getActivePlayer().makeMove(move);
                            if(moveTransition.getMoveStatus().isDone()){
//                                chessBoard = move.execute();
                            }
                        }
                    }
                }

                @Override
                public void mousePressed(final MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(final MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(final MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(final MouseEvent mouseEvent) {

                }
            });
        }
        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){

                try {
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath+board.getTile(this.tileId).getPiece().getPieceAlliance().toString().charAt(0) +
                            board.getTile(this.tileId).getPiece().getPieceType().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                      e.printStackTrace();
//                    throw new RuntimeException(e);
                }
            }
        }
        private void assignTileColor() {
            if(BoardUtils.FIRST_ROW[this.tileId] ||
               BoardUtils.THIRD_ROW[this.tileId] ||
               BoardUtils.FIFTH_ROW[this.tileId] ||
               BoardUtils.SEVENTH_ROW[this.tileId]){
                setBackground(this.tileId%2 == 0 ? lightTileColor : darkTileColor);
            }else if(BoardUtils.SECOND_ROW[this.tileId] ||
                     BoardUtils.FOURTH_ROW[this.tileId] ||
                     BoardUtils.SIXTH_ROW[this.tileId]  ||
                     BoardUtils.EIGHTH_ROW[this.tileId]){
                setBackground(this.tileId%2 != 0 ? lightTileColor : darkTileColor);
            }
        }
    }
}
