package cs1302.arcade;

import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 * Application subclass for {@code ArcadeApp}.
 * @version 2019.fa
 */
public class ArcadeApp extends Application {

    Group group = new Group();           // main container
    Random rng = new Random();           // random number generator
    Rectangle r = new Rectangle(20, 20); // some rectangle


    // main menu variables
    VBox vBox;
    VBox cvBox;
    VBox rvBox;
    HBox cContainer;
    HBox rContainer;
    Scene menuScene;
    Scene cScene;
    Scene rScene;

    Menu menu;
    MenuItem exit;
    MenuBar menuBar;
    Button cButton; // centipede start
    Button rButton; // reversi start
    TextField cInstructions;
    TextField rInstructions;
    Stage stage;

    // event handlers

    Rectangle e = new Rectangle(20, 20); // temp enemy rectangel
    boolean daemon = true;
    boolean rightE = true;
    boolean upE = false;
    boolean movingBullet = false;
    boolean upPressed = false;
    boolean downPressed = false;
    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean spacePressed = false;
    boolean upReleased = false;
    boolean downReleased = false;
    boolean leftReleased = false;
    boolean rightReleased = false;
    boolean spaceReleased = false;

    int eSpeed = 4;
    int eDown = 20;
    Rectangle[] c = new Rectangle[10];
    Rectangle bullet = new Rectangle();
    final Timeline timeline = gamePlayLoop();

    /**
     * Return a mouse event handler that moves to the rectangle to a random
     * position any time a mouse event is generated by the associated node.
     * @return the mouse event handler
     */
    private EventHandler<? super MouseEvent> createMouseHandler() {
        return event -> {
            System.out.println(event);
            r.setX(rng.nextDouble() * (640 - r.getWidth()));
            r.setY(rng.nextDouble() * (480 - r.getHeight()));
        };
    } // createMouseHandler

