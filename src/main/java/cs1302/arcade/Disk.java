package cs1302.arcade;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;

/**
 * A class that extends the {@code Circle} class, with extra functionality.
 *
 */

public class Disk extends Circle {

    private int row;
    private int col;
    private String color;

    /**
     * Creates a Disk with a variety of parameters.
     *
     * @param r the radius
     * @param f the Paint of this disk
     * @param row the row location of this disk
     * @param co the column location of this disk
     * @param color the color of this disk.
     */

    public Disk(double r, Paint f, int row, int co, String color) {
        super(r, f);
        this.row = row;
        this.col = col;
        this.color = color;
    }

    /**
     * This method returns the row of the current disk.
     *
     * @return the row the disk in currently in.
     */

    public int getRow() {
        return row;
    }


    /**
     * This method returns the columnof the current disk.
     *
     * @return the column the disk in currently in.
     */

    public int getCol() {
        return col;
    }



    /**
     * This method returns the color of the current disk.
     *
     * @return the color the disk currently is.
     */

    public String getColor() {
        return color;
    }


    /**
     * This method sets the color of the current disk.
     *
     * @param color the color to change the disk to
     */

    public void setColor(String color) {
        this.color = color;
    }
}
