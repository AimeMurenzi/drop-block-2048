/**
 * @Author: Aimé
 * @Date:   2022-03-24 17:31:50
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-05-06 07:09:12
 */
package be.freeaime.drop_block_2048.models;

public class Grid {

    public static final int GRID_COLS = 5;
    public static final int GRID_ROWS = 6;
    private Block[][] gridArray = new Block[GRID_ROWS][GRID_COLS];

    public Grid() {
        initGrid();
    }

    private void assignBlockNeighbors() {
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                Block leftBlock = getLeftBlock(gridArray, row, col);
                Block rightBlock = getRightBlock(gridArray, row, col);
                Block topBlock = getColTopBlock(gridArray, row, col);
                Block bottomBlock = getBottomBlock(gridArray, row, col);
                //
                gridArray[row][col].setLeftBlock(leftBlock);
                gridArray[row][col].setRightBlock(rightBlock);
                gridArray[row][col].setTopBlock(topBlock);
                gridArray[row][col].setBottomBlock(bottomBlock);

            }
        }
    }

    private void initGrid() {
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                int type = 0;
                gridArray[row][col] = new Block(type, new Coordinate(row, col));
            }
        }
        assignBlockNeighbors();
    }

    private Block getLeftBlock(Block[][] gridArray, int row, int col) {
        int leftBlockIndex = col - 1;
        return leftBlockIndex < 0 ? null : gridArray[row][leftBlockIndex];
    }

    private Block getRightBlock(Block[][] gridArray, int row, int col) {
        int rightBlockIndex = col + 1;
        return rightBlockIndex < GRID_COLS ? gridArray[row][rightBlockIndex] : null;
    }

    private Block getColTopBlock(Block[][] gridArray, int row, int col) {
        int topBlockIndex = row - 1;
        return topBlockIndex < 0 ? null : gridArray[topBlockIndex][col];
    }

    private Block getBottomBlock(Block[][] gridArray, int row, int col) {
        int bottomBlockIndex = row + 1;
        return bottomBlockIndex < GRID_ROWS ? gridArray[bottomBlockIndex][col] : null;
    }

    public Block[][] getGridArray() {
        return gridArray;
    }

    public Block getColTopBlock(int col) {
        boolean colCheck = (col >= 0) && (col < GRID_COLS);
        if (colCheck) {
            for (int row = 0; row < GRID_ROWS; row++) {
                if (!gridArray[row][col].isEmpty()) {
                    return gridArray[row][col];
                }
            }
            return gridArray[GRID_ROWS - 1][col];
        }
        return null;
    }

    public Block getBlock(int row, int col) {
        boolean widthIndexCheck = (row >= 0) && (row < GRID_ROWS);
        boolean heightIndexCheck = (col >= 0) && (col < GRID_COLS);
        if (widthIndexCheck && heightIndexCheck) {
            return gridArray[row][col];
        }
        return null;
    }
}
