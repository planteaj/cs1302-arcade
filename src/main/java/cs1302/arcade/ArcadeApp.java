package cs1302.arcade;

import java.util.Random;
import java.lang.Thread;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.shape.Rectangle;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Transition;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.shape.Circle;
import javafx.scene.layout.Priority;
/**
 * Application subclass for {@code ArcadeApp}.
 * @version 2019.fa
 */

public class ArcadeApp extends Application {

    int width = 640;
    int height = 480;

    Group group = new Group();           // main container
    Random rng = new Random();           // random number generator
    Sprite r = new Sprite(); // some rectangle
    Canvas reversiCanvas = new Canvas(width, height);
    final Canvas canvas = new Canvas(width, height);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    GraphicsContext rGc = reversiCanvas.getGraphicsContext2D();

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

    Text cInstructions = new Text( "Centipede is a game in which you control a ship at the bottom" +
                                   "of the screen\n with the arrow keys. You use the space bar" +
                                   "to shoot the centipede who starts\n at th top of the screen" +
                                   "and moves down. If the Centipede is hit, it splits into two\n" +
                                   "centipedes and the part that was hit turns into a mushroom.\n" +
                                   "The goal is to make it through levels and rack up\n points" +
                                   "without losing all three of your lives.");
    Text rInstructions;
    Label cScoreLabel;
    int cScore;
    String cScoreText;
    Stage stage;

    // event handlers

    Rectangle e = new Rectangle(20, 20); // temp enemy rectangel
    boolean daemon = true;
    boolean upE = false;
    boolean moveC = false;
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
    double eSpeed = .02;
    int eDown = 20;
    int centipedeNum = 10;
    ArrayList<Sprite> centipedes = new ArrayList<Sprite>();
    ArrayList<Boolean> rightE = new ArrayList<Boolean>();
    ArrayList<Sprite> mushroom = new ArrayList<Sprite>();
    Sprite bullet = new Sprite();
    int cLives = 3;
    int cLevel = 1;
    int mushS = mushroom.size();
    Timeline timeline = new Timeline();
    Timeline rTimeline = rGamePlayLoop();
    Timeline levelC = new Timeline();
    Timeline winC = new Timeline();
    final Duration oneFrameAmt = Duration.millis(1000 / 60);
    //reversi variables
    GridPane rGrid = new GridPane();
    int boardSize = 8;
    //Boolean[][] othelloBoard = new Boolean[boardSize][boardSize];
    String[][] othelloBoard = new String[boardSize][boardSize];
    Sprite[][] othelloPieces = new Sprite[boardSize][boardSize];
    Disk[][] boardPieces = new Disk[boardSize][boardSize];
    String currentColor = "blue";
    Group rGroup = new Group();
    Double mouseX = 0.0;
    Double mouseY = 0.0;
    Boolean mouseClicked = false;    /**
     * Return a mouse event handler that moves to the rectangle to a random
     * position any time a mouse event is generated by the associated node.
     * @return the mouse event handler
     */

    private EventHandler<? super MouseEvent> createMouseHandler() {
        return event -> {
            System.out.println(event);
            mouseClicked = true;
        };
    } // createMouseHandler

    private EventHandler<? super MouseEvent> createReleaseMouseHandler() {
        return event -> {
            mouseClicked = false;
        };
    }


