/**
 * @Author: Aimé
 * @Date:   2022-03-24 17:19:53
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-03-30 11:32:13
 */
package be.freeaime.drop_block_2048.models;

public class Block {
    private int type;
    private Coordinate coordinate;
    private Block leftBlock;
    private Block rightBlock;
    private Block topBlock;
    private Block bottomBlock;

    public Block(int type, Coordinate coordinate) {
        this.type = type;
        this.coordinate = coordinate;
    }

    public Block(int type, Coordinate coordinate, Block leftBlock, Block rightBlock, Block topBlock,
            Block bottomBlock) {
        this.type = type;
        this.coordinate = coordinate;
        this.leftBlock = leftBlock;
        this.rightBlock = rightBlock;
        this.topBlock = topBlock;
        this.bottomBlock = bottomBlock;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Block getBottomBlock() {
        return bottomBlock;
    }

    public void setBottomBlock(Block bottomBlock) {
        this.bottomBlock = bottomBlock;
    }

    public Block getLeftBlock() {
        return leftBlock;
    }

    public void setLeftBlock(Block leftBlock) {
        this.leftBlock = leftBlock;
    }

    public Block getRightBlock() {
        return rightBlock;
    }

    public void setRightBlock(Block rightBlock) {
        this.rightBlock = rightBlock;
    }

    public Block getTopBlock() {
        return topBlock;
    }

    public void setTopBlock(Block topBlock) {
        this.topBlock = topBlock;
    }

    public void delete() {
        setType(0);
    }

    public boolean isEmpty() {
        return type == 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + type;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Block other = (Block) obj;
        if (type != other.type)
            return false;
        return true;
    }

}
