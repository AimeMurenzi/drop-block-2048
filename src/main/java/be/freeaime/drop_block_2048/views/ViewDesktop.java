/**
 * @Author: Aimé
 * @Date:   2022-03-26 19:08:11
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-07-27 21:35:50
 */
package be.freeaime.drop_block_2048.views;

import static java.lang.System.exit;

import java.util.Random;

import be.freeaime.drop_block_2048.interfaces.ClickCallBack;
import be.freeaime.drop_block_2048.logic.Game;
import be.freeaime.drop_block_2048.models.Block;
import be.freeaime.drop_block_2048.models.Grid;
import be.freeaime.drop_block_2048.models.Score;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewDesktop extends Application implements ClickCallBack {

    public static final ViewFancyInputDialogue FANCY_DIALOGUE = new ViewFancyInputDialogue();
    public static final ViewHighScore VIEW_HIGH_SCORE = new ViewHighScore();
    private ViewGrid viewGrid;
    private Label scoreLabel = new Label("Player 1 Score: 0");
    private Grid grid = new Grid();
    private Button quitButton = new Button("Quit");
    private Button scoreBoardButton = new Button("Score Board");
    private HBox menuHBox = new HBox(quitButton, scoreBoardButton);
    private final ViewBlock currentViewBlock = new ViewBlock();

    private int roundCount = 1;
    private int currentBlockType = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        runMemoryMonitorStats(primaryStage);
        viewGrid = new ViewGrid(this);
        VBox horizontal = new VBox();
        horizontal.prefWidth(64 * 5);
        horizontal.getStyleClass().add("bottom-border");
        VBox mainVBox = new VBox(//
                scoreLabel, //
                currentViewBlock, //
                new StackPane(viewGrid, new ViewDropCol(this)), //
                horizontal, //
                menuHBox//
        );
        Scene scene = new Scene(
                new StackPane(//
                        VIEW_HIGH_SCORE, //
                        FANCY_DIALOGUE, //
                        mainVBox),
                800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toString());
        String icon = getClass().getResource("/images/211667_a_controller_game_icon.png").toString();
        primaryStage.getIcons().add(new Image(icon));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            exit(0);
        });
        FANCY_DIALOGUE.getInput("Please Enter Player Name",
                (message) -> {
                    if (message != null && message.length() > 0) {
                        Game.PLAYER_SCORE.setPlayerName(message);
                        updateScoreLabel();
                    }
                });

        viewGrid.updateView(grid);
        VBox.setMargin(scoreLabel, new Insets(20, 20, 30, 20));
        menuHBox.setAlignment(Pos.CENTER_LEFT);
        scoreBoardButton.setOnAction(
                even -> VIEW_HIGH_SCORE.showScoreBoard(
                        Game.SCORES,
                        new Score(
                                Game.PLAYER_SCORE.getPlayerName(),
                                Game.PLAYER_SCORE.getScore())));
        mainVBox.setAlignment(Pos.CENTER);
        Game.loadTopScores();
        Random random = new Random();
        int blockType = random.nextInt(6) + 1;
        currentBlockType = 1 << blockType;
        currentViewBlock.updateType(currentBlockType);

    }

    private void updateScoreLabel() {
        scoreLabel.setText(String.format("%s | Round %d/3 | Score: %d",
                Game.PLAYER_SCORE.getPlayerName(), roundCount, Game.PLAYER_SCORE.getScore()));
    }

    @Override
    public void clickUpdate(int col) {

        Block topBlockOnCol = grid.getColTopBlock(col);
        Block dropBlock = null;
        if (topBlockOnCol.isEmpty()) {
            dropBlock = topBlockOnCol;
            dropBlock.setType(currentBlockType);// random block
        } else {
            Block topBlock = topBlockOnCol.getTopBlock();
            if (topBlock == null) {
                return;
            } else {
                dropBlock = topBlock;
                dropBlock.setType(currentBlockType);
            }
        }
        boolean foundNeighbors = Game.playBlock(dropBlock);
        viewGrid.updateView(grid);
        Game.removeGaps(grid);
        viewGrid.updateView(grid);
        // loop through every column
        while (foundNeighbors) {
            foundNeighbors = Game.exploreNeighborhood(grid);
            Game.removeGaps(grid);
            viewGrid.updateView(grid);
        }
        updateScoreLabel();
        Random random = new Random();
        int blockType = random.nextInt(6) + 1;
        currentBlockType = 1 << blockType;
        currentViewBlock.updateType(currentBlockType);
    }

    public static void runMemoryMonitorStats(Stage primaryStage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        long heapSize = (long) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                                / 1024 / 1024);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                primaryStage.setTitle("Drop Block 2048 - " + heapSize + "MB / "
                                        + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "MB JVM");
                            }
                        });
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
