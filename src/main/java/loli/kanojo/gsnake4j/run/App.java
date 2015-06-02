package loli.kanojo.gsnake4j.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loli.kanojo.gsnake4j.cfg.Configuration;
import loli.kanojo.gsnake4j.thread.Drawer;
import loli.kanojo.gsnake4j.thread.Snake;
import loli.kanojo.gsnake4j.ui.Window;

/**
 * 入口
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.4
 * @url https://github.com/icgeass/gsnake4j
 */
public class App {
    private final static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        logger.debug("启动中");
        logger.debug("初始化配置");
        Configuration.setWindow(new Window());
        logger.debug("启动移动线程");
        new Thread(Snake.getInstance()).start();
        logger.debug("启动刷新线程");
        new Thread(Drawer.getInstance()).start();
        logger.debug("启动完成");
    }
}