    private Timeline gamePlayLoop() {


        final Duration oneFrameAmt = Duration.millis(1000/60);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                                               new EventHandler<ActionEvent>() {
                                                   public void handle(ActionEvent event) {
               for(int i = 0; i < c.length; i++) {
                   if(i == c.length - 1) {
                       c[i].setX(e.getX());
                       c[i].setY(e.getY());
                   } else {
                       c[i].setY(c[i+1].getY());
                       c[i].setX(c[i+1].getX());
                   }
               }
                   if(!upE && e.getX() > 620) {
                       e.setY(e.getY() + eDown);
                       rightE = false;
                   } else if(!upE && e.getX() <= 0.0) {
                       e.setY(e.getY() + eDown);
                       rightE = true;
                   }
                   if(rightE) {
                       e.setX(e.getX() + eSpeed);
                   } else {
                       e.setX(e.getX() - eSpeed);
                   } //else
                   System.out.println("x value: " + bullet.getX());
                   System.out.println("Y value: " + bullet.getY());
                   if(e.getY()  > 440){
                       upE = true;
                   }
                   if(upE && e.getY() < 380) {
                       upE = false;
                   }
                   if(upE == true && e.getX() > 600) {
                       e.setY(e.getY() - eDown);
                       rightE = false;
                   } else if(upE && e.getX() <= 0.0) {
                       e.setY(e.getY() - eDown);
                       rightE = true;
                   }

                   if(leftPressed == true) {
                       if(r.getX() <= 0 ) {
                           r.setX(0.0);
                       } else {
                           r.setX(r.getX() - 10.0);
                       } //else
                   } // if

                   if(rightPressed == true) {
                       if(r.getX() > 600) {
                           r.setX(620);
                       } else {
                           r.setX(r.getX() + 10.0);
                       } //else
                   } //if

                   if(upPressed == true) {
                       if(r.getY() < 380) {
                           r.setY(360);
                       } else {
                           r.setY(r.getY() - 10);
                       }
                   } //if

                   if(downPressed == true) {
                       if(r.getY() > 440) {
                           r.setY(460);
                       } else {
                           r.setY(r.getY() + 10);
                       }
                   }//if

                   if(bullet.getY() >= -5){
                       bullet.setY(bullet.getY()-5);
                       movingBullet = true;
                   } else {
                       movingBullet = false;
                   }

                   if(spacePressed && movingBullet == false) {
                       bullet.setX(r.getX());
                       bullet.setY(r.getY());
                   } //if
                                                   }
                                               }); // oneFrame
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(oneFrame);
        return timeline;
    } //
    /**
     * Return a key event handler that moves to the rectangle to the left
     * or the right depending on what key event is generated by the associated
     * node.
     * @return the key event handler
     */
    private EventHandler<? super KeyEvent> createHandlerOnPressed() {
        return event -> {
            System.out.println(event);
            switch (event.getCode()) {
            case LEFT:  // KeyCode.LEFT
                leftPressed = true;
                break;
            case RIGHT: // KeyCode.RIGHT
                rightPressed = true;
                break;
            case UP:
                upPressed = true;
                break;
            case DOWN:
                downPressed = true;
                break;
            case SPACE:
                spacePressed = true;
                break;
            default:
                // do nothing
            } // switch
            // TODO bounds checking
        };
    } // createHandlerOnPressed


        private EventHandler<? super KeyEvent> createHandlerOnReleased() {
        return event -> {
            System.out.println(event);
            switch (event.getCode()) {
            case LEFT:  // KeyCode.LEFT
                leftPressed =false;
                break;
            case RIGHT: // KeyCode.RIGHT
                rightPressed = false;
                break;
            case UP:
                upPressed =false;
                break;
            case DOWN:
                downPressed = false;
                break;
            case SPACE:
                spacePressed = false;
                break;
            default:
            } // switch
        };
    } // createHandlerOnReleased

    private EventHandler<ActionEvent> exitApp = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        };

    private EventHandler<ActionEvent> startCentipede = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.setTitle("Centipede");
                stage.setScene(cScene);
                stage.sizeToScene();
                stage.show();
            }
        };

    private EventHandler<ActionEvent> startReversi = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.setTitle("Reversi");
                stage.setScene(rScene);
                stage.sizeToScene();
                stage.show();
            }
        };

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        vBox = new VBox();
        rvBox = new VBox();
        cContainer = new HBox();
        rContainer = new HBox();
        menuScene = new Scene(vBox, 640, 480);
        cScene = centipede();
        rScene = new Scene(rvBox, 640, 480);
        menu = new Menu("File");
        exit = new MenuItem("Exit");
        menuBar = new MenuBar();
        cButton = new Button("Centipede");
        rButton = new Button("Reversi");
        cInstructions = new TextField("Fill in later");
        rInstructions = new TextField("Fill in later");

        // add children to parents
        cContainer.getChildren().addAll(cButton, cInstructions);
        rContainer.getChildren().addAll(rButton, rInstructions);
        menu.getItems().add(exit);
        menuBar.getMenus().add(menu);
        vBox.getChildren().addAll(menuBar, cContainer, rContainer);

        // generate events
        menu.setOnAction(exitApp);
        stage.setTitle("Game Launcher");
        stage.setScene(menuScene);
        stage.sizeToScene();
        stage.show();

        cButton.setOnAction(startCentipede);
        rButton.setOnAction(startReversi);
        //      Scene scene = new Scene(group, 640, 480);
        group.requestFocus();

    } // start

    public static void runOnNewThread(boolean daemon, Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(daemon);
        t.start();
                       } // runOnNewThread

    private Scene centipede() {
        Scene centipede = new Scene(group, 640, 480);
        r.setX(320);
        r.setY(460);
        group.getChildren().addAll(r, e);                // add to main container
        group.setOnKeyPressed(createHandlerOnPressed());
        group.setOnKeyReleased(createHandlerOnReleased());
        Group enemy = new Group();
        for(int i = 0; i < 10; i++) {
            c[i] = new Rectangle(20, 20);
            c[i].setX(100-(20*i));
            c[i].setY(240);
            group.getChildren().add(c[i]);
        }
        group.getChildren().add(centipedeGun());
        timeline.play();
        return centipede;
    } //centipede

    public Rectangle centipedeGun() {
        bullet = new Rectangle(5, 10);
        bullet.setX(-5);
        return bullet;
    }
} // ArcadeApp
