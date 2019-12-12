package cs1302.arcade;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

/**
 * This Class creates an object with a size, position, and image.
 * We can use this to store and move around images and compare images with ease.
 *
 */
public class Sprite {
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    /**
     * This is the Constructor for the Sprite class.
     * Saves a x and y position
     *
     */

    public Sprite() {
        positionX = 0;
        positionY = 0;
    }

    /**
     *  This method sets the image for a specific sprite, using an Image Object.
     * @param i an {@code Image} object.
     */

    public void setImage(Image i) {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }


    /**
     * This method sets an image for a specifc sprite using a string url.
     * @param filename A string with the location of an image.
     */

    public void setImage(String filename) {
        Image i = new Image(filename);
        setImage(i);
    }

    /**
     * This Method sets an image for a specific sprite using a string url,
and sets the size of the sprite as well.
     * @param filename a string with the location of an image
     * @param width the width of the sprite
     * @param height the height of the sprite
     */

    public void setImage(String filename, int width, int height) {
        Image i = new Image(filename, width, height, false, false);
        setImage(i);
    }

    /**
     * This method sets the x position of the specific sprite.
     *
     * @param x the x position of the sprite
     */

    public void setX(double x) {
        positionX = x;
    }

    /**
     * This method sets the y position of the sprite.
     *
     * @param y the y position of the sprite
     */

    public void setY(double y) {
        positionY = y;
    }

    /**
     * This method returns the x position of the sprite.
     *
     * @return the current x position of the sprite
     */

    public double getX() {
        return positionX;
    }

    /**
     * This method returns the y position of the sprite.
     *
     * @return the current y position of the sprite
     */

    public double getY() {
        return positionY;
    }


    /**
     * This method draws the image onto the current canvas.
     *
     * @param gc the GraphicsContext of the canvas.
     */

    public void render(GraphicsContext gc) {
        gc.drawImage( image, positionX, positionY );
    }

    /**
     * This method gets the boundary of the sprite.
     *
     * @return a Rectangle2D with position and location of the sprite
     */

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    /**
     * This method checks to see if two sprites are interescting.
     *
     * @param s the sprite we are checking for intersections
     * @return a boolean value for whether the sprites intersect or not
     */

    public boolean intersects(Sprite s) {
        return s.getBoundary().intersects( this.getBoundary() );
    }


    /**
     * This method checks to see if a sprite is inteersecting with a location.
     *
     * @param x the x value of the intersection point
     * @param y the y value of the intersection point
     * @return a boolean value for whether the sprite intersects or not
     */

    public boolean intersects(double x, double y) {
        Rectangle2D temp = new Rectangle2D(x, y, 20, 20);
        return temp.intersects(this.getBoundary());
    }

}
