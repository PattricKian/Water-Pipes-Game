package sk.stuba.fei.uim.oop.controls;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.board.Tile;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;



public class GameLogic extends UniversalAdapter {

    public static final int INITIAL_BOARD_SIZE = 8;
    private JFrame mainGame;
    private Board currentBoard;
    @Getter
    private JLabel label;
    @Getter
    private JLabel boardSizeLabel;
    private int currentBoardSize;
    private Tile highlightedTile = null;
    private int currentLevel = 1;



    private JButton buttonRestart;
    private JButton buttonCheck;


    public GameLogic(JFrame mainGame, JButton buttonRestart, JButton buttonCheck) {
        this.mainGame = mainGame;
        this.currentBoardSize = INITIAL_BOARD_SIZE;
        this.initializeNewBoard(this.currentBoardSize);
        this.mainGame.add(this.currentBoard);
        this.label = new JLabel();
        this.boardSizeLabel = new JLabel();
        this.updateLevelLabel();
        this.updateBoardSizeLabel();
        this.buttonRestart = buttonRestart;
        this.buttonCheck = buttonCheck;
    }

    private void updateLevelLabel() {
        this.label.setText("LEVEL: " + currentLevel  );
        this.mainGame.revalidate();
        this.mainGame.repaint();
    }

    private void updateBoardSizeLabel() {
        this.boardSizeLabel.setText("CURRENT BOARD SIZE: " + this.currentBoardSize);
        this.mainGame.revalidate();
        this.mainGame.repaint();
    }

    private void gameRestart(boolean resetLevel) {
        this.mainGame.remove(this.currentBoard);
        this.initializeNewBoard(this.currentBoardSize);
        this.mainGame.add(this.currentBoard);
        if (resetLevel) {
            this.currentLevel = 1;
        } else {
            this.currentLevel++;
        }
        this.updateLevelLabel();
    }

    private void initializeNewBoard(int dimension) {
        this.currentBoard = new Board(dimension);
        this.currentBoard.addMouseMotionListener(this);
        this.currentBoard.addMouseListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonRestart) {
            this.gameRestart(true);
            this.mainGame.revalidate();
            this.mainGame.repaint();
            this.mainGame.setFocusable(true);
            this.mainGame.requestFocus();
        } else if (e.getSource() == buttonCheck) {


            if (this.currentBoard.checkAdjacentPipeDirection()){

                JOptionPane.showMessageDialog(mainGame, "You win! Press OK to generate next level.", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                this.gameRestart(false);
                this.updateLevelLabel();

            }
        }


    }



    @Override
    public void mouseMoved(MouseEvent e) {
        Component current = this.currentBoard.getComponentAt(e.getX(), e.getY());
        if (!(current instanceof Tile)) {
            if (!this.currentBoard.getBounds().contains(e.getPoint())) {

                for (Component component : this.currentBoard.getComponents()) {
                    if (component instanceof Tile) {
                        ((Tile) component).setHighlight(false);
                    }
                }
                this.currentBoard.repaint();
            }
            return;
        }

        ((Tile) current).setHighlight(true);

        for (Component component : this.currentBoard.getComponents()) {
            if (component instanceof Tile && component != current) {
                ((Tile) component).setHighlight(false);
            }
        }

        this.currentBoard.repaint();
    }



    @Override
    public void mousePressed(MouseEvent e) {

        Component current = this.currentBoard.getComponentAt(e.getX(), e.getY());
        if (!(current instanceof Tile)) {
            return;
        }
        Tile clickedTile = (Tile) current;
        clickedTile.rotatePipe();


    }


    @Override
    public void stateChanged(ChangeEvent e) {
        this.currentBoardSize = ((JSlider) e.getSource()).getValue();
        this.updateBoardSizeLabel();
        this.gameRestart(true);
        this.mainGame.setFocusable(true);
        this.mainGame.requestFocus();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                this.gameRestart(true);
                break;
            case KeyEvent.VK_ENTER:
                if (this.currentBoard.checkAdjacentPipeDirection()){
                    JOptionPane.showMessageDialog(mainGame, "You win! Press OK to generate next level.", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                    this.gameRestart(false);
                    this.updateLevelLabel();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                this.mainGame.dispose();
        }
    }

}
