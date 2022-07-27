/**
 * @Author: Aimé
 * @Date:   2022-07-25 13:20:07
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-07-27 22:07:35
 */
package be.freeaime.main;

import java.util.Random;
import java.util.Scanner;

import be.freeaime.drop_block_2048.logic.Game;
import be.freeaime.drop_block_2048.models.Block;
import be.freeaime.drop_block_2048.models.Grid;
import be.freeaime.drop_block_2048.models.Level;
import be.freeaime.drop_block_2048.views.ViewDesktop;
import be.freeaime.drop_block_2048.views.ViewMain;
import javafx.application.Platform;

public class MainApp {

    public static void main(String[] args) {

        Game.consoleMode = args.length != 0;
        Game.level = Level.EASY;
        Game.SCORES.add(Game.PLAYER_SCORE);

        if (Game.consoleMode) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("=========== Drop Block 2048 ============");
            System.out.println("=================================");
            System.out.println("q to quit");
            System.out.print("Please Enter Player Name: ");
            String line = scanner.nextLine();

            // String line = "";
            if (!line.equals("q")) {
                Game.PLAYER_SCORE.setPlayerName(line);
            }

            Grid grid = new Grid();

            while (!line.equals("q")) {

                Random random = new Random();

                while (!line.toLowerCase().equals("q")) {
                    ViewMain.showGrid(grid);
                    int blockType = random.nextInt(6) + 1;
                    int nextBlockType = 1 << blockType;
                    System.out.println("Current Block: " + nextBlockType);
                    System.out.println("q to quit");
                    System.out.println("Please Select Column: ");
                    line = scanner.nextLine();
                    int colInput = 0;

                    try {
                        colInput = Integer.parseInt(line);
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    // figure out top block in column
                    // add dropBlock on top of topBlock
                    Block topBlockOnCol = grid.getColTopBlock(colInput);
                    // Block topBlockOnCol = grid.getColTopBlock(0);
                    Block dropBlock = null;

                    if (topBlockOnCol.isEmpty()) {
                        dropBlock = topBlockOnCol;
                        dropBlock.setType(nextBlockType);
                    }

                    else {
                        Block topBlock = topBlockOnCol.getTopBlock();

                        if (topBlock == null) {
                            continue;
                        }

                        else {
                            dropBlock = topBlock;
                            dropBlock.setType(nextBlockType);
                        }
                    }

                    boolean foundNeighbors = Game.playBlock(dropBlock);
                    ViewMain.showGrid(grid);
                    Game.removeGaps(grid);
                    ViewMain.showGrid(grid);

                    // loop through every column
                    while (foundNeighbors) {
                        // search for block that are equal and merge them per column
                        foundNeighbors = Game.exploreNeighborhood(grid);
                        Game.removeGaps(grid);
                        ViewMain.showGrid(grid);
                    }
                }
            }

            System.out.println("=================================");
            System.out.println("\nTop 10 High Scores");
            System.out.println();
            System.out.println("===== Thank you for playing =====");
            System.out.printf("Player: %s Score: %d\n", //
                    Game.PLAYER_SCORE.getPlayerName(), //
                    Game.PLAYER_SCORE.getScore());
            System.out.println("=================================");
            scanner.close();
        }

        else {
            Platform.startup(() -> {
            }

            );
            ViewDesktop.main(args);
        }

    }
}