/**
 * @Author: Aimé
 * @Date:   2022-03-26 15:05:10
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-05-09 23:19:59
 */
package be.freeaime.drop_block_2048.views;

import be.freeaime.drop_block_2048.interfaces.ClickCallBack;
import be.freeaime.drop_block_2048.models.Grid;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class ViewGrid extends GridPane {
    private ViewBlock[][] viewBlocks = new ViewBlock[Grid.GRID_ROWS][Grid.GRID_COLS];

    public ViewGrid(ClickCallBack clickCallBack) {
        getStyleClass().add("GridPane");
        for (int row = 0; row < Grid.GRID_ROWS; row++) {
            for (int col = 0; col < Grid.GRID_COLS; col++) {
                viewBlocks[row][col] = new ViewBlock();
                this.add(viewBlocks[row][col], col, row);
            }
        }
        setAlignment(Pos.CENTER);
        setVgap(5);

    }

    public void updateView(Grid grid) {
        for (int row = 0; row < Grid.GRID_ROWS; row++) {
            for (int col = 0; col < Grid.GRID_COLS; col++) {
                viewBlocks[Grid.GRID_ROWS - 1 - row][col].updateType(grid.getBlock(row, col).getType());
            }

        }
    }

}
