/**
 * @Author: Aimé
 * @Date:   2022-03-24 20:18:24
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-05-06 07:04:45
 */
package be.freeaime.drop_block_2048.views;

import be.freeaime.drop_block_2048.logic.Game;
import be.freeaime.drop_block_2048.models.Block;
import be.freeaime.drop_block_2048.models.Grid;

public class ViewMain {
    private static void consoleDisplay(Grid grid) {
        drawLine();
        drawTopXLegend();
        for (int row = Grid.GRID_ROWS - 1; row >= 0; row--) {
            print(Integer.toString(row));// y side numbers
            for (int col = 0; col < Grid.GRID_COLS; col++) {
                Block block = grid.getBlock(row, col);
                if (block == null) {
                    System.out.println(" ");
                } else {
                    if (block.getType() == 0) {
                        print("_");
                    } else {
                        print(Integer.toString(block.getType()));
                    }
                }
            }
            System.out.print("\n");
        }
        drawLine();
        System.out.println("Player: " + Game.PLAYER_SCORE.getPlayerName() + " Score: " + Game.PLAYER_SCORE.getScore());

    }

    private static void drawTopXLegend() {
        print(" ");
        for (int i = 0; i < Grid.GRID_COLS; i++) {
            print(Integer.toString(i));
        }
        System.out.print("\n");
    }

    private static void drawLine() {
        for (int i = 0; i < Grid.GRID_COLS + 2; i++) {
            print("=");
        }
        System.out.print("\n");
    }

    private static void print(String block) {
        System.out.printf("%3s", block);
    }

    public static void showGrid(Grid grid) {
        if (Game.consoleMode)
            consoleDisplay(grid);
    }

}
