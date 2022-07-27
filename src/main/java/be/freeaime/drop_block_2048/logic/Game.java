/**
 * @Author: Aimé
 * @Date:   2022-03-26 18:16:30
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-07-27 22:06:25
 */
package be.freeaime.drop_block_2048.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import be.freeaime.drop_block_2048.models.Block;
import be.freeaime.drop_block_2048.models.Coordinate;
import be.freeaime.drop_block_2048.models.Grid;
import be.freeaime.drop_block_2048.models.Level;
import be.freeaime.drop_block_2048.models.OneDirection;
import be.freeaime.drop_block_2048.models.Score;

public class Game {

    public static boolean consoleMode;
    public static Level level;
    public static final Score PLAYER_SCORE = new Score("Player 1", 0);
    public static final List<Score> SCORES = new ArrayList<>();
    private static final String scoreSaveLocation = Paths
            .get(System.getProperty("user.home"), "drop_block_2048-top-scores.txt")
            .toString();
    private static final String scorePlanBSaveLocation = Paths
            .get(System.getProperty("user.dir"), "drop_block_2048-top-scores.txt")
            .toString();

    public static void addToScoreBoard() {
        Collections.sort(SCORES);
    }

    public static boolean playBlock(Block dropBlock) {
        // all the blocks linked to this block
        List<Block> linkedBlocks = new ArrayList<>();
        // used to implement keep block that we have not checked its neighbors
        Stack<Block> blockStack = new Stack<>();
        // this is used to record of blocked we have already checked for neighbors
        // so that we dont have infinite loops of searching
        // why HashSet contains has big O notation search performance of O(1)
        Set<Coordinate> visitedCoordinates = new HashSet<>();

        blockStack.push(dropBlock);
        while (!blockStack.empty()) {
            Block currentBlock = blockStack.pop();
            if (currentBlock == null) {
                continue;
            }
            Block leftBlock = currentBlock.getLeftBlock();
            Block rightBlock = currentBlock.getRightBlock();
            Block topBlock = currentBlock.getTopBlock();
            Block bottomBlock = currentBlock.getBottomBlock();

            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock,
                    leftBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock,
                    rightBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock,
                    topBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock,
                    bottomBlock);
            currentBlock = null;
        }
        // here is where the points are calculated
        if (linkedBlocks.size() > 1) {
            int dropBlockType = dropBlock.getType();
            for (int i = 1; i < linkedBlocks.size(); i++) {
                dropBlockType <<= 1;
            }
            for (Block linkedBlock : linkedBlocks) {
                linkedBlock.delete();
            }
            dropBlock.setType(dropBlockType);
            PLAYER_SCORE.addPoint(dropBlockType * linkedBlocks.size());
            Game.saveTopScores();
            return true;
        }
        return false;
    }

    /**
     * loops thought grid invoking playBlock on every non empty block.
     * breaks loop when playBlock return true.
     * 
     * @param grid
     * @return
     */
    public static boolean exploreNeighborhood(Grid grid) {
        boolean foundNeighbors = false;
        for (int row = 0; row < Grid.GRID_ROWS; row++) {
            for (int col = 0; col < Grid.GRID_COLS; col++) {
                Block topBlock = grid.getBlock(row, col);
                if (!topBlock.isEmpty() && playBlock(topBlock)) {
                    return true;
                }
            }
        }
        return foundNeighbors;
    }

    public static void saveTopScores() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(scoreSaveLocation);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {
            objectOutputStream.writeObject(SCORES);
        } catch (Exception e) {
            // plan B
            try (FileOutputStream fileOutputStream = new FileOutputStream(scorePlanBSaveLocation);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {
                objectOutputStream.writeObject(SCORES);
            } catch (Exception e1) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadTopScores() {
        Object scoreObject = null;
        try (FileInputStream fileInputStream = new FileInputStream(scoreSaveLocation);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
            scoreObject = objectInputStream.readObject();
        } catch (Exception e) {
            try (FileInputStream fileInputStream = new FileInputStream(scorePlanBSaveLocation);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
                scoreObject = objectInputStream.readObject();
            } catch (Exception e1) {
            }
        }
        try {
            if (scoreObject != null) {
                List<Score> scores = (ArrayList<Score>) scoreObject;
                for (Score score : scores) {
                    SCORES.add(score);
                }
                Collections.sort(SCORES);
            }

        } catch (Exception e) {
        }

    }

    public static void openScoreList() {

    }

    public static boolean isGameOver(Grid grid) {
        for (int row = 0; row < Grid.GRID_ROWS; row++) {
            for (int col = 0; col < Grid.GRID_COLS; col++) {
                int linkCount = getLinkCount(grid.getBlock(row, col));
                if (linkCount >= 3) {
                    return false;
                }
            }
        }
        return true;
    }

    // sorting logic
    // inspiration voor this algorithm
    // this is modified version of a sorting algo I found here
    // https://www.javatpoint.com/how-to-sort-an-array-in-java
    // this algo is modified to bring 0 up in a column
    public static void removeGaps(Grid grid) {
        for (int col = 0; col < Grid.GRID_COLS; col++) {
            for (int row = 0; row < Grid.GRID_ROWS; row++) {
                Block block = grid.getBlock(row, col);
                if (block.getType() == 0) {// empty
                    int currentRow = row;
                    for (int previousRow = row - 1; previousRow >= 0; previousRow--) {
                        Block blockBefore = grid.getBlock(previousRow, col);
                        int previousType = blockBefore.getType();
                        if (previousType > 0) {
                            Block currentBlock = grid.getBlock(currentRow, col);
                            currentBlock.setType(previousType);
                            blockBefore.setType(0);
                        }
                        currentRow--;
                    }
                }
            }
        }
    }

    private static void normalModeTraverse(Block block, List<Block> linkedBlocks) {

        // you could use normal recursion but i avoid it because it has stackoverflow
        // issues
        // this app does have enough recursions to cause a stackoverflow
        // but i'll still avoid it anyway and opt to use Depth First Search algorithm

        Stack<Block> blockStack = new Stack<>();
        blockStack.push(block);
        // why HashSet contains has O(1) search performance
        Set<Coordinate> visitedCoordinates = new HashSet<>();
        while (!blockStack.empty()) {
            Block currentBlock = blockStack.pop();
            if (currentBlock == null) {
                continue;
            }
            Block leftBlock = currentBlock.getLeftBlock();
            Block rightBlock = currentBlock.getRightBlock();
            Block topBlock = currentBlock.getTopBlock();
            Block bottomBlock = currentBlock.getBottomBlock();

            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock, leftBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock, rightBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock, topBlock);
            checkForIdenticalLinks(linkedBlocks, blockStack, visitedCoordinates, currentBlock, bottomBlock);
            currentBlock = null;
        }

    }

    private static void checkForIdenticalLinks(
            List<Block> linkedBlocks,
            Stack<Block> blockStack,
            Set<Coordinate> visitedCoordinates,
            Block currentBlock,
            Block linkBlock) {
        if (currentBlock.equals(linkBlock)
                && !visitedCoordinates.contains(linkBlock.getCoordinate())) {
            linkedBlocks.add(linkBlock);
            blockStack.push(linkBlock);
            visitedCoordinates.add(linkBlock.getCoordinate());
        }
    }

    /**
     * 
     * @param block
     * @return number of links found
     */
    private static int getLinkCount(Block block) {
        return getLinkCount(block, false);
    }

    /**
     * 
     * @param block
     * @param deleteLinks if true then delete links with >=3 of the same
     * @return number of links found
     */
    public static int getLinkCount(Block block, boolean deleteLinks) {
        if (block.isEmpty()) {
            return 0;
        }
        // arraylist for keeping same linked block
        // for later removal if there are 3 or more;
        List<Block> linkedBlocks = new ArrayList<>();
        switch (level) {
            case EASY:
                normalModeTraverse(block, linkedBlocks);
                break;
            case NORMAL:
                normalModeTraverse(block, linkedBlocks);
                break;
            case HARD:
                normalModeTraverse(block, linkedBlocks);
                break;
            case NIGHTMARE:
                normalModeTraverse(block, linkedBlocks);
                break;
            case DARKSOULS:
                traverseBlocks(block, linkedBlocks, OneDirection.LEFT);
                traverseBlocks(block, linkedBlocks, OneDirection.RIGHT);
                traverseBlocks(block, linkedBlocks, OneDirection.UP);
                traverseBlocks(block, linkedBlocks, OneDirection.DOWN);
                break;
            default:
                break;
        }

        if (deleteLinks && linkedBlocks.size() >= 3) {
            for (Block linkBlock : linkedBlocks) {
                linkBlock.delete();
            }
        }
        return linkedBlocks.size();
    }

    private static void traverseBlocks(Block block, List<Block> linkedBlocks, OneDirection direction) {
        // one could use recursion but i avoid it because it has stackoverflow issues
        // this app does not have enough recursions to cause a stackoverflow
        // but i'll still avoid it anyway and opt to use Depth First Search algorithm

        Stack<Block> blockStack = new Stack<>();
        blockStack.push(block);
        while (!blockStack.empty()) {
            Block currentBlock = blockStack.pop();
            if (currentBlock == null) {
                continue;
            }
            Block nextBlock = null;
            switch (direction) {
                case LEFT:
                    nextBlock = currentBlock.getLeftBlock();
                    break;
                case RIGHT:
                    nextBlock = currentBlock.getRightBlock();
                    break;
                case UP:
                    nextBlock = currentBlock.getTopBlock();
                    break;
                case DOWN:
                    nextBlock = currentBlock.getBottomBlock();
                    break;
                default:
                    break;
            }
            if (currentBlock.equals(nextBlock)) {
                linkedBlocks.add(nextBlock);
                blockStack.push(nextBlock);
            }
        }
    }

}
