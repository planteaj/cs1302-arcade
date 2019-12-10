package cs1302.arcade;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Sprite
{
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    public Sprite()
    {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
    }

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }
    public void setImage(String filename, int width, int height){
        Image i = new Image(filename, width, height, false, false);
        setImage(i);
    }

    public void setX(double x)
    {
        positionX = x;
    }

    public void setY(double y)
        {
            positionY = y;
        }

    public double getX(){
        return positionX;
    }

    public double getY() {
        return positionY;
    }

    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
    public boolean intersects(double x, double y) {
        Rectangle2D temp = new Rectangle2D(x,y,20,20);
        return temp.intersects(this.getBoundary());
    }

}
