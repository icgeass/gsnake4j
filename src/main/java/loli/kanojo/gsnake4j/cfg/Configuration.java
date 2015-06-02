package loli.kanojo.gsnake4j.cfg;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

import loli.kanojo.gsnake4j.bean.DialogInfo;
import loli.kanojo.gsnake4j.bean.Record;
import loli.kanojo.gsnake4j.ui.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 运行时配置和管理
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.4
 * @url https://github.com/icgeass/gsnake4j
 */
public class Configuration {

    private final static Logger logger = LoggerFactory.getLogger(Configuration.class);
    private final static Random rand = new Random();

    // 菜单点击按钮框
    private final static Map<String, DialogInfo> mapId2Dialog = new HashMap<String, DialogInfo>();
    // 背景图片
    private final static List<String> liBackImage = new ArrayList<String>();
    // 每局记录
    private final static List<Record> liRecord = new ArrayList<Record>();
    // nodes颜色
    private final static List<Color> liColor = new ArrayList<Color>();

    // 窗体
    private static Window window = null;
    // 键盘敏感会在转弯时立即响应一次，不敏感则会根据在每个时间周期响应一次。（按键、移动和重绘）
    private static boolean isKeySensitive = Constants.CFG_IS_KEY_SENSITIVE;
    // 暂停时间点
    private static long pausePoint = Long.MIN_VALUE;

    // 背景图与nodes颜色
    private static String currBackPath = Constants.PATH_DEFAULT_IMAGE_BACK;
    private static Color currNodesColor = Constants.SNAKE_COLOR;

    static {
        liRecord.add(new Record(0));
        mapId2Dialog.put(Constants.MENU_REAL_TIME_RESPONSE, new DialogInfo("按键已更改为实时响应", "提示", JOptionPane.WARNING_MESSAGE));
        mapId2Dialog.put(Constants.MENU_PERIIOD_REPONSE, new DialogInfo("按键已更改为周期响应", "提示", JOptionPane.WARNING_MESSAGE));
        mapId2Dialog.put(Constants.MENU_HIGHEST_SCORE, new DialogInfo("局数: 第%s局\r\n获得食物数: %s\r\n得分: %s\r\n所用时间: %s", "最高得分", JOptionPane.INFORMATION_MESSAGE));
        mapId2Dialog.put(Constants.MENU_OPERATION, new DialogInfo("方向键: 控制方向和速度\r\n空格键: 开始/暂停\r\nShift键: 恢复默认速度\r\nEsc键 : 退出\r\n注意: ☆周期响应模式下按键过快可导致操作失灵☆", "使用帮助", JOptionPane.QUESTION_MESSAGE));
        mapId2Dialog.put(Constants.MENU_ABOUT, new DialogInfo("作者: %s\r\n邮箱: %s\r\n版本: %s", "关于", JOptionPane.INFORMATION_MESSAGE));
        mapId2Dialog.put(Constants.ID_YES_NO_WHEN_GAME_OVER, new DialogInfo("%s\r\n局数: 第%s局\r\n得分: %s\r\n所用时间: %s\r\n获得食物: %s", "是否继续？", JOptionPane.ERROR_MESSAGE));
        mapId2Dialog.put(Constants.ID_YES_NO_WHEN_EXIT, new DialogInfo("确认退出?", "确定", JOptionPane.QUESTION_MESSAGE));
        liBackImage.add("/back1.jpg");
        liBackImage.add("/back2.jpg");
        liBackImage.add("/back3.jpg");
        liBackImage.add("/back4.jpg");
        liBackImage.add("/back5.jpg");
        liBackImage.add("/back6.jpg");
        liColor.add(new Color(164, 168, 170, 180).brighter());
        liColor.add(new Color(131, 204, 254, 180).brighter());
        liColor.add(new Color(238, 149, 213, 180).brighter());
        liColor.add(new Color(236, 207, 63, 180).brighter());
        liColor.add(new Color(166, 213, 94, 180).brighter());
        liColor.add(new Color(113, 214, 216, 180).brighter());
        liColor.add(new Color(249, 172, 80, 180).brighter());
        liColor.add(new Color(248, 108, 110, 180).brighter());
        liColor.add(new Color(236, 86, 167, 180).brighter());
        liColor.add(new Color(64, 204, 115, 180).brighter());
        liColor.add(new Color(189, 135, 254, 180).brighter());
        liColor.add(new Color(81, 174, 251, 180).brighter());
        liColor.add(new Color(146, 149, 254, 180).brighter());
        liColor.add(new Color(188, 178, 167, 180).brighter());
    }

    private Configuration() {
    }

    /**
     * 每局游戏开始时调用此方法，处理每局记录
     */
    public static void newGame() {
        Record nextRecord = new Record(getCurrentRecord().getId() + 1);
        Collections.sort(liRecord);
        // 保留最高得分前十
        while (liRecord.size() > 10) {
            liRecord.remove(liRecord.size() - 1);
        }
        liRecord.add(nextRecord);
        logger.debug("设置当前局{}, 信息: {}", nextRecord.getId(), nextRecord);
    }

