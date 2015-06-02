package loli.kanojo.gsnake4j.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import loli.kanojo.gsnake4j.cfg.Configuration;
import loli.kanojo.gsnake4j.cfg.Constants;
import loli.kanojo.gsnake4j.listener.WindowActionListener;
import loli.kanojo.gsnake4j.listener.WindowKeyListener;

/**
 * 窗口
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.4
 * @url https://github.com/icgeass/gsnake4j
 */
public class Window extends JFrame {

    private final static long serialVersionUID = 1L;
    private Canvas canvas;
    private JLabel jLabel;

    public Window() {
        if (null != this.getClass().getResource(Constants.PATH_IMAGE_ICON)) {
            this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(Constants.PATH_IMAGE_ICON)));
        }
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle(Constants.ABOUT_VERSION);
        this.addKeyListener(new WindowKeyListener(this));
        this.setJMenuBar(new JMenuBar());
        String[] menuLab = { Constants.MENU_OPTIONS, Constants.MENU_CHANGE_BACK, Constants.MENU_SETTIONS, Constants.MENU_HELP };
        String[][] menuItemLab = { { Constants.MENU_RESTART, Constants.MENU_EXIT }, Configuration.getMenuItemBackImageNames(), { Constants.MENU_PERIIOD_REPONSE, Constants.MENU_REAL_TIME_RESPONSE }, { Constants.MENU_HIGHEST_SCORE, Constants.MENU_OPERATION, Constants.MENU_ABOUT } };
        for (int i = 0; i < menuLab.length; i++) {
            JMenu jMenu = new JMenu(menuLab[i]);
            this.getJMenuBar().add(jMenu);
            for (int j = 0; j < menuItemLab[i].length; j++) {
                JMenuItem jMenuItem = new JMenuItem(menuItemLab[i][j]);
                jMenu.add(jMenuItem);
                jMenuItem.addActionListener(new WindowActionListener(this));
            }
        }
        this.setLayout(new BorderLayout());

        Container container = this.getContentPane();
        jLabel = new JLabel(Constants.TEXT_LOADING, JLabel.LEFT);
        container.add(jLabel, BorderLayout.NORTH);

        canvas = new Canvas();
        canvas.setSize(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
        container.add(canvas, BorderLayout.CENTER);

        // 窗体的key监听器不能监听画布的，需要为画布使用单独的监听器
        canvas.addKeyListener(new WindowKeyListener(this));
        this.pack();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JLabel getjLabel() {
        return jLabel;
    }

}
