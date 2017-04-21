package com.zeroq6.gsnake4j.thread;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Random;

import com.zeroq6.gsnake4j.bean.Node;
import com.zeroq6.gsnake4j.bean.Record;
import com.zeroq6.gsnake4j.cfg.Configuration;
import com.zeroq6.gsnake4j.cfg.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重绘线程
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.5
 * @url https://github.com/icgeass/gsnake4j
 */
public class Drawer implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(Drawer.class);

    private final static Drawer INSTANCE = new Drawer();

    private Random rand = new Random();
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);

    private Drawer() {
    }

    public static Drawer getInstance() {
        return INSTANCE;
    }

    @Override
    public void run() {
        while (true) {
            reFresh();
            try {
                Thread.sleep(Constants.INTERVAL_UI_REFRESH);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 刷新界面
     * 由于前景背景的交换填充, 界面会有一定的闪烁
     */
    public void reFresh() {
        // 标签状态刷新
        Record record = Configuration.getCurrentRecord();
        int interval = Snake.getInstance().getInterval();
        int scorePerFood = Constants.SCORE_PER_FOOD + (int) ((Constants.INTERVAL_MOVE_DEFAULT - interval) * Constants.SCORE_PER_INTERVAL);
        int speed = (Constants.INTERVAL_MOVE_DEFAULT - interval) / Constants.INTERVAL_MOVE_PER_CHANGE;
        int foodNum = record.getFoodNum();
        Configuration.getWindow().getLabel().setText(String.format(Constants.TEXT_JLABEL, String.valueOf(record.getId()), String.valueOf(record.getScore()), sdf.format(record.getRuntime()), String.valueOf(interval), String.valueOf(scorePerFood), speed < 0 ? String.valueOf(speed) : "+" + speed, String.valueOf(foodNum)));
        // 背景绘制。图片不存在用黑底填充；存在，填充背景图, 如果加载背景图失败则仍使用黑底填充
        if (null != this.getClass().getResource(Constants.PATH_IMAGE_ICON)) {
            if (!fillBackGroundWithImage(Configuration.getCurrBackPath(), 0, 0, Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT)) {
                fillRectWithColor(0, 0, Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT, Color.BLACK);
            }
        } else {
            fillRectWithColor(0, 0, Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT, Color.BLACK);
        }
        // 绘制移动snake节点
        synchronized (Snake.getInstance()) {
            for (Node node : Snake.getInstance().getNodes()) {
                int[] snakeNodeCoordinate = getCoordinateByNode(node);
                fillRectWithColor(snakeNodeCoordinate[0], snakeNodeCoordinate[1], Constants.NODE_WIDTH, Constants.NODE_HEIGHT, Configuration.getCurrNodesColor());
            }
        }
        // 绘制食物
        int[] foodCoordinate = getCoordinateByNode(Snake.getInstance().getFood());
        this.fillRectWithColor(foodCoordinate[0], foodCoordinate[1], Constants.NODE_WIDTH, Constants.NODE_HEIGHT, getRandomColor());
        logger.trace("界面刷新: {}", System.currentTimeMillis());
    }

    /**
     * 得到Node左上角顶点坐标
     * 
     * @param node
     * @return
     */
    private int[] getCoordinateByNode(Node node) {
        int[] re = new int[2];
        re[0] = (node.getX() - 1) * (Constants.NODE_WIDTH + 1);
        re[1] = (node.getY() - 1) * (Constants.NODE_HEIGHT + 1);
        return re;
    }

    /**
     * 绘制矩形框
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @param color
     */
    private void fillRectWithColor(int x, int y, int width, int height, Color color) {
        Graphics g = Configuration.getWindow().getCanvas().getGraphics();
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    /**
     * 绘制背景图
     * 
     * @param url
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    private boolean fillBackGroundWithImage(String url, int x, int y, int width, int height) {
        Graphics g = Configuration.getWindow().getCanvas().getGraphics();
        return g.drawImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(url)), 0, 0, Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT, null);
    }

    /**
     * 得到随机颜色
     * 
     * @return
     */
    private Color getRandomColor() {
        return new Color(rand.nextInt(16) * 16, rand.nextInt(16) * 16, rand.nextInt(16) * 16, rand.nextInt(16) * 16).brighter();
    }

}
