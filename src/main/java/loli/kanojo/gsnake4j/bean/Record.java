package loli.kanojo.gsnake4j.bean;

import loli.kanojo.gsnake4j.thread.Snake;

/**
 * 记录
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.4
 * @url https://github.com/icgeass/gsnake4j
 */
public class Record implements Comparable<Record> {

    private final int id; // 局数
    private final long beginTime = System.currentTimeMillis(); // 开始时间
    private long alterableBeginTime = beginTime; // 可变开始时间, 暂停时移动该时间以计时
    private long endTime = Long.MIN_VALUE;
    private int score = 0; // 本局得分
    private int foodNum = 0; // 食物数
    private long runtime = 0; // 本局耗时

    public Record(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getBeginTime() {
        return beginTime;
    }

    /**
     * 为了保证在界面刷新时能实时得到本局所用时间, 所以必须用当前时间减去可变开始时间
     * 
     * @return
     */
    public long getRuntime() {
        // 这局没有结束，没有暂停（因为暂停只是在那个时间点记录暂停时间，只有当再次开始时才会设置新的开始时间，不然会因为暂停时间段内没有设置开始而导致时间仍然在走）
        if (getEndTime() == Long.MIN_VALUE && Snake.getInstance().getIsRunable()) {
            setRuntime(System.currentTimeMillis() - getAlterableBeginTime());
        }
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    // ////////////////////////////////////////////////

    public long getAlterableBeginTime() {
        return alterableBeginTime;
    }

    public void setAlterableBeginTime(long alterableBeginTime) {
        this.alterableBeginTime = alterableBeginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFoodNum() {
        return foodNum;
    }

    public void setFoodNum(int foodNum) {
        this.foodNum = foodNum;
    }

    @Override
    public int compareTo(Record o) {
        return o.getScore() - this.getScore();
    }

    @Override
    public String toString() {
        return "Record [id=" + id + ", beginTime=" + beginTime + ", alterableBeginTime=" + alterableBeginTime + ", endTime=" + endTime + ", score=" + score + ", foodNum=" + foodNum + ", runtime=" + runtime + "]";
    }

}