    /**
     * 结束游戏退出应用调用此方法
     */
    public static void endGame() {
        newGame();
        for (Record r : liRecord) {
            logger.debug(r.toString());
        }
        System.exit(0);
    }

    /**
     * 设置当前局结束时间
     */
    public static void setCurrEndTime() {
        getCurrentRecord().setEndTime(System.currentTimeMillis());
        logger.debug("设置第{}局结束时间{}", getCurrentRecord().getId(), getCurrentRecord().getEndTime());
    }

    /**
     * 【改变运行状态一律调用此方法以计时】
     * 设置nodes是否移动
     * 传人改变后的运行状态isRunable
     * 
     * @param isRunable
     */
    public static void startOrPause(boolean isRunable) {
        // 下一局已经开始
        if (getCurrentRecord().getEndTime() != Long.MIN_VALUE) {
            // 在配置中重置时间记录信息，等待暂停
            pausePoint = Long.MIN_VALUE;
            logger.debug("第{}局已结束, 忽略{}", getCurrentRecord().getId(), isRunable ? "开始" : "暂停");
            return;
        }
        // 暂停时记录时间点，开始时移动开始时间
        if (!isRunable) {
            // 忽略连续暂停
            if (pausePoint == Long.MIN_VALUE) {
                pausePoint = System.currentTimeMillis();
                logger.debug("第{}局, 暂停: {}", getCurrentRecord().getId(), pausePoint);
            } else {
                logger.debug("第{}局, 忽略暂停", getCurrentRecord().getId());
            }
        } else {
            // 忽略连续开始
            if (pausePoint != Long.MIN_VALUE) {
                getCurrentRecord().setAlterableBeginTime(getCurrentRecord().getAlterableBeginTime() + (System.currentTimeMillis() - pausePoint));;
                pausePoint = Long.MIN_VALUE;
                logger.debug("第{}局, 开始: {}, 设置新开始时间{}", getCurrentRecord().getId(), System.currentTimeMillis(), getCurrentRecord().getAlterableBeginTime());
            } else {
                logger.debug("第{}局, 忽略开始", getCurrentRecord().getId());
            }
        }
    }

    /**
     * 得到最高得分对应的记录
     * 
     * @return
     */
    public static Record getHighestRecord() {
        return liRecord.get(0);
    }

    /**
     * 得到当前局数记录
     * 
     * @return
     */
    public static Record getCurrentRecord() {
        return liRecord.get(liRecord.size() - 1);
    }

    // /////////////////////////

    /**
     * 根据key得到对话框信息
     * 
     * @param key
     * @return
     */
    public static DialogInfo getDialogByKey(String key) {
        return mapId2Dialog.get(key);
    }

    /**
     * 得到切换背景菜单的菜单项值的字符串数组，不能切换则返回空数组
     * 
     * @return
     */
    public static String[] getMenuItemBackImageNames() {
        // 如果背景图片集合大小为0, 则不会添加子菜单
        String[] re = new String[liBackImage.size()];
        for (int i = 0; i < re.length; i++) {
            re[i] = Constants.MENU_BACK_PREFIX + (i + 1);
        }
        return re;
    }

    /**
     * 重新开始时设置nodes颜色为默认
     */
    public static void setNodesColorByDefault() {
        currNodesColor = Constants.SNAKE_COLOR;
    }

    /**
     * 每次获得食物时，根据配置设置nodes随机颜色，如果不能设置则使用默认颜色
     */
    public static void setNodesColorByRandom() {
        if (Constants.CFG_AUTO_CHANGE_NODES_WHEN_GOT_FOOD && liColor.size() != 0) {
            currNodesColor = liColor.get(rand.nextInt(liColor.size()));
        } else {
            currNodesColor = Constants.SNAKE_COLOR;
        }
    }

    /**
     * 根据菜单点击设置背景图
     * 
     * @param index
     */
    public static void setBackImageByIndex(Integer index) {
        currBackPath = liBackImage.get(index);
    }

    /**
     * 在每局结束时，根据配置设置窗口随机背景图片，如果不能设置则使用默认背景图
     */
    public static void setBackImageByRandom() {
        if (Constants.CFG_AUTO_CHANGE_BACK_WHEN_NEXT_GAME && liBackImage.size() != 0) {
            currBackPath = liBackImage.get(rand.nextInt(liBackImage.size()));
        } else {
            currBackPath = Constants.PATH_DEFAULT_IMAGE_BACK;
        }
    }

    // ////////////////////////////

    public static String getCurrBackPath() {
        return currBackPath;
    }

    public static Color getCurrNodesColor() {
        return currNodesColor;
    }

    // ///////////////////////////////

    public static Window getWindow() {
        return window;
    }

    public static void setWindow(Window window) {
        Configuration.window = window;
    }

    public static boolean isKeySensitive() {
        return isKeySensitive;
    }

    public static void setKeySensitive(boolean isKeySensitive) {
        Configuration.isKeySensitive = isKeySensitive;
    }

}
