/**
 * @Author: Aimé
 * @Date:   2022-03-26 22:36:33
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-27 01:05:25
 */
package be.freeaime.drop_block_2048.views;

import java.util.List;

import be.freeaime.drop_block_2048.models.Score;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ViewHighScore extends GridPane {
    private VBox mainVBox = new VBox();
    private Label congratsLabel = new Label();
    private Label playerScoreLabel = new Label();
    private Button okButton = new Button("OK");

    public ViewHighScore() {
        getStyleClass().add("GridPane");
        this.add(new Label("Top 10 Scores"), 0, 0);
        this.add(new ScrollPane(mainVBox), 0, 1);
        this.add(congratsLabel, 0, 2);
        this.add(playerScoreLabel, 0, 3);
        this.add(okButton, 0, 4);
        okButton.setOnAction(event -> this.toBack());
        setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setVgap(10);
        this.setHgap(10);
    }

    public void showScoreBoard(List<Score> scores, Score currentPlayScore) {
        mainVBox.getChildren().clear();
        for (int i = 0; i < scores.size(); i++) {
            mainVBox.getChildren().add(new Label(
                    String.format("Rank %d %-10d %s", i + 1, scores.get(i).getScore(), scores.get(i).getPlayerName())));

        }
        if (scores.size() < 10) {
            congratsMessage();
        } else {
            Score tenthScore = scores.get(9);
            if (currentPlayScore.getScore() > tenthScore.getScore()) {
                congratsMessage();
            } else {
                congratsLabel.setText("Your score is ");
            }
        }
        playerScoreLabel.setText(
                String.format("%s | Score: %d", currentPlayScore.getPlayerName(), currentPlayScore.getScore()));

        this.toFront();
    }

    private void congratsMessage() {
        playerScoreLabel.setText("Congratulation! you are in the Top Ten");
    }

}
