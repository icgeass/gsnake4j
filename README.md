# gsnake4j
<!-- #![logo](https://github.com/icgeass/gsnake4j/raw/master/etc/logo.png) -->

### 介绍

使用Java实现的桌面贪食蛇程序

### 实现功能

* 基本操作
* 提供实时和周期两种响应模式
* 实时信息统计
* 顶部菜单控制
* 使用背景图，图标，透明色
* 随机生成节点位置

### 操作方法

* 方向键：控制移动方向，加速，减速
* 空格键：开始, 暂停
* Shift键盘：恢复默认速度
* Esc键：退出

### 运行截图
![screenshot](https://github.com/icgeass/gsnake4j/raw/master/etc/screenshot.png)

### 常见问题

1. 什么是周期响应和实时响应，为什么会有这两种响应方式
    * 周期响应是指在一个移动周期间隔内只响应一次按键，实时响应是指每次按键立即响应并刷新界面
    * 在一个移动周期内如果响应多次按键（转向），可能会导致前进方向与当前方向相反，游戏结束，但是界面没有及时响应；根本原因是移动周期和键盘响应不同步，所以提供周期和实时两种模式

### 引用

* [slf4j](http://www.slf4j.org/)
* [Logback](http://logback.qos.ch/)







