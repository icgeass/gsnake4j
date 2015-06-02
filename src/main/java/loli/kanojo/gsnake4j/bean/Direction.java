package loli.kanojo.gsnake4j.bean;

/**
 * 方向枚举
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.4
 * @url https://github.com/icgeass/gsnake4j
 */
public enum Direction {

    UP(1), DOWN(-1), LEFT(2), RIGHT(-2);

    public final int value;

    private Direction(int value) {
        this.value = value;
    }

    public boolean isGoBack(Direction direction) {
        return this.value + direction.value == 0;
    }

    public boolean isGoAhead(Direction direction) {
        return this == direction;
    }

    public boolean isTurn(Direction direction) {
        return Math.abs(Math.abs(this.value) - Math.abs(direction.value)) == 1;
    }

};
