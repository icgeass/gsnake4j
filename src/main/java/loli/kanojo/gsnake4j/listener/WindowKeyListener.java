package loli.kanojo.gsnake4j.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loli.kanojo.gsnake4j.bean.DialogInfo;
import loli.kanojo.gsnake4j.bean.Direction;
import loli.kanojo.gsnake4j.cfg.Configuration;
import loli.kanojo.gsnake4j.cfg.Constants;
import loli.kanojo.gsnake4j.thread.Drawer;
import loli.kanojo.gsnake4j.thread.Snake;

/**
 * 监听键盘按键
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.4
 * @url https://github.com/icgeass/gsnake4j
 */
public class WindowKeyListener implements KeyListener {

    private final static Logger logger = LoggerFactory.getLogger(WindowKeyListener.class);

    // 用于解决实时响应中转向时按键与周期到触发的连续移动（一个周期间隔内），如果按键触发移动则周期到等待下一个周期触发。
    // 每次按键触发移动时自增该值，在move中判断属于周期到且该值不与另一同步变量x相同（则说明该周期内按键产生了移动），则周期到等待下一个周期触发并同步另一同步变量x和该值相同
    private static long lastMovedByKey = Long.MIN_VALUE;

    // 周期响应中按下转向键时时间向前最近一次周期到触发移动的时间点，该时间点后的一个移动周期内不再响应按键
    private static long lastMovedByInterval = 0;

    private JFrame jFrame = null;

    public WindowKeyListener(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_SPACE) {
            Snake.getInstance().setIsRunable(!Snake.getInstance().getIsRunable());
        } else if (keycode == KeyEvent.VK_ESCAPE) {
            boolean isRunable = Snake.getInstance().getIsRunable();
            Snake.getInstance().setIsRunable(false);
            DialogInfo dialogInfo = Configuration.getDialogByKey(Constants.ID_YES_NO_WHEN_EXIT);
            if (JOptionPane.showConfirmDialog(jFrame, dialogInfo.getMessage(), dialogInfo.getTitle(), JOptionPane.YES_NO_OPTION, dialogInfo.getMessageType()) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
            Snake.getInstance().setIsRunable(isRunable);
        }
        // 暂停时只响应空格、退出键
        if (!Snake.getInstance().getIsRunable()) {
            logger.debug("暂停: 过滤按键{}", keycode);
            return;
        }
        // 周期响应(非键盘敏感)的情况下，若未到达响应周期则不作处理；防止连续两次快速改变方向时前进方向变为后退（在snake一个移动周期内）
        if (!Configuration.isKeySensitive() && System.currentTimeMillis() - lastMovedByInterval < Snake.getInstance().getInterval()) {
            logger.debug("周期响应: 过滤按键{}", keycode);
            return;
        }
        Direction directionUserTyped = null;
        if (keycode == KeyEvent.VK_UP) {
            directionUserTyped = Direction.UP;
        } else if (keycode == KeyEvent.VK_DOWN) {
            directionUserTyped = Direction.DOWN;
        } else if (keycode == KeyEvent.VK_LEFT) {
            directionUserTyped = Direction.LEFT;
        } else if (keycode == KeyEvent.VK_RIGHT) {
            directionUserTyped = Direction.RIGHT;
        } else if (keycode == KeyEvent.VK_SHIFT) {
            Snake.getInstance().setInterval(Constants.INTERVAL_MOVE_DEFAULT);
        }
        // 过滤非方向键
        if (null == directionUserTyped) {
            return;
        }
        // 处理加速减速
        if (Snake.getInstance().getDirection().isGoAhead(directionUserTyped)) {
            Snake.getInstance().speedUp();
        } else if (Snake.getInstance().getDirection().isGoBack(directionUserTyped)) {
            Snake.getInstance().speedDown();
        }
        // 只响应相对于左右的按键
        if (Snake.getInstance().getDirection().isTurn(directionUserTyped)) {
            Snake.getInstance().setDirection(directionUserTyped);
            // 实时响应则直接调用snake移动和界面刷新
            if (Configuration.isKeySensitive()) {
                logger.debug("实时响应: 开始调用移动和刷新");
                lastMovedByKey++;
                Snake.getInstance().move(Constants.SNAKE_MOVE_BY_KEY);
                Drawer.getInstance().reFresh();
            } else {
                // 周期响应设置本次移动向前最近一次周期移动的时间，在该时间点向后的一个周期内只允许按键改变方向一次
                lastMovedByInterval = Snake.getInstance().getLastMovedByInterval();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public static long getLastMovedByKey() {
        return lastMovedByKey;
    }

}
