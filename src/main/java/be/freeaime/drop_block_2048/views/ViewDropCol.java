/**
 * @Author: Aimé
 * @Date:   2022-03-29 02:59:33
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-29 06:37:52
 */
package be.freeaime.drop_block_2048.views;

import be.freeaime.drop_block_2048.interfaces.ClickCallBack;
import be.freeaime.drop_block_2048.models.Grid;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ViewDropCol extends GridPane {

    public ViewDropCol(ClickCallBack clickCallBack) {
        HBox hBox = new HBox();
        add(hBox, 0, 0);
        for (int col = 0; col < Grid.GRID_COLS; col++) {
            Button button = new Button();
            button.setPrefSize(70, 70 * 7);
            button.setUserData(col);
            button.getStyleClass().add("drop-col");
            hBox.getChildren().add(button);
            button.setOnAction(event -> {
                System.out.println((int) button.getUserData());
                clickCallBack.clickUpdate((int) button.getUserData());
            });
        }
        setAlignment(Pos.CENTER);
    }

}
