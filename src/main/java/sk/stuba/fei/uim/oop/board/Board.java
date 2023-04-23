


package sk.stuba.fei.uim.oop.board;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class Board extends JPanel {

    private Tile[][] board;
    private Tile startPipe;
    private Tile endPipe;
    private boolean[][] visited;
    private Stack<Tile> path;
    private boolean end;


    public Board(int dimension) {

        this.initializeBoard(dimension);
        this.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        this.setBackground(Color.LIGHT_GRAY);
        this.generatePath();


    }

    private void initializeBoard(int dimension) {
        this.board = new Tile[dimension][dimension];
        this.visited = new boolean[dimension][dimension];
        this.path = new Stack<>();
        this.setLayout(new GridLayout(dimension, dimension));


        int top = 0;
        int bottom = dimension - 1;
        int left = 0;
        int right = dimension - 1;


        int startY = (int) (Math.random() * dimension);
        int endY = (int) (Math.random() * dimension);


        if (startY == endY) {
            if (startY == top) {
                endY = bottom;
            } else {
                endY = top;
            }
        }


        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.board[i][j] = new Tile(i, j);
                this.add(this.board[i][j]);

                if (i == left && j == startY) {
                    this.board[i][j].setState(State.PIPE_START);
                    this.startPipe = this.board[i][j];

                }

                if (i == right && j == endY) {
                    this.board[i][j].setState(State.PIPE_END);
                    this.endPipe = this.board[i][j];

                }
            }
        }

    }


    private void generatePath() {

        visited = new boolean[board.length][board[0].length];


        Tile currentTile = startPipe;
        visited[currentTile.getRow()][currentTile.getCol()] = true;

        path = new Stack<>();
        path.push(currentTile);


        while (!path.isEmpty() && currentTile != endPipe) {

            ArrayList<Tile> neighbors = getUnvisitedNeighbors(currentTile);
            if (neighbors.isEmpty()) {

                path.pop();
                if (!path.isEmpty()) {
                    currentTile = path.peek();
                }
            } else {

                Tile nextTile = neighbors.get((int) (Math.random() * neighbors.size()));
                visited[nextTile.getRow()][nextTile.getCol()] = true;
                path.push(nextTile);
                currentTile = nextTile;
            }
        }


        for (Tile tile : path) {
            if (tile != startPipe && tile != endPipe) {
                Tile previousTile = path.get(path.indexOf(tile) - 1);
                Tile nextTile = path.get(path.indexOf(tile) + 1);
                if (previousTile.getCol() == nextTile.getCol()) {
                    tile.setState(State.PIPE_VERTICAL);
                    tile.rotatePipeRandomly();
                    tile.updateDirection();
                } else if (previousTile.getRow() == nextTile.getRow()) {
                    tile.setState(State.PIPE_HORIZONTAL);
                    tile.rotatePipeRandomly();
                    tile.updateDirection();
                } else {
                    tile.setState(State.PIPE_CORNER);
                    tile.rotatePipeRandomly();
                    tile.updateDirection();

                }

            }

        }
    }

    private ArrayList<Tile> getUnvisitedNeighbors(Tile tile) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        int row = tile.getRow();
        int col = tile.getCol();
        if (row > 0 && !visited[row - 1][col]) {
            neighbors.add(board[row - 1][col]);
        }
        if (row < board.length - 1 && !visited[row + 1][col]) {
            neighbors.add(board[row + 1][col]);
        }
        if (col > 0 && !visited[row][col - 1]) {
            neighbors.add(board[row][col - 1]);
        }
        if (col < board[0].length - 1 && !visited[row][col + 1]) {
            neighbors.add(board[row][col + 1]);
        }
        return neighbors;
    }

    public boolean checkAdjacentPipeDirection() {
        Tile currentPipe = startPipe;
        currentPipe.updateTileBackground(currentPipe);
        Tile adjacentPipe;
        Tile previousPipe;
        end = false;


        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                resetTileBackground(tile);
                currentPipe.updateTileBackground(currentPipe);
                tile.updateDirection();
                tile.setVisitedTile(false);
            }
        }


        boolean matchFound = false;
        while (!end) {

            int row = currentPipe.getRow();
            int col = currentPipe.getCol();


            if (!currentPipe.isVisitedTile()) {
                currentPipe.setVisitedTile(true);


            } else {

                return false;
            }


            if (row > 0 && !board[row - 1][col].isVisitedTile() && board[row - 1][col].getState() != State.EMPTY && (board[row - 1][col].getDirection() != Direction.HORIZONTAL && board[row - 1][col].getDirection() != Direction.UPLEFT && board[row - 1][col].getDirection() != Direction.UPRIGHT)) {
                adjacentPipe = board[row - 1][col];
                previousPipe = currentPipe;
                previousPipe.updateDirection();
                currentPipe.updateDirection();
                adjacentPipe.updateDirection();

                if ((currentPipe.getDirection() == Direction.VERTICAL || currentPipe.getDirection() == Direction.UPLEFT || currentPipe.getDirection() == Direction.UPRIGHT) && (adjacentPipe.getDirection() == Direction.VERTICAL || adjacentPipe.getDirection() == Direction.DOWNRIGHT || adjacentPipe.getDirection() == Direction.DOWNLEFT)) {
                    currentPipe = adjacentPipe;
                    currentPipe.updateTileBackground(currentPipe);
                    matchFound = true;
                } else {
                    continue;
                }

            } else if (col > 0 && !board[row][col - 1].isVisitedTile() && board[row][col - 1].getState() != State.EMPTY && (board[row][col - 1].getDirection() != Direction.VERTICAL && board[row][col - 1].getDirection() != Direction.UPLEFT && board[row][col - 1].getDirection() != Direction.DOWNLEFT)) {
                adjacentPipe = board[row][col - 1];
                previousPipe = currentPipe;
                previousPipe.updateDirection();
                currentPipe.updateDirection();
                adjacentPipe.updateDirection();
                if ((currentPipe.getDirection() == Direction.HORIZONTAL || currentPipe.getDirection() == Direction.DOWNLEFT || currentPipe.getDirection() == Direction.UPLEFT) && (adjacentPipe.getDirection() == Direction.HORIZONTAL || adjacentPipe.getDirection() == Direction.DOWNRIGHT || adjacentPipe.getDirection() == Direction.UPRIGHT)) {
                    currentPipe = adjacentPipe;
                    currentPipe.updateTileBackground(currentPipe);
                    matchFound = true;
                } else {
                    continue;
                }
            } else if (col < board[0].length - 1 && !board[row][col + 1].isVisitedTile() && board[row][col + 1].getState() != State.EMPTY && !(board[row][col + 1].getDirection() == Direction.VERTICAL || board[row][col + 1].getDirection() == Direction.UPRIGHT || board[row][col + 1].getDirection() == Direction.DOWNRIGHT)) {
                board[row][col + 1].updateDirection();
                adjacentPipe = board[row][col + 1];
                previousPipe = currentPipe;
                previousPipe.updateDirection();
                currentPipe.updateDirection();
                adjacentPipe.updateDirection();

                if ((currentPipe.getDirection() == Direction.HORIZONTAL || currentPipe.getDirection() == Direction.DOWNRIGHT || currentPipe.getDirection() == Direction.UPRIGHT) && (adjacentPipe.getDirection() == Direction.HORIZONTAL || adjacentPipe.getDirection() == Direction.DOWNLEFT || adjacentPipe.getDirection() == Direction.UPLEFT)) {
                    currentPipe = adjacentPipe;
                    currentPipe.updateTileBackground(currentPipe);
                    matchFound = true;
                } else {
                    continue;
                }

            } else if (row < board.length - 1 && !board[row + 1][col].isVisitedTile() && board[row + 1][col].getState() != State.EMPTY) {
                adjacentPipe = board[row + 1][col];
                previousPipe = currentPipe;
                previousPipe.updateDirection();
                currentPipe.updateDirection();
                adjacentPipe.updateDirection();

                if ((currentPipe.getDirection() == Direction.VERTICAL || currentPipe.getDirection() == Direction.DOWNLEFT || currentPipe.getDirection() == Direction.DOWNRIGHT) && (adjacentPipe.getDirection() == Direction.VERTICAL || adjacentPipe.getDirection() == Direction.UPRIGHT || adjacentPipe.getDirection() == Direction.UPLEFT)) {
                    currentPipe = adjacentPipe;
                    currentPipe.updateTileBackground(currentPipe);
                    matchFound = true;

                } else {
                    continue;
                }


            }

            if (!matchFound) {
                return false;
            }

            if (row == endPipe.getRow() && col == endPipe.getCol()) {
                end = true;
                return true;
            }

        }

        return false;
    }



    public void resetTileBackground(Tile tile) {
        tile.setBackground(null);
        repaint();
    }


}
















