package sk.stuba.fei.uim.oop.board;


import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Tile extends JPanel {

    @Getter
    @Setter
    private State state;
    @Setter
    private boolean highlight;
    @Getter
    @Setter
    private boolean visitedTile;
    @Getter
    @Setter
    private Direction direction;
    @Getter
    @Setter




    private int row;
    private int col;
    private int rotationAngle = 0;

    public Tile(int row, int col) {

        this.state = State.EMPTY;

        this.row = row;
        this.col = col;

        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(Color.lightGray);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int componentWidth = getWidth();
        int componentHeight = getHeight();
        int x = componentWidth / 2;
        int y = componentHeight / 2;

        g2d.setStroke(new BasicStroke(7));

        setHighlight(highlight);

        switch (this.state) {
            case PIPE_START:

                g.setColor(Color.GRAY);
                int circleDiameter = Math.min(componentWidth, componentHeight) - 20;
                int circleX = (componentWidth - circleDiameter) / 2;
                int circleY = (componentHeight - circleDiameter) / 2;
                g2d.drawOval(circleX, circleY, circleDiameter, circleDiameter);


                g.setColor(Color.BLUE);
                g2d.rotate(Math.toRadians(this.rotationAngle), x, y);
                g2d.setStroke(new BasicStroke(20));
                g2d.drawLine(x, -circleDiameter / 2 + 10, x, circleDiameter / 2 - 10);
                g2d.rotate(-Math.toRadians(this.rotationAngle), x, y);
                this.direction = Direction.VERTICAL;
                break;


            case PIPE_END:
                g.setColor(Color.GRAY);
                circleDiameter = Math.min(componentWidth, componentHeight) - 20;
                circleX = (componentWidth - circleDiameter) / 2;
                circleY = (componentHeight - circleDiameter) / 2;
                g2d.drawOval(circleX, circleY, circleDiameter, circleDiameter);


                g.setColor(Color.GREEN);
                g2d.rotate(Math.toRadians(this.rotationAngle), x, y);
                g2d.setStroke(new BasicStroke(20));
                g2d.drawLine(x, -circleDiameter / 2 + 10, x, circleDiameter / 2 - 10);
                g2d.rotate(-Math.toRadians(this.rotationAngle), x, y);
                this.direction = Direction.VERTICAL;
                break;

            case PIPE_HORIZONTAL:
                g.setColor(Color.DARK_GRAY);
                g2d.rotate(Math.toRadians(this.rotationAngle), x, y);
                g2d.setStroke(new BasicStroke(20));
                g2d.drawLine(0, y, componentWidth, y);
                g2d.rotate(-Math.toRadians(this.rotationAngle), x, y);
                this.direction = Direction.HORIZONTAL;
                break;

            case PIPE_VERTICAL:
                g.setColor(Color.DARK_GRAY);
                g2d.rotate(Math.toRadians(this.rotationAngle), x, y);
                g2d.setStroke(new BasicStroke(20));
                g2d.drawLine(x, 0, x, componentHeight);

                g2d.rotate(-Math.toRadians(this.rotationAngle), x, y);

                this.setDirection(Direction.VERTICAL);
                break;

            case PIPE_CORNER:
                g.setColor(Color.DARK_GRAY);
                g2d.rotate(Math.toRadians(this.rotationAngle), x, y);
                g2d.setStroke(new BasicStroke(20));
                g2d.drawLine(0, y, x, y);
                g2d.drawLine(x, y, x, componentHeight);
                g2d.rotate(-Math.toRadians(this.rotationAngle), x, y);
                this.setDirection(Direction.DOWNLEFT);
                break;
        }

    }


    public void rotatePipe() {
        this.rotationAngle += 90;
        if (this.rotationAngle == 360)
            this.rotationAngle = 0;

        updateDirection();
        repaint();
    }

    public void rotatePipeRandomly() {
        Random random = new Random();
        int randomNum = random.nextInt(2);

        if (randomNum == 0) {
            this.rotationAngle += 90;
            if (this.rotationAngle == 360)
                this.rotationAngle = 0;
                repaint();

        }

    }




    public void updateDirection() {
        switch (state) {
            case PIPE_CORNER:
                switch (rotationAngle) {
                    case 0:
                        this.setDirection(Direction.DOWNLEFT);
                        break;
                    case 90:
                        this.setDirection(Direction.UPLEFT);
                        break;
                    case 180:
                        this.setDirection(Direction.UPRIGHT);
                        break;
                    case 270:
                        this.setDirection(Direction.DOWNRIGHT);
                        break;
                }
                break;
            case PIPE_HORIZONTAL:
                switch (rotationAngle) {
                    case 0:
                    case 180:
                        this.setDirection(Direction.HORIZONTAL);
                        break;
                    case 90:
                    case 270:
                        this.setDirection(Direction.VERTICAL);
                        break;
                }
                break;
            case PIPE_VERTICAL:
                switch (rotationAngle) {
                    case 0:
                    case 180:
                        this.setDirection(Direction.VERTICAL);
                        break;
                    case 90:
                    case 270:
                        this.setDirection(Direction.HORIZONTAL);
                        break;
                }
            case PIPE_START:
                switch (rotationAngle) {
                    case 0:
                    case 180:
                        this.setDirection(Direction.VERTICAL);
                        break;
                    case 90:
                    case 270:
                        this.setDirection(Direction.HORIZONTAL);
                        break;
                }
            case PIPE_END:
                switch (rotationAngle) {
                    case 0:
                    case 180:
                        this.setDirection(Direction.VERTICAL);
                        break;
                    case 90:
                    case 270:
                        this.setDirection(Direction.HORIZONTAL);
                        break;
                }
                break;

        }
    }



    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        if (highlight) {
            this.setBorder(BorderFactory.createLineBorder(Color.red, 3));
        } else {
            this.setBorder(BorderFactory.createLineBorder(Color.black));

            this.highlight = false;
        }
    }






    public void updateTileBackground(Tile tile) {
        switch (tile.getState()) {
            case PIPE_START :
            case PIPE_END  :
            case PIPE_CORNER:
            case PIPE_VERTICAL:
            case PIPE_HORIZONTAL:
                tile.setBackground(Color.GREEN);
                break;

            default:
                break;
        }

        repaint();
    }










}