    private Timeline rGamePlayLoop() {
        Duration oneFrameAmt = Duration.millis(1000 / 60);
        final KeyFrame rFrame = new KeyFrame(oneFrameAmt, new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    for (int i = 0; i < boardSize; i ++) {
                        for (int j = 0; j < boardSize; j++) {
                            //othelloPieces[i][j].render(rGc);
                            if (othelloPieces[i][j].intersects(mouseX, mouseY) && mouseClicked) {
                                System.out.println("true");
                            } //if
                        } //for
                    } //for
                } //handle
            }); // oneFrame
        Timeline rTimeline = new Timeline();
        rTimeline.setCycleCount(Animation.INDEFINITE);
        rTimeline.getKeyFrames().add(rFrame);
        return rTimeline;
    } //rGamePlayLoop

    public void renderC() {
        gc.clearRect(0, 0, width, height);
        r.render(gc);
        for (int i = 0; i < centipedes.size(); i++) {
            centipedes.get(i).render(gc);
        }
        centipedeMovement();
        for (int i = 0; i < mushroom.size(); i++) {
            mushroom.get(i).render(gc);
        }
    }

    public void bulletCheck() {
        if (bullet.getY() >= -5) {
            bullet.render(gc);
            bullet.setY(bullet.getY() - 10);
            movingBullet = true;
        } else {
            movingBullet = false;
        }
        if (spacePressed && movingBullet == false) {
            bullet.setX(r.getX());
            bullet.setY(r.getY());
        } //if
        for (int i = 0; i < mushroom.size(); i++) {
            if (bullet.intersects(mushroom.get(i))) {
                bullet.setY(-5);
                mushroom.remove(i);
                cScore += 5;
            } //if
        } //for
        for (int i = 0; i < centipedes.size(); i++) {
            if (bullet.intersects(centipedes.get(i)) && centipedes.size() > 0) {
                bullet.setY(-5);
                createMushroom(mushS,centipedes.get(i).getX(),centipedes.get(i).getY());
                centipedes.remove(i);
                cScore += 25;
            } //if
            if (r.intersects(centipedes.get(i))) {
                cLives --;
                centipedeCreation();
            } //if
        } //for
    }

    final KeyFrame oneFrame = new KeyFrame(oneFrameAmt, new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                renderC();
                if (leftPressed == true) {
                    if (r.getX() <= 0 ) {
                        r.setX(0.0);
                    } else {
                        r.setX(r.getX() - 5.0);
                    } //else
                } // if
                if (rightPressed == true) {
                    if (r.getX() > 600) {
                        r.setX(620);
                    } else {
                        r.setX(r.getX() + 5.0);
                    } //else
                } //if
                if (upPressed == true) {
                    if (r.getY() < 380) {
                        r.setY(360);
                    } else {
                        r.setY(r.getY() - 5.0);
                    }
                } //if
                if (downPressed == true) {
                    if (r.getY() > height - 70) {
                        r.setY(height - 50);
                    } else {
                        r.setY(r.getY() + 5.0);
                    }
                } //if
                bulletCheck();
                if (cLevel > 4) {
                    gc.clearRect(0, 0, 640, 480);
                    gc.strokeText(" You Win!", 320,  240, 10000);
                    winC.play();
                }
                if (centipedes.size() == 0 && cLevel <= 4) {
                    gc.clearRect(0, 0, width, height);
                    gc.strokeText("Level: " + cLevel, 320, 240, 10000);
                    levelC.play();
                } //if
                if (cLives == 0) {
                    stage.setScene(menuScene);
                    timeline.stop();
                } //if
                cScoreText = "Score: " + cScore;
                gc.fillText( cScoreText, 540, 20, 300);
                gc.strokeText(cScoreText,540, 20, 300);
            } //handle
        }); // oneFrame


    private void gamePlayLoop() {
        KeyFrame winScreen = new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    stage.setScene(menuScene);
                }
            });
        KeyFrame LevelScreen = new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    timeline.pause();
                    cLevel++;
                    eSpeed += .05;
                    centipedeNum += 2;
                    centipedeCreation();
                    mushrooms();
                    timeline.play();
                }
            });
        levelC.getKeyFrames().add(LevelScreen);
        levelC.setCycleCount(1);
        levelC.play();
        winC.getKeyFrames().add(winScreen);
        winC.setCycleCount(1);
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(oneFrame);
    } //gameplayoop
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
                leftPressed = false;
                break;
            case RIGHT: // KeyCode.RIGHT
                rightPressed = false;
                break;
            case UP:
                upPressed = false;
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

    private EventHandler<ActionEvent> mainMenuChange = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.setScene(menuScene);
            }
        };

    private EventHandler<ActionEvent> startCentipede = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                cLevel = 1;
                cScore = 0;
                cLives = 3;
                centipedeNum = 10;
                eSpeed = .02;
                centipedeCreation();
                mushrooms();
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

    private EventHandler<MouseEvent> playReversi = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Disk disk = (Disk) e.getSource();
                String color = getCurrentColor();
                if (checkPiece(disk) == false) {
                    return;
                } else {
                    if (checkRow(disk) != -1) {
                        int rowChange = checkRow(disk);
                        if (rowChange < disk.getCol()) {
                            for (int j = rowChange; j < disk.getCol(); j++) {
                                othelloBoard[disk.getRow()][j] = color;
                            }
                        } else {
                            for (int j = disk.getCol(); j < rowChange + 1; j++) {
                                othelloBoard[disk.getRow()][j] = color;
                            }
                        }
                    }
                    if (checkCol(disk) != -1) {
                        int colChange = checkCol(disk);
                        if (colChange < disk.getRow()) {
                            for (int i = colChange; i < disk.getRow(); i++) {
                                othelloBoard[i][disk.getCol()] = color;
                            }
                        } else {
                            for (int i = disk.getRow(); i < colChange + 1; i++) {
                                othelloBoard[i][disk.getCol()] = color;
                            }
                        }
                    }
                    /*
                    if (checkDiag(disk) != -1) {

                    }
                    */
                    for (int i = 0; i < boardSize; i++) {
                        for (int j = 0; j < boardSize; j++) {
                            boardPieces[i][j].setColor(othelloBoard[i][j]);
                        }
                    }
                    updateBoard();
                    if (color.equals("blue")) {
                        setCurrentColor("red");
                    } else {
                        setCurrentColor("blue");
                    }
                }
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
        menuScene = new Scene(vBox, width, height);
        cScene = centipede();
        rScene = reversi();
        menu = new Menu("File");
        exit = new MenuItem("Exit");
        menuBar = new MenuBar();
        cButton = new Button("Centipede");
        rButton = new Button("Reversi");

        rInstructions = new Text("Reversi is a strategy board" +
                                 " game for two players,played on an 8×8 uncheckered\n board." +
                                 " Players take turns placing disks on the board with their" +
                                 " assigned color facing\n up. During a play, any disks of the" +
                                 " opponent's color that are in a straight line and\n bounded by" +
                                 " the disk just placed and another disk of the current player's" +
                                 " color are\n turned over to the current player's color. ");
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
        group.requestFocus();

    } // start

    public static void runOnNewThread(boolean daemon, Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(daemon);
        t.start();
    } // runOnNewThread

    private Scene reversi() {
        rvBox = new VBox();
        Scene reversi = new Scene(rvBox, 640, 480); //rGroup
        rGroup.setOnMousePressed(createMouseHandler());
        rGroup.setOnMouseReleased(createReleaseMouseHandler());
        othelloPieces();
        rGrid.setGridLinesVisible(true);
        rvBox.getChildren().add(rGrid); //reversiCanvas
        VBox.setVgrow(rGrid, Priority.ALWAYS);
        rTimeline.play();
        return reversi;
    } //reversi

    public void othelloPieces() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j ++) {
                Disk disk;
                if (i == 3 && j == 3) {
                    disk = new Disk(10.0, Paint.valueOf("blue"), 3, 3, "blue");
                    rGrid.add(disk, 3, 3);
                } else if (i == 3 && j == 4) {
                    disk = new Disk(10.0, Paint.valueOf("red"), 3, 4, "red");
                    rGrid.add(disk, 4, 3);
                } else if (i == 4 && j == 3) {
                    disk = new Disk(10.0, Paint.valueOf("red"), 4, 3, "red");
                    rGrid.add(disk, 3, 4);
                } else if (i == 4 && j == 4) {
                    disk = new Disk(10.0, Paint.valueOf("blue"), 4, 4, "blue");
                    rGrid.add(disk, 4, 4);
                } else {
                    disk = new Disk(10.0, Paint.valueOf("white"), i, j, "white");
                    disk.setOnMousePressed(playReversi); //need to define function playOthello
                    //GridPane board = new GridPane();
                    //disk.setOnMousePressed(playOthello); //need to define function playOthello
                    rGrid.add(disk, j, i);
                } //else
                boardPieces[i][j] = disk;
                othelloBoard[i][j] = "white";
            } //for
        } //for
        othelloBoard[3][3] = "blue";
        othelloBoard[3][4] = "red";
        othelloBoard[4][3] = "red";
        othelloBoard[4][4] = "blue";
    } //othelloPieces


    public void updateBoard() {
        rGrid.getChildren().clear();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Disk disk = boardPieces[i][j];
                if (disk.getColor().equals("white")) {
                    disk.setOnMousePressed(playReversi);
                }
                rGrid.add(disk, j, i);
            }
        }
        rGrid.setGridLinesVisible(true);
    }

    public int checkCol(Disk disk) {
        int result = -1;
        int row = disk.getRow();
        int col = disk.getCol();
        if (othelloBoard[row - 1][col].equals(disk.getColor()) == false) {
            for (int i = row - 1; i >= 0; i--) {
                if (othelloBoard[i][col].equals("white")) {
                    break;
                } else if (othelloBoard[i][col].equals(disk.getColor())) {
                    result = i + 1;
                }
            }
        } else if (othelloBoard[row + 1][col].equals(disk.getColor()) == false) {
            for (int i = row + 1; i < 8; i++) {
                if (othelloBoard[i][col].equals("white")) {
                    break;
                } else if (othelloBoard[i][col].equals(disk.getColor())) {
                    result = i - 1;
                }
            }
        }
        return result;
    }

    public int checkRow(Disk disk) {
        int result = -1;
        int row = disk.getRow();
        int col = disk.getCol();
        if (othelloBoard[row][col - 1].equals(disk.getColor()) == false) {
            for (int j = col - 1; j >= 0; j--) {
                if (othelloBoard[row][j].equals("white")) {
                    break;
                } else if (othelloBoard[row][j].equals(disk.getColor())) {
                    result = j + 1;
                }
            }
        } else if (othelloBoard[row][col + 1].equals(disk.getColor()) == false) {
            for (int j = col + 1; j < 8; j++) {
                if (othelloBoard[row][j].equals("white")) {
                    break;
                } else if (othelloBoard[row][j].equals(disk.getColor())) {
                    result = j - 1;
                }
            }
        }
        return result;
    }

    public boolean checkPiece(Disk disk) {
        boolean result = true;
        int row = disk.getRow();
        int col = disk.getCol();
        if (row == 0 && col == 0) {
            if (othelloBoard[row + 1][col].equals("w") && othelloBoard[row][col + 1].equals("w") &&
                othelloBoard[row + 1][col + 1].equals("w")) {
                result = false;
            }
        } else if (row == 0 && col == 7) {
            if (othelloBoard[row][col - 1].equals("w") && othelloBoard[row + 1][col].equals("w")
                && othelloBoard[row + 1][col - 1].equals("w")) {
                result = false;
            }
        } else if (row == 7 && col == 7) {
            if (othelloBoard[row - 1][col].equals("w") && othelloBoard[row][col - 1].equals("w")
                && othelloBoard[row - 1][col - 1].equals("w")) {
                result = false;
            }
        } else if (row == 7 && col == 0) {
            if (othelloBoard[row - 1][col].equals("w") && othelloBoard[row][col + 1].equals("w")
                && othelloBoard[row - 1][col + 1].equals("w")) {
                result = false;
            }
        } else if (row == 0) {
            if (othelloBoard[row][col - 1].equals("w") && othelloBoard[row][col + 1].equals("w")
                && othelloBoard[row + 1][col - 1].equals("w") && othelloBoard[row + 1][col]
                .equals("w") && othelloBoard[row + 1][col + 1].equals("w")) {
                result = false;
            }
        } else if (col == 7) {
            if (othelloBoard[row - 1][col].equals("w") && othelloBoard[row + 1][col].equals("w")
                && othelloBoard[row - 1][col - 1].equals("w") && othelloBoard[row][col - 1]
                .equals("w") && othelloBoard[row + 1][col + 1].equals("w")) {
                result = false;
            }
        } else if (row == 7) {
            if (othelloBoard[row][col - 1].equals("w") && othelloBoard[row][col + 1].equals("w")
                && othelloBoard[row - 1][col - 1].equals("w") && othelloBoard[row - 1][col]
                .equals("w") && othelloBoard[row - 1][col + 1].equals("w")) {
                result = false;
            }
        } else if (col == 0) {
            if (othelloBoard[row - 1][col].equals("w") && othelloBoard[row + 1][col].equals("w")
                && othelloBoard[row - 1][col + 1].equals("w") && othelloBoard[row][col + 1]
                .equals("w") && othelloBoard[row + 1][col + 1].equals("w")) {
                result = false;
            }
        } else {
            if (othelloBoard[row - 1][col - 1].equals("w") && othelloBoard[row - 1][col]
                .equals("w") && othelloBoard[row - 1][col + 1].equals("w") &&
                othelloBoard[row][col - 1].equals("w") && othelloBoard[row][col + 1].equals("w")
                && othelloBoard[row + 1][col - 1].equals("w") && othelloBoard[row + 1][col]
                .equals("w") && othelloBoard[row + 1][col + 1].equals("w")) {
                result = false;
            }
        }
        return result;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(String c) {
        currentColor = c;
    }

    private Scene centipede() {
        Scene centipede = new Scene(group, width, height);
        gamePlayLoop();
        r.setX(320);
        r.setY(420);
        Menu cMenu = new Menu("File");
        MenuItem cExit = new MenuItem("Exit");
        MenuItem cMainMenu = new MenuItem("Main Menu");
        cExit.setOnAction(exitApp);
        cMainMenu.setOnAction(mainMenuChange);
        MenuBar cMenuBar = new MenuBar();
        cMenu.getItems().add(cExit);
        cMenu.getItems().add(cMainMenu);
        cMenuBar.getMenus().add(cMenu);
        VBox cVbox = new VBox();
        cVbox.getChildren().addAll(cMenuBar, canvas);
        group.getChildren().add(cVbox);
        group.setOnKeyPressed(createHandlerOnPressed());
        group.setOnKeyReleased(createHandlerOnReleased());
        centipedeCreation();
        centipedeGun();
        mushrooms();
        return centipede;
    } //centipede

    public void centipedeLevel() {
        timeline.pause();
        cLevel ++;
        eSpeed += .05;
        centipedeNum += 2;
        gc.clearRect(0, 0, 640, 480);
        centipedeCreation();
        mushrooms();
        gc.strokeText("Level: " + cLevel, 320, 240, 10000);
        //try {
        //  Thread.sleep(2500);
        //} catch (InterruptedException ex) {
        //   return;
        //} //catch
        timeline.play();
    } //centipedeLevel

    public void centipedeGun() {
        bullet = new Sprite();
        bullet.setImage("file:resources/bullet.png", 15, 20);
        bullet.setX(-5);
        r.setImage("file:resources/recentipede.jpg", 20, 20);
    } //centipedeGun

    public void centipedeCreation() {
        timeline.pause();
        centipedes.clear();
        for (int i = 0; i < centipedeNum; i++) {
            centipedes.add(i, new Sprite());
            centipedes.get(i).setImage("file:resources/recentipede.jpg",20 ,20);
            centipedes.get(i).setX(100 + (i * 20));
            centipedes.get(i).setY(30);
            rightE.add(i, true);
        } //for
        timeline.play();
    } //centipedeCreation()

    public void centipedeMovement() {
        for (int i = centipedes.size() - 1; i >= 0; i-- ) {
            for (int j = 0; j < mushroom.size(); j++) {
                if (!upE && centipedes.get(i).getX() > 610) {
                    rightE.set(i, false);
                    centipedes.get(i).setY(centipedes.get(i).getY() + eDown);
                } else if (!upE && centipedes.get(i).getX() <= 0.0) {
                    rightE.set(i, true);
                    centipedes.get(i).setY(centipedes.get(i).getY() + eDown);
                } //else
                if (rightE.get(i) && centipedes.get(i).intersects(mushroom.get(j)) && !moveC && i
                    != centipedes.size() - 1
                    && !centipedes.get(i).intersects(centipedes.get(i + 1))) {
                    rightE.set(i, false);
                    centipedes.get(i).setY(centipedes.get(i).getY() + eDown);
                    moveC = true;
                } else if (rightE.get(i) && centipedes.get(i).intersects(mushroom.get(j))
                           && !moveC) {
                    rightE.set(i, false);
                    centipedes.get(i).setY(centipedes.get(i).getY() + eDown);
                } else if (!rightE.get(i) && centipedes.get(i).intersects(mushroom.get(j)) &&
                           !moveC) {
                    rightE.set(i,true);
                    centipedes.get(i).setY(centipedes.get(i).getY() + eDown);
                    moveC = true;
                }
                if (rightE.get(i)) {
                    centipedes.get(i).setX(centipedes.get(i).getX() + eSpeed);
                    moveC = false;
                } else {
                    centipedes.get(i).setX(centipedes.get(i).getX() - eSpeed);
                    moveC = false;
                } //else
                if (centipedes.get(i).getY()  > height - 70) {
                    upE = true;
                }
                if (upE && centipedes.get(i).getY() < 380) {
                    upE = false;
                }
                if (upE == true && centipedes.get(i).getX() > 600) {
                    centipedes.get(i).setY(centipedes.get(i).getY() - eDown);
                    rightE.set(i, false);
                } else if (upE && centipedes.get(i).getX() <= 0.0) {
                    centipedes.get(i).setY(centipedes.get(i).getY() - eDown);
                    rightE.set(i, true);
                } //elseif
            } //for
        } //for
    } //centipedeMovement

    public void mushrooms() {
        double rand = 0;
        mushroom.clear();
        int position = 0;
        for (int i = 0; i < 32; i++) {
            for (int j = 1; j < 18; j++) {
                rand = Math.random();
                if (rand < .05) {
                    createMushroom(position, i * 20.0, j * 20.0);
                    position ++;
                }
            } //if
        } //if
    } //mushroom

    public void createMushroom(int i , double x, double y) {
        mushroom.add(i, new Sprite());
        mushroom.get(i).setImage("file:resources/Mushroom.jpg",20 ,20);
        mushroom.get(i).setX(0 + x);
        mushroom.get(i).setY(10 + y);
    }
} // ArcadeApp
