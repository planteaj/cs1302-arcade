package cs1302.arcade;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;

public class Disk extends Circle {

    private int row;
    private int col;
    private String color;

    public Disk(double r, Paint f, int row, int co, String color) {
        super(r, f);
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
