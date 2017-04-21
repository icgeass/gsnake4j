package com.zeroq6.gsnake4j.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.zeroq6.gsnake4j.cfg.Configuration;
import com.zeroq6.gsnake4j.cfg.Constants;
import com.zeroq6.gsnake4j.thread.Snake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeroq6.gsnake4j.bean.DialogInfo;
import com.zeroq6.gsnake4j.bean.Record;

/**
 * 监听菜单点击
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.5
 * @url https://github.com/icgeass/gsnake4j
 */
public class WindowActionListener implements ActionListener {

    private final static Logger logger = LoggerFactory.getLogger(WindowActionListener.class);

    private final static SimpleDateFormat sdf = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);

    private JFrame jFrame = null;

    public WindowActionListener(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        logger.debug("点击菜单项: {}", command);
        // 若返回null则不会用到弹出框
        DialogInfo dialogInfo = Configuration.getDialogByKey(command);
        if (command.equals(Constants.MENU_RESTART)) {
            Configuration.setCurrEndTime();
            Snake.getInstance().init();
        } else if (command.equals(Constants.MENU_EXIT)) {
            Configuration.setCurrEndTime();
            Configuration.endGame();
        } else if (command.contains(Constants.MENU_BACK_PREFIX)) {
            Integer indexBackImage = Integer.valueOf(command.replace(Constants.MENU_BACK_PREFIX, ""));
            Configuration.setBackImageByIndex(indexBackImage - 1);
        } else if (command.equals(Constants.MENU_REAL_TIME_RESPONSE)) {
            Configuration.setKeySensitive(true);
            JOptionPane.showMessageDialog(jFrame, dialogInfo.getMessage(), dialogInfo.getTitle(), dialogInfo.getMessageType());
        } else if (command.equals(Constants.MENU_PERIOD_RESPONSE)) {
            Configuration.setKeySensitive(false);
            JOptionPane.showMessageDialog(jFrame, dialogInfo.getMessage(), dialogInfo.getTitle(), dialogInfo.getMessageType());
        } else if (command.equals(Constants.MENU_HIGHEST_SCORE)) {
            Record record = Configuration.getHighestRecord();
            String id = String.valueOf(record.getId());
            String foodNum = String.valueOf(record.getFoodNum());
            String score = String.valueOf(record.getScore());
            String formatedRuntime = sdf.format(record.getRuntime());
            JOptionPane.showMessageDialog(jFrame, String.format(dialogInfo.getMessage(), id, foodNum, score, formatedRuntime), dialogInfo.getTitle(), dialogInfo.getMessageType());
        } else if (command.equals(Constants.MENU_OPERATION)) {
            JOptionPane.showMessageDialog(jFrame, dialogInfo.getMessage(), dialogInfo.getTitle(), dialogInfo.getMessageType());
        } else if (command.equals(Constants.MENU_ABOUT)) {
            JOptionPane.showMessageDialog(jFrame, String.format(dialogInfo.getMessage(), Constants.ABOUT_AUTHOR, Constants.ABOUT_EMAIL, Constants.ABOUT_VERSION), dialogInfo.getTitle(), dialogInfo.getMessageType());
        }
    }

}
