package com.zeroq6.gsnake4j.thread;

import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JOptionPane;

import com.zeroq6.gsnake4j.bean.Node;
import com.zeroq6.gsnake4j.bean.DialogInfo;
import com.zeroq6.gsnake4j.bean.Direction;
import com.zeroq6.gsnake4j.bean.Record;
import com.zeroq6.gsnake4j.cfg.Configuration;
import com.zeroq6.gsnake4j.cfg.Constants;
import com.zeroq6.gsnake4j.listener.WindowKeyListener;
import com.zeroq6.gsnake4j.utils.Kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 移动线程，控制nodes移动，生成等
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.5
 * @url https://github.com/icgeass/gsnake4j
 */
public class Snake implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(Snake.class);

    private final static Snake INSTANCE = new Snake();

    private Random rand = new Random();
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);
    // 同步变量
    private long lastMovedByInterval = 0;
    private long lastMovedByKey = WindowKeyListener.getLastMovedByKey();
    // ///////////////////////
    private Direction direction = Direction.UP;
    private Boolean isRunable = true;
    private int interval = Constants.INTERVAL_MOVE_DEFAULT;
    // //////////////////
    private LinkedList<Node> nodes = new LinkedList<Node>();
    private Node food = null;

    private Snake() {
        init();
    }

    public static Snake getInstance() {
        return Snake.INSTANCE;
    }

    @Override
    public void run() {
        while (true) {
            while (isRunable) {
                Snake.getInstance().move(Constants.SNAKE_MOVE_BY_INTERVAL);
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(Constants.INTERVAL_PAUSE_WAIT_SCAN); // 防止暂停时对CPU的过度抢占
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 每局初始化操作
     */
    public void init() {
        Direction d = null;
        this.setFood(generateFood());
        while ((d = this.generateNodes()) == null);
        this.setDirection(d);
        this.setInterval(Constants.INTERVAL_MOVE_DEFAULT);
        Configuration.setBackImageByRandom();
        Configuration.setNodesColorByDefault();
        // 最后调用开始下一局, 因为会重置当前局, 并且startOrPause中会有当局时间的一些处理
        Configuration.newGame();
        this.setIsRunable(true); // 这里的开始其实是会被过滤掉的，因为上一局结束下一局开始后只会先接受暂停
    }

    /**
     * 移动获取食物
     * 通过调用luckyOrUnlucky(), 并通过返回结果
     * 
     * @param source
     */
    public void move(int source) {
        // 在实时响应（键盘敏感）下, 如果一个周期内响应了手动按键移动，则不再响应一个周期到触发的移动
        if (Configuration.isKeySensitive() && this.lastMovedByKey != WindowKeyListener.getLastMovedByKey()) {
            if (source == Constants.SNAKE_MOVE_BY_INTERVAL) {
                lastMovedByKey = WindowKeyListener.getLastMovedByKey();
                logger.debug("实时响应: 阻止周期到移动, 等待下一周期");
                return;
            }
        }
        Record record = Configuration.getCurrentRecord();
        int luckyOrUnlucky = luckyOrUnlucky();// 得到食物
        if (source == Constants.SNAKE_MOVE_BY_INTERVAL) {
            this.setLastMovedByInterval(System.currentTimeMillis());
            logger.trace("周期响应: {}", getLastMovedByInterval());
        } else {
            logger.debug("实时响应: {}", System.currentTimeMillis());
        }
        if (record.getRuntime() <= Constants.GAME_TIME_LIMITED && luckyOrUnlucky != Constants.SNAKE_EAT_Oooooops) {
            if (luckyOrUnlucky == Constants.SNAKE_EAT_LUCKY) {
                this.setFood(generateFood());
                record.setScore(record.getScore() + Constants.SCORE_PER_FOOD + (int) ((Constants.INTERVAL_MOVE_DEFAULT - interval) * Constants.SCORE_PER_INTERVAL));
                record.setFoodNum(nodes.size() - Constants.SNAKE_NODE_NUM);
                // 获得食物自动改变nodes颜色
                Configuration.setNodesColorByRandom();
            }
        } else {
            // 记录当前局数的结束时间
            Configuration.setCurrEndTime();
            this.setIsRunable(false); //先设置结束时间，后设置暂停，因为暂停会根据结束时间判断当前局是否结束
            Toolkit.getDefaultToolkit().beep();
            // 对话框信息
            DialogInfo dialogInfo = Configuration.getDialogByKey(Constants.ID_YES_NO_WHEN_GAME_OVER);
            String msg = dialogInfo.getMessage();
            String title = dialogInfo.getTitle();
            Integer msgType = dialogInfo.getMessageType();
            // 当前局数信息
            String id = String.valueOf(record.getId());
            String score = String.valueOf(record.getScore());
            String formatedRuntime = sdf.format(record.getRuntime());
            String foodNum = String.valueOf(record.getFoodNum());
            // 对话框提示信息
            String tips = Constants.CONFIRM_MSG_GAME_OVER;
            // 当同时打破记录并且超时时提示打破记录
            if (Integer.valueOf(score) > Configuration.getHighestRecord().getScore()) {
                tips = Constants.CONFIRM_MSG_BREAK_RECORD;
            } else if (record.getRuntime() > Constants.GAME_TIME_LIMITED) {
                tips = Constants.CONFIRM_MSG_TIME_OUT;
            }
            // try again?
            if (JOptionPane.showConfirmDialog(Configuration.getWindow(), String.format(msg, tips, id, score, formatedRuntime, foodNum), title, JOptionPane.YES_NO_OPTION, msgType) == JOptionPane.NO_OPTION) {
                // 由于会重置当前局数信息, 所以在得到当前局数信息之后使用
                // 退出时调用此方法设置最高得分局数（虽然立即会结束重新）
                Configuration.endGame();
                System.exit(0);
            } else {
                init();
            }
        }

    }

    /**
     * 控制snake移动, 返回移动后的状态
     * 
     * @return
     */
    private int luckyOrUnlucky() {
        int re = Constants.SNAKE_EAT_Oooooops;
        Node nextNode = null;
        if (direction == Direction.UP) {
            nextNode = new Node(nodes.get(0).getX(), nodes.get(0).getY() - 1);
        } else if (direction == Direction.DOWN) {
            nextNode = new Node(nodes.get(0).getX(), nodes.get(0).getY() + 1);
        } else if (direction == Direction.LEFT) {
            nextNode = new Node(nodes.get(0).getX() - 1, nodes.get(0).getY());
        } else if (direction == Direction.RIGHT) {
            nextNode = new Node(nodes.get(0).getX() + 1, nodes.get(0).getY());
        }
        if (isNextNodeOooooops(nextNode)) {
            re = Constants.SNAKE_EAT_Oooooops;
        } else {
            synchronized (this) {
                nodes.add(0, nextNode);
                if (nodes.contains(food)) {
                    re = Constants.SNAKE_EAT_LUCKY;
                } else {
                    re = Constants.SNAKE_EAT_UNLUCKY;
                    nodes.removeLast();
                }
            }
        }
        return re;
    }

    /**
     * 创建食物(递归如果失败)
     * 
     * @return
     */
    private Node generateFood() {
        int x = rand.nextInt(Constants.NODE_NUM_X) + 1;
        int y = rand.nextInt(Constants.NODE_NUM_Y) + 1;
        Node food = new Node(x, y);
        if (nodes.contains(food)) {
            return generateFood();
        }
        return food;
    }

    /**
     * 生成nodes，返回方向，如果生成失败则重置nodes并返回null
     * 
     * @return
     */
    private Direction generateNodes() {
        Direction re = null;
        boolean isGenerateFailed = false;
        // 生成nodes
        synchronized (this) {
            Node head = generateFood(); // 起始节点
            nodes.clear();
            nodes.add(head);
            for (int i = 0; i < Constants.SNAKE_NODE_NUM; i++) {
                Node nextNode = generateEnabledNextnode(nodes.getLast());
                if (null != nextNode) {
                    nodes.addLast(nextNode);
                } else {
                    isGenerateFailed = true;
                    break;
                }
            }
        }
        // 设定方向，找出node0在node1的哪个方向
        Direction d = null;
        int xDiff = nodes.get(0).getX() - nodes.get(1).getX();
        int yDiff = nodes.get(0).getY() - nodes.get(1).getY();
        if (xDiff == 0 && yDiff == -1) {
            d = Direction.UP;
        } else if (xDiff == 0 && yDiff == 1) {
            d = Direction.DOWN;
        } else if (xDiff == -1 && yDiff == 0) {
            d = Direction.LEFT;
        } else if (xDiff == 1 && yDiff == 0) {
            d = Direction.RIGHT;
        }
        synchronized (this) {
            if (null == d) {
                logger.error("非相邻节点{}, {}, nodes{}", nodes.get(0), nodes.get(1), nodes);
                nodes.clear();
            } else {
                if (!isGenerateFailed) {
                    nodes.removeFirst(); // node产生时就多一个节点，移除第一个节点后，保证第一次前进不会撞
                    re = d;
                } else {
                    logger.error("无法产生节点{}的相邻节点, 当前nodes{}", nodes.getLast(), nodes);
                }
            }
        }
        logger.debug("初始化nodes, 方向{}, 详细{}", re, nodes);
        return re;
    }

    /**
     * 根据给定节点随机生成可用的与该节点相邻的节点，如果没有这样的节点返回null
     * 
     * @return
     */
    private Node generateEnabledNextnode(Node node) {
        Node re = null;
        // 相邻节点，上下左右
        Node[] nextNodes = new Node[] { new Node(node.getX(), node.getY() - 1), new Node(node.getX(), node.getY() + 1), new Node(node.getX() - 1, node.getY()), new Node(node.getX() + 1, node.getY()) };
        // 随机排序
        Kit.sortArrayRandom(nextNodes);
        for (int i = 0; i < nextNodes.length; i++) {
            if (!isNextNodeOooooops(nextNodes[i])) {
                re = nextNodes[i];
                break;
            }
        }
        return re;
    }

    /**
     * 判断下一个节点是否【不合法】
     * 
     * @param nextNode
     * @return
     */
    private boolean isNextNodeOooooops(Node nextNode) {
        return nextNode.getX() < 1 || nextNode.getX() > Constants.NODE_NUM_X || nextNode.getY() < 1 || nextNode.getY() > Constants.NODE_NUM_Y || nodes.contains(nextNode);
    }

    /**
     * 减速
     */
    public void speedUp() {
        interval -= Constants.INTERVAL_MOVE_PER_CHANGE;
        if (interval < Constants.INTERVAL_MOVE_MIN) {
            interval = Constants.INTERVAL_MOVE_MIN;
        }
    }

    /**
     * 减速
     */
    public void speedDown() {
        interval += Constants.INTERVAL_MOVE_PER_CHANGE;
        if (interval > Constants.INTERVAL_MOVE_MAX) {
            interval = Constants.INTERVAL_MOVE_MAX;
        }
    }

    // ////////////////////////////////////////////////////

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    // ///////////////////////////////

    public Boolean getIsRunable() {
        return isRunable;
    }

    /**
     * 调用Configuration.startOrPause方法以便记录每局运行时间
     * 
     * @param isRunable
     */
    public void setIsRunable(Boolean isRunable) {
        Configuration.startOrPause(isRunable);
        this.isRunable = isRunable;
    }

    // ///////////////////////////////

    public long getLastMovedByInterval() {
        return lastMovedByInterval;
    }

    private void setLastMovedByInterval(long lastMovedByInterval) {
        this.lastMovedByInterval = lastMovedByInterval;
    }

    public Node getFood() {
        return food;
    }

    private void setFood(Node food) {
        this.food = food;
    }

    // ////////////////////////////////

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

}
