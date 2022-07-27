/**
 * @Author: Aimé
 * @Date:   2022-03-26 20:19:01
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-26 22:18:47
 */
package be.freeaime.drop_block_2048.views;

import be.freeaime.drop_block_2048.interfaces.GenericCallBack;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ViewFancyInputDialogue extends GridPane {
    private TextField inputTextField = new TextField();
    private Label messageLabel = new Label();
    private Button okButton = new Button("OK");
    private GenericCallBack genericCallBack;

    public ViewFancyInputDialogue() {
        // setStyle("-fx-background-color: red;");
        getStyleClass().add("GridPane");
        inputTextField.promptTextProperty().setValue("Player 1");

        this.add(messageLabel, 0, 0);
        this.add(inputTextField, 0, 1);
        this.add(okButton, 0, 2);
        setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(12);
        okButton.setOnAction(event -> {
            if (genericCallBack != null) {
                genericCallBack.callBack(inputTextField.getText());
                genericCallBack = null;
                System.out.println("click");
            }
            this.toBack();
        });
    }

    public void getInput(String message, GenericCallBack genericCallBack) {
        this.toFront();
        this.messageLabel.setText(message);
        this.genericCallBack = genericCallBack;
    }

}
