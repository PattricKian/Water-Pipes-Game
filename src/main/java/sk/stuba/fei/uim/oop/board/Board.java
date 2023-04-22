
//TODO : SPRAVIT PREVIOUSTILE ABY SOM UCHOVAL HODNOTU PREDOSLEJ DIRECTION



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
    private boolean visitedTile;

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
                    System.out.println("Start Pipe coordinates: (" + startPipe.getRow() + "," + startPipe.getCol() + ")");
                }

                if (i == right && j == endY) {
                    this.board[i][j].setState(State.PIPE_END);
                    this.endPipe = this.board[i][j];
                    System.out.println("End Pipe coordinates: (" + endPipe.getRow() + "," + endPipe.getCol() + ")");
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

                    tile.updateDirection();
                } else if (previousTile.getRow() == nextTile.getRow()) {
                    tile.setState(State.PIPE_HORIZONTAL);

                    tile.updateDirection();
                } else {

                    tile.setState(State.PIPE_CORNER);
                    tile.rotatePipeRandomly();
                    tile.updateDirection();

                }
                System.out.println("Tile coordinates" + tile.getRow() + tile.getCol() + "             TILE DIRECTION:" + tile.getDirection() + "      TILE TYPE:" + tile.getState());
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

    public boolean printAdjacentPipeDirection() {
        Tile currentPipe = startPipe;
        currentPipe.updateTileBackground(currentPipe);
        System.out.println("END PIPA" + endPipe.getRow() + endPipe.getCol());
        Tile adjacentPipe = null;
        end = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j].setVisitedTile(false);
            }
        }

        while (!end) {

            int row = currentPipe.getRow();
            int col = currentPipe.getCol();

            if (!currentPipe.isVisitedTile()) {
                currentPipe.setVisitedTile(true);
            } else {

                continue;
            }



            if (row > 0 && !board[row - 1][col].isVisitedTile() && board[row - 1][col].getState() != State.EMPTY) {
                adjacentPipe = board[row - 1][col];

                currentPipe.updateDirection();
                adjacentPipe.updateDirection();
                System.out.println("adjancetTILE:" + adjacentPipe.getRow() + adjacentPipe.getCol() + adjacentPipe.getDirection() + "CURRENTTILE: " + currentPipe.getDirection());
                if ((currentPipe.getDirection() == Direction.VERTICAL || currentPipe.getDirection() == Direction.UPLEFT || currentPipe.getDirection() == Direction.UPRIGHT) && (adjacentPipe.getDirection() == Direction.VERTICAL || adjacentPipe.getDirection() == Direction.DOWNRIGHT || adjacentPipe.getDirection() == Direction.DOWNLEFT)) {
                    System.out.println("POZERAM ABOVE");
                    currentPipe = adjacentPipe;
                    currentPipe.updateTileBackground(currentPipe);
                    System.out.println("PASUJEM ABOVE");

                } else return false;

            }

            else if (col > 0 && !board[row][col - 1].isVisitedTile() && board[row][col - 1].getState() != State.EMPTY) {
                adjacentPipe = board[row][col - 1];

                currentPipe.updateDirection();
                adjacentPipe.updateDirection();
                System.out.println("adjancetTILE:" + adjacentPipe.getRow() + adjacentPipe.getCol() + adjacentPipe.getDirection() + "CURRENTTILE: " + currentPipe.getDirection());

                if ((currentPipe.getDirection() == Direction.HORIZONTAL || currentPipe.getDirection() == Direction.DOWNLEFT || currentPipe.getDirection() == Direction.UPLEFT) && (adjacentPipe.getDirection() == Direction.HORIZONTAL || adjacentPipe.getDirection() == Direction.DOWNRIGHT || adjacentPipe.getDirection() == Direction.UPRIGHT)) {
                    System.out.println("POZERAM LEFT");
                    currentPipe = adjacentPipe;
                    currentPipe.updateTileBackground(currentPipe);
                    System.out.println("PASUJEM LEFT");
                } else return false;
            }


            else if (col < board[0].length - 1 && !board[row][col + 1].isVisitedTile() && board[row][col + 1].getState() != State.EMPTY) {
                adjacentPipe = board[row][col + 1];
                currentPipe.updateDirection();
                adjacentPipe.updateDirection();
                System.out.println("adjancetTILE:" + adjacentPipe.getRow() + adjacentPipe.getCol() + adjacentPipe.getDirection() + "CURRENTTILE: " + currentPipe.getDirection());
                if ((currentPipe.getDirection() == Direction.HORIZONTAL || currentPipe.getDirection() == Direction.DOWNRIGHT || currentPipe.getDirection() == Direction.UPRIGHT) && (adjacentPipe.getDirection() == Direction.HORIZONTAL || adjacentPipe.getDirection() == Direction.DOWNLEFT || adjacentPipe.getDirection() == Direction.UPLEFT)) {
                    System.out.println("POZERAM RIGHT");
                    currentPipe = adjacentPipe;
                    currentPipe.updateTileBackground(currentPipe);

                    System.out.println("PASUJEM RIGHT");
                } else return false;

            }





            else if (row < board.length - 1 && !board[row + 1][col].isVisitedTile() && board[row + 1][col].getState() != State.EMPTY) {
                adjacentPipe = board[row + 1][col];
                System.out.println("POZERAM BELOW");
                currentPipe.updateDirection();
                adjacentPipe.updateDirection();


                System.out.println("adjancetTILE:" + adjacentPipe.getRow() + adjacentPipe.getCol() + adjacentPipe.getDirection() + "CURRENTTILE: " + currentPipe.getDirection());
                if ((currentPipe.getDirection() == Direction.VERTICAL || currentPipe.getDirection() == Direction.DOWNLEFT || currentPipe.getDirection() == Direction.DOWNRIGHT) && (adjacentPipe.getDirection() == Direction.VERTICAL || adjacentPipe.getDirection() == Direction.UPRIGHT || adjacentPipe.getDirection() == Direction.UPLEFT)) {

                    currentPipe = adjacentPipe;
                    currentPipe.updateTileBackground(currentPipe);
                    System.out.println("PASUJEM BELOW");

                } else return false;


            }

            if (row == endPipe.getRow() && col == endPipe.getCol()) {
                end = true;
                System.out.println("U WIN");
                return true;
            }
        }

        return true;
    }


}

















   /* public boolean checkPipes() {
        Tile currentTile = startPipe;

        while (currentTile != endPipe) {
            int currentRow = currentTile.getRow();
            int currentCol = currentTile.getCol();
            currentTile.updateDirection();
            System.out.println("Tile coordinates NOVE" + currentRow + currentCol + "             TILE DIRECTION:" + currentTile.getDirection() + "      TILE TYPE:" + currentTile.getState() );

            Tile nextTile = path.get(path.indexOf(currentTile) + 1);
            int nextRow = nextTile.getRow();
            int nextCol = nextTile.getCol();
            nextTile.updateDirection();
            System.out.println("Tile coordinates NEXT" + nextRow + nextCol + "             TILE DIRECTION:" + nextTile.getDirection() + "      TILE TYPE:" + nextTile.getState() );
            Direction currentDirection = currentTile.getDirection();
            Direction nextDirection = nextTile.getDirection();

            switch (currentDirection) {
                case HORIZONTAL:
                    if (nextDirection == Direction.HORIZONTAL && nextCol > currentCol) {
                        break;
                    }
                    if (nextDirection == Direction.DOWNLEFT && nextRow > currentRow) {
                        break;
                    }
                    if (nextDirection == Direction.UPLEFT && nextRow < currentRow) {
                        break;
                    }
                    System.out.println("Incorrect connection at position (" + nextRow + "," + nextCol + ")");
                    return false;
                case VERTICAL:
                    if (nextDirection == Direction.VERTICAL && nextRow > currentRow) {
                        break;
                    }
                    if (nextDirection == Direction.UPLEFT && nextCol < currentCol) {
                        break;
                    }
                    if (nextDirection == Direction.UPRIGHT && nextCol > currentCol) {
                        break;
                    }
                    System.out.println("Incorrect connection at position (" + nextRow + "," + nextCol + ")");
                    return false;
                case UPLEFT:
                    if (nextDirection == Direction.HORIZONTAL && nextCol > currentCol) {
                        break;
                    }
                    if (nextDirection == Direction.UPLEFT && nextRow < currentRow) {
                        break;
                    }
                    if (nextDirection == Direction.UPRIGHT && nextRow > currentRow) {
                        break;
                    }
                    System.out.println("Incorrect connection at position (" + nextRow + "," + nextCol + ")");
                    return false;
                case UPRIGHT:
                    if (nextDirection == Direction.VERTICAL && nextRow > currentRow) {
                        break;
                    }
                    if (nextDirection == Direction.UPLEFT && nextCol > currentCol) {
                        break;
                    }
                    if (nextDirection == Direction.UPRIGHT && nextCol < currentCol) {
                        break;
                    }
                    System.out.println("Incorrect connection at position (" + nextRow + "," + nextCol + ")");
                    return false;
                case DOWNLEFT:
                    if (nextDirection == Direction.VERTICAL && nextRow < currentRow) {
                        break;
                    }
                    if (nextDirection == Direction.DOWNLEFT && nextCol > currentCol) {
                        break;
                    }
                    if (nextDirection == Direction.UPLEFT && nextCol < currentCol) {
                        break;
                    }
                    System.out.println("Incorrect connection at position (" + nextRow + "," + nextCol + ")");
                    return false;
                case DOWNRIGHT:
                    if (nextDirection == Direction.HORIZONTAL && nextCol < currentCol) {
                        break;
                    }
                    if (nextDirection == Direction.DOWNLEFT && nextRow > currentRow) {
                        break;
                    }
                    if (nextDirection == Direction.DOWNRIGHT && nextRow < currentRow) {
                        break;
                    }
                    System.out.println("Incorrect connection at position (" + nextRow + "," + nextCol + ")");
                    return false;
            }

            currentTile = nextTile;
        }

        return true;
    }*/