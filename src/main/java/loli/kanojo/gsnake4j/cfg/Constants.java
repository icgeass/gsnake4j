package loli.kanojo.gsnake4j.cfg;

import java.awt.Color;

/**
 * 常量
 * 充分了解其含义下修改
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.4
 * @url https://github.com/icgeass/gsnake4j
 */
public class Constants {

    // 菜单
    public final static String MENU_OPTIONS = "选项";
    public final static String MENU_CHANGE_BACK = "更换背景";
    public final static String MENU_SETTIONS = "设置";
    public final static String MENU_HELP = "帮助";
    public final static String MENU_RESTART = "重新开始";
    public final static String MENU_EXIT = "退出";
    public final static String MENU_BACK_PREFIX = "背景";
    public final static String MENU_REAL_TIME_RESPONSE = "实时响应";
    public final static String MENU_PERIIOD_REPONSE = "周期响应";
    public final static String MENU_HIGHEST_SCORE = "最高得分";
    public final static String MENU_OPERATION = "操作方法";
    public final static String MENU_ABOUT = "关于";
    // 关于
    public final static String ABOUT_AUTHOR = "icgeass";
    public final static String ABOUT_EMAIL = "icgeass@hotmail.com";
    public final static String ABOUT_VERSION = "贪食蛇 - v1.0.4";
    // 加载
    public final static String TEXT_LOADING = "Loading...";
    // 普通常量
    public final static String ID_YES_NO_WHEN_GAME_OVER = "confirm_yes_no_when_game_over";
    public final static String ID_YES_NO_WHEN_EXIT = "confirm_yes_no_when_exit";
    // 一局结束时提示语
    public final static String CONFIRM_MSG_GAME_OVER = "本局结束！";
    public final static String CONFIRM_MSG_BREAK_RECORD = "恭喜，你打破了记录！";
    public final static String CONFIRM_MSG_TIME_OUT = "本局超时！";
    // 时间间隔
    public final static int INTERVAL_MOVE_PER_CHANGE = 20;
    public final static int INTERVAL_MOVE_MAX = 300;
    public final static int INTERVAL_MOVE_MIN = 20;
    public final static int INTERVAL_MOVE_DEFAULT = 220;
    public final static int INTERVAL_UI_REFRESH = 100; // 也就是时间刷新频率
    public final static int INTERVAL_PAUSE_WAIT_SCAN = 50;
    // 得分
    public final static int SCORE_PER_FOOD = 5;
    public final static double SCORE_PER_INTERVAL = 0.05;
    // 节点, 画布, 窗口
    public final static int NODE_WIDTH = 10;
    public final static int NODE_HEIGHT = 10;
    public final static int NODE_NUM_X = 40;
    public final static int NODE_NUM_Y = 30;
    public final static Color CANVAS_COLOR = Color.BLACK;
    public final static int CANVAS_WIDTH = ((NODE_WIDTH + 1) * NODE_NUM_X) - 1;
    public final static int CANVAS_HEIGHT = ((NODE_HEIGHT + 1) * NODE_NUM_Y) - 1;
    public final static int WINDOW_WIDTH = CANVAS_WIDTH + 6; // 窗口比画布宽高多一定像素
    public final static int WINDOW_HEIGHT = CANVAS_HEIGHT + 68;
    // 图标路径，默认背景路径
    public final static String PATH_IMAGE_ICON = "/icon.png";
    public final static String PATH_DEFAULT_IMAGE_BACK = "/back1.jpg";
    // 是否自动切换背景和nodes颜色，是否键盘敏感
    public final static boolean CFG_AUTO_CHANGE_BACK_WHEN_NEXT_GAME = true;
    public final static boolean CFG_AUTO_CHANGE_NODES_WHEN_GOT_FOOD = true;
    public final static boolean CFG_IS_KEY_SENSITIVE = false;
    // 时间格式化模式
    public final static String PATTERN_DATE_FORMAT = "mm分ss秒";
    // JLabel信息框
    public final static String TEXT_JLABEL = " 第%s局           你的得分: %s           本局时间: %s           %s / %s / %s / %s";
    // 每局时间限制, 和日期格式化模式对应
    public final static long GAME_TIEM_LIMITED = 60 * (60 - 1) * 1000;
    // snake
    public final static Color SNAKE_COLOR = new Color(255, 255, 255, 180).brighter();
    public final static int SNAKE_NODE_NUM = 5;
    public final static int SNAKE_EAT_Oooooops = 0;
    public final static int SNAKE_EAT_LUCKY = 1;
    public final static int SNAKE_EAT_UNLUCKY = 2;
    public final static int SNAKE_MOVE_BY_KEY = 0;
    public final static int SNAKE_MOVE_BY_INTERVAL = 1;
}
